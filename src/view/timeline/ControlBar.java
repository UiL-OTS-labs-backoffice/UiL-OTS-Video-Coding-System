package view.timeline;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import view.player.IMediaPlayer;
import view.timeline.utilities.INavbarObserver;
import controller.Globals;
import controller.IVideoControllerObserver;

public class ControlBar  extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final int MAX_INT_VALUE = 2147483647;
	
	private Globals g;
	private IMediaPlayer player;
	private TimeLineBar navbar;
	private JScrollBar scrollBar;
	private JSlider slider;
	
	private boolean useLongConversion; // if video duration > MAX_INT_VALUE, devide/64
	
	protected ControlBar(TimeLineBar navbar, Globals g)
	{
		this.g = g;
		this.navbar = navbar;
		setLayout(new BorderLayout(0, 0));
		addSizeSlider();
		addScrollBar();
		this.g.getVideoController().register(new IVideoControllerObserver(){
			@Override
			public void videoInstantiated() {
				ControlBar.this.videoInstantiated();
			}
		});
		
		navbar.register(new INavbarObserver(){

			@Override
			public void visibleAreaChanged(final long begin, long end,
					final long visibleTime, float visiblePercentage) {
				// TODO Auto-generated method stub
				SwingUtilities.invokeLater(new Runnable(){
					
					@Override
					public void run() {
						if(!slider.getValueIsAdjusting())
						{
							slider.setValue(percentageFromVisibleTime(visibleTime));
						}
						if(!scrollBar.getValueIsAdjusting())
						{
							scrollBar.setValue(fromLong(begin)); 
							scrollBar.setVisibleAmount(fromLong(visibleTime));
						}
					}
				});
			}
			
		});
	}
	
	public void videoInstantiated()
	{
		this.player = g.getVideoController().getPlayer();
		useLongConversion = player.getMediaDuration() >= MAX_INT_VALUE;
		scrollBar.setMaximum((int) (player.getMediaDuration()));
		scrollBar.setVisibleAmount((int) player.getMediaDuration());
	}
	
	/**
	 * Adds a slider for zooming in on the time line
	 */
	private void addSizeSlider()
	{
		JPanel sliderPanel = new JPanel();
		FlowLayout layout = (FlowLayout)sliderPanel.getLayout();
		layout.setVgap(0);
		layout.setAlignment(SwingConstants.VERTICAL);
		
		slider = new JSlider();
		slider.setFocusable(false);
		slider.setPaintTicks(true);
		slider.setMinimum(1);
		slider.setMaximum(100);
		slider.setValue(100);
		slider.setToolTipText("Zoom in or out");
		sliderPanel.add(slider);
		sliderPanel.add(getLookingGlass());
		
		add(sliderPanel, BorderLayout.EAST);
		
		slider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				Thread stateChangerThread = new Thread(){
					public void run(){
						if(slider.getValueIsAdjusting())
						{
							navbar.setVisibleTime(visibleTimeFromPercentage(slider.getValue()));
							if(navbar.getCurrentStartVisibleTime() + navbar.getVisibleTime() > player.getMediaDuration())
							{
								navbar.setCurrentStartVisibleTime(player.getMediaDuration() - navbar.getVisibleTime());
							}
						}
					}
				};
				stateChangerThread.start();
			}
		});
	}
	
	private JLabel getLookingGlass(){
		ImageIcon glass = readLookingGlassIcon();
		JLabel glassLabel = new JLabel("100%");
		glassLabel.setIcon(glass);
		glassLabel.setToolTipText("Use the slider to zoom in or out on the time line");
		glassLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
		glassLabel.setPreferredSize(new Dimension(65, 27));
		
		final JLabel referencedLabel = glassLabel;
		navbar.register(new INavbarObserver() {
			
			@Override
			public void visibleAreaChanged(long begin, long end,
					long visibleTime, final float visiblePercentage) {
				referencedLabel.setText(String.format("%.0f%%", visiblePercentage));				
			}
			
		});
		return glassLabel;
	}
	
	/**
	 * Adds a scroll bar for scrolling through the time line
	 */
	private void addScrollBar()
	{
		scrollBar = new JScrollBar();
		scrollBar.setOrientation(JScrollBar.HORIZONTAL);
		scrollBar.setMinimum(0);
		scrollBar.setMaximum(slider.getMaximum());
		add(scrollBar, BorderLayout.CENTER);
		scrollBar.setVisibleAmount(slider.getMaximum());
		
		
		// listener
		scrollBar.addAdjustmentListener(new AdjustmentListener(){
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				
				final long newStartTime = fromInt(e.getValue());
				Thread valueAdjustedThread = new Thread()
				{
					public void run()
					{
						if(scrollBar.getValueIsAdjusting())
						{
							navbar.setCurrentStartVisibleTime(newStartTime);
						}
					}
				};
				valueAdjustedThread.start();
			}
		});
	}
	
	/**
	 * Roughly calculate the visible area expressed in milliseconds of the total 
	 * video by the visible percentage value.
	 * Warning: Only use when the slider has been adjusted, because of precision
	 * @param pct		Value for visible percentage
	 * @return
	 */
	private long visibleTimeFromPercentage(int pct)
	{
		return (long) ((float) pct * (float) player.getMediaDuration() * 0.01);
	}
	
	/**
	 * Calculate the integer value of the percentage of the total video that is
	 * visible.
	 * Warning: integer is not very precise. Only use for time slider
	 * @param time		The visible time duration
	 * @return			Visible percentage of total video
	 */
	private int percentageFromVisibleTime(long time)
	{
		return (int) Math.round((float) time / (float) player.getMediaDuration() * 100f);
	}
	
	/**
	 * Make sure a long value fits in an integer type
	 * @param value		long value
	 * @return			scaled integer value if necessary. Otherwise long value cast as int
	 */
	private int fromLong(long value)
	{
		return (useLongConversion) ? (int) (value / MAX_INT_VALUE) : (int) value;
	}
	
	/**
	 * Re-convert a converted int value to a long
	 * @param value		converted int value
	 * @return			original long value
	 */
	private long fromInt(int value)
	{
		return (useLongConversion) ? value * MAX_INT_VALUE : (long) value;
	}
	
	/**
	 * Method to read the looking glass icon for the zoom slider
	 * @return
	 */
	private static ImageIcon readLookingGlassIcon(){
		try {
			BufferedImage image = ImageIO.read(ControlBar.class.getResource("/img/looking_glass.png"));
			ImageIcon icon = new ImageIcon(image);
			return icon;
		} catch (IOException e) {
			return null;
		}
	}
}

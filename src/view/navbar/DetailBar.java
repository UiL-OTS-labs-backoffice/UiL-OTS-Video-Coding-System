package view.navbar;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;

import controller.Globals;
import view.navbar.PanelTimeframe;



public class DetailBar extends ABar{
	
	private static final long serialVersionUID = 1L;
	private static final int INDICATOR_WIDTH = 30;
	private ArrayList<JSeparator> hallmarks;
	private ArrayList<JSeparator> smallHallmarks;
	private ArrayList<JLabel> hallmarkLabels;
	
	/**
	 * Construct a new detail bar JPanel
	 * @param duration		Total video duration
	 * @param navbar		Reference to parent Navbar object
	 */
	protected DetailBar(Navbar navbar, Globals g)
	{
		super(navbar, g);
		setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		hallmarks = new ArrayList<JSeparator>();
		smallHallmarks = new ArrayList<JSeparator>();
		hallmarkLabels = new ArrayList<JLabel>();
		addResizeListener();
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void setIndicatorSettings()
	{
		indicator.setPreferredWidth(INDICATOR_WIDTH);
		indicator.setMargins(10);
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void paintTimeFrame(PanelTimeframe tf)
	{
		int left = xByTime(tf.getStart());
		int right = xByTime(tf.getEnd());
		
		int y = (tf.getType() == InformationPanel.TYPE_LOOK) ? getHeight()/2 + 20 : 35;
		
		Rectangle r = new Rectangle(left, y, right - left, (getHeight() /2) - 30) ;
		indicator.setBounds(getIndicatorRectangle());
		tf.setSize(r);
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void paintTimeFrames()
	{
		super.paintTimeFrames();
		repaintHallmarks();
	}

	/**
	 * {@inheritDoc}
	 */
	protected void repaintHallmarks()
	{
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				removeAllHallmarks();
				
				int amount = Math.round(getWidth() / 10f);
				for(int i = 0; i < amount; i++)
				{
					int x = (i+1) * 10;
					if(i % 10 == 0)
					{
						JSeparator sep = getHallmarkAtX(x, false);
						hallmarks.add(sep);
						add(sep);
						
						JLabel lbl = getHallmarkLabelAtX(x);
						hallmarkLabels.add(lbl);
						add(lbl);
						lbl.setForeground(Color.GRAY);
					} else { 
						JSeparator sep = getHallmarkAtX(x, true);
						smallHallmarks.add(sep);
						add(sep);
					}
				}
			}
			
		});
	}
	
	private void removeAllHallmarks()
	{
		while(!hallmarks.isEmpty()) 
		{
			remove(hallmarks.get(0));
			hallmarks.remove(0);
			
			remove(hallmarkLabels.get(0));
			hallmarkLabels.remove(0);
			
			remove(smallHallmarks.get(0));
			smallHallmarks.remove(0);
		}
		
		while(!smallHallmarks.isEmpty())
		{
			remove(smallHallmarks.get(0));
			smallHallmarks.remove(0);
		}
	}
	
	private JSeparator getHallmarkAtX(int x, boolean small)
	{
		Rectangle rct = new Rectangle(x, 20, 1, small ? 10 : getHeight()-21);
		JSeparator sep = new JSeparator();
		sep.setOrientation(JSeparator.VERTICAL);
		sep.setBounds(rct);
		sep.setVisible(true);
		sep.setForeground(Color.LIGHT_GRAY);
		
		return sep;
	}
	
	private JLabel getHallmarkLabelAtX(int x)
	{
		String timeLabel = String.format("%d", timeByX(x) + navbar.getCurrentStartVisibleTime());
		JLabel lbl = new JLabel(timeLabel, SwingConstants.CENTER);
		Rectangle lblrct = new Rectangle(x-50, 1, 100, 15);
		lbl.setBounds(lblrct);
		return lbl;
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected int xByTime(long time)
	{
		float tNorm = ((float) time - (float) navbar.getCurrentStartVisibleTime()) / ((float) navbar.getCurrentEndVisibleTime() - (float) navbar.getCurrentStartVisibleTime());
		return (int) (tNorm * getWidth());
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected long timeByX(int xCoord)
	{
		return (long)(((float) xCoord * 
			(float) (navbar.getCurrentEndVisibleTime() - navbar.getCurrentStartVisibleTime()) 
				/(float) getWidth()));
	}
	
	private void addResizeListener()
	{
		this.addComponentListener(new ComponentListener(){

			@Override
			public void componentResized(ComponentEvent e) {
				removeAllHallmarks();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}
	
}

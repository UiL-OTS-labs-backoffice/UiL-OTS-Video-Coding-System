package view.timeline.paneltimeframe;

import controller.Globals;
import view.player.IMediaPlayerListener;
import view.timeline.ABar;
import view.timeline.TimeLineBar;
import model.AbstractTimeFrame;
import model.TimeObserver.ITimeFrameObserver;

import java.awt.Color;
import java.awt.SystemColor;
import java.awt.image.BufferedImage;
import java.awt.TexturePaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 *	Each time frame has its own panel.
 */
public class PanelTimeframe extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private static final Color LOOK_COLOR = SystemColor.inactiveCaption;
	private static final Color TRIAL_COLOR = SystemColor.activeCaption;
	private static final Color TRIAL_INCOMPLETE_COLOR = new Color(189, 207,211);
	private static final Color LOOK_INCOMPLETE_COLOR = new Color(181,185,188);
	private static final Color TIMEOUT_COLOR = Color.RED;
	
	private static final int DETAIL_MARGIN = 5;
	private static final int OVERVIEW_MARGIN = 1;
	
	private AbstractTimeFrame tf;
	private JLabel labelbl, commentLbl;
	private ContextMenu contextMenu;
	private ABar pane;
	private Globals g;
	private TimeLineBar navbar;
	private PanelTimeframe panel;
	
	private TexturePaint slate;
	private int margin;
	
	private IMediaPlayerListener mpl;
	
	/**
	 * Constructor for a new Panel time frame tuple
	 * @param tf			The time frame
	 * @param parentPane	The pane this panel should be added to
	 * @param g				Reference to Globals instance
	 * @param navbar		Reference to navbar instance
	 */
	public PanelTimeframe(AbstractTimeFrame tf, ABar parentPane, Globals g, TimeLineBar navbar)
	{
		this.panel = this;
		this.tf = tf;
		this.pane = parentPane;
		this.g = g;
		this.navbar = navbar;
		
		this.margin = (pane.getType() == ABar.TYPE_DETAIL) ? DETAIL_MARGIN : OVERVIEW_MARGIN;
		
		tf.registerFrameListener(new ITimeFrameObserver(){

			@Override
			public void timeChanged(AbstractTimeFrame tf) {
				resize();
				if(mpl != null && tf.hasEnded())
				{
					deregisterMpl();
				}
			}

			@Override
			public void commentChanged(AbstractTimeFrame tf, String comment) {
				updateInfo();
			}
		});
		
		this.mpl = new IMediaPlayerListener(){

			@Override
			public void mediaStarted() { }

			@Override
			public void mediaPaused() { }

			@Override
			public void mediaTimeChanged() {
				timeChanged();
			}
		};
		
		
		panel.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX() + panel.getX();
				final long newTime = pane.timeByX(x);
				new Thread(){
					public void run()
					{
						Globals.getInstance().getVideoController().setMediaTime(newTime);
					}
				}.start();				
			}
			@Override
			public void mousePressed(MouseEvent e) { }
			@Override
			public void mouseReleased(MouseEvent e) { }
			@Override
			public void mouseEntered(MouseEvent e) { }
			@Override
			public void mouseExited(MouseEvent e) { }
			
		});
		
		if(!tf.hasEnded()){
			g.getVideoController().getPlayer().register(this.mpl);
		}
		
		createLayout();
	}
	
	private void deregisterMpl(){
		g.getVideoController().getPlayer().deregister(mpl);
	}
	
	private void timeChanged(){
		long time = Globals.getInstance().getVideoController().getMediaTime();
		long endTime = (tf.getEnd() >= 0L) ? Math.min(tf.getEnd(), time) : time;
		final Rectangle rect = pane.getTfRect(getStart(), endTime, tf.getType());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				panel.setSize(rect);
			}
		});
	}
	
	public void resize()
	{
		final Rectangle rect = pane.getTfRect(getStart(), getEnd(), tf.getType());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				panel.setSize(rect);
			}
		});
	}
		
	/**
	 * Method to ensure complete removal of Panel time frame from view
	 */
	public void remove()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				panel.removeAll();
				pane.remove(panel);
			}
		});
	}
	
	/**
	 * Creates a layout for the panel
	 */
	private void createLayout()
	{
		this.slate = new TexturePaint(getTexture(), new Rectangle(0,0,10,10));
		
		this.labelbl = new JLabel();
		this.commentLbl = new JLabel();
		this.commentLbl.setForeground(Color.DARK_GRAY);
		
		this.contextMenu = new ContextMenu(g, tf);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
				setBorder(new EmptyBorder(margin, margin, 0, 0));
				add(labelbl);
				add(commentLbl);
				setComponentPopupMenu(contextMenu);
				pane.add(panel);
			}
		});
	}
	
	/**
	 * Method to update the size of the time frame, if anything changed on the GUI
	 * @param size
	 */
	public void setSize(final Rectangle size)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setBounds(size);
				updateBorder();
				updateBackground();
				updateInfo();
				repaint();
			}
		});
	}
	
	/**
	 * Method to update the background based on what type the time frame is and if it has ended
	 */
	private void updateBackground()
	{
		if(tf.hasEnded())
		{
			setBackground((tf.getType() == AbstractTimeFrame.TYPE_LOOK) ? LOOK_COLOR : TRIAL_COLOR);
		} else {
			setBackground((tf.getType() == AbstractTimeFrame.TYPE_LOOK) ? LOOK_INCOMPLETE_COLOR : TRIAL_INCOMPLETE_COLOR);
		}
	}
	
	/**
	 * Method to update the labels, in case start or end time, or comments have changed
	 * @param index
	 */
	private void updateInfo()
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				int number = g.getController().getNumber(tf);
				int nrlength = Integer.toBinaryString(number).length();
				String label = String.format((tf.getType() == AbstractTimeFrame.TYPE_LOOK) ? "Look %d" : "Trial %d", number);
				String comment = tf.getComment();
				setToolTipText(label + ": " + ((tf.getComment() == null) ? "No comments added" : tf.getComment()));
				
				if(getWidth() < 40 + 5 * nrlength)
				{
					if(getWidth() > 5 * nrlength){
						label = Integer.toString(number);
						comment = "";
					} else {
						label = "";
						comment = "";
					}
				}
				
				labelbl.setText(label);
				commentLbl.setText((pane.getType() == ABar.TYPE_DETAIL) ? comment : "");
			}
		});
	}
	
	/**
	 * Method to update the margin to show text even if half the panel is hidden
	 */
	private void updateBorder()
	{
		int left = margin;
		if(pane.getType() == ABar.TYPE_DETAIL && tf.getBegin() <  navbar.getCurrentStartVisibleTime())
		{
			long end = (tf.hasEnded()) ? tf.getEnd() : g.getVideoController().getMediaTime();
			left = (int) Math.round(
					(double) (navbar.getCurrentStartVisibleTime() - tf.getBegin()) / 
					(double) (end - tf.getBegin()) 
					* getWidth() + left); 
		}
		
		setBorder(new EmptyBorder(margin, left, 0, 0));
	}
	
	/**
	 * Paints the timeout area once the frame is painted
	 */
	public void paintComponent(final Graphics g)
	{
		super.paintComponent(g);
		long timeout = tf.getTimeout();
		if(timeout > -1l)
		{
			int x = xByTime(timeout);
			Graphics2D g2 = (Graphics2D) g;
			Rectangle2D rect = new Rectangle2D.Double(x, 0, getWidth() - x, getHeight());
			
			g2.setPaint(slate);
			g2.fill(rect);
		}
	}
	
	/**
	 * Method that returns the start time of the Panel Time frame
	 * @return	Start time of time frame
	 */
	public long getStart()
	{
		return tf.getBegin();
	}
	
	/**
	 * Method that returns the end time of the time frame
	 * @return	End time of time frame
	 */
	public long getEnd()
	{
		return tf.getEnd();
	}
	
	/**
	 * Method to get the type of the current time frame
	 * @return	integer Type of time frame (AbstractTimeFrame.TYPE...)
	 */
	public int getType()
	{
		return tf.getType();
	}
	
	/**
	 * Calculation method for the x coordinate on the panel given a certain time
	 * @param time		A certain time
	 * @return			The x coordinate that corresponds to the time 
	 */
	private int xByTime(long time)
	{
		long end = tf.hasEnded() ? tf.getEnd() : g.getVideoController().getMediaTime(); 
		int x = Math.round(((float) time - (float) tf.getBegin()) / ((float) end - (float) tf.getBegin()) * (float) getWidth());
		return x;
	}
	
	/**
	 * Creates the texture for the timeout area
	 * @return	BufferedImage of 10x10 pixels containing a texture for the timeout area
	 */
	private static BufferedImage getTexture()
	{
		BufferedImage bi = new BufferedImage(10,10, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D) bi.getGraphics();
		Line2D diagonal = new Line2D.Float(0, 0, 10, 10);
		g2.setColor(TIMEOUT_COLOR);
		g2.draw(diagonal);
		return bi;
	}
}

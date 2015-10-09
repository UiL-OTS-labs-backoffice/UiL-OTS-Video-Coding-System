package view.navbar;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;

import controller.Globals;
import view.navbar.PanelTimeframe;

public class OverviewBar extends ABar {

	private static final long serialVersionUID = 1L;
	private static final int INDICATOR_WIDTH = 10;
	private static final int BOX_BORDER_WIDTH = 2;
	
	private JPanel overviewBox;
	
	private int xStart;
	private long visibleEndTime;
	private boolean resizeRight = false, resizeLeft = false;
	private long minVisibleTime;
	
	/**
	 * Constructor method for OverviewBar object
	 * @param navbar		Reference to the Navbar object
	 */
	protected OverviewBar(Navbar navbar, Globals g)
	{
		super(navbar, g);
		minVisibleTime = 0;
		addOverviewBox();
	}
	
	public void videoInstantiated()
	{
		super.videoInstantiated();
		minVisibleTime = (long) Math.round((float) player.getMediaDuration() / 100.0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void componentResized()
	{
		super.componentResized();
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				paintBox();
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void paintTimeFrame(PanelTimeframe tf)
	{
		int left = xByTime(tf.getStart());
		int right = xByTime(tf.getEnd());
		
		int y = (tf.getType() == InformationPanel.TYPE_LOOK) ? getHeight()/2 : BOX_BORDER_WIDTH;
		
		Rectangle r2 = new Rectangle(left, y, right-left, getHeight()/2 - BOX_BORDER_WIDTH);
		tf.setSize(r2);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected int xByTime(long time)
	{
		return (int) ((float) time * (float) getWidth() / (float) player.getMediaDuration());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected long timeByX(int xCoord) {
		return (long) ((float) xCoord / (float) getWidth() * player.getMediaDuration());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void setIndicatorSettings() {
		indicator.setPreferredWidth(INDICATOR_WIDTH);
		indicator.setMargins(2);
	}
	
	/******************************************
	 * 
	 * Class extension only functions
	 * 
	 *****************************************/
	
	
	/**
	 * Resize- and paint the overview box
	 */
	public void paintBox() {
		int left = (int) ((float) navbar.getCurrentStartVisibleTime() / (float) player.getMediaDuration() * (float) getWidth());
		int right = (int) ((float) navbar.getVisiblePercentage() * (float) getWidth() / 100.0);
		Rectangle rBox = new Rectangle(left, 0, right, getHeight());
		overviewBox.setBounds(rBox);
	}
	
	/**
	 * Add the panel that indicates what part of the timeline a user is seeing
	 */
	private void addOverviewBox()
	{
		overviewBox = new JPanel();
		overviewBox.setBorder(new MatteBorder(BOX_BORDER_WIDTH,BOX_BORDER_WIDTH,BOX_BORDER_WIDTH,BOX_BORDER_WIDTH, new Color(152,60,48,100)));
		overviewBox.setOpaque(false);
		add(overviewBox);
		
		overviewBox.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
				xStart = e.getXOnScreen();
				if(e.getX() < BOX_BORDER_WIDTH){
					resizeLeft = true;
					resizeRight = false;
					visibleEndTime = navbar.getCurrentEndVisibleTime();
				}else if (e.getX() > overviewBox.getWidth() - BOX_BORDER_WIDTH)
				{
					resizeRight = true;
					resizeLeft = false;
				}
				else {
					resizeRight = false;
					resizeLeft = false;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
	
		overviewBox.addMouseMotionListener(new MouseMotionListener(){
	
			@Override
			public void mouseDragged(MouseEvent e) {
				long dt = timeByX(e.getXOnScreen() - xStart);
				if(resizeRight)
				{
					long net = navbar.getCurrentEndVisibleTime() + dt;
					if(net <= navbar.getCurrentStartVisibleTime() + minVisibleTime) net = navbar.getCurrentStartVisibleTime() + minVisibleTime;
					else if(net > player.getMediaDuration()) net = player.getMediaDuration();
					navbar.setCurrentEndVisibleTime(net);
				} else if (resizeLeft)
				{
					long nst = navbar.getCurrentStartVisibleTime() + dt;
					if(nst < 0) nst = 0;
					else if(nst >= navbar.getCurrentEndVisibleTime() - minVisibleTime) nst = navbar.getCurrentEndVisibleTime() - minVisibleTime;
					navbar.setCurrentStartVisibleTime(nst);
					navbar.setCurrentEndVisibleTime(visibleEndTime);
				} else {
					long nt = navbar.getCurrentStartVisibleTime() + dt;
					if(nt < 0) nt = 0;
					else if(nt + navbar.getVisibleTime() > player.getMediaDuration()) nt = player.getMediaDuration() - navbar.getVisibleTime();
					navbar.setCurrentStartVisibleTime(nt);
				}
				
				xStart = e.getXOnScreen();
			}
	
			@Override
			public void mouseMoved(MouseEvent e) {
				if(e.getX() < BOX_BORDER_WIDTH)
				{
					overviewBox.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
				} else if (e.getX() > overviewBox.getWidth() - BOX_BORDER_WIDTH)
				{
					overviewBox.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
				} else {
					overviewBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			}
			
		});
	}
}

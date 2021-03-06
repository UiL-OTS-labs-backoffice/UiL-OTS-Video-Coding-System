package view.timeline;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;

import model.AbstractTimeFrame;
import controller.Globals;

public class OverviewBar extends ABar {

	private static final long serialVersionUID = 1L;
	private static final int BOX_BORDER_WIDTH = 2;
	
	public static final int TYPE = ABar.TYPE_OVERVIEW;
	
	private JPanel overviewBox;
	
	private int xStart;
	private long visibleEndTime;
	private boolean resizeRight = false, resizeLeft = false;
	
	/**
	 * Constructor method for OverviewBar object
	 * @param navbar		Reference to the TimeLineBar object
	 */
	protected OverviewBar(TimeLineBar navbar, Globals g)
	{
		super(navbar, g);
		minVisibleTime = 0;
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				addOverviewBox();
				setBorder(new MatteBorder(1, 1, 1, 1,
						(Color) new Color(0, 0, 0)));
			}
		});
	}
	
	public Rectangle getTfRect(long start, long end, int type)
	{
		int left = xByTime(start);
		int right = xByTime((end == -1L) ? Globals.getInstance().getVideoController().getMediaTime() : end);
		int y = (type == AbstractTimeFrame.TYPE_LOOK) ? getHeight() / 2
				: BOX_BORDER_WIDTH;
		return new Rectangle(left, y, right - left, getHeight()
				/ 2 - BOX_BORDER_WIDTH);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int xByTime(long time)
	{
		return (int) ((float) time * (float) getWidth() / (float) player.getMediaDuration());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long timeByX(int xCoord) {
		return (long) ((float) xCoord / (float) getWidth() * player.getMediaDuration());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long timeByXinView(int xCoord) {
		return timeByX(xCoord);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getType()
	{
		return TYPE;
	}
	
	/******************************************
	 * 
	 * Class extension only functions
	 * 
	 *****************************************/
	
	
	/**
	 * Resize- and paint the overview box
	 */
	@Override
	public void paintBox() {
		int left = (int) ((float) navbar.getCurrentStartVisibleTime()
				/ (float) player.getMediaDuration() * (float) getWidth());
		int right = (int) ((float) navbar.getVisiblePercentage()
				* (float) getWidth() / 100.0);
		final Rectangle rBox = new Rectangle(left, 0, right, getHeight());
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				overviewBox.setBounds(rBox);
			}
		});
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
				int x = e.getX() + overviewBox.getX();
				final long newTime = OverviewBar.this.timeByXinView(x);
				new Thread(){
					public void run()
					{
						vc.setMediaTime(newTime);
					}
				}.start();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				final int xStartInThread = e.getXOnScreen();
				final int eX = e.getX();
				Thread mousePressedT = new Thread()
				{
					public void run()
					{
						xStart = xStartInThread;
						if(eX < BOX_BORDER_WIDTH){
							resizeLeft = true;
							resizeRight = false;
							visibleEndTime = navbar.getCurrentEndVisibleTime();
						}else if (eX > overviewBox.getWidth() - BOX_BORDER_WIDTH)
						{
							resizeRight = true;
							resizeLeft = false;
						}
						else {
							resizeRight = false;
							resizeLeft = false;
						}
					}
				};
				mousePressedT.start();
				
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
				final long dt = timeByX(e.getXOnScreen() - xStart);
				final int XonScreenFinal = e.getXOnScreen();
				
				Thread mouseDraggingThread = new Thread()
				{
					public void run()
					{
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
						xStart = XonScreenFinal;
					}
				};
				mouseDraggingThread.start();
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

package view.navbar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.AbstractTimeContainer;
import model.AbstractTimeFrame;
import model.TimeObserver.ITimeContainerObserver;
import controller.Globals;
import controller.IVideoControllerObserver;
import controller.IVideoControls;
import view.navbar.listeners.TimeMouseListener;
import view.navbar.listeners.TimeMouseMotionListener;
import view.navbar.paneltimeframe.PanelTimeframe;
import view.navbar.utilities.INavbarObserver;
import view.player.IMediaPlayer;

public abstract class ABar extends JPanel implements ITimeContainerObserver, INavbarObserver, IVideoControllerObserver{

	private static final long serialVersionUID = 1L;
	private static final int ALLOWED_DRAG_MARGIN = 10;
	
	public static final int TYPE_DETAIL = 1;
	public static final int TYPE_OVERVIEW = 2;
	
	protected Globals g;
	protected IMediaPlayer player;
	protected IVideoControls vc;
	
	protected Navbar navbar;
	protected HashMap<AbstractTimeFrame, PanelTimeframe> timeFrames = 
			new HashMap<AbstractTimeFrame, PanelTimeframe>();
	
	protected TimeIndicator indicator;
	
	/**
	 * Constructor method creates a new ABar object.
	 * @param duration	Total video duration
	 * @param navbar	Reference to parent Navbar object
	 */
	protected ABar(Navbar navbar, Globals g)
	{
		this.g = g;
		this.vc = g.getVideoController();
		vc.register(this);
		this.navbar = navbar;
		this.setLayout(null);
		this.setBackground(new Color(220,227,232));
		navbar.register(this);
	}
	
	protected void loadTimeframes()
	{
		for(AbstractTimeFrame tr : Globals.getInstance().getExperimentModel().getItems())
		{
			AbstractTimeContainer trial = (AbstractTimeContainer) tr;
			trial.registerContainerListener(this);
			timeFrames.put(trial, new PanelTimeframe(trial, this, g, navbar));
			for(AbstractTimeFrame l : trial.getItems())
			{
				PanelTimeframe ptf = new PanelTimeframe(l, this, g, navbar);
				timeFrames.put(l, ptf);
				ptf.resize();
			}
		}
	}
	
	@Override
	public void itemAdded(AbstractTimeContainer container, AbstractTimeFrame item, int itemNumber)
	{
		if(item.getType() == model.AbstractTimeFrame.TYPE_TRIAL)
		{
			((AbstractTimeContainer) item).registerContainerListener(this);
		}
		timeFrames.put(item, new PanelTimeframe(item, this, g, navbar));
	}
	
	@Override
	public void itemRemoved(AbstractTimeContainer container, AbstractTimeFrame item)
	{
		timeFrames.get(item).remove();
		timeFrames.remove(item);
	}
	
	@Override
	public void numberOfItemsChanged(AbstractTimeContainer container) { }
	
	@Override
	public void videoInstantiated()
	{
		g.getExperimentModel().registerContainerListener(this);
		player = g.getVideoController().getPlayer();
		
		indicator = new TimeIndicator(this, player);
		
		loadTimeframes();
		
		addMouseListener(new TimeMouseListener(this, navbar, vc));
		addMouseMotionListener(new TimeMouseMotionListener(this, navbar, vc));
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.RED);
		Line2D l = indicator.getLine();
//		g2.draw(
//				l
//			);
	}
	
	/**
	 * Method to invoke when the window has changed by zooming,
	 * scrolling or resizing. Repaints all time frames
	 */
	protected void paintTimeFrames()
	{
		for(PanelTimeframe ptf : timeFrames.values())
		{
			ptf.resize();
		}
	}
	
	/**
	 * Method to call if media time changed. Updates indicators for
	 * current media time
	 */
	public void mediaTimeChanged(long time)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				indicator.repositionIndicator();
			}
		});
	}
	
	/**
	 * True if a clicked point is within the draggable area of the time
	 * indicator
	 * @param e
	 * @return
	 */
	public boolean draggableArea(MouseEvent e)
	{
		int mediaX = xByTime(player.getMediaTime());
		return e.getX() > mediaX - ALLOWED_DRAG_MARGIN && e.getX() < mediaX + ALLOWED_DRAG_MARGIN; 
	}
	
	/**
	 * Returns the rectangle bounds for a time frame, calculated
	 * based on the start and end time and the type of time frame
	 * @param start	Start time of the time frame
	 * @param end	End time of the time frame
	 * @param type	Type of the time frame
	 * @return		Rectangle containing new dimensions for time frame
	 */
	public abstract Rectangle getTfRect(long start, long end, int type);
	
	/**
	 * Calculate the x coordinate for a certain video time
	 * @param time 	the video time
	 * @return	int giving the x coordinate that corresponds 
	 * 				with the time
	 */
	public abstract int xByTime(long time);
	
	/**
	 * Get the video time that corresponds with a certain x coordinate
	 * in the current view
	 * @param xCoord	The x coordinate in the current view
	 * @return			Time corresponding to x coordinate
	 */
	public abstract long timeByX(int xCoord);
	
	public abstract long timeByXinView(int xCoord);
	
	/**
	 * Get the type of this component
	 * @return	1 if detail, 2 if overview
	 */
	public abstract int getType();
	
	/**
	 * Events that are unnecessary for this object
	 */
	public void playerStarted() { };
	public void playerPaused() { };
}

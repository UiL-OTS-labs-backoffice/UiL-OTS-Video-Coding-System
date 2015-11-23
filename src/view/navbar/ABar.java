package view.navbar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.AbstractTimeContainer;
import model.AbstractTimeFrame;
import controller.Globals;
import controller.IVideoControls;
import view.navbar.listeners.TimeMouseListener;
import view.navbar.listeners.TimeMouseMotionListener;
import view.navbar.paneltimeframe.PanelTimeframe;
import view.player.IMediaPlayer;

public abstract class ABar extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final int ALLOWED_DRAG_MARGIN = 10;
	
	public static final int TYPE_DETAIL = 1;
	public static final int TYPE_OVERVIEW = 2;
	
	protected Globals g;
	protected IMediaPlayer player;
	protected IVideoControls vc;
	
	protected Navbar navbar;
	protected ConcurrentHashMap<AbstractTimeFrame, PanelTimeframe> timeFrames = 
			new ConcurrentHashMap<AbstractTimeFrame, PanelTimeframe>();
	
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
		this.navbar = navbar;
		this.setLayout(null);
		this.setBackground(new Color(220,227,232));
	}
	
	protected void loadTimeframes()
	{
		for(AbstractTimeFrame tr : Globals.getInstance().getExperimentModel().getItems())
		{
			AbstractTimeContainer trial = (AbstractTimeContainer) tr;
			timeFrames.put(trial, new PanelTimeframe(trial, this, g, navbar));
			for(AbstractTimeFrame l : trial.getItems())
			{
				timeFrames.put(l, new PanelTimeframe(l, this, g, navbar));
			}
		}
	}
	
	/**
	 * Add a new time frame to the view
	 * @param tf	Reference to abstract time frame object
	 */
	public void addTimeFrame(AbstractTimeFrame tf)
	{
		timeFrames.put(tf, new PanelTimeframe(tf, this, g, navbar));
	}
	
	/**
	 * Remove a time frame from the view
	 * @param tf 	Reference to abstract time frame object that is to be removed
	 */
	public void removeTimeFrame(AbstractTimeFrame tf)
	{
		timeFrames.get(tf).remove();
		timeFrames.remove(tf);
	}
	
	public void videoInstantiated()
	{
		player = g.getVideoController().getPlayer();
		
		indicator = new TimeIndicator(this, player);
		
		loadTimeframes();
		paintTimeFrames();
		
		addMouseListener(new TimeMouseListener(this, navbar, vc));
		addMouseMotionListener(new TimeMouseMotionListener(this, navbar, vc));
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.RED);
		g2.draw(indicator.getLine());
	}
	
	/**
	 * Method to invoke when the window has changed by zooming,
	 * scrolling or resizing. Repaints all time frames
	 */
	protected void paintTimeFrames()
	{
		for(AbstractTimeFrame tf : timeFrames.keySet())
		{
			paintTimeFrame(timeFrames.get(tf));
		}
	}
	
	/**
	 * Method to call if media time changed. Updates indicators for
	 * current media time
	 */
	public void mediaTimeChanged()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				indicator.repositionIndicator();
				paintTimeFrames();
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
	 * Resizes a single time frame based on current visible area and 
	 * width of the canvas
	 * @param tf	time frame to be resized
	 */
	protected abstract void paintTimeFrame(PanelTimeframe tf);
	
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
}

package view.timeline;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JPanel;

import model.AbstractTimeContainer;
import model.AbstractTimeFrame;
import model.TimeObserver.ITimeContainerObserver;
import model.TimeObserver.ITimeContainerSubject;
import controller.Globals;
import controller.IVideoControllerObserver;
import controller.IVideoControls;
import view.player.IMediaPlayer;
import view.player.IMediaPlayerListener;
import view.timeline.listeners.TimeMouseListener;
import view.timeline.listeners.TimeMouseMotionListener;
import view.timeline.paneltimeframe.PanelTimeframe;
import view.timeline.utilities.INavbarObserver;

public abstract class ABar extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final int ALLOWED_DRAG_MARGIN = 10;
	
	public static final int TYPE_DETAIL = 1;
	public static final int TYPE_OVERVIEW = 2;
	
	protected Globals g;
	protected IMediaPlayer player;
	protected IVideoControls vc;
	private IVideoControllerObserver vcObserver;
	
	protected TimeLineBar navbar;
	protected HashMap<AbstractTimeFrame, PanelTimeframe> timeFrames = 
			new HashMap<AbstractTimeFrame, PanelTimeframe>();
	
	protected TimeIndicator indicator;
	protected long minVisibleTime;
	
	/**
	 * Constructor method creates a new ABar object.
	 * @param duration	Total video duration
	 * @param navbar	Reference to parent TimeLineBar object
	 */
	protected ABar(TimeLineBar navbar, Globals g)
	{
		this.g = g;
		this.vc = g.getVideoController();
		this.navbar = navbar;
		this.setLayout(null);
		this.setBackground(new Color(220,227,232));
		
		registerInstantiatedObserver();
		registerNavbarObserver();
	}
	
	/**
	 * Method to load the already existing time frames from the model.
	 * SHould be called only once
	 */
	protected void loadTimeframes()
	{
		for(AbstractTimeFrame tr : Globals.getInstance().getExperimentModel().getItems())
		{
			AbstractTimeContainer trial = (AbstractTimeContainer) tr;
			registerTimeContainerObserver(trial);
			timeFrames.put(trial, new PanelTimeframe(trial, this, g, navbar));
			for(AbstractTimeFrame l : trial.getItems())
			{
				PanelTimeframe ptf = new PanelTimeframe(l, this, g, navbar);
				timeFrames.put(l, ptf);
			}
		}
	}
	
	/**
	 * Method to paint the indicat for this component
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		indicator.repositionIndicator();
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
	
	public void paintBox() { }
	
	
	/**********************************
	 * 
	 * EVENT LISTENERS AND OBSERVERS
	 * 
	 *********************************/
	
	/**
	 * Method to add an event observer to the Video Controller
	 * that listens for video instantiation. 
	 * This is a separate observer from the other listener 
	 * methods, because some other actions should only
	 * be executed after video instantiation 
	 */
	private void registerInstantiatedObserver(){
		vcObserver = new IVideoControllerObserver(){
			
			@Override
			public void videoInstantiated() { 
				ABar.this.videoInstantiated(); 
			}
		};
		this.vc.register(vcObserver);
	}
	
	/**
	 * Method to deregister the observer that listens
	 * for video instantiation and registers an observer
	 * that listens for media time changed events
	 */
	private void registerVCObserver(){
		this.vc.deregister(this.vcObserver);
		this.player.register(new IMediaPlayerListener(){

			@Override
			public void mediaTimeChanged() {
				repaint();
			}

			@Override
			public void mediaStarted() { }
			@Override
			public void mediaPaused() { }
		});
		this.vc.register(this.vcObserver);
	}
	
	/**
	 * Method to register listeners for the navbar events
	 */
	private void registerNavbarObserver(){
		navbar.register(new INavbarObserver(){
			
			private final Object MUTEX = new Object();
			
			@Override
			public void visibleAreaChanged(long begin, long end,
				long visibleTime, float visiblePercentage) {
				final HashMap<AbstractTimeFrame, PanelTimeframe> ptfLocal;
				synchronized (MUTEX) {
					ptfLocal = new HashMap<AbstractTimeFrame, PanelTimeframe>(timeFrames);
				}
				new Thread() {
					public void run(){
						for(PanelTimeframe ptf : ptfLocal.values()){
							ptf.resize();
						}
						repaint();
						paintBox();
					}
				}.start();
			}
			
		});
	}
	
	/**
	 * Method to register listeners to the experiment model
	 */
	private void registerTimeContainerObserver(ITimeContainerSubject subject){
		subject.registerContainerListener(new ITimeContainerObserver(){

			@Override
			public void itemAdded(AbstractTimeContainer container, AbstractTimeFrame item, int itemNumber)
			{
				if(item.getType() == model.AbstractTimeFrame.TYPE_TRIAL)
				{
					((AbstractTimeContainer) item).registerContainerListener(this);
				}
				timeFrames.put(item, new PanelTimeframe(item, ABar.this, g, navbar));
			}
			
			@Override
			public void itemRemoved(AbstractTimeContainer container, AbstractTimeFrame item)
			{
				timeFrames.get(item).remove();
				timeFrames.remove(item);
			}
			
			@Override
			public void numberOfItemsChanged(AbstractTimeContainer container) { }
		});
	}
	
	/**
	 * Method to execute for the ABar class, once the video is instantiated
	 */
	private void videoInstantiated()
	{
		player = g.getVideoController().getPlayer();
		indicator = new TimeIndicator(this, player);
		minVisibleTime = (long) Math.round((float) player.getMediaDuration() / 100.0);
		
		loadTimeframes();
		
		addMouseListener(new TimeMouseListener(this, navbar, vc));
		addMouseMotionListener(new TimeMouseMotionListener(this, navbar, vc));
		registerTimeContainerObserver(g.getExperimentModel());
		registerVCObserver();
		
		this.addComponentListener(new ComponentListener(){

			@Override
			public void componentResized(ComponentEvent e) {
				paintTimeFrames();
			}

			@Override
			public void componentMoved(ComponentEvent e) { }

			@Override
			public void componentShown(ComponentEvent e) { }

			@Override
			public void componentHidden(ComponentEvent e) { }
		});
	}
	
	
}

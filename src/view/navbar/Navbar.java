package view.navbar;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.Look;
import model.Trial;
import view.navbar.utilities.INavbarObserver;
import view.navbar.utilities.INavbarSubject;
import view.player.IMediaPlayer;
import view.player.IMediaPlayerListener;
import controller.Globals;
import controller.IVideoControllerObserver;
import controller.IVideoControls;

/**
 * Navbar is the controller class for all the time bar elements in the view
 */
public class Navbar extends JPanel implements INavbarSubject{

	private static final long serialVersionUID = 1L;
	
	private List<INavbarObserver> observers;
	private final Object MUTEX = new Object();
	
	private Globals g;
	private IMediaPlayer player;
	private InformationPanel information;
	private ControlBar controls;
	
	private long visibleTime;
	private long currentStartVisibleTime;
	private long currentEndVisibleTime;
	private float visiblePercentage;
	private boolean isDragging;
	
	/**
	 * Constructor
	 * @param g 	Reference to Globals instance
	 */
	public Navbar(Globals g)
	{
		this.g = g;
		this.g.getVideoController().register(new IVideoControllerObserver(){
			@Override
			public void videoInstantiated() {
				Navbar.this.videoInstantiated();
			}
		});
		this.observers = new ArrayList<INavbarObserver>();
		this.currentStartVisibleTime = 0;
		createLayout();
	}
	
	/**
	 * Private method that creates the layout of the component
	 * @param g
	 */
	private void createLayout()
	{
		information = new InformationPanel(this, g);
		controls = new ControlBar(this, g);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setLayout(new BorderLayout(0, 0));
				add(information, BorderLayout.CENTER);
				add(controls, BorderLayout.SOUTH);
				addComponentListener(new ComponentListener(){
					@Override
					public void componentResized(ComponentEvent e) {
						final Navbar com = (Navbar) e.getComponent();
						new Thread(){
							public void run()
							{
								com.componentResized();
							}
						}.start();
					}

					@Override
					public void componentMoved(ComponentEvent e) { }

					@Override
					public void componentShown(ComponentEvent e) { }

					@Override
					public void componentHidden(ComponentEvent e) { }
					
				});
			}
		});
	}
	
	/**
	 * Method to call after the video player is loaded, which then
	 * creates references to the video player and starts the single
	 * thread executor
	 */
	public void videoInstantiated()
	{
		this.player = g.getVideoController().getPlayer();
		this.visibleTime = player.getMediaDuration();
		this.currentEndVisibleTime = player.getMediaDuration();
		
		this.player.register(new IMediaPlayerListener(){

			@Override
			public void mediaStarted() { }
			@Override
			public void mediaPaused() { }

			@Override
			public void mediaTimeChanged() {
				final IVideoControls vc = Globals.getInstance().getVideoController();
				final long time 	= vc.IsLoaded() ? vc.getMediaTime() : 1;
				final long begin 	= getCurrentStartVisibleTime();
				final long end 		= getCurrentEndVisibleTime();
				final long total 	= vc.IsLoaded() ? vc.getMediaDuration() : 1;
				final long visible 	= getVisibleTime();
				
				SwingUtilities.invokeLater(new Runnable(){
					
					@Override
					public void run() {
						if(isDragging() || vc.IsLoaded() && vc.isPlaying() && begin < time && time < end) {
							if(time > end - Math.round(visible * .3f) && end < total)
							{
								setCurrentStartVisibleTime(begin + 250);
							} else if (time < begin + Math.round(visible * .3f) && begin > 0)
							{
								setCurrentStartVisibleTime(begin - 250);
							}
						}
						updateButtons();
					}
				});
			}
			
		});
		
		new DebugInfo(this, g.getVideoController());
	}
	
	/**
	 * Updates the button text and state
	 * @param tnr		Current trial number
	 * @param t			Current trial
	 * @param lnr		Current look number
	 * @param l			Current look
	 * @param time		Current time
	 */
	public void updateButtons()
	{
		long time = g.getVideoController().getMediaTime();
		int tnr = g.getExperimentModel().getItemForTime(time);
		
		boolean nt = g.getExperimentModel().canAddItem(time) >= 0 & tnr <= 0;
		boolean et = false, nl = false, el = false;
		boolean tm = false; // Timeout?
		int lnr = 0;
		if(tnr != 0)
		{
			Trial t = (Trial) g.getExperimentModel().getItem(Math.abs(tnr));
			lnr = t.getItemForTime(time);
			et = t.canEnd(time) && lnr <= 0;
			nl = t.canAddItem(time) >= 0 && lnr <= 0;
			
			if(lnr != 0)
			{
				Look l = (Look) t.getItem(Math.abs(lnr));
				tm = tnr > 0 && l.getEnd() > -1 && time - l.getEnd() > g.getExperimentModel().getTimeout() && g.getExperimentModel().getUseTimeout();
				el = tnr > 0 && l.canEnd(time);
			}
		}
		
		g.getEditor().updateButtons(tnr, lnr, nt, et, nl, el, tnr > 0, lnr > 0);
		g.getEditor().getBottomBar().setTimeoutText(tm);
	}
	
	/**
	 * Method to call if the component was resized, because sub-panels don't
	 * always listen to this correctly.
	 * The method makes sure all panels are redrawn in the correct proportions
	 */
	public void componentResized()
	{
		information.componentResized();
	}
	
	/**
	 * Method to call if the visible area changed
	 */
	public void visibleAreaChanged()
	{
		List<INavbarObserver> observersLocal;
		synchronized(MUTEX) {
			observersLocal = new ArrayList<INavbarObserver>(this.observers);
		}
		for(INavbarObserver obj : observersLocal){
			obj.visibleAreaChanged(
					this.currentStartVisibleTime, 
					this.currentEndVisibleTime, 
					this.visibleTime, 
					this.visiblePercentage
				);
		}
	}
	
	/**
	 * Method to change the first time point of the video that
	 * is currently visible in the detail view bar
	 * @param time	New start point for visible time
	 */
	public void setCurrentStartVisibleTime(long time)
	{
		this.currentStartVisibleTime = time;
		this.currentEndVisibleTime = time + visibleTime;
		visibleAreaChanged();
	}
	
	/**
	 * Get method for the first point in the time of the video that is
	 * visible in the detail view bar
	 * @return 	long 	first visible point in time in detail view bar
	 */
	public long getCurrentStartVisibleTime()
	{
		return currentStartVisibleTime;
	}
	
	/**
	 * Method to change the last time point of the video that is visible
	 * in the detail view bar
	 * @param time 	The new current time point of the video that is the last
	 * 				visible time point
	 */
	public void setCurrentEndVisibleTime(long time)
	{
		this.currentEndVisibleTime = time;
		this.visibleTime = this.currentEndVisibleTime - this.currentStartVisibleTime;
		calculateVisiblePercentage();
		visibleAreaChanged();
	}
	
	/**
	 * Get method for the last point in the time of the video that is
	 * visible in the detail view bar
	 * @return 	long 	last visible point in time in detail view bar
	 */
	public long getCurrentEndVisibleTime()
	{
		return this.currentEndVisibleTime;
	}
	
	/**
	 * Method to change the amount of time that is visible from the current start
	 * point (i.e. when zooming in or out occurs)
	 * @param time	amount of visible time in the detail view bar
	 */
	public void setVisibleTime(long time)
	{
		this.visibleTime = time;
		this.currentEndVisibleTime = this.currentStartVisibleTime + time;
		calculateVisiblePercentage();
		visibleAreaChanged();
	}
	
	/**
	 * Get method for the total time that is visible in the detail view bar
	 * @return long 	amount of time that is visible in the detail view bar
	 */
	public long getVisibleTime()
	{
		return this.visibleTime;
	}
	
	/**
	 * Get method for the percentage of time of the total video duration that
	 * is currently visible in the detail view bar
	 * @return	float 	percentage of total time that is visible in detail view
	 * 					bar
	 */
	public float getVisiblePercentage()
	{
		return visiblePercentage;
	}
	
	/**
	 * Private method that calculates the percentage of visible time out of the total
	 * media duration from the amount of time that is visible
	 */
	private void calculateVisiblePercentage()
	{
		visiblePercentage = (float) visibleTime / (float) player.getMediaDuration() * 100f;
	}
	
	/**
	 * If the overview bar is used to change the part of the video that is visible
	 * in the detail view bar, isDragging should be true
	 * @param isDragging 	Weather or not this is the case
	 */
	public void setIsDragging(boolean isDragging)
	{
		this.isDragging = isDragging;
	}
	
	/**
	 * True iff the overview bar is used to change what part of the video is visible
	 * in the detail view bar
	 * @return  True iff this is the case
	 */
	public boolean isDragging()
	{
		return this.isDragging;
	}

	@Override
	public void register(INavbarObserver obj) {
		if(obj == null) throw new NullPointerException("Null Observer");
		synchronized(MUTEX) {
			if(!this.observers.contains(obj)) this.observers.add(obj);
		}
	}

	@Override
	public void deregister(INavbarObserver obj) {
		synchronized(MUTEX) {
			observers.remove(obj);
		}
	}
}

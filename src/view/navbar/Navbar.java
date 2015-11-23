package view.navbar;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import model.Look;
import model.Trial;
import view.player.IMediaPlayer;
import controller.Globals;
import controller.IVideoControls;

/**
 * Navbar is the controller class for all the time bar elements in the view
 */
public class Navbar extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private Globals g;
	private IMediaPlayer player;
	private InformationPanel information;
	private ControlBar controls;
	
	private long visibleTime;
	private long currentStartVisibleTime;
	private long currentEndVisibleTime;
	private float visiblePercentage;
	private boolean isDragging;
	
	private final ScheduledExecutorService executorService = 
			Executors.newSingleThreadScheduledExecutor();
	
	private UpdateScrollable updateScrollable;
	
	/**
	 * Constructor
	 * @param g 	Reference to Globals instance
	 */
	public Navbar(Globals g)
	{
		this.g = g;
		createLayout();
		this.currentStartVisibleTime = 0;
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
		
		updateScrollable = new UpdateScrollable(this);
		executorService.scheduleAtFixedRate(
					updateScrollable,
					0L,
					1L,
					TimeUnit.MILLISECONDS
				);
		
		this.visibleTime = player.getMediaDuration();
		this.currentEndVisibleTime = player.getMediaDuration();
		
		information.videoInstantiated();
		controls.videoInstantiated();
		
		new DebugInfo(this, g.getVideoController());
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
	 * Method to call if the media time has changed
	 */
	public void mediaTimeChanged()
	{
		information.mediaTimeChanged();
	}
	
	/**
	 * Method to call if the visible area changed
	 */
	public void visibleAreaChanged()
	{
		information.visibleAreaChanged();
		controls.visibleAreaChanged();
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
	 * Single Thread Executor class scrolls the visible area based on current media
	 * time
	 */
	private final class UpdateScrollable implements Runnable
	{
		
		private final Navbar navbar;
		private final Globals g;
		
		private UpdateScrollable(Navbar navbar)
		{
			this.navbar = navbar;
			this.g = Globals.getInstance();
		}
		
		@Override
		public void run() {
			final IVideoControls vc = Globals.getInstance().getVideoController();
			final long time 	= vc.IsLoaded() ? vc.getMediaTime() : 1;
			final long begin 	= navbar.getCurrentStartVisibleTime();
			final long end 		= navbar.getCurrentEndVisibleTime();
			final long total 	= vc.IsLoaded() ? vc.getMediaDuration() : 1;
			final long visible 	= navbar.getVisibleTime();
			
			navbar.mediaTimeChanged();
			
			SwingUtilities.invokeLater(new Runnable(){
				
				@Override
				public void run() {
					if(navbar.isDragging() || vc.IsLoaded() && vc.isPlaying() && begin < time && time < end) {
						if(time > end - Math.round(visible * .3f) && end < total)
						{
							navbar.setCurrentStartVisibleTime(begin + 250);
						} else if (time < begin + Math.round(visible * .3f) && begin > 0)
						{
							navbar.setCurrentStartVisibleTime(begin - 250);
						}
					}
					updateButtons();
				}
			});
		}
		
		/**
		 * Updates the button text and state
		 * @param tnr		Current trial number
		 * @param t			Current trial
		 * @param lnr		Current look number
		 * @param l			Current look
		 * @param time		Current time
		 */
		private void updateButtons()
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
	}
	
	public void updateLabels()
	{
		updateScrollable.updateButtons();
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
	
//	public void addTimeFrame(model.AbstractTimeFrame tf)
//	{
//		information.addTimeFrame(tf);
//	}
//	
//	public void removeTimeFrame(model.AbstractTimeFrame tf)
//	{
//		information.removeTimeFrame(tf);
//	}
}

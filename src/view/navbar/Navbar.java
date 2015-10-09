package view.navbar;

import java.awt.BorderLayout;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import view.player.IMediaPlayer;
import controller.Globals;
import controller.IVideoControls;
import model.AbstractTimeFrame;

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
	
	public Navbar(Globals g)
	{
		this.g = g;
		createLayout(g);
		this.currentStartVisibleTime = 0;
	}
	
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
	}
	
	private void createLayout(Globals g)
	{
		setLayout(new BorderLayout(0, 0));
		
		information = new InformationPanel(this, g);
		add(information, BorderLayout.CENTER);
		
		controls = new ControlBar(this, g);
		add(controls, BorderLayout.SOUTH);
	}

	public void addTimeFrame(AbstractTimeFrame tf, int type) {
		// TODO remove
		information.addTimeFrame(tf, type);		
	}
	
	public void componentResized()
	{
		information.componentResized();
	}
	
	public void mediaTimeChanged()
	{
		redraw();
	}
	
	public void setCurrentStartVisibleTime(long time)
	{
		this.currentStartVisibleTime = time;
		this.currentEndVisibleTime = time + visibleTime;
		redraw();
	}
	
	public long getCurrentStartVisibleTime()
	{
		return currentStartVisibleTime;
	}
	
	public void setCurrentEndVisibleTime(long time)
	{
		this.currentEndVisibleTime = time;
		this.visibleTime = this.currentEndVisibleTime - this.currentStartVisibleTime;
		calculateVisiblePercentage();
		redraw();
	}
	
	public long getCurrentEndVisibleTime()
	{
		return this.currentEndVisibleTime;
	}
	
	public void setVisibleTime(long time)
	{
		this.visibleTime = time;
		this.currentEndVisibleTime = this.currentStartVisibleTime + time;
		calculateVisiblePercentage();
		redraw();
	}
	
	public long getVisibleTime()
	{
		return this.visibleTime;
	}
	
	public float getVisiblePercentage()
	{
		return visiblePercentage;
	}
	
	private void calculateVisiblePercentage()
	{
		visiblePercentage = (float) visibleTime / (float) player.getMediaDuration() * 100f;
	}
	
	private void redraw()
	{
		if(updateScrollable != null) updateScrollable.update();
	}
	
	private final class UpdateScrollable implements Runnable
	{
		
		Navbar navbar;
		
		private UpdateScrollable(Navbar navbar)
		{
			this.navbar = navbar;
		}
		
		@Override
		public void run() {
			final IVideoControls vc = Globals.getInstance().getVideoController();
			final long time 	= vc.IsLoaded() ? vc.getMediaTime() : 1;
			final long begin 	= navbar.getCurrentStartVisibleTime();
			final long end 		= navbar.getCurrentEndVisibleTime();
			final long total 	= vc.IsLoaded() ? vc.getMediaDuration() : 1;
			final long visible 	= navbar.getVisibleTime();
			
			SwingUtilities.invokeLater(new Runnable(){

				@Override
				public void run() {
					if(navbar.isDragging() || vc.IsLoaded() && vc.isPlaying() ) {
						if(time > end - Math.round(visible * .3f) && end < total)
						{
							navbar.setCurrentStartVisibleTime(begin + Math.round(.01f * total));
						} else if (time < begin + Math.round(visible * .3f) && begin > 0)
						{
							navbar.setCurrentStartVisibleTime(begin - Math.round(.01f * total));
						}
					}
				}
			});
		}
		
		public void update(){
			controls.visibleAreaChanged();
			information.repaintDetails();
		}
	}
	
	protected void setIsDragging(boolean isDragging)
	{
		this.isDragging = isDragging;
	}
	
	protected boolean isDragging()
	{
		return this.isDragging;
	}
	
}

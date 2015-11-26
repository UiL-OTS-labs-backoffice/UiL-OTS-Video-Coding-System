package view.navbar.utilities;

import javax.swing.SwingUtilities;

import view.navbar.Navbar;
import model.Look;
import model.Trial;
import controller.Globals;
import controller.IVideoControls;
import controller.IVideoControllerObserver;

/**
 * Single Thread Executor class scrolls the visible area based on current media
 * time
 */
public final class NavbarUpdater implements Runnable
{
	
	private Navbar navbar;
	private Globals g;
	
	public NavbarUpdater(Navbar navbar)
	{
		this.navbar = navbar;
		this.g = Globals.getInstance();
		g.getVideoController().register(new IVideoControllerObserver(){

			@Override
			public void mediaTimeChanged(long time) { 
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						updateButtons();
					}
				}); 
			}

			@Override
			public void playerStarted() { }

			@Override
			public void playerPaused() { }

			@Override
			public void videoInstantiated() { }
			
		});
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
}
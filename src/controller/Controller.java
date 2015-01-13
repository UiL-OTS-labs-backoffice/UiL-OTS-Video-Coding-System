package controller;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import view.*;
import model.*;

public class Controller {
	
	private static Controller instance;
	
	public static Controller getInstance()
	{
		if(instance == null)
			instance = new Controller();
		return instance;
	}
	
	public String getUrl()
	{
		return Globals.experimentModel.getUrl();
	}

	/**
	 * Function to open the settings menu
	 */
	public void openSettings()
	{
		ExperimentSettings e = ExperimentSettings.getInstance();
		Experiment g = Globals.experimentModel;
		e.setSettings(
				g.getExp_id(),
				g.getExp_name(),
				g.getRes_id(),
				g.getPP_id(),
				g.getShow_exp_id(),
				g.getShow_exp_name(),
				g.getShow_res_id(),
				g.getShow_pp_id()
				);
		e.show();
	}
	
	/**
	 * Function to save the settings
	 */
	public void setSettings()
	{
		ExperimentSettings s = ExperimentSettings.getInstance();
		Globals.experimentModel.setSettings(
				s.getExp_id(),
				s.getExp_name(),
				s.getRes_id(),
				s.getPP_id(),
				s.getShow_exp_id(),
				s.getShow_exp_name(),
				s.getShow_res_id(),
				s.getShow_pp_id()
			);
	}
	
	/**
	 * Function to open a new video file
	 */
	public void videoUrlChooser()
	{
		String url = VideoSelector.show();
		if( url != null)
		{
			Globals.experimentModel.setUrl(url);
			Globals.editorView.getVideoPlayer().start(url);
		}
			
	}
	
	public void updateTrialNumber()
	{
		int curTrialNumber = Globals.experimentModel.getCurrentTrialNumber();
		int curLook = Globals.experimentModel.getCurrentLookNumber();
		
		String t = (curTrialNumber == -1) ? "Start a new trial" : 
			Integer.toString(curTrialNumber);
		String l = (curLook == -1) ? "Start a new look" : 
			Integer.toString(curLook);
		
		Globals.editorView.setInfo(t, l, "NaN");
	}
	
	/**
	 * Go back one frame
	 */
	public void prevFrame()
	{
		/*DirectMediaPlayer player = Globals.editorView.getPlayer();
		long fps = (long) (Globals.editorView.getVideoPlayer().getFps() * 1000);
		System.out.println("Fps: " + fps);
		
		long frameDuration = 1000000/fps;
		System.out.println("Frame duration: " + frameDuration + " ms");
		
		long curTime = player.getTime();
		System.out.println("Current time: " + curTime);
		player.setTime(curTime-frameDuration);
		System.out.println("New time: " + player.getTime());
		System.out.println("");*/
		
		Globals.editorView.getVideoPlayer().render();
	}
}

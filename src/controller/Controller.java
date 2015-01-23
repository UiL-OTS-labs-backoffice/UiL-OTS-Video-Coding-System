package controller;

import view.*;
import model.*;

public class Controller {
	
	/**
	 * Controller instance
	 */
	private static Controller instance;
	
	/**
	 * Get instance method
	 * @return	Instance of the controller
	 */
	public static Controller getInstance()
	{
		if(instance == null)
			instance = new Controller();
		return instance;
	}
	
	/**
	 * Get the media URL
	 * @return	Media URL
	 */
	public String getUrl()
	{
		return Globals.getExperimentModel().getUrl();
	}

	/**
	 * Function to open the settings menu
	 * Calls to the view
	 */
	public void openSettings()
	{
		ExperimentSettings e = Globals.getSettingsView();
		Experiment g = Globals.getExperimentModel();
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
	 * Calls to the model
	 */
	public void setSettings()
	{
		ExperimentSettings s = Globals.getSettingsView();
		Globals.getExperimentModel().setSettings(
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
	 * 
	 * Opens a window to select a video file
	 * Calls the view to add a new video player with the video file
	 */
	public void videoUrlChooser()
	{
		String url = VideoSelector.show();
		if( url != null)
		{
			VLCMediaPlayer player = new VLCMediaPlayer(url);
			Globals.getExperimentModel().setUrl(url);
			Globals.getEditor().addVideoPlayerSurface(player);
			Globals.getVideoController().setPlayer(player);
		}
	}
	
	/**
	 * Updates the trial number to a new trial
	 */
	public void updateTrialNumber()
	{
		int curTrialNumber = Globals.getExperimentModel().getCurrentTrialNumber();
		int curLook = Globals.getExperimentModel().getCurrentLookNumber();
		
		String t = (curTrialNumber == -1) ? "Start a new trial" : 
			Integer.toString(curTrialNumber);
		String l = (curLook == -1) ? "Start a new look" : 
			Integer.toString(curLook);
		
		Globals.getEditor().setInfo(t, l, "0 ms");
	}
}

package controller;

import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.LinkedList;

import view.panels.ExperimentSettings;
import view.panels.VideoSelector;
import view.player.VLCMediaPlayer;
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
			updateCurrentFileLabel();
			updateLabels(0);
		}
	}
	
	/**
	 * Updates the trial information labels
	 */
	public void updateLabels(long time)
	{	
		// Get model reference	
		model.Experiment exp = Globals.getExperimentModel();
		
		// Current trial number and current look number
		String tn, ln;
		
		// Button texts
		String endTrial, endLook;
		
		// Do some trial checking
		int trial = exp.getTrialByTime(time);
		int lastTrial;
		if(trial == -1)
		{
			lastTrial = exp.getLastTrialByTime(time);
			if(lastTrial > 0)
				endTrial = String.format("Extend trial %d", lastTrial);
			else
				endTrial = "End trial";
			tn = " ";
		} 
		else
		{
			endTrial = String.format("End trial %d", trial);
			tn = Integer.toString(trial);
			lastTrial = trial;
		}
		
		// Do some look checking
		int look = exp.getLookByTime(trial, time);
		int lastLook;
		if(look == -1)
		{
			lastLook = exp.getLastLookByTime(time);
			if (lastLook > 0)
				endLook = String.format("Extend look %d", lastLook);
			else
				endLook = "End look";
			ln = " ";
		}
		else
		{
			endLook = String.format("End look %d", look);
			ln = Integer.toString(look);
			lastLook = look;
		}
		
		// Get some extra numbers
		int looks = exp.getNumberOfLooks(trial);
		looks = (looks == -1) ? 0 : looks;
		int trials = exp.getNumberOfTrials();
		trials = (trials == -1) ? 0 : trials;
		long lookTime = exp.getLookTime(trial);
		
		// Prepare label texts
		String t = String.format("%s / %d", tn, trials);
		String l = String.format("%s / %d", ln, looks);
		String s = String.format("%d ms", lookTime);
		
		// 
		boolean canNewTrial = exp.canAddTrial(time);
		boolean canEndTrial = trial > 0 && exp.getTrials().get(trial-1).canEndTrial(time);
		
		boolean canNewLook = exp.canAddLook(trial, time);
		boolean canEndLook = look > 0 && exp.canEndLook(trial, look, time);
//		canEndLook = true;

		
		// Set information
		Globals.getEditor().setInfo(t, l, s);
		Globals.getEditor().updateButtons(endTrial, endLook,
    			canNewTrial, canEndTrial, canNewLook, canEndLook);
	}
	
	/**
	 * Get method for the list of trials
	 * @return	LinkedList<Trial>  list of trials
	 */
	public LinkedList<model.Trial> getTrials()
	{
		return Globals.getExperimentModel().getTrials();
	}
	
	/**
	 * Creates a new trial at the current cursor time
	 * @return	True if trial could be created
	 */
	public boolean newTrial()
	{
		long time = Globals.getVideoController().getMediaTime();
		boolean succes = Globals.getExperimentModel().addTrial(time);
		updateLabels(time);
		return succes;
	}
	
	/**
	 * Creates a new look at the current cursor time
	 * @return		True if look could be created
	 */
	public boolean newLook()
	{
		long time = Globals.getVideoController().getMediaTime();
		boolean succes = Globals.getExperimentModel().addLook(time);
		updateLabels(time);
		return succes;
	}
	
	/**
	 * Sets the end time of the current trial to the current cursor time
	 * @return		True if end time could be adapted
	 */
	public boolean setEndTrial()
	{
		long time = Globals.getVideoController().getMediaTime();
		int curTrialNumber = Globals.getExperimentModel().getLastTrialByTime(time);
		boolean succes =  Globals.getExperimentModel().endTrial(curTrialNumber, time);
		updateLabels(time);
		return succes;
	}
	
	
	/**
	 * Sets the end time of the current look to the current cursor time
	 * @return		True if look could be ended here
	 */
	public boolean setEndLook()
	{
		long time = Globals.getVideoController().getMediaTime();
		int trial = Globals.getExperimentModel().getTrialByTime(time);
		int look = Globals.getExperimentModel().getLastLookByTime(trial, time);
		boolean succes = Globals.getExperimentModel().endLook(trial, look, time);
		updateLabels(time);
		return succes;
	}
	
	/**
	 * Tells the view to update the file label
	 */
	public void updateCurrentFileLabel()
	{
		String curFile = Globals.getExperimentModel().getUrl();
		Globals.getEditor().setFile((curFile == null) ? "Select a file to play" : curFile.replaceFirst(".*/([^/?]+).*", "$1"));
	}
	
	/**
	 * Get the assigned key for an action
	 * @param action	Action ID
	 * @return			Assigned key if key is assigned to action.
	 * 					0 otherwise
	 * 					-1 if action doesn't exist
	 */
	public int getKey(String action)
	{
		return Globals.getKeyCodeModel().getKey(action);
	}
	
	/**
	 * Get the readable name of an action
	 * @param action	Action ID
	 * @return			Readable name if action exists, null otherwise
	 */
	public String getName(String action)
	{
		return Globals.getKeyCodeModel().getName(action);
	}
	
	/**
	 * Assign a new key to an existing action
	 * @param action	Name of the action
	 * @param key		number of the new key
	 */
	public void setKey(String action, int key)
	{
		Globals.getKeyCodeModel().setKey(action, key);
	}
	
	/**
	 * Get a TreeSet of all available action
	 * @return	TreeSet containing all actions
	 */
	public Collection<String> getActions()
	{
		return Globals.getKeyCodeModel().getActions();
	}
	
	/**
	 * Method to check if an action exists
	 * @param action	Action ID
	 * @return			True iff action exists
	 */
	public boolean isValidAction(String action)
	{
		return Globals.getKeyCodeModel().isValidAction(action);
	}
	
	/**
	 * Converts a key ID code to a readable string
	 * @param ID	Code of the key
	 * @return		String name of the key
	 */
	public String codeToString(String ID)
	{
		int key = getKey(ID);
		return (key > 0) ? KeyEvent.getKeyText(key) : "";
	}
	
}

package controller;

import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import view.panels.CSVExportSelector;
import view.panels.ExperimentSettings;
import view.panels.SaveAs;
import view.panels.VideoSelector;
import view.player.VLCMediaPlayer;
import model.*;

public class Controller {
	
	/**
	 * Controller instance
	 */
	private Globals g;
	
	protected Controller(Globals g)
	{
		this.g = g;
	}
	
	/**
	 * Get the media URL
	 * @return	Media URL
	 */
	public String getUrl()
	{
		return g.getExperimentModel().getUrl();
	}

	/**
	 * Function to open the settings menu
	 * Calls to the view
	 */
	public void openSettings()
	{
		ExperimentSettings e = g.getSettingsView();
		Experiment ex = g.getExperimentModel();
		e.setSettings(
				ex.getExp_id(),
				ex.getExp_name(),
				ex.getRes_id(),
				ex.getPP_id(),
				ex.getShow_exp_id(),
				ex.getShow_exp_name(),
				ex.getShow_res_id(),
				ex.getShow_pp_id()
				);
		e.show();
	}
	
	/**
	 * Function to save the settings
	 * Calls to the model
	 */
	public void setSettings()
	{
		ExperimentSettings s = g.getSettingsView();
		g.getExperimentModel().setSettings(
				s.getExp_name(),
				s.getExp_id(),
				s.getRes_id(),
				s.getPP_id(),
				s.getShow_exp_id(),
				s.getShow_exp_name(),
				s.getShow_res_id(),
				s.getShow_pp_id()
			);
	}
	
	/**
	 * Method to load the video selected by the user in the editor view
	 * and to show the editor
	 * @param url		The video url
	 */
	public void setVideo(String url)
	{
		VLCMediaPlayer player = new VLCMediaPlayer(url);
		g.getExperimentModel().setUrl(url);
		g.getEditor().addVideoPlayerSurface(player);
		g.getVideoController().setPlayer(player);
		g.getEditor().show();
	}
	
	/**
	 * Method to write the Experiment model as a serialized class to the
	 * user provided file location
	 */
	public boolean save()
	{
		return controller.serializer.Serializer.writeExperimentModel();
	}
	
	/**
	 * Method to write the experiment model to another location than provided
	 * by the user
	 */
	public void saveAs()
	{
		SaveAs dialog = new SaveAs();
		dialog.setVisible(true);
	}
	
	public boolean export()
	{
		view.panels.CSVExportSelector.getInstance();
		String name = CSVExportSelector.show();
		if (name == null)
			return false;
		else{
			if (!(name.endsWith(".csv") || name.endsWith(".CSV")))
				name += ".csv";
			controller.export.CSVExport exporter = new controller.export.CSVExport();
			return exporter.writeCsv(name);
		}
		
	}
	
	public boolean open(String url)
	{
		model.Experiment exp = controller.serializer.Serializer.readExperimentModel(url);
		if (exp != null)
		{
			if(!new java.io.File(
					exp.getUrl()).exists())
			{
				JOptionPane.showMessageDialog(new JPanel(), 
						"The video that was used with this project doesn't "
						+ "exist and has probably been replaced or renamed.\n "
						+ "Please select the original video.\n\n "
						+ "Make sure you select exactly the same video, because "
						+ "the coding is based on the video time.\n"
						+ "If the video has been "
						+ "altered, the coding results will no longer be correct", 
						"Video doesn't exist",
						JOptionPane.ERROR_MESSAGE);
				String newURL = VideoSelector.show();
				if (newURL == null)
					return false;
				exp.setUrl(newURL);
			}
			
			exp.setGlobals(g);
			g.setExperimentModel(exp);
			g.getEditor();
			g.getEditor().show();
			setVideo(exp.getUrl());
			updateLabels(0L);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Updates the trial information labels
	 */
	public void updateLabels(long time)
	{	
		// Get model reference	
		model.Experiment exp = g.getExperimentModel();
		
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
		
		boolean canNewTrial = exp.canAddTrial(time);
		boolean canEndTrial = trial > 0 && exp.getTrials().get(trial-1).canEndTrial(time);
		
		boolean canNewLook = exp.canAddLook(trial, time);
		boolean canEndLook = look > 0 && exp.canEndLook(trial, look, time);
//		canEndLook = true;
		
		// Set information
		g.getEditor().setInfo(t, l, s);
		g.getEditor().updateButtons(endTrial, endLook,
    			canNewTrial, canEndTrial, canNewLook, canEndLook);
	}
	
	/**
	 * Get method for the list of trials
	 * @return	LinkedList<Trial>  list of trials
	 */
	public LinkedList<model.Trial> getTrials()
	{
		return g.getExperimentModel().getTrials();
	}
	
	/**
	 * Creates a new trial at the current cursor time
	 * @return	True if trial could be created
	 */
	public boolean newTrial()
	{
		long time = g.getVideoController().getMediaTime();
		boolean succes = g.getExperimentModel().addTrial(time);
		updateLabels(time);
		return succes;
	}
	
	/**
	 * Creates a new look at the current cursor time
	 * @return		True if look could be created
	 */
	public boolean newLook()
	{
		long time = g.getVideoController().getMediaTime();
		boolean succes = g.getExperimentModel().addLook(time);
		updateLabels(time);
		return succes;
	}
	
	/**
	 * Sets the end time of the current trial to the current cursor time
	 * @return		True if end time could be adapted
	 */
	public boolean setEndTrial()
	{
		long time = g.getVideoController().getMediaTime();
		int curTrialNumber = g.getExperimentModel().getLastTrialByTime(time);
		boolean succes =  g.getExperimentModel().endTrial(curTrialNumber, time);
		updateLabels(time);
		return succes;
	}
	
	
	/**
	 * Sets the end time of the current look to the current cursor time
	 * @return		True if look could be ended here
	 */
	public boolean setEndLook()
	{
		long time = g.getVideoController().getMediaTime();
		int trial = g.getExperimentModel().getTrialByTime(time);
		int look = g.getExperimentModel().getLastLookByTime(trial, time);
		boolean succes = g.getExperimentModel().endLook(trial, look, time);
		updateLabels(time);
		return succes;
	}
	
	/**
	 * Tells the view to update the file label
	 */
	public void updateCurrentFileLabel()
	{
		String curFile = g.getExperimentModel().getUrl();
		g.getEditor().setFile((curFile == null) ? "Select a file to play" : curFile.replaceFirst(".*/([^/?]+).*", "$1"));
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
		return g.getKeyCodeModel().getKey(action);
	}
	
	/**
	 * Get the readable name of an action
	 * @param action	Action ID
	 * @return			Readable name if action exists, null otherwise
	 */
	public String getName(String action)
	{
		return g.getKeyCodeModel().getName(action);
	}
	
	/**
	 * Assign a new key to an existing action
	 * @param action	Name of the action
	 * @param key		number of the new key
	 */
	public void setKey(String action, int key)
	{
		g.getKeyCodeModel().setKey(action, key);
	}
	
	/**
	 * Get a TreeSet of all available action
	 * @return	TreeSet containing all actions
	 */
	public Collection<String> getActions()
	{
		return g.getKeyCodeModel().getActions();
	}
	
	/**
	 * Method to check if an action exists
	 * @param action	Action ID
	 * @return			True iff action exists
	 */
	public boolean isValidAction(String action)
	{
		return g.getKeyCodeModel().isValidAction(action);
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

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
				ex.getExp_name(),
				ex.getExp_id(),
				ex.getRes_id(),
				ex.getPP_id(),
				ex.getShow_exp_name(),
				ex.getShow_exp_id(),
				ex.getShow_res_id(),
				ex.getShow_pp_id()
				);
		e.setTimeout(ex.getUseTimeout(), ex.getTimeout());
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
				s.getShow_exp_name(),
				s.getShow_exp_id(),
				s.getShow_res_id(),
				s.getShow_pp_id()
			);
		g.getExperimentModel().setUseTimeout(s.getUseTimeout());
		g.getExperimentModel().setTimeout(s.getTimeout());
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
		g.getExperimentModel().setEnd(player.getMediaDuration());
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
	
	/**
	 * Method to open the experiment model from a previously saved location
	 * @param url		The location of the file to be opened
	 * @return			true iff successful
	 */
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
	 * Method to export the experiment information to a CSV file
	 * @return
	 */
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
			return exporter.exportTotalLookTime(name);
		}
		
	}
	
	/**
	 * Method to export the overview information
	 * @return
	 */
	public boolean exportOverview()
	{
		view.panels.CSVExportSelector.getInstance();
		String name = CSVExportSelector.show();
		if(name == null)
			return false;
		else{
			if (!(name.endsWith(".csv") || name.endsWith(".CSV")))
				name += ".csv";
			controller.export.CSVExport exporter = new controller.export.CSVExport();
			return exporter.exportExtendedInformation(name);
		}
	}
	
	/**
	 * Updates the buttons and labels for the trials and looks
	 * @param time	The current time stamp.
	 */
	public void updateLabels(long time)
	{	
		int tnr = g.getExperimentModel().getItemForTime(time);
		updateButtons(tnr, time);
		generateInfo(tnr, time);
		g.getEditor().updateSlider();
	}
	
	/**
	 * Generates the labels for current trial and look and total look time
	 * @param tnr		Current trial number
	 * @param t			Current trial
	 * @param lnr		Current look number
	 */
	private void generateInfo(int tnr, long time)
	{
		int totalTrials = g.getExperimentModel().getNumberOfItems();
		String trial, look, totalTime;
		if(totalTrials > 0)
		{
			trial = (tnr > 0) ? Integer.toString(tnr) : " ";
			trial += String.format(" / %d", totalTrials);
			Trial t = (Trial) g.getExperimentModel().getItem(Math.abs(tnr));
			totalTime = String.format("%d ms", t.getTotalTimeForItems());
			int totalLooks = t.getNumberOfItems();
			
			int lnr = t.getItemForTime(time);
			
			if (totalLooks > 0)
			{
				look = (lnr > 0) ? Integer.toString(lnr) : " ";
				look += String.format(" / %d", totalLooks);
			} else {
				look = "Start a new look";
			}
		} else {
			trial = "Start a new trial";
			look = trial;
			totalTime = trial;
		}
		
		g.getEditor().setInfo(trial, look, totalTime);
	}
	
	/**
	 * Updates the button text and state
	 * @param tnr		Current trial number
	 * @param t			Current trial
	 * @param lnr		Current look number
	 * @param l			Current look
	 * @param time		Current time
	 */
	private void updateButtons(int tnr, long time)
	{
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
				tm = l.getEnd() > -1 && time - l.getEnd() > g.getExperimentModel().getTimeout() && g.getExperimentModel().getUseTimeout();
				el = tnr > 0 && l.canEnd(time);
			}
		}
		
		g.getEditor().updateButtons(endOrExtend(tnr, "trial"), endOrExtend(lnr, "look"), 
				nt, et, nl, el, tnr > 0, lnr > 0, 
				String.format("Comment trial %d", Math.abs(tnr)), String.format("Comment look %d", Math.abs(lnr))
			);
		g.getEditor().setTimeoutText(tm);
	}
	
	/**
	 * Formats the text for the buttons
	 * @param nr		The item number
	 * @param type		"look" or "trial"
	 * @return			A nice string for on a button
	 */
	private String endOrExtend(int nr, String type)
	{
		String str = (nr < 0) ? "Extend %s %d" : "End %s %d";
		return String.format(str, type, Math.abs(nr));
	}
	
	
	/**
	 * Get method for the list of trials
	 * @return	LinkedList<Trial>  list of trials
	 */
	public LinkedList<AbstractTimeFrame> getTrials()
	{
		return g.getExperimentModel().getItems();
	}
	
	/**
	 * Creates a new trial at the current cursor time
	 */
	public void newTrial()
	{
		long time = g.getVideoController().getMediaTime();
		g.getExperimentModel().addItem(time);
		updateLabels(time);
	}
	
	/**
	 * Creates a new look at the current cursor time
	 */
	public void newLook()
	{
		long time = g.getVideoController().getMediaTime();
		int tnr = g.getExperimentModel().getItemForTime(time);
		if(tnr != 0)
		{
			Trial trial = (Trial) g.getExperimentModel().getItem(Math.abs(tnr));
			trial.addItem(time);
			updateLabels(time);
		} else {
			throw new IllegalStateException("Not currently in a trial");
		}
	}
	
	/**
	 * Sets the end time of the current trial to the current cursor time
	 */
	public void setEndTrial()
	{
		long time = g.getVideoController().getMediaTime();
		int tnr = Math.abs(g.getExperimentModel().getItemForTime(time));
		Trial t = (Trial) g.getExperimentModel().getItem(tnr);
		t.setEnd(time);
		updateLabels(time);
	}
	
	
	/**
	 * Sets the end time of the current look to the current cursor time
	 */
	public void setEndLook()
	{
		long time = g.getVideoController().getMediaTime();
		int tnr = g.getExperimentModel().getItemForTime(time);
		Trial t = (Trial) g.getExperimentModel().getItem(tnr);
		int lnr = Math.abs(t.getItemForTime(time));
		Look l = (Look) t.getItem(lnr);
		l.setEnd(time);
		updateLabels(time);
	}
	
	/**
	 * Removes the trial at the current time from the list, along with all
	 * looks in that trial.
	 * Doesn't do anything if no trial is active for the current time
	 */
	public void removeCurrentTrial()
	{
		long time = g.getVideoController().getMediaTime();
		int tnr = g.getExperimentModel().getItemForTime(time);
		if(tnr > 0){
			g.getExperimentModel().removeItem(tnr);
		}
	}
	
	/**
	 * Removes the active look at the current time
	 * Does nothing if no trial or look is active
	 */
	public void removeCurrentLook()
	{
		long time = g.getVideoController().getMediaTime();
		int tnr = g.getExperimentModel().getItemForTime(time);
		if(tnr > 0)
		{
			Trial t = (Trial) g.getExperimentModel().getItem(tnr);
			int lnr = t.getItemForTime(time);
			if (lnr > 0)
			{
				t.removeItem(lnr);
			}
		}
		
	}
	
	/**
	 * Removes all looks from the active trial at the current time
	 * Does nothing if no trial is active
	 */
	public void removeAllCurrentLooks()
	{
		long time = g.getVideoController().getMediaTime();
		int tnr = g.getExperimentModel().getItemForTime(time);
		if(tnr > 0)
		{
			Trial t = (Trial) g.getExperimentModel().getItem(tnr);
			while(t.getNumberOfItems() > 0)
			{
				t.removeItem(1);
			}
		}	
	}
	
	/**
	 * Returns the number of trials
	 * @return	The number of trials
	 */
	public int getNumberOfTrials()
	{
		return g.getExperimentModel().getNumberOfItems();
	}
	
	/**
	 * Returns the number of looks for a trial
	 * @param trial		Target trial number
	 * @return			number of looks in the trial
	 */
	public Trial getTrial(int trial)
	{
		return (Trial) g.getExperimentModel().getItem(trial);
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

package controller;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
		g.getVideoController().videoInstantiated();
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
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SaveAs dialog = new SaveAs();
				dialog.setVisible(true);
			}
		});
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
				String newURL = VideoSelector.show(new File(g.getPreferencesModel().getLastVideoDirectory(System.getProperty("user.home"))));
				if (newURL == null)
					return false;
				exp.setUrl(newURL);
			}
			
			exp.setGlobals(g);
			g.setExperimentModel(exp);
			
			g.getEditor();
			g.getEditor().show();
			setVideo(exp.getUrl());
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Method to export the experiment information to a CSV file
	 * @return
	 */
	public boolean export(String name, int type)
	{
//		view.panels.CSVExportSelector.getInstance();
//		String name = CSVExportSelector.show();
//		if (name == null)
//			return false;
//		else{
//			if (!(name.endsWith(".csv") || name.endsWith(".CSV")))
//				name += ".csv";
//			controller.export.CSVExport exporter = new controller.export.CSVExport();
//			return exporter.exportTotalLookTime(name);
//		}
		
		controller.export.CSVExport exporter = new controller.export.CSVExport();
		boolean result;
		if(type == CSVExportSelector.EXPORT_AS_OVERVIEW){
			result = exporter.exportExtendedInformation(name);
		} else {
			result = exporter.exportTotalLookTime(name);
		}
		return result;
	}
	
//	/**
//	 * Method to export the overview information
//	 * @return
//	 */
//	public boolean exportOverview()
//	{
//		view.panels.CSVExportSelector.getInstance();
//		String name = CSVExportSelector.show();
//		if(name == null)
//			return false;
//		else{
//			if (!(name.endsWith(".csv") || name.endsWith(".CSV")))
//				name += ".csv";
//			controller.export.CSVExport exporter = new controller.export.CSVExport();
//			return exporter.exportExtendedInformation(name);
//		}
//	}
	
	/**
	 * Get the number of an item
	 * @param tf	Abstract Time Frame item
	 * @return		Number for tf
	 */
	public int getNumber(AbstractTimeFrame tf)
	{
		return g.getExperimentModel().getNumberForItem(tf);
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
		t.calculateTimeout();
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
		removeTrial(tnr);
	}
	
	public void removeTrial(int tnr)
	{
		if(tnr > 0 && g.getExperimentModel().getItems().size() >= tnr){
			AbstractTimeContainer trial = (AbstractTimeContainer) g.getExperimentModel().getItem(tnr);
//			for(AbstractTimeFrame look : ((Trial) trial).getItems())
//			{
//				g.getEditor().getBottomBar().getNavbar().removeTimeFrame(look);
//			}
//			g.getEditor().getBottomBar().getNavbar().removeTimeFrame(trial);
			trial.removeAll();
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
			removeLook(tnr, lnr);
		}
	}
	
	public void removeLook(int tnr, int lnr)
	{
		if(tnr > 0 && tnr <= g.getExperimentModel().getItems().size())
		{
			Trial trial = (Trial) g.getExperimentModel().getItem(tnr);
			if (lnr > 0 && lnr <= trial.getItems().size())
			{
//				g.getEditor().getBottomBar().getNavbar().removeTimeFrame(trial.getItem(lnr));
				trial.removeItem(lnr);
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
			t.removeAll();
//			while(t.getNumberOfItems() > 0)
//			{
//				g.getEditor().getBottomBar().getNavbar().removeTimeFrame(t.getItem(1));
//				t.removeItem(1);
//			}
		}	
	}
	
	public void removeLooksInTrial(int tnr)
	{
		if(tnr > 0 && tnr <= g.getExperimentModel().getItems().size())
		{
			Trial trial = (Trial) g.getExperimentModel().getItem(tnr);
//			for(AbstractTimeFrame look : trial.getItems())
//			{
//				g.getEditor().getBottomBar().getNavbar().removeTimeFrame(look);
//			}
			trial.removeAll();
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

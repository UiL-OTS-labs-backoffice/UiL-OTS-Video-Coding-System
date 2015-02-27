package controller;
/**
 * Class to contain globals
 * This class simulates a static class
 * @author mooij006
 *
 */

import view.*;
import view.panels.ExperimentSettings;
import model.*;

/**
 * Globals class contains reference to static instances that can be accessed
 * throughout the application
 */
public class Globals {
	
	/**
	 * Instance of this Globals class
	 */
	private static Globals instance;
	
	// Controller
	private static Controller controller;
	private static IVideoControls videoController;
	private static model.QuickKeys keyCodeModel;
	
	// Views
	private static Editor editorView;
	private static ExperimentSettings settingsView;
	
	// Experiment model instance
	private Experiment experimentModel;
	

	/**
	 * Constructor for global class
	 * Creates a new instance of the controller, the editor and 
	 * the settingsView
	 */
	private Globals()
	{
		controller = Controller.getInstance();
		videoController = VLCVideoController.getInstance();
		keyCodeModel = model.QuickKeys.getInstance();
		editorView = Editor.getInstance();
		settingsView = ExperimentSettings.getInstance();
		experimentModel = new Experiment();
	}
	
	/**
	 * Returns an instance of the global classs
	 * @return
	 */
	public static Globals getInstance()
	{
		if(instance == null)
			instance = new Globals();
		return instance;
	}
	
	/**
	 * Method to get the controller instance
	 * @return	Controller
	 */
	public static Controller getController()
	{
		return controller;
	}
	
	/**
	 * Method to get the video controller instance
	 * @return	video controller
	 */
	public static IVideoControls getVideoController()
	{
		return videoController;
	}
	
	public static model.QuickKeys getKeyCodeModel()
	{
		return keyCodeModel;
	}
	
	/**
	 * Method to get the editor view instance
	 * @return	Editor view
	 */
	public static Editor getEditor()
	{
		return editorView;
	}
	
	/**
	 * Method to get the settings view instance
	 * @return	Settings view
	 */
	public static ExperimentSettings getSettingsView()
	{
		return settingsView;
	}
	
	/**
	 * Method to get the experiment model instance
	 * @return	Experiment model
	 */
	public Experiment getExperimentModel()
	{
		return experimentModel;
	}
	
	/**
	 * Method to change the experiment model when an existing project
	 * is loaded
	 */
	public void setExperimentModel(Experiment experiment)
	{
		this.experimentModel = experiment;
	}
}

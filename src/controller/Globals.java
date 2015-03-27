package controller;
/**
 * Class to contain globals
 * This class simulates a static class
 * @author mooij006
 *
 */

import javax.swing.SwingUtilities;

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
	private Controller controller;
	private IVideoControls videoController;
	private model.QuickKeys keyCodeModel;
	
	// Views
	private Editor editorView;
	private ExperimentSettings settingsView;
	
	// Experiment model instance
	private Experiment experimentModel;
	

	/**
	 * Constructor for global class
	 * Creates a new instance of the controller, the editor and 
	 * the settingsView
	 */
	private Globals()
	{
		instance = this;
		controller = new Controller(instance);
		videoController = new VLCVideoController(instance);
		experimentModel = new Experiment(instance);
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				keyCodeModel = model.QuickKeys.getInstance();
				editorView = new Editor(instance);
				settingsView = new ExperimentSettings(instance);
				
				view.panels.projectOpener opener = new view.panels.projectOpener(instance);
				opener.setVisible(true);
			}
		});
	}
	
	/**
	 * Returns an instance of the global classs
	 * @return
	 */
	public static Globals getInstance()
	{
		if(instance == null)
			new Globals();
		return instance;
	}
	
	/**
	 * Method to get the controller instance
	 * @return	Controller
	 */
	public Controller getController()
	{
		return controller;
	}
	
	/**
	 * Method to get the video controller instance
	 * @return	video controller
	 */
	public IVideoControls getVideoController()
	{
		return videoController;
	}
	
	public model.QuickKeys getKeyCodeModel()
	{
		return keyCodeModel;
	}
	
	/**
	 * Method to get the editor view instance
	 * @return	Editor view
	 */
	public Editor getEditor()
	{
		return editorView;
	}
	
	/**
	 * Method to get the settings view instance
	 * @return	Settings view
	 */
	public ExperimentSettings getSettingsView()
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

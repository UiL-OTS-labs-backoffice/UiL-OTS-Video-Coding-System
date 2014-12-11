package controller;
/**
 * Class to contain globals
 * This class simulates a static class
 * @author mooij006
 *
 */

import view.*;
import model.*;

public class Globals {
	
	private static Globals instance;
	
	// Controller
	public static Controller controller;
	
	// Views
	public static Editor editorView;
	public static ExperimentSettings settingsView;
	
	// Experiment model instance
	public static Experiment experimentModel;
	

	/**
	 * Constructor for global class
	 * Creates a new instance of the controller, the editor and 
	 * the settingsView
	 */
	private Globals()
	{
		controller = Controller.getInstance();
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
}

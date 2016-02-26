package controller;
/**
 * Class to contain globals
 * This class simulates a static class
 * @author mooij006
 *
 */

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import view.*;
import view.panels.ExperimentOverview;
import view.panels.ExperimentSettings;
import view.panels.ProjectSelector;
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
	private boolean debug;
	
	// Controller
	private Controller controller;
	private IVideoControls videoController;
	private model.QuickKeys keyCodeModel;
	
	// Views
	private Editor editorView;
	private ExperimentSettings settingsView;
	private ExperimentOverview overview;
	
	// Experiment model instance
	private Experiment experimentModel;
	private ApplicationPreferences preferences;
	
	private static ArrayList<BufferedImage> IconImages = new ArrayList<BufferedImage>();
	
	/**
	 * Constructor for global class
	 * Creates a new instance of the controller, the editor and 
	 * the settingsView
	 */
	private Globals()
	{
		instance = this;
		controller = new Controller(instance);
		preferences = new ApplicationPreferences();
		videoController = new VLCVideoController(instance);
		experimentModel = new Experiment(instance);
		keyCodeModel = model.QuickKeys.getInstance(preferences);
		
		readIcons();
		
		editorView = new Editor(instance);
		settingsView = new ExperimentSettings(instance);
	}
	
	public void showNewProject(){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if(!preferences.getClosedProperly()) {
					int recover = JOptionPane.showConfirmDialog(null, 
							"It looks like the program was exited without saving last time.\nDo you want to check for backup files?",
							"UiL OTS Video Coding System - Recovery", JOptionPane.YES_NO_OPTION);
					if(recover == JOptionPane.YES_OPTION){
				
						String url = ProjectSelector.show(getBackupLocation());
						
						if (url != null)
						{
							if(controller.open(url))
							{
								return;
							}
							else {
								JOptionPane.showMessageDialog(new JPanel(), "Sorry! It looks like the project couldn't be opened", "Opening project failed", JOptionPane.ERROR_MESSAGE);
							}
						}
					}
				}
				view.panels.projectOpener opener = new view.panels.projectOpener(
						instance);
				opener.setVisible(true);
			}
		});
	}
	
	private static void readIcons(){
		String path = "/img/icons/";
		int sizes[] = {16, 24, 32, 48, 64, 96, 128, 256, 512};
		for(int i = 0; i < sizes.length; i++)
		{
			String filename = String.format("%s%dx%d.png", path, sizes[i], sizes[i]);
			
			try {
				BufferedImage image = ImageIO.read(Globals.class.getResource(filename));
				IconImages.add(image);
			} catch (IOException e) {
				System.out.println(filename);
				e.printStackTrace();
				System.out.println("");
			}
		}
	}
	
	/**
	 * Returns an instance of the global class
	 * @return
	 */
	public static Globals getInstance()
	{
		if(instance == null){
			new Globals();
		}
		return instance;
	}
	
	public void open(final String url){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (!controller.open(url)) {
					JOptionPane
							.showMessageDialog(
									new JPanel(),
									"Sorry! It looks like the project couldn't be opened",
									"Opening project failed",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		});
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
	
	public static ArrayList<BufferedImage> getIcons(){
		if(IconImages == null)
			readIcons();
		return IconImages;
	}
	
	/**
	 * Method to get the preferences model instance
	 * @return
	 */
	public ApplicationPreferences getPreferencesModel()
	{
		return preferences;
	}
	
	public void showExperimentOverview(){
		if(this.overview == null){
			this.overview = new view.panels.ExperimentOverview();
		}
		this.overview.setVisible(true);
	}
	
	public void disposeExperimentOverview(){
		this.overview = null;
	}
	
	public enum OsType { Windows, Linux, Mac };
	public enum OsArch {OS32, OS64};
	
	public static OsType getOs(){
		String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		if (OS.indexOf("win") >= 0) {
			return OsType.Windows;
		} else if (OS.indexOf("os x") >= 0) {
			return OsType.Mac;
		} else {
			return OsType.Linux;
		}
	}
	
	public static OsArch getArch(){
		String Arch = System.getProperty("os.arch", "generic").toLowerCase(Locale.ENGLISH);
		if(Arch.indexOf("64") >= 0){
			return OsArch.OS64;
		} else {
			return OsArch.OS32;
		}
	}
	
	public static File getBackupLocation(){
		File dir;
		if (getOs() == OsType.Windows) {
			String location = System.getenv("APPDATA");
			dir = new File(location + File.separator + "UiLOTSVideoCodingSystem");
		} else {
			String location = System.getProperty("user.home");
			dir = new File(location + File.separator + ".UiLOTSVideoCodingSystem");
		}
		dir = new File(dir.getAbsolutePath() + File.separator + "autosave");
		if(!dir.exists()) dir.mkdirs();
		return dir;
	}
	
	public boolean debug(){
		return this.debug;
	}
	
	public void debug(boolean debug){
		this.debug = debug;
	}
}

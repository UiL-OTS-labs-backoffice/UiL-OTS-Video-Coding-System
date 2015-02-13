package view;

import java.awt.BorderLayout;
import java.awt.KeyboardFocusManager;

import javax.swing.JFrame;

import view.bottombar.BottomBar;
import view.events.KeyDispatch;
import view.menu.MainMenu;
import view.player.IMediaPlayer;
import view.player.VLCMediaPlayer;
import controller.*;

/**
 * Main view of the program
 * @author mooij006
 *
 */
public class Editor {
	
	// Instance of editor
	private static Editor instance;

	// Instance of controller
	Controller c = Globals.getController();
	
    // Create a new media player instance for the run-time platform
    private IMediaPlayer videoPlayer;
	
	// Panels
    private JFrame frame;
    private BottomBar bottom_bar;
    private VideoManipulationButtons playButtons;
    
    private java.awt.Component visualComponent = null;
    
    /**
     * Getmethod for instance of Editor view
     * @return	Editor
     */
    public static Editor getInstance()
    {
    	if(instance == null)
    		instance = new Editor();
    	return instance;
    }
    
    /**
     * Private constructor for editor
     * Creates the main view of the program
     * Set to private to ensure singleton
     */
    private Editor()
    {
    	createFrame();
    	addMenu();
    	addControlBar();
    	addVideoManipulationButtons();
    	KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyDispatch());
    }
    
    /**
     * Constructs the JFrame
     */
    private void createFrame()
    {
        frame = new JFrame("UiL OTS Labs Video Coding Software");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(400, 150);
        frame.setSize(1024, 768);
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
    }
    
    /**
     * Adds the menu to the frame
     */
    private void addMenu()
    {
    	MainMenu menu = new MainMenu();
    	frame.setJMenuBar(menu);
    }
    
    /**
     * Function to add the control bar to the bottom of the main view
     */
    private void addControlBar()
    {
    	bottom_bar = new BottomBar();
        frame.getContentPane().add(bottom_bar, BorderLayout.SOUTH);
    }
    
    /**
     * Adds the video controls to the frame
     */
    private void addVideoManipulationButtons()
    {
    	playButtons = new VideoManipulationButtons();
    	bottom_bar.add(playButtons, BorderLayout.CENTER);
    }
    
    /**
     * Creates a video panel and sets it to the content frame
     * This is only called when the media file has been selected
     */
    public void addVideoPlayerSurface(VLCMediaPlayer player)
    {
    	videoPlayer = player;
    	
    	// Delete current video player component if exists
    	if(visualComponent != null)
    		frame.getContentPane().remove(visualComponent);
    	
    	visualComponent = videoPlayer.getVisualComponent();
    	frame.getContentPane().add(visualComponent);
    	playButtons.setEnableButtons(true);
    	bottom_bar.getTimeCodes().playerStarted(player);
    	frame.revalidate();
    }
    
    /**
     * Shows the main view
     */
    public void show(){
        
        frame.setVisible(true);
        c.updateLabels(0L);
        c.updateCurrentFileLabel();
    }
    
    /**
     * Method to set the trial look numbers and the current total look time
     * This gets passed along to the bottom_bar panel
     * @param trial		Trial Number at current play position
     * @param look		Look Number at current play position
     * @param time		Total look time at current play position
     */
    public void setInfo(String trial, String look, String time)
    {
    	bottom_bar.setInfo(trial, look, time);
    }
    
    /**
     * Method to change the value of the file label in the trial information
     * by passing the argument to the bottom bar
     * @param file		Name of the file to be set
     */
    public void setFile(String file)
    {
    	bottom_bar.setFile(file);
    }
    
    public void updateButtons(
			String endTrial, String endLook,
			boolean nt, boolean et, boolean nl, boolean el
		)
    {
    	bottom_bar.updateButtons(endTrial, endLook,	nt, et, nl, el);
    }
    
    
    
}
package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import controller.Controller;
import controller.Globals;

/**
 * Main view of the program
 * @author mooij006
 *
 */
public class Editor {
	
	// Instance of editor
	private static Editor instance;

	// Instance of controller
	Controller c = Globals.controller;
	
    // Create a new media player instance for the run-time platform
    private EmbeddedMediaPlayer mediaPlayer;

	// Panels
    private JFrame frame;
    private BottomBar bottom_bar;
    
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
     * Shows the main view
     */
    public void show(){
        
        frame.setVisible(true);
        c.updateTrialNumber();
    }
    
    /**
     * Returns the instance of the media player
     * @return	EmbeddedMediaPlayer
     */
    public EmbeddedMediaPlayer getPlayer()
    {
    	return mediaPlayer;
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
     * Private constructor for editor
     * Creates the main view of the program
     * Set to private to ensure singleton
     */
    private Editor()
    {
    	createFrame();
    	addMenu();
    	addVideoPlayerSurface();
    	addControlBar();
    	addVideoManipulationButtons();
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
    
    private void addMenu()
    {
    	MainMenu menu = new MainMenu();
    	frame.setJMenuBar(menu);
    }
    
    /**
     * Creates a video panel and sets it to the content frame
     */
    private void addVideoPlayerSurface()
    {
    	//Adding the panel to the frame and getting the media player
        VideoPlayer videoPlayer = new VideoPlayer();
        mediaPlayer = videoPlayer.getMediaPlayer();
        frame.getContentPane().add(videoPlayer);
    }
    
    /**
     * Function to add the control bar to the bottom of the main view
     */
    private void addControlBar()
    {
    	bottom_bar = new BottomBar(mediaPlayer);
        frame.getContentPane().add(bottom_bar, BorderLayout.SOUTH);
    }
    
    private void addVideoManipulationButtons()
    {
    	VideoManipulationButtons playButtons = 
    			new VideoManipulationButtons(mediaPlayer);
    	bottom_bar.add(playButtons, BorderLayout.CENTER);
    }
    
    
    
}
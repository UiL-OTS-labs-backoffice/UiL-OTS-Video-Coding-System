package view;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import view.bottombar.BottomBar;
import view.event.binding.KeyBindingHandler;
//import view.events.CodingKeyListener;
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
	
	// Instance of controller
	Globals g;
	Controller c;
	
    // Create a new media player instance for the run-time platform
    private IMediaPlayer videoPlayer;
	
	// Panels
    private JFrame frame;
    private BottomBar bottom_bar;
    
    private java.awt.Component visualComponent = null;
    
    private MainMenu menu;
    
    /**
     * Private constructor for editor
     * Creates the main view of the program
     * Set to private to ensure singleton
     */
    public Editor(Globals g)
    {
    	this.g = g;
    	this.c = this.g.getController();
    	SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createFrame();
				addMenu();
			}
		});
		addControlBar();
		g.getVideoController().register(new IVideoControllerObserver(){
			@Override
			public void videoInstantiated() {
		    	frame.setTitle(String.format("UiL OTS Labs Video Coding Software - %s", c.getUrl()));
			}
		});
    }
    
    /**
     * Constructs the JFrame
     */
    private void createFrame()
    {
        frame = new JFrame("UiL OTS Labs Video Coding Software - %s");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocation(400, 150);
        frame.setSize(1024, 768);
        frame.setIconImages(Globals.getIcons());
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        
//        frame.addKeyListener(new CodingKeyListener(g));
//        frame.setFocusable(true);
        
        frame.addWindowListener(new WindowListener(){
			@Override
			public void windowClosing(WindowEvent e) {
				int result = JOptionPane.showConfirmDialog(frame, "Do you want to save before closing?", "alert", JOptionPane.YES_NO_CANCEL_OPTION);
				switch(result)
				{
					case JOptionPane.CANCEL_OPTION: 
						break;
					case JOptionPane.YES_OPTION: 
						if(!c.save())
						{
							JOptionPane.showMessageDialog(new JPanel(), "Sorry! Looks like the file couldn't be saved!", "Save failed", JOptionPane.ERROR_MESSAGE);
						} else {
							System.exit(0);
						}
						break;
					case JOptionPane.NO_OPTION: 
						System.exit(0);
						break;
				}
			}

			public void windowActivated(WindowEvent e) { }
			public void windowClosed(WindowEvent e) { }
			public void windowDeactivated(WindowEvent e) { }
			public void windowDeiconified(WindowEvent e) { }
			public void windowIconified(WindowEvent e) { }
			public void windowOpened(WindowEvent e) { }
        });
    }
    
    /**
     * Adds the menu to the frame
     */
    private void addMenu()
    {
    	menu = new MainMenu(g);
    	frame.setJMenuBar(menu);
    }
    
    /**
     * Function to add the control bar to the bottom of the main view
     */
    private void addControlBar()
    {
    	bottom_bar = new BottomBar(g);
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.getContentPane().add(bottom_bar, BorderLayout.SOUTH);
				new KeyBindingHandler(bottom_bar);
			}
		});
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
    	SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.getContentPane().add(visualComponent);
			}
		});
    }
    
    /**
     * Shows the main view
     */
    public void show(){
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.setVisible(true);
			}
		});
    }
}
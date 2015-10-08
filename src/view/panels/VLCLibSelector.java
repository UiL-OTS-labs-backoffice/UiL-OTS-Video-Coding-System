package view.panels;

import java.io.File;

import javax.swing.JFileChooser;

import model.ApplicationPreferences;

/**
 * Video selector
 *
 */
public class VLCLibSelector {
	
	/**
	 * Private constructor
	 */
	public VLCLibSelector() { }
	
	
	/**
	 * Shows a file selector for the video file to be used
	 * @return	Path of selected file if valid, otherwise null
	 */
	public static String show(ApplicationPreferences prefs)
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setCurrentDirectory(new File(prefs.getVLCUrl()));
	    
	    int returnVal = chooser.showOpenDialog(chooser);
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
    		  return chooser.getSelectedFile().getPath();
	    }

	    return null;
	    
	}
	
}

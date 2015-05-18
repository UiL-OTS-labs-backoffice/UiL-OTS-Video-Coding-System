package view.panels;

import javax.swing.JFileChooser;

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
	public static String show()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    
	    int returnVal = chooser.showOpenDialog(chooser);
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
    		  return chooser.getSelectedFile().getPath();
	    } else if (returnVal == JFileChooser.CANCEL_OPTION) {
	    	System.exit(1);
	    }

	    return null;
	    
	}
	
}

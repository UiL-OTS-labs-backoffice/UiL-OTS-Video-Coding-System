package view.panels;

import javax.swing.JFileChooser;


/**
 * Shows a windows where the user can select a directory where the
 * project should be saved
 */
public class SaveDialog {
	
	/**
	 * Single instance
	 */
	private static SaveDialog instance;
	
	/**
	 * Private constructor
	 */
	private SaveDialog() { }
	
	/**
	 * getmethod for instance of videoselector
	 * @return		video selector instance
	 */
	public SaveDialog getInstance()
	{
		if(instance == null)
			instance = new SaveDialog();
		return instance;
	}
	
	/**
	 * Shows a file selector for the video file to be used
	 * @return	Path of selected file if valid, otherwise null
	 */
	public static String show()
	{
		JFileChooser chooser = new JFileChooser();
		
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
	    
	    int returnVal = chooser.showSaveDialog(chooser);
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	return chooser.getSelectedFile().getPath();
	    }
	    else return null;
	}	
}

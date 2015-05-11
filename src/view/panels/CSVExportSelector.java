package view.panels;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * Shows a windows where the user can select a directory where the
 * project should be saved
 */
public class CSVExportSelector {
	
	/**
	 * Single instance
	 */
	private static CSVExportSelector instance;
	
	private static String[] extensions = {"csv"};
	
	/**
	 * Private constructor
	 */
	private CSVExportSelector() { }
	
	/**
	 * getmethod for instance of videoselector
	 * @return		video selector instance
	 */
	public static CSVExportSelector getInstance()
	{
		if(instance == null)
			instance = new CSVExportSelector();
		return instance;
	}
	
	/**
	 * Shows a file selector for the video file to be used
	 * @return	Path of selected file if valid, otherwise null
	 */
	public static String show()
	{
		JFileChooser chooser = new JFileChooser();
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"CSV file", extensions);
	    
	    chooser.setFileFilter(filter);
	    chooser.addChoosableFileFilter(filter);
		
		chooser.setAcceptAllFileFilterUsed(false);
	    
	    int returnVal = chooser.showSaveDialog(chooser);
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	return chooser.getSelectedFile().getPath();
	    }
	    else return null;
	}	
}
package view;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;



/**
 * Video selector
 * @author mooij006
 *
 */
public class VideoSelector {
	
	private static VideoSelector instance;

	/**
	 * Private constructor
	 */
	private VideoSelector()
	{
		
	}
	
	/**
	 * getmethod for instance of videoselector
	 * @return		video selector instance
	 */
	public VideoSelector getInstance()
	{
		if(instance == null)
			instance = new VideoSelector();
		return instance;
	}
	
	/**
	 * Shows a file selector for the video file to be used
	 */
	public static String show()
	{
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "All video files", "mpeg", "avi", "asf", "wmv", "wma", 
	        "mp4", "mov", "3gp", "mkv");
	    chooser.setFileFilter(filter);
	    chooser.addChoosableFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(chooser);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	      return isValidFile(chooser.getSelectedFile()) ? 
	    		  chooser.getSelectedFile().getPath() : null;
	    } else {
	    	return null;
	    }
	}
	
	private static boolean isValidFile(File f)
	{
		String fi = f.getName().toLowerCase();
		return f.isFile() && (fi.endsWith("mpeg") || fi.endsWith("avi") ||
				fi.endsWith("asf") || fi.endsWith("wmv") || fi.endsWith("wma") ||
				fi.endsWith("mp4") || fi.endsWith("mov") || fi.endsWith("3gp") ||
				fi.endsWith("mkv") );
	}
	
	
	
}

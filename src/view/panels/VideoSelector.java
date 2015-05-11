package view.panels;


import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * Video selector
 *
 */
public class VideoSelector {
	
	/**
	 * Single instance
	 */
	private static VideoSelector instance;
	
	private static String[] extensions = {"mpeg", "avi", "asf", "wmv", "wma", 
        "mp4", "mov", "3gp", "mkv"};
	
	
	/**
	 * Private constructor
	 */
	private VideoSelector() { }
	
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
	 * @return	Path of selected file if valid, otherwise null
	 */
	public static String show()
	{
		JFileChooser chooser = new JFileChooser();
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"All video files", extensions);
	    
	    chooser.setFileFilter(filter);
	    chooser.addChoosableFileFilter(filter);
	    
	    int returnVal = chooser.showOpenDialog(chooser);
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	if (isValidFile(chooser.getSelectedFile()))
	    		  return chooser.getSelectedFile().getPath();
	    }
	    
	    if(returnVal != JFileChooser.CANCEL_OPTION)
	    	JOptionPane.showMessageDialog(new JPanel(), error_message(), "Invalid file extension", JOptionPane.ERROR_MESSAGE);
	    return null;
	    
	}
	
	/**
	 * Checks the extension of a file
	 * @param f		File
	 * @return		True if valid extension
	 */
	private static boolean isValidFile(File f)
	{
		String fi = f.getName().toLowerCase();
		
		if(f.isFile())
			for(String e : extensions)
				if(fi.endsWith(e)) return true;
		
		return false;
	}
	
	/**
	 * Creates the error message containing all available extensions
	 */
	private static String error_message()
	{
		String e = "The extension of the file you chose is not supported.\n";
	    e += "Please select a file that has any of the following extensions:\n";
	    e += stringJoiner(extensions, ", ");
	    return e;
	}
	
	private static String stringJoiner(String[] list, String sep)
	{
		int l = list.length;
		String s;
		
		switch(l)
		{
		case 0: 
			s = "";
			break;
		case 1: 
			s = list[0];
			break;
		case 2: 
			s = String.format("%s%s%s", list[0], sep, list[1]);
			break;
		default:
			s = list[0];
			for(int i = 1; i < l-1; i++)
			{
				s += String.format("%s%s", sep, list[i]);
			}
			s += String.format("%s%s%s", s, sep, list[l-1]);
			break;
		}
		
		return s;
	}
	
	
}

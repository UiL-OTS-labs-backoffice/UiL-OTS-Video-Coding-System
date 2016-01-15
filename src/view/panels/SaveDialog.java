package view.panels;
import java.io.File;
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
	private SaveDialog() { 
		
	}
	
	/**
	 * get method for instance of video selector
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
	public static String show(File dir)
	{
		JFileChooser chooser = new JFileChooser();
		
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setCurrentDirectory(new File(dir.getAbsolutePath()));
//	    int returnVal = chooser.showSaveDialog(chooser);
		chooser.setApproveButtonText("Select");
		int returnVal = chooser.showOpenDialog(null);
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	return chooser.getSelectedFile().getAbsolutePath();
	    }
	    else return null;
	}	
}

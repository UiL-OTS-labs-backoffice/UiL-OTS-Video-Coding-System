package view.panels;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;





import model.ApplicationPreferences;
import controller.Globals;


/**
 * Shows a windows where the user can select a directory where the
 * project should be saved
 */
public class CSVExportSelector {
	
	/**
	 * Single instance
	 */
	
	private JFileChooser chooser;
	private int returnVal;
	private static String[] extensions = {"csv"};
	
	private static final String EXPORT_AS_PROJECT_FILTER 	= "CSV file (project)";
	private static final String EXPORT_AS_OVERVIEW_FILTER 	= "CSV file (overview)";
	
	public static final int EXPORT_AS_PROJECT = 0;
	public static final int EXPORT_AS_OVERVIEW = 1;
	
	/**
	 * Private constructor
	 */
	public CSVExportSelector() { 
		show();
	}
	
	/**
	 * Shows a file selector for the video file to be used
	 * @return	Path of selected file if valid, otherwise null
	 */
	public void show()
	{
		ApplicationPreferences prefs = Globals.getInstance().getPreferencesModel();
		chooser = new JFileChooser();
		chooser.setDialogTitle("Export project to CSV");
		
		final String exportOverview ="<html><body>The 'Export Overview' method, generates a CSV file containing <i>all</i> coded data. This means each trial and look, including every start, end, and look time, is exported</body></html>";
		final String exportProject = "<html><body>The 'Export Project' method generates a CSV file, containing only the total time of all looks in a trial, and no information about the seperate looks</body></html>";
		final JLabel methodDescription = new JLabel(exportProject);
		
		methodDescription.setBorder(new EmptyBorder(0,0,10,0));
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(methodDescription, BorderLayout.NORTH);
		panel.add(chooser.getComponent(3), BorderLayout.CENTER);
		chooser.add(panel, BorderLayout.SOUTH);
		
		final FileNameExtensionFilter export_project = new FileNameExtensionFilter(
				EXPORT_AS_PROJECT_FILTER, extensions);
		final FileNameExtensionFilter export_overview = new FileNameExtensionFilter(
				EXPORT_AS_OVERVIEW_FILTER, extensions);
	 
	    chooser.addChoosableFileFilter(export_project);
	    chooser.addChoosableFileFilter(export_overview);
	    chooser.setFileFilter(export_project);
	    
	    File here2go = new File(prefs.getLastCSVDirectory(
	    		prefs.getLastProjectDirectory(System.getProperty("user.home")))
	    	);
	    
	    try{
	    	 chooser.setCurrentDirectory(here2go);
	    } catch(IndexOutOfBoundsException e){
	    	// This is OK. It's trying to fire an event that's useless in this circumstance.
	    }
	   
	    
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.addPropertyChangeListener(JFileChooser.FILE_FILTER_CHANGED_PROPERTY, new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getNewValue() == export_project){
					methodDescription.setText(exportProject);
				} else {
					methodDescription.setText(exportOverview);
				}
			}
		});
	    
	    returnVal = chooser.showSaveDialog(chooser);
	    if(isApproved()) {
	    	prefs.setLastCSVDirectory(chooser.getSelectedFile().getParent());
	    }
	}
	
	/**
	 * Method to check if a valid file was selected
	 * @return	True if a valid file was selected
	 */
	public boolean isApproved(){
		return (returnVal == JFileChooser.APPROVE_OPTION);
	}
	
	/**
	 * Method to get the selected path
	 * @return	String	file path
	 * @throws IllegalStateException if the file was not approved
	 */
	public String getFilePath() throws IllegalStateException{
		if (!isApproved()){
			throw new IllegalStateException("File was not approved"); 
		}
		String path = chooser.getSelectedFile().getPath();
		if (!(path.endsWith(".csv") || path.endsWith(".CSV")))
			path += ".csv";
		return path;
	}
	
	/**
	 * Method to extract the selected export type
	 * @param chooser	The chooser where an export type is selected
	 * @return			The integer constant belonging to that type
	 */
	public int getExporterMethod(){
		if(chooser.getFileFilter().getDescription() == EXPORT_AS_OVERVIEW_FILTER){
			return EXPORT_AS_OVERVIEW;
		} else {
			return EXPORT_AS_PROJECT;
		}
	}
	
	/**
	 * Method to show a confirmation dialog if the saving went successfully
	 */
	public void showConfirmationWindow(){
		String msg = String.format(
				"%s was successfully exported to %s",
				(getExporterMethod() == EXPORT_AS_OVERVIEW) ? "Overview" : "Project",
				getFilePath()
			);
		JOptionPane.showMessageDialog(new JFrame(), msg);
	}
}

package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * JPanel that contains the trial information
 * @author mooij006
 *
 */
public class TrialInformation extends JPanel {

	private static final long serialVersionUID = 24188418938061483L;
	
	// Grid bag layout
	GridBagLayout informationTable;
	
	// Fields
    private JLabel trialNumber, lookNumber, lookTime, currentFile;
    
    /**
     * Constructor creates the panel
     */
    public TrialInformation()
    {
    	createLayout();
    	addCurrentFileLabels();
    	addTrialNumberLabels();
    	addLookNumberLabels();
    	addTotalLookTimeLabels();
    }
    
    /**
     * Method to set the trial look numbers and the current total look time
     * @param trial		Trial Number at current play position
     * @param look		Look Number at current play position
     * @param time		Total look time at current play position
     */
    public void setInfo(String trial, String look, String time)
    {
    	this.trialNumber.setText(trial);
    	this.lookNumber.setText(look);
    	this.lookTime.setText(time);
    }
    
    /**
     * Method to change the value of the file label
     * @param file		Name of the file to be set
     */
    public void setFile(String file)
    {
    	this.currentFile.setText(file);
    }
    
    private void createLayout()
    {
    	informationTable = new GridBagLayout();
    	informationTable.columnWidths = new int[] {0,0,300,0};
    	informationTable.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
    	informationTable.columnWeights = new double[]{1.0, 1.0, 1.0, 
    			Double.MIN_VALUE};
    	informationTable.rowWeights = new double[]{1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 
    			Double.MIN_VALUE};
        
    	// Set layout
    	this.setLayout(informationTable);
    }
    
    /**
     * Add current file labels
     */
    private void addCurrentFileLabels()
    {
    	JLabel fileLabel = new JLabel("Current file:");
    	GridBagConstraints gbc_fileLabel = new GridBagConstraints();
    	gbc_fileLabel.anchor = GridBagConstraints.EAST;
    	gbc_fileLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_fileLabel.gridx = 1;
    	gbc_fileLabel.gridy = 1;
    	add(fileLabel, gbc_fileLabel);
    
    	currentFile = new JLabel("");
    	GridBagConstraints gbc_currentFile = new GridBagConstraints();
    	gbc_currentFile.anchor = GridBagConstraints.WEST;
    	gbc_currentFile.insets = new Insets(0, 0, 5, 0);
    	gbc_currentFile.gridx = 2;
    	gbc_currentFile.gridy = 1;
    	add(currentFile, gbc_currentFile);
    }
    
    /**
     * Add the trial number labels
     */
    private void addTrialNumberLabels()
    {
    	
    	// Create labels
    	JLabel trialLabel = new JLabel("Trial:");
    	
    	// Grid Bag Constraints for labels
    	GridBagConstraints gbc_trialLabel = new GridBagConstraints();
    	
    	gbc_trialLabel.anchor = GridBagConstraints.EAST;
    	gbc_trialLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_trialLabel.gridx = 1;
    	gbc_trialLabel.gridy = 2;
    	
    	// Add labels to trial information bar
    	add(trialLabel, gbc_trialLabel);
    	
    	trialNumber = new JLabel("");
    	GridBagConstraints gbc_trialNumber = new GridBagConstraints();
        
        gbc_trialNumber.insets = new Insets(0, 0, 5, 0);
        gbc_trialNumber.anchor = GridBagConstraints.WEST;
        gbc_trialNumber.gridx = 2;
        gbc_trialNumber.gridy = 2;
    	add(trialNumber, gbc_trialNumber);
    }
    
    private void addLookNumberLabels()
    {
    	// Create labels
    	JLabel lookLabel = new JLabel("Look number:");
    	
    	// Grid Bag Constraints for labels
    	GridBagConstraints gbc_lookLabel = new GridBagConstraints();
    	
    	gbc_lookLabel.anchor = GridBagConstraints.EAST;
    	gbc_lookLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_lookLabel.gridx = 1;
    	gbc_lookLabel.gridy = 3;
    	
    	// Add labels to trial number panel
    	add(lookLabel, gbc_lookLabel);
    	
    	lookNumber = new JLabel("");
    	GridBagConstraints gbc_lookNumber = new GridBagConstraints();
    	
    	gbc_lookNumber.anchor = GridBagConstraints.WEST;
        gbc_lookNumber.insets = new Insets(0, 0, 5, 0);
        gbc_lookNumber.gridx = 2;
        gbc_lookNumber.gridy = 3;
    	add(lookNumber, gbc_lookNumber);
    }
    
    private void addTotalLookTimeLabels()
    {
    	lookTime = new JLabel("");
    	GridBagConstraints gbc_lookTime = new GridBagConstraints();
    	
    	gbc_lookTime.anchor = GridBagConstraints.WEST;
    	gbc_lookTime.insets = new Insets(0, 0, 5, 0);
    	gbc_lookTime.gridx = 2;
    	gbc_lookTime.gridy = 4;
    	add(lookTime, gbc_lookTime);
    	
    	// Create labels
    	JLabel lookTimeLabel = new JLabel("Total look time:");
    	
    	// Grid Bag Constraints for labels
    	GridBagConstraints gbc_lookTimeLabel = new GridBagConstraints();
    	
    	gbc_lookTimeLabel.anchor = GridBagConstraints.EAST;
    	gbc_lookTimeLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_lookTimeLabel.gridx = 1;
    	gbc_lookTimeLabel.gridy = 4;
    	
    	// Add labels to trial number panel
    	add(lookTimeLabel, gbc_lookTimeLabel);
    }
}

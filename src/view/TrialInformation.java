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
    private JLabel trialNumber, lookNumber, lookTime;
    
    /**
     * Constructor creates the panel
     */
    public TrialInformation()
    {
    	createLayout();
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
     * Add the trial number labels
     */
    private void addTrialNumberLabels()
    {
    	// Create panel
    	JPanel trialNRS = new JPanel();
    	
    	// Grid bag constraints
    	GridBagConstraints gbc_trialNRS = new GridBagConstraints();
    	gbc_trialNRS.insets = new Insets(0,0,5,5);
    	gbc_trialNRS.fill = GridBagConstraints.BOTH;
    	gbc_trialNRS.gridx = 0;
    	gbc_trialNRS.gridy = 1;
    	add(trialNRS, gbc_trialNRS);
    	
    	// Create labels
    	JLabel trialLabel = new JLabel("Trial:");
    	trialNumber = new JLabel("");
    	
    	// Grid Bag Constraints for labels
    	GridBagConstraints gbc_trialLabel = new GridBagConstraints();
    	GridBagConstraints gbc_trialNumber = new GridBagConstraints();
    	
    	gbc_trialLabel.anchor = GridBagConstraints.EAST;
    	gbc_trialLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_trialLabel.gridx = 1;
    	gbc_trialLabel.gridy = 1;
        
        gbc_trialNumber.insets = new Insets(0, 0, 5, 5);
        gbc_trialNumber.anchor = GridBagConstraints.WEST;
        gbc_trialNumber.gridx = 2;
        gbc_trialNumber.gridy = 1;
    	
    	// Add labels to trial information bar
    	add(trialLabel, gbc_trialLabel);
    	add(trialNumber, gbc_trialNumber);
    }
    
    private void addLookNumberLabels()
    {
    	// Create and add panel
    	JPanel lookNRS = new JPanel();

    	// Grid bag constraints
    	GridBagConstraints gbc_lookNRS = new GridBagConstraints();
    	gbc_lookNRS.insets = new Insets(0,0,5,5);
    	gbc_lookNRS.fill = GridBagConstraints.BOTH;
    	gbc_lookNRS.gridx = 0;
    	gbc_lookNRS.gridy = 2;
    	add(lookNRS, gbc_lookNRS);
    	
    	// Create labels
    	JLabel lookLabel = new JLabel("Look number:");
    	lookNumber = new JLabel("");
    	
    	// Grid Bag Constraints for labels
    	GridBagConstraints gbc_lookLabel = new GridBagConstraints();
    	GridBagConstraints gbc_lookNumber = new GridBagConstraints();
    	
    	gbc_lookLabel.anchor = GridBagConstraints.EAST;
    	gbc_lookLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_lookLabel.gridx = 1;
    	gbc_lookLabel.gridy = 2;
    	
    	gbc_lookNumber.anchor = GridBagConstraints.WEST;
        gbc_lookNumber.insets = new Insets(0, 0, 5, 0);
        gbc_lookNumber.gridx = 2;
        gbc_lookNumber.gridy = 2;
    	
    	// Add labels to trial number panel
    	add(lookLabel, gbc_lookLabel);
    	add(lookNumber, gbc_lookNumber);
    }
    
    private void addTotalLookTimeLabels()
    {
    	// Create and add panel
    	JPanel lookTimePanel = new JPanel();
    	
    	// Grid bag constraints
    	GridBagConstraints gbc_lookTimePanel = new GridBagConstraints();
    	gbc_lookTimePanel.insets = new Insets(0,0,5,5);
    	gbc_lookTimePanel.fill = GridBagConstraints.BOTH;
    	gbc_lookTimePanel.gridx = 0;
    	gbc_lookTimePanel.gridy = 3;
    	add(lookTimePanel, gbc_lookTimePanel);
    	
    	// Create labels
    	JLabel lookTimeLabel = new JLabel("Total look time:");
    	lookTime = new JLabel("");
    	
    	// Grid Bag Constraints for labels
    	GridBagConstraints gbc_lookTimeLabel = new GridBagConstraints();
    	GridBagConstraints gbc_lookTime = new GridBagConstraints();
    	
    	gbc_lookTimeLabel.anchor = GridBagConstraints.EAST;
    	gbc_lookTimeLabel.insets = new Insets(0, 0, 5, 5);
    	gbc_lookTimeLabel.gridx = 1;
    	gbc_lookTimeLabel.gridy = 3;
    	
    	gbc_lookTime.anchor = GridBagConstraints.WEST;
    	gbc_lookTime.insets = new Insets(0, 0, 5, 0);
    	gbc_lookTime.gridx = 2;
    	gbc_lookTime.gridy = 3;
    	
    	// Add labels to trial number panel
    	add(lookTimeLabel, gbc_lookTimeLabel);
    	add(lookTime, gbc_lookTime);
    }
}

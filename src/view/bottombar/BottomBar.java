package view.bottombar;

import java.awt.BorderLayout;

import javax.swing.JPanel;

public class BottomBar extends JPanel {
	
	private static final long serialVersionUID = 8873779601345831556L;
	
	// Trial information panel instance
	private TrialInformation trialInformation;
	private PlayerControlsPanel timecodes;
	
	public BottomBar()
	{
		setLayout(new BorderLayout(0, 0));
		addTimeBar();
		addTrialControls();
		AddTrialInformation();
	}
	
	/**
     * Method to set the trial look numbers and the current total look time
     * This gets passed along to the trial information panel
     * @param trial		Trial Number at current play position
     * @param look		Look Number at current play position
     * @param time		Total look time at current play position
     */
    public void setInfo(String trial, String look, String time)
    {
    	trialInformation.setInfo(trial, look, time);
    }
    
    /**
     * Method to change the value of the file label in the trial information
     * @param file		Name of the file to be set
     */
    public void setFile(String file)
    {
    	trialInformation.setFile(file);
    }
    
    public PlayerControlsPanel getTimeCodes()
    {
    	return timecodes;
    }
	
	/**
	 * Adds the time slider video controls to the bottom bar panel
	 */
	private void addTimeBar()
	{
		timecodes = new PlayerControlsPanel();
		add(timecodes, BorderLayout.NORTH);
	}
	
	/**
     * Function to add trial control buttons to view
     */
    private void addTrialControls()
    {
    	TrialControls trial_controls = new TrialControls();
        add(trial_controls, BorderLayout.EAST);
    }
    
    /**
     * Function to add the trial information to the view
     */
    private void AddTrialInformation()
    {
    	trialInformation = new TrialInformation();
    	add(trialInformation, BorderLayout.WEST);
    }
	
	
}

package view.bottombar;

import java.awt.BorderLayout;

import controller.*;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import view.navbar.Navbar;

public class BottomBar extends JPanel {
	
	private static final long serialVersionUID = 8873779601345831556L;
	
	// Trial information panel instance
	private TrialInformation trialInformation;
	private TrialControls trial_controls;
	private PlayerControlsPanel timecodes;
	private JPanel controlPanel;
	private Navbar navbar;
	
	private Globals g;
	
	public BottomBar(Globals g)
	{
		this.g = g;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setLayout(new BorderLayout(0, 0));
			}
		});
		addTimeBar();
		addControlPanel();
		addDetailBar();
	}
	
	public void videoInstantiated(){
		navbar.videoInstantiated();
	}
	
	/**
     * Method to set the trial look numbers and the current total look time
     * This gets passed along to the trial information panel
     * @param trial		Trial Number at current play position
     * @param look		Look Number at current play position
     * @param time		Total look time at current play position
     */
    public void setInfo(final String trial, final String look, final String time)
    {
		trialInformation.setInfo(trial, look, time);
    }
    
    /**
     * Method to update the buttons in the bottom bar
     * @param trial		The currently active trial number
     * @param look		The currently active look number
     * @param nt		The state of the new trial button
     * @param et		The state of the end trial button
     * @param nl		The state of the new look button
     * @param el		The state of the end look button
     * @param lookComment 
     * @param trialComment 
     */
    public void updateButtons(
			int trial, int look,
			boolean nt, boolean et, boolean nl, boolean el
		)
	{
    	trial_controls.update(trial, look, nt, et, nl, el);
	}
    
    /**
     * Method to change the value of the file label in the trial information
     * @param file		Name of the file to be set
     */
    public void setFile(String file)
    {
		trialInformation.setFile(file);
    }
    
    /**
     * Method to get the panel that contains the time codes
     * @return	PlayerCOntrolsPanel instance
     */
    public PlayerControlsPanel getTimeCodes()
    {
    	return timecodes;
    }
	
	/**
	 * Adds the time slider video controls to the bottom bar panel
	 * TODO remove this
	 */
	private void addTimeBar()
	{
		timecodes = new PlayerControlsPanel(g);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				add(timecodes, BorderLayout.NORTH);
			}
		});
	}
	
	// TODO remove trial information. Add controls left to player controls, whereever those are
	private void addControlPanel()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				controlPanel = new JPanel();
				controlPanel.setLayout(new BorderLayout(0, 0));
				add(controlPanel, BorderLayout.NORTH);
			}
		});
		addTrialControls();
		AddTrialInformation();
	}
	
	/**
     * Function to add trial control buttons to view
     */
    private void addTrialControls()
    {
		trial_controls = new TrialControls(g);
        SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				controlPanel.add(trial_controls, BorderLayout.EAST);
			}
		});
    }
    
    /**
     * Function to add the trial information to the view
     */
    private void AddTrialInformation()
    {
		trialInformation = new TrialInformation();
    	SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				controlPanel.add(trialInformation, BorderLayout.WEST);
			}
		});
    }
	
    private void addDetailBar()
    {
		navbar = new Navbar(g);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				add(navbar, BorderLayout.SOUTH);
			}
		});
    }

	public void mediaTimeChanged() {
		navbar.mediaTimeChanged();
	}
	
	public void addTimeFrame(model.AbstractTimeFrame tf)
	{
		navbar.addTimeFrame(tf);
	}
	
	public void removeTimeFrame(model.AbstractTimeFrame tf)
	{
		navbar.removeTimeFrame(tf);
	}
}

package view.bottombar;

import java.awt.BorderLayout;

import controller.*;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import view.VideoManipulationButtons;
import view.navbar.Navbar;

public class BottomBar extends JPanel {
	
	private static final long serialVersionUID = 8873779601345831556L;
	
	// Trial information panel instance
	private JPanel controlPanel;
	private TrialControls trial_controls;
	private VideoManipulationButtons playButtons;
	
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
		addControlPanel();
		addDetailBar();
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
    
	private void addControlPanel()
	{
		controlPanel = new JPanel();
		trial_controls = new TrialControls(g);
		playButtons = new VideoManipulationButtons(g);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				controlPanel.setLayout(new BorderLayout(0, 0));
				controlPanel.add(playButtons, BorderLayout.WEST);
				controlPanel.add(trial_controls, BorderLayout.EAST);
				add(controlPanel, BorderLayout.NORTH);
			}
		});
	}
    
    private void addDetailBar()
    {
		navbar = new Navbar(g);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				add(navbar, BorderLayout.CENTER);
			}
		});
    }
    
    public void setTimeoutText(final boolean state)
    {
		playButtons.setTimeoutText(state);
    }
}

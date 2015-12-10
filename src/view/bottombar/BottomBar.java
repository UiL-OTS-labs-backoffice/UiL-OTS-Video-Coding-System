package view.bottombar;

import java.awt.BorderLayout;

import controller.*;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import view.timeline.TimeLineBar;

public class BottomBar extends JPanel {
	
	private static final long serialVersionUID = 8873779601345831556L;
	
	private JPanel controlPanel;
	private TrialControls trial_controls;
	private VideoManipulationButtons playButtons;
	private TimeLineBar navbar;
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
		navbar = new TimeLineBar(g);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				add(navbar, BorderLayout.CENTER);
			}
		});
    }
}

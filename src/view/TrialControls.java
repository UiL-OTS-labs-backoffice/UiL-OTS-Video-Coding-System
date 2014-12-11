package view;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class TrialControls extends JPanel {
	
	private static final long serialVersionUID = -2015592156911727320L;
	
	// Buttons
	JButton newTrial, endTrial, newLook, endLook;

	public TrialControls()
	{
		setLayout(new BorderLayout(0,0));
		createButtons();
	}
	
	private void createButtons()
	{
		newTrial = new JButton("New trial");
		
		JPanel inBetween = new JPanel();
		inBetween.setLayout(new BorderLayout(0,0));
		
		endTrial = new JButton("End trial");
		
		newLook = new JButton("Start new Look");
		endLook = new JButton("End Look");
		
		add(newTrial, BorderLayout.NORTH);
		add(inBetween, BorderLayout.CENTER);
		add(endLook, BorderLayout.SOUTH);
		
		inBetween.add(endTrial, BorderLayout.NORTH);
		inBetween.add(newLook, BorderLayout.SOUTH);
	}
}

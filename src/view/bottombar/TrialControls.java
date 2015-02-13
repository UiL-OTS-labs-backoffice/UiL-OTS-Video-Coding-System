package view.bottombar;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TrialControls extends JPanel {
	
	private static final long serialVersionUID = -2015592156911727320L;
	
	// Buttons
	private JButton newTrial, endTrial, newLook, endLook;
	
	private static final Dimension BUTTON_SIZE = new Dimension(150, 25);

	public TrialControls()
	{
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		addNewTrialButton();
		addEndTrialButton();
		addNewLookButton();
		addEndLookButton();
	}
	
	/**
	 * Method to update the text and state of the experiment buttons
	 * @param newTrial		new trial button text
	 * @param endTrial		end trial button text
	 * @param newLook		new look button text
	 * @param endLook		end look button text
	 * @param nt			new trial enabled state
	 * @param et			end trial enabled state
	 * @param nl			new look enabled sate 
	 * @param el			end look enabled state
	 */
	public void update(
			String endTrial,String endLook,
			boolean nt, boolean et, boolean nl, boolean el
		)
	{
		this.newTrial.setEnabled(nt);
		
		this.endTrial.setText(endTrial);
		this.endTrial.setEnabled(et);
		
		this.newLook.setEnabled(nl);
		
		this.endLook.setText(endLook);
		this.endLook.setEnabled(el);
	}

	private void addNewTrialButton()
	{
		newTrial = new JButton("New trial");
		newTrial.setToolTipText("New Trial");
		
		newTrial.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!controller.Controller.getInstance().newTrial())
				{
					System.out.println("Couldn't create trial");
				}
			}
		});
		
		add(newTrial, Component.CENTER_ALIGNMENT);
		newTrial.setPreferredSize(BUTTON_SIZE);
		newTrial.setMaximumSize(BUTTON_SIZE);
		newTrial.setFocusable(false);
	}
	
	private void addEndTrialButton()
	{
		endTrial = new JButton("End trial");
		endTrial.setToolTipText("End trial");
		
		endTrial.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!controller.Controller.getInstance().setEndTrial())
				{
					System.out.println("Trial couldn't be ended");
				}
			}
			
		});
		
		add(endTrial, Component.CENTER_ALIGNMENT);
		endTrial.setMaximumSize(BUTTON_SIZE);
		endTrial.setFocusable(false);
	}
	
	private void addNewLookButton()
	{
		newLook = new JButton("New look");
		newLook.setToolTipText("New look");
		
		newLook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!controller.Controller.getInstance().newLook())
				{
					System.out.println("Couldn't create look");
				}
			}
		});
		
		add(newLook, Component.CENTER_ALIGNMENT);
		newLook.setMaximumSize(BUTTON_SIZE);
		newLook.setFocusable(false);
	}
	
	private void addEndLookButton()
	{
		endLook = new JButton("End look");
		endLook.setToolTipText("End look");
		
		endLook.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(!controller.Controller.getInstance().setEndLook())
				{
					System.out.println("Look couldn't be ended");
				}
			}
			
		});
		
		add(endLook, Component.CENTER_ALIGNMENT);
		endLook.setMaximumSize(BUTTON_SIZE);
		endLook.setFocusable(false);
	}
	
	
}

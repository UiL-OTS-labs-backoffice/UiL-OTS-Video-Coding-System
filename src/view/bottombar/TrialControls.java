package view.bottombar;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import model.AbstractTimeFrame;
import model.Trial;
import view.panels.CommentEditor;
import controller.Controller;
import controller.Globals;
import controller.IVideoControls;

public class TrialControls extends JPanel {
	
	private static final long serialVersionUID = -2015592156911727320L;
	
	private Globals g;
	private Controller c;
	private IVideoControls v;
	
	// Buttons
	private JButton newTrial, endTrial, newLook, endLook, trialComment,lookComment;
	
	private static final Dimension BUTTON_SIZE = new Dimension(150, 25);

	public TrialControls(Globals g)
	{
		this.g = g;
		c = g.getController();
		v = g.getVideoController();
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		addNewTrialButton();
		addEndTrialButton();
		addNewLookButton();
		addEndLookButton();
		addCommentsButtons();
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
	 * @param lookComment 
	 * @param trialComment 
	 */
	public void update(
			String endTrial,String endLook,
			boolean nt, boolean et, boolean nl, boolean el, String trialComment, String lookComment
		)
	{
		this.newTrial.setEnabled(nt);
		
		this.endTrial.setText(endTrial);
		this.endTrial.setEnabled(et);
		this.trialComment.setEnabled(et || nl || el);
		this.trialComment.setText(trialComment);
		
		this.newLook.setEnabled(nl);
		
		this.endLook.setText(endLook);
		this.endLook.setEnabled(el);
		
		this.lookComment.setEnabled(el);
		this.lookComment.setText(lookComment);
	}

	private void addNewTrialButton()
	{
		newTrial = new JButton("New trial");
		newTrial.setToolTipText("New Trial");
		
		newTrial.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				c.newTrial();
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
				c.setEndTrial();
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
				c.newLook();
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
				c.setEndLook();
			}
			
		});
		
		add(endLook, Component.CENTER_ALIGNMENT);
		endLook.setMaximumSize(BUTTON_SIZE);
		endLook.setFocusable(false);
	}
	
	private void addCommentsButtons()
	{
		trialComment = new JButton("Trial comment");
		trialComment.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent event) {
				int tn = Math.abs(g.getExperimentModel().getItemForTime(v.getMediaTime()));
				Trial t = c.getTrial(tn);
				new CommentEditor(t.getBegin(), t, String.format("Trial %d",tn));
			}
		});
		
		lookComment = new JButton("Look comment");
		lookComment.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				int tn = Math.abs(g.getExperimentModel().getItemForTime(v.getMediaTime()));
				Trial t = c.getTrial(tn);
				int ln = Math.abs(t.getItemForTime(v.getMediaTime()));
				AbstractTimeFrame l = t.getItem(ln);
				new CommentEditor(t.getBegin(), l, String.format("Trial %d look %d",tn, ln));
			}
		});
		
		add(trialComment, Component.LEFT_ALIGNMENT);
		trialComment.setMaximumSize(BUTTON_SIZE);
		trialComment.setFocusable(false);
		
		add(lookComment, Component.RIGHT_ALIGNMENT);
		lookComment.setMaximumSize(BUTTON_SIZE);
		lookComment.setFocusable(false);
	}
	
}

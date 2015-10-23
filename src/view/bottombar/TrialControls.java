package view.bottombar;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setLayout(new BoxLayout(TrialControls.this, BoxLayout.PAGE_AXIS));
				addNewTrialButton();
				addEndTrialButton();
				addNewLookButton();
				addEndLookButton();
				addCommentsButtons();
			}
		});
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
			final String endTrial,final String endLook,
			final boolean nt, final boolean et, final boolean nl, final boolean el, final String trialComment, final String lookComment
		)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				TrialControls.this.newTrial.setEnabled(nt);
				TrialControls.this.endTrial.setText(endTrial);
				TrialControls.this.endTrial.setEnabled(et);
				TrialControls.this.trialComment.setEnabled(et || nl || el);
				TrialControls.this.trialComment.setText(trialComment);
				TrialControls.this.newLook.setEnabled(nl);
				TrialControls.this.endLook.setText(endLook);
				TrialControls.this.endLook.setEnabled(el);
				TrialControls.this.lookComment.setEnabled(el);
				TrialControls.this.lookComment.setText(lookComment);
			}
		});
	}

	private void addNewTrialButton()
	{
		newTrial = new JButton("New trial");
		newTrial.setToolTipText("New Trial");
		
		newTrial.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Thread ntT = new Thread(){
					public void run(){
						c.newTrial();
					}
				};
				ntT.start();
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
				Thread etT = new Thread(){
					public void run(){
						c.setEndTrial();
					}
				};
				etT.start();
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
				Thread nlT = new Thread(){
					public void run(){
						c.newLook();
					}
				};
				nlT.start();
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
				Thread elT = new Thread(){
					public void run(){
						c.setEndLook();
					}
				};
				elT.start();
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
				Thread actionThread = new Thread(){
					public void run(){
						final int tn = Math.abs(g.getExperimentModel().getItemForTime(v.getMediaTime()));
						final Trial t = c.getTrial(tn);
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								new CommentEditor(t.getBegin(), t, String
										.format("Trial %d", tn));
							}
						});
					}
				};
				actionThread.start();
				
			}
		});
		
		lookComment = new JButton("Look comment");
		lookComment.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event) {
				Thread actionThread = new Thread(){
					public void run(){
						final int tn = Math.abs(g.getExperimentModel().getItemForTime(v.getMediaTime()));
						final Trial t = c.getTrial(tn);
						final int ln = Math.abs(t.getItemForTime(v.getMediaTime()));
						final AbstractTimeFrame l = t.getItem(ln);
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								new CommentEditor(t.getBegin(), l, String.format(
										"Trial %d look %d", tn, ln));
							}
						});
					}
				};
				actionThread.start();
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

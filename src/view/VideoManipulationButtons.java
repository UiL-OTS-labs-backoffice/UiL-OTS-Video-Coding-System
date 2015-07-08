package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.Globals;
import controller.IVideoControls;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JLabel;

/**
 * Panel with buttons for video manipulation
 */
public class VideoManipulationButtons extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9024137258475533717L;
	
	private Globals g;
	private IVideoControls c;
	
	/**
	 * Buttons
	 */
	private JButton prevTrial, nextTrial, prevFrame, nextFrame, playPause;
	private JPanel panel;
	private JLabel lblTimeout;
	
	/**
	 * Constructor class, creates the buttons
	 */
	public VideoManipulationButtons(Globals g)
	{
		this.g = g;
		c = this.g.getVideoController();
		
		setLayout(new BorderLayout());
		
		panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		
		addTimeoutText();
		addPrevTrialButton();
		addPrevFrameButton();
		addPauseButton();
		addNextFrameButton();
		addNextTrialButton();
	}
	
	public void setEnableButtons(boolean state)
	{
		prevTrial.setEnabled(state);
		nextTrial.setEnabled(state);
		prevFrame.setEnabled(state);
		nextFrame.setEnabled(state);
		playPause.setEnabled(state);
	}
	
	private void addTimeoutText()
	{
		lblTimeout = new JLabel("Timeout");
		lblTimeout.setHorizontalAlignment(SwingConstants.CENTER);
		lblTimeout.setFont(new Font("Tahoma", Font.PLAIN, 35));
		lblTimeout.setForeground(Color.RED);
		lblTimeout.setVisible(false);
		add(lblTimeout, BorderLayout.CENTER);
	}
	
	/**
	 * Adds a previous trial button
	 * Clicking this button will take the video to the end of the previous
	 * trial (if any)
	 */
	private void addPrevTrialButton()
	{
		prevTrial = new JButton("<<");
		panel.add(prevTrial);
		prevTrial.setToolTipText("Previous trial");
		
		prevTrial.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		c.prevTrial();
        	}
        });
		prevTrial.setFocusable(false);
		prevTrial.setEnabled(false);
	}
	
	/**
	 * Adds a next trial button
	 * Clicking this button will advance the video to the start of the next
	 * trial (if any)
	 */
	private void addNextTrialButton()
	{
		nextTrial = new JButton(">>");
		panel.add(nextTrial);
		nextTrial.setToolTipText("Next trial");
		
		nextTrial.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
        		c.nextTrial();
        	}
        });
		nextTrial.setFocusable(false);
		nextTrial.setEnabled(false);
	}
	
	/**
	 * Adds previous frame button. 
	 * Clicking this button will load the previous frame in the video
	 */
	private void addPrevFrameButton()
	{
		prevFrame = new JButton("<");
		panel.add(prevFrame);
		prevFrame.setToolTipText("Previous frame");
		
		prevFrame.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
        		c.prevFrame();
        	}
        });
		prevFrame.setFocusable(false);
		prevFrame.setEnabled(false);
	}
	
	/**
	 * Adds a next frame button
	 * Clicking this button will advance the video one frame
	 */
	private void addNextFrameButton()
	{
		nextFrame = new JButton(">");
		panel.add(nextFrame);
		nextFrame.setToolTipText("Next frame");
		
		nextFrame.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
        		c.nextFrame();
        	}
        });
		nextFrame.setFocusable(false);
		nextFrame.setEnabled(false);
	}
	
	/**
	 * Adds a button to pause the video.
	 * If the video is already paused, this will continue the playing of the
	 * video
	 */
	private void addPauseButton()
	{
		playPause = new JButton("\u25b6");
		panel.add(playPause);
		playPause.setToolTipText("Play or pause video");
		playPause.setPreferredSize(new Dimension(45,26));
		
		playPause.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e) {
        		c.play();
        	}
        });
		playPause.setFocusable(false);
		playPause.setEnabled(false);
	}
	
	public void setPlay(boolean state)
	{
		if (state)
			playPause.setText("\u25b6");
		else
			playPause.setText("||");
	}
	
	/**
	 * Method to show or hide the text that indicates a timeout
	 * @param state	Boolean. True iff text is to show
	 */
	public void setTimeoutText(boolean state)
	{
		lblTimeout.setVisible(state);
	}
}

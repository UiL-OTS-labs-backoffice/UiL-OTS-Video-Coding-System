package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import controller.Globals;

/**
 * Panel with buttons for video manipulation
 */
public class VideoManipulationButtons extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9024137258475533717L;
	
	/**
	 * Buttons
	 */
	private JButton prevTrial, nextTrial, prevFrame, nextFrame, playPause;
	
	/**
	 * Constructor class, creates the buttons
	 */
	public VideoManipulationButtons()
	{
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
	
	/**
	 * Adds a previous trial button
	 * Clicking this button will take the video to the end of the previous
	 * trial (if any)
	 */
	private void addPrevTrialButton()
	{
		prevTrial = new JButton("<<");
		prevTrial.setToolTipText("Previous trial");
		
		prevTrial.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Globals.getVideoController().prevTrial();
        	}
        });
		
		add(prevTrial);
		
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
		nextTrial.setToolTipText("Next trial");
		
		nextTrial.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Globals.getVideoController().nextTrial();
        	}
        });
		
		add(nextTrial);
		
		nextTrial.setEnabled(false);
	}
	
	/**
	 * Adds previous frame button. 
	 * Clicking this button will load the previous frame in the video
	 */
	private void addPrevFrameButton()
	{
		prevFrame = new JButton("<");
		prevFrame.setToolTipText("Previous frame");
		
		prevFrame.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Globals.getVideoController().prevFrame();
        	}
        });
		
		add(prevFrame);
		
		prevFrame.setEnabled(false);
	}
	
	/**
	 * Adds a next frame button
	 * Clicking this button will advance the video one frame
	 */
	private void addNextFrameButton()
	{
		nextFrame = new JButton(">");
		nextFrame.setToolTipText("Next frame");
		
		nextFrame.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Globals.getVideoController().nextFrame();
        	}
        });
		
		add(nextFrame);
		
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
		playPause.setToolTipText("Play or pause video");
		playPause.setPreferredSize(new Dimension(45,26));
		
		playPause.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Globals.getVideoController().play();
        		if(Globals.getVideoController().isPlaying())
        			playPause.setText("\u25b6");
        		else
        			playPause.setText("||");
        	}
        });
		
		add(playPause);
		
		playPause.setEnabled(false);
	}
}

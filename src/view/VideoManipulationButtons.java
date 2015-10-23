package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
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
		});
	}
	
	public void setEnableButtons(final boolean state)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				prevTrial.setEnabled(state);
				nextTrial.setEnabled(state);
				prevFrame.setEnabled(state);
				nextFrame.setEnabled(state);
				playPause.setEnabled(state);
			}
		});
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
		
		prevTrial.setToolTipText("Previous trial");
		
		prevTrial.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Thread prevTrialThread = new Thread(){
        			public void run(){
        				c.prevTrial();
        			}
        		};
        		prevTrialThread.start();
        	}
        });
		
		prevTrial.setFocusable(false);
		prevTrial.setEnabled(false);
		
		panel.add(prevTrial);
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
    			Thread nextTrialThread = new Thread(){
    				public void run(){
    					c.nextTrial();
    				}
    			};
    			nextTrialThread.start();
        	}
        });
		nextTrial.setFocusable(false);
		nextTrial.setEnabled(false);
		
		panel.add(nextTrial);
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
    			Thread prevFrameThread = new Thread(){
    				public void run(){
    					c.prevFrame();
    				}
    			};
        		prevFrameThread.start();
        	}
        });
		prevFrame.setFocusable(false);
		prevFrame.setEnabled(false);
		
		panel.add(prevFrame);
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
        		Thread nextFrameThread = new Thread(){
        			public void run(){
        				c.nextFrame();
        			}
        		};
        		nextFrameThread.start();
        	}
        });
		nextFrame.setFocusable(false);
		nextFrame.setEnabled(false);
		
		panel.add(nextFrame);
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
        		Thread togglePlayThread = new Thread(){
        			public void run(){
        				c.play();
        			}
        		};
    			togglePlayThread.start();
        	}
        });
		playPause.setFocusable(false);
		playPause.setEnabled(false);
		
		panel.add(playPause);
	}
	
	public void setPlay(final boolean state)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (state) {
					playPause.setText("\u25b6");
				} else {
					playPause.setText("||");
				}
			}
		});	
	}
	
	/**
	 * Method to show or hide the text that indicates a timeout
	 * @param state	Boolean. True iff text is to show
	 */
	public void setTimeoutText(final boolean state)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				lblTimeout.setVisible(state);
			}
		});
	}
}

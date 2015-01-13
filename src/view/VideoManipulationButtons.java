package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import controller.Controller;

public class VideoManipulationButtons extends JPanel {

	private static final long serialVersionUID = 9024137258475533717L;
	
	// Instance of the video player
//	private EmbeddedMediaPlayer videoPlayer;
	private DirectMediaPlayer videoPlayer;
	
//	public VideoManipulationButtons(EmbeddedMediaPlayer videoPlayer)
	public VideoManipulationButtons(DirectMediaPlayer videoPlayer)
	{
		this.videoPlayer = videoPlayer;
		addPrevTrialButton();
		addPrevFrameButton();
		addPauseButton();
		addNextFrameButton();
		addNextTrialButton();
	}
	
	/**
	 * Adds a previous trial button
	 * Clicking this button will take the video to the end of the previous
	 * trial (if any)
	 */
	public void addPrevTrialButton()
	{
		JButton prevTrial = new JButton("<<");
		prevTrial.setToolTipText("Previous trial");
		add(prevTrial);
		
		// TODO: implement and enable when ready
		prevTrial.setEnabled(false);
	}
	
	/**
	 * Adds a next trial button
	 * Clicking this button will advance the video to the start of the next
	 * trial (if any)
	 */
	public void addNextTrialButton()
	{
		JButton nextTrial = new JButton(">>");
		nextTrial.setToolTipText("Next trial");
		add(nextTrial);
		
		// TODO: implement and enable when ready
		nextTrial.setEnabled(false);
	}
	
	/**
	 * Adds previous frame button. 
	 * Clicking this button will load the previous frame in the video
	 */
	public void addPrevFrameButton()
	{
		JButton prevFrame = new JButton("<");
		prevFrame.setToolTipText("Previous frame");
		
		prevFrame.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Controller.getInstance().prevFrame();
        	}
        });
		
		add(prevFrame);
		
		// TODO: implement and enable when ready
		//prevFrame.setEnabled(false);
	}
	
	/**
	 * Adds a next frame button
	 * Clicking this button will advance the video one frame
	 */
	public void addNextFrameButton()
	{
		JButton nextFrame = new JButton(">");
		nextFrame.setToolTipText("Next frame");
		
		nextFrame.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		videoPlayer.nextFrame();
        	}
        });
		
		add(nextFrame);
	}
	
	/**
	 * Adds a button to pause the video.
	 * If the video is already paused, this will continue the playing of the
	 * video
	 */
	public void addPauseButton()
	{
		JButton playPause = new JButton("Play/Pause");
		playPause.setToolTipText("Play or pause video");
		
		playPause.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		videoPlayer.pause();
        	}
        });
		
		add(playPause);
	}
	
}

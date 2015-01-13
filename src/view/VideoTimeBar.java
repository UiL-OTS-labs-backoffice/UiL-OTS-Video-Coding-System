package view;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import javax.swing.JLabel;
import javax.swing.JSlider;

import java.awt.BorderLayout;

import javax.swing.SwingConstants;

import java.awt.Color;

import javax.swing.UIManager;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VideoTimeBar extends JPanel {

	private static final long serialVersionUID = -8389945220035956775L;
	
	//private EmbeddedMediaPlayer videoPlayer;
	private DirectMediaPlayer videoPlayer;
	
	
	// TODO: Fix clickable end
	private boolean endFixed = true;
	
	private JLabel timeLabel, endLabel;
	private JSlider positionSlider;
	
	/**
	 * Constructor for timebar panel
	 * @param videoPlayer	EmbeddedMediaPlayer that is used for playing video
	 */
	//public VideoTimeBar(EmbeddedMediaPlayer videoPlayer)
	public VideoTimeBar(DirectMediaPlayer videoPlayer)
	{
		this.videoPlayer = videoPlayer;
		createUI();
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new UpdateRunnable(videoPlayer), 
				0, 1);
		
	}
	
	/**
	 * Creates the User Interface of the timebar
	 */
	private void createUI()
	{
		setLayout(new BorderLayout(0, 0));
		
		// Time Label
		timeLabel = new JLabel("hh:mm:ii");
		
		// Slider
		positionSlider = new JSlider();
		positionSlider.setValue(0);
		positionSlider.setMaximum(10000);
		positionSlider.setToolTipText("Video progress");
		
		// End label
		endLabel = new JLabel("hh:mm:ii");
		endLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				endFixed = !endFixed;
			}
		});
		
		// Add to view
		add(timeLabel, BorderLayout.WEST);
		add(positionSlider, BorderLayout.CENTER);
		add(endLabel, BorderLayout.EAST);
	}
	
	/**
	 * Sets the time to the time label
	 * @param time		Time in milliseconds
	 */
	private void updateTime(long time)
	{
		 String s = String.format("%02d:%02d:%02d", 
	        		TimeUnit.MILLISECONDS.toHours(time), 
	        		TimeUnit.MILLISECONDS.toMinutes(time) - 
	        		TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)), 
	        		TimeUnit.MILLISECONDS.toSeconds(time) - 
	        		TimeUnit.MINUTES.toSeconds(
	        				TimeUnit.MILLISECONDS.toMinutes(time)
	        		)
	        	);
		 timeLabel.setText(s);
	}
	
	/**
	 * Sets the time to the end time label
	 * @param time		End time in milliseconds
	 */
	private void updateEndTime(long time)
	{
		String s = (endFixed) ? "" : "-";
		s += String.format("%02d:%02d:%02d", 
	        		TimeUnit.MILLISECONDS.toHours(time), 
	        		TimeUnit.MILLISECONDS.toMinutes(time) - 
	        		TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)), 
	        		TimeUnit.MILLISECONDS.toSeconds(time) - 
	        		TimeUnit.MINUTES.toSeconds(
	        				TimeUnit.MILLISECONDS.toMinutes(time)
	        		)
	        	);
		endLabel.setText(s);
	}
	
	private void updatePosition(int position)
	{
		positionSlider.setValue(position);
	}
	
	
	/**
	 * Runnable to update the slider
	 * @author mooij006
	 *
	 */
	private final class UpdateRunnable extends TimerTask {
		
		//private final EmbeddedMediaPlayer mediaPlayer;
		private final DirectMediaPlayer mediaPlayer;
		
		//public UpdateRunnable(EmbeddedMediaPlayer mediaPlayer)
		public UpdateRunnable(DirectMediaPlayer mediaPlayer)
		{
			this.mediaPlayer = mediaPlayer;
		}
		
		@Override
		public void run() {
			final long time = mediaPlayer.getTime();
			final int position = (int)(mediaPlayer.getPosition() * 10000.0f);
			final long endTime = (endFixed) ? mediaPlayer.getLength() : mediaPlayer.getLength() - mediaPlayer.getTime();
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() 
				{
					updateTime(time);
					updatePosition(position);
					updateEndTime(endTime);
				}
			});
		}
		
	}

}

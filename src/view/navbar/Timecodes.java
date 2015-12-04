package view.navbar;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import view.player.IMediaPlayer;
import view.player.IMediaPlayerListener;
import view.formatter.Time;
import controller.Globals;
import controller.IVideoControllerObserver;
import controller.IVideoControls;

public class Timecodes extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IVideoControls vc;
	private IMediaPlayer player;
	private boolean remaining;
	private JLabel currentTime;
	private JLabel remainingTime;
	private IVideoControllerObserver vco;
	
	/**
	 * Constructor method for time codes
	 * @param g		Globals reference
	 */
	public Timecodes(Globals g){
		this.vc = g.getVideoController();
		this.remaining = false;
		setLayout(new BorderLayout(0,0));
		setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		createCurrentTimeLabel();
		createEndTimeLabel();
		registerListeners();
	}
	
	/**
	 * Creates the label that indicates the current time
	 */
	private void createCurrentTimeLabel(){
		currentTime = new JLabel();
		add(currentTime, BorderLayout.WEST);
	}
	
	/**
	 * Creates the label that indicates the end time
	 */
	private void createEndTimeLabel(){
		remainingTime = new JLabel();
		remainingTime.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				Timecodes.this.remaining = !Timecodes.this.remaining;
			}

			@Override
			public void mousePressed(MouseEvent e) { }

			@Override
			public void mouseReleased(MouseEvent e) { }

			@Override
			public void mouseEntered(MouseEvent e) { }

			@Override
			public void mouseExited(MouseEvent e) { }
			
		});
		add(remainingTime, BorderLayout.EAST);
	}
	
	/**
	 * Registers listeners for when the time has changed
	 */
	private void registerListeners(){
		vco = new IVideoControllerObserver(){
			@Override
			public void videoInstantiated() {
				Timecodes.this.player = vc.getPlayer();
				vc.getPlayer().register(new IMediaPlayerListener(){

					@Override
					public void mediaStarted() { }

					@Override
					public void mediaPaused() { }

					@Override
					public void mediaTimeChanged() {
						updateLabels();
					}
					
				});
				updateLabels();
				vc.deregister(vco);
			}
		};
		vc.register(vco);
	}
	
	/**
	 * Method to update the time labels if necessary
	 */
	private void updateLabels(){
		currentTime.setText(Time.format(player.getMediaTime()));
		long endTime = player.getMediaDuration();
		if(this.remaining){
			endTime -= player.getMediaTime();
		}
		remainingTime.setText(Time.format(endTime));
	}
}

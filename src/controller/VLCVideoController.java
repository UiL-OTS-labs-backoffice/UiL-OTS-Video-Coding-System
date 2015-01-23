package controller;

import java.awt.Component;

/**
 * Implementation of IVideoController for the VLC Video Player Object
 * @author mooij006
 *
 */
public class VLCVideoController implements IVideoControls {
	
	/**
	 * Instance of the video Controller
	 */
	private static VLCVideoController instance;
	
	/**
	 * Instance of media player
	 */
	private view.IMediaPlayer player;
	
	/**
	 * Get method for the class's instance
	 * @return	Instance of VLCVideoController
	 */
	public static VLCVideoController getInstance() {
		if(instance == null)
			instance = new VLCVideoController();
		return instance;
	}
	
	/** 
	 * Ensures single instance
	 */
	private VLCVideoController(){}
	
	@Override
	public void setPlayer(view.IMediaPlayer player) {
		this.player = player;
	}

	@Override
	public view.IMediaPlayer getPlayer() {
		return player;
	}	

	@Override
	public boolean IsLoaded() {
		return (player != null);
	}

	@Override
	public void play() {
		player.start();
	}

	@Override
	public void nextFrame() {
		player.nextFrame();
	}

	@Override
	public void prevFrame() {
		player.previousFrame();
	}

	@Override
	public void secondAhead() {
		long time = player.getMediaTime();
		long duration = player.getMediaDuration();
		long newTime = time + 1000l;
		if(newTime < duration)
			player.setMediaTime(time + 1000l);
	}

	@Override
	public void secondBack() {
		long time = player.getMediaTime();
		long newTime = time + 1000l;
		if(newTime > 0)
			player.setMediaTime(time + 1000l);
	}

	@Override
	public void nextTrial() {
		// TODO Auto-generated method stub

	}

	@Override
	public void prevTrial() {
		// TODO Auto-generated method stub

	}

	@Override
	public void nextLook() {
		// TODO Auto-generated method stub

	}

	@Override
	public void prevLook() {
		// TODO Auto-generated method stub

	}

	@Override
	public void JumpToEnd() {
		player.setMediaTime(player.getMediaDuration());
	}

	@Override
	public void JumpToStart() {
		player.setMediaTime(0l);
	}

	@Override
	public Component getVisualComponent() {
		return player.getVisualComponent();
	}

	@Override
	public int getSourceHeight() {
		return player.getSourceHeight();
	}

	@Override
	public int getSourceWidth() {
		return player.getSourceWidth();
	}

	@Override
	public float getAspectRatio() {
		return player.getAspectRatio();
	}

	@Override
	public void setAspectRatio(float aspectRatio) {
		player.setAspectRatio(aspectRatio);
	}

	@Override
	public void stop() {
		player.stop();
	}

	@Override
	public boolean isPlaying() {
		return player.isPlaying();
	}

	@Override
	public double getMilliSecondsPerSample() {
		return player.getMilliSecondsPerSample();
	}

	@Override
	public long getOffset() {
		return player.getOffset();
	}

	@Override
	public long getMediaTime() {
		return player.getMediaTime();
	}

	@Override
	public void setMediaTime(long time) {
		player.setMediaTime(time);
	}

	@Override
	public void setFrameStepsToFrameBegin(boolean stepsToFrameBegin) {
		player.setFrameStepsToFrameBegin(stepsToFrameBegin);
	}

	@Override
	public float getRate() {
		return player.getRate();
	}

	@Override
	public void setRate(float rate) {
		player.setRate(rate);
	}

	@Override
	public boolean isFrameRateAutoDetected() {
		return player.isFrameRateAutoDetected();
	}

	@Override
	public long getMediaDuration() {
		return player.getMediaDuration();
	}

	@Override
	public float getPosition() {
		return player.getPosition();
	}

	@Override
	public void setPosition(float position) {
		player.setPosition(position);
	}

}

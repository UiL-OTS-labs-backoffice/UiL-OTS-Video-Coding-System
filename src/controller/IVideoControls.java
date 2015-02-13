package controller;

/**
 * Interface for VideoControls
 * 
 * Sets all the functions that can manipulate the video player
 * @author mooij006
 *
 */
public interface IVideoControls {
	
	public void setPlayer(view.player.IMediaPlayer player);
	
	/**
	 * Get the video player class that is being used
	 * @return	Video player if any
	 */
	public view.player.IMediaPlayer getPlayer();
	
	/**
	 * Checks if a video File is loaded and a video player object
	 * has been instantiated
	 * @return	True if video file is loaded
	 */
	public boolean IsLoaded();
	
	/**
	 * Gets the display Component for this Player.
	 * Unfortunately, the VLC player insists on its Component to be displayed,
	 * even for audio-only media.
	 */
	public java.awt.Component getVisualComponent();

	/**
	 * Get source Height
	 * @return
	 */
	public int getSourceHeight();

	/**
	 * Get Source Width
	 */
	public int getSourceWidth();

	public float getAspectRatio();

	/**
	 * Enforces an aspect ratio for the media component.
	 * Only these 8 exact strings are understood by VLC.
	 * Other strings result in the default aspect ratio.
	 *
	 * @param aspectRatio the new aspect ratio
	 */
	public void setAspectRatio(float aspectRatio);
	
	/**
	 * Plays the video if it is paused and pauses it if it's playing
	 */
	public void play();
	
	/**
	 * Stop the media player
	 */
	public void stop();
	
	/**
	 * Tell if this player is playing
	 *
	 * @return true if the player is playing, false otherwise
	 */
	public boolean isPlaying();
	
	/**
	 * Jumps to the next frame
	 */
	public void nextFrame();
	
	/**
	 * Jumps to the previous frame
	 */
	public void prevFrame();
	
	/**
	 * Jumps one second ahead
	 */
	public void secondAhead();
	
	/**
	 * Jumps one second back
	 */
	public void secondBack();
	
	/**
	 * Jumps to the next trial
	 */
	public void nextTrial();
	
	/**
	 * Jumps to the previous trial
	 */
	public void prevTrial();
	
	/**
	 * Jumps to the next look
	 */
	public void nextLook();
	
	/**
	 * Jumps to the previous look
	 */
	public void prevLook();
	
	/**
	 * Jumps to the end of the video file
	 */
	public void JumpToEnd();
	
	/**
	 * Jumps to the beginning of the video file
	 */
	public void JumpToStart();
	
	/**
	 * TODO We don't know always how many frames per second the media is...
	 * That hopefully becomes known while playing, but even that
	 * depends in the format of the media. It seems to work for MPEG4
	 * but not for MPEG(2).
	 *
	 * @return the step size for one frame
	 */
	public double getMilliSecondsPerSample();
	
	public double getStdev();

	/**
	 * Gets this Clock's current media time in milli seconds.
	 *
	 * @return DOCUMENT ME!
	 */
	public long getMediaTime();

	/**
	 * Sets the media time in milliseconds.
	 * This means that the player is set to that time + the time offset.
	 * Also sets the time for all controlled media players.
	 *
	 * @param time in msec.
	 */
	public void setMediaTime(long time);
	
	/**
	 * Gets the current temporal scale factor.
	 *
	 * @return DOCUMENT ME!
	 */
	public float getRate();

	/**
	 * Sets the temporal scale factor.
	 *
	 * @param rate DOCUMENT ME!
	 */
	public void setRate(float rate);

	/**
	 * @see
	 * mpi.eudico.client.annotator.player.ElanMediaPlayer#isFrameRateAutoDetected()
	 */
	public boolean isFrameRateAutoDetected();

	/**
	 * Get the duration of the media represented by this object in milli
	 * seconds.
	 *
	 * @return DOCUMENT ME!
	 */
	public long getMediaDuration();
	
	/**
	 * Gets the current position of the media represented by this object in
	 * percentage points
	 * @return	current position
	 */
	public float getPosition();
	
	/**
	 * Sets a new position for the media represented by this object.
	 * @param position	Position in media file in percentage points
	 */
	public void setPosition(float position);
}

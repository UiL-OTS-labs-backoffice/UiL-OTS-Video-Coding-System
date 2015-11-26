package view.player;

import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

public interface IMediaPlayer {

	public EmbeddedMediaPlayer getMediaPlayer();

	public java.awt.Component createNewVisualComponent();

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
	 * Starts the Player as soon as possible
	 */
	public void start();

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
	 * TODO We don't know always how many frames per second the media is...
	 * That hopefully becomes known while playing, but even that
	 * depends in the format of the media. It seems to work for MPEG4
	 * but not for MPEG(2).
	 *
	 * @return the step size for one frame
	 */
	public double getMilliSecondsPerSample();
	
	/**
	 * The standard deviation for is half the inverse of the frame rate.
	 * Within this period of time, you can never be sure of the accuracy
	 * @return	standard deviation
	 */
	public double getStdev();

	/**
	 * Gets this Clock's current media time in milli seconds.
	 *
	 * @return DOCUMENT ME!
	 */
	public long getMediaTime();

	/**
	 * Sets the media time in milliseconds.
	 * This means that the player is set to that time
	 * Also sets the time for all controlled media players.
	 *
	 * @param time in msec.
	 */
	public void setMediaTime(long time);

	/**
	 * nextFrame() doesn't work properly on all media files.
	 * And even when it works, the media time is very imprecise.
	 */
	public void nextFrame();

	/**
	 * Since time registration is very imprecise, stepping a frame back is not likely to work properly.
	 * But we can try...
	 */
	public void previousFrame();

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
	
	public void register(IMediaPlayerListener obj);
	
	public void deregister(IMediaPlayerListener obj);

}
package view.player;

public interface IMediaPlayerListener {
	
	
	/**
	 * Event that tells every listener media has started playing
	 */
	public void mediaStarted();
	
	/**
	 * Event that tells everything the media was paused
	 */
	public void mediaPaused();

	/**
	 * Event that tells listeners media time has changed
	 */
	public void mediaTimeChanged();
}

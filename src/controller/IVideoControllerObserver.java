package controller;

public interface IVideoControllerObserver {
	
	/**
	 * Method to notify observer that the media time has changed
	 * @param time
	 */
	public void mediaTimeChanged(long time);
	
	/**
	 * Method to notify the observer that the video has started playing
	 */
	public void playerStarted();
	
	/**
	 * Method to notify the observer that the video has been paused
	 */
	public void playerPaused();
	
	/**
	 * Method to notify observers that the video has been instantiated and loaded
	 */
	public void videoInstantiated();
}

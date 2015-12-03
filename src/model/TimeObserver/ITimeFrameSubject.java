package model.TimeObserver;

public interface ITimeFrameSubject {
	
	/**
	 * Method to register a new observer to the TimeFrame subject
	 * @param obj	The observer to be registered
	 */
	public void registerFrameListener(ITimeFrameObserver obj);
	
	/**
	 * Method to deregister an observer from the TimeFrame subject
	 * @param obj	The observer to be deregistered
	 */
	public void unregisterFrameListener(ITimeFrameObserver obj);
	
	/**
	 * Method to notify observers if the time frame
	 * time changed (by changing begin or end times)
	 */
	public void timeChanged();
	
}

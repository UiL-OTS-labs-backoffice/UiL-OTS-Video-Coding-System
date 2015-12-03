package view.navbar.utilities;

public interface INavbarSubject {
	
	/**
	 * Register a new observer to the navbar
	 * @param obj	The observer to be registered
	 */
	public void register(INavbarObserver obj);
	
	/**
	 * Deregister an observer from the navbar
	 * @param obj	The observer to be deregistered
	 */
	public void deregister(INavbarObserver obj);
	
	/**
	 * Method to notify observers of a change in the
	 * visible area
	 */
	public void visibleAreaChanged();

}

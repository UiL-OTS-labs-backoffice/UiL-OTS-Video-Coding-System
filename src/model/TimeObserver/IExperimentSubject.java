package model.TimeObserver;

public interface IExperimentSubject {
	/**
	 * Method to register a new observer to the time container subject
	 * @param obj	The observer to be registered
	 */
	public void addExperimentListener(IExperimentListener obj);
	
	/**
	 * Method to deregister an observer from the time container subject
	 * @param obj	The observer to be deregistered
	 */
	public void removeExperimentListener(IExperimentListener obj);
}

package model.TimeObserver;

import model.AbstractTimeFrame;

public interface ITimeContainerSubject {

	/**
	 * Method to register a new observer to the time container subject
	 * @param obj	The observer to be registered
	 */
	public void registerContainerListener(ITimeContainerObserver obj);
	
	/**
	 * Method to deregister an observer from the time container subject
	 * @param obj	The observer to be deregistered
	 */
	public void unregisterContainerListener(ITimeContainerObserver obj);
	
	/**
	 * Method to notify all observers a time frame was added
	 * @param item			The added item
	 * @param itemNumber	The new item number of the added item
	 */
	public void itemAdded(AbstractTimeFrame item, int itemNumber);
	
	/**
	 * Method to notify all observers a time frame was deleted
	 * @param item			The removed item
	 * @param itemNumber	The item number of the removed item from
	 * 						before it was removed
	 */
	public void itemRemoved(AbstractTimeFrame item, int itemNumber);
	
	/**
	 * Method to notify all observers the amount of items was changed,
	 * either by insertion or deletion
	 */
	public void numberOfItemsChanged();
	
}

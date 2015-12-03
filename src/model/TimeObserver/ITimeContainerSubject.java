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
	 * Should also notify observers of numberOfItemsChanged
	 * @param item			The added item
	 * @param itemNumber	The new item number of the added item
	 */
	public void itemAdded(AbstractTimeFrame item, int itemNumber);
	
	/**
	 * Method to notify all observers a time frame was deleted.
	 * Should also notify observers of numberOfItemsChanged
	 * @param item			The removed item
	 * @param itemNumber	The item number of the removed item from
	 * 						before it was removed
	 */
	public void itemRemoved(AbstractTimeFrame item);
	
}

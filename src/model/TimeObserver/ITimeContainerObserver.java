package model.TimeObserver;

import model.AbstractTimeContainer;
import model.AbstractTimeFrame;

public interface ITimeContainerObserver {
	
	/**
	 * Method to notify the observer an item was added to the observed time container
	 * @param container		The affected time container
	 * @param tf			The time frame that was added to container
	 * @param itemNumber	The item number of the new item
	 */
	public void itemAdded(AbstractTimeContainer container, AbstractTimeFrame tf, int itemNumber);
	
	/**
	 * Method to notify the observer an item was deleted from the observed time container
	 * @param container		The affected time container
	 * @param tf			The time frame that was deleted from the container
	 * @param itemNumber	The item number of the item that was deleted (from before it
	 * 						was deleted)
	 */
	public void itemRemoved(AbstractTimeContainer container, AbstractTimeFrame tf, int itemNumber);
	
	/**
	 * Method to notify the observer that the amount of items was changed, either by insertion
	 * or delition
	 * @param container		The affected time container
	 */
	public void numberOfItemsChanged(AbstractTimeContainer container);
}

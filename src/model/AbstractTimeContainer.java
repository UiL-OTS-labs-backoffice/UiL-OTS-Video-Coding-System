package model;

import java.util.LinkedList;

import model.AbstractTimeFrame;

/**
 * This abstract class extends the timeframe
 * In this class, a time frame can contain one or more sub time frames that
 * need to fall within the range if this time frame
 */
public abstract class AbstractTimeContainer extends AbstractTimeFrame
{
	/**
	 * SerialVersion UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The list of subitems that this container contains
	 */
	protected LinkedList<AbstractTimeFrame> items;

	/**
	 * The constructor for Timecontainer calls the super constructor from
	 * Timeframe and creates a new LinkedList that will contain the sub items
	 * @param time
	 */
	public AbstractTimeContainer(long time)
	{
		super(time);
		items = new LinkedList<AbstractTimeFrame>();
	}

	/**
	 * Method to get all the subitems
	 * @return	LinkedList of type Timeframe that contains all subitems of this
	 * 					container
	 */
	public LinkedList<AbstractTimeFrame> getItems()
	{
		return items;
	}
	
	/**
	 * Get a specific item in this container
	 * @param item		The item number
	 * @return			The item with item number 'item'
	 */
	public AbstractTimeFrame getItem(int item)
	{
		return items.get(getIndex(item));
	}
	
	/**
	 * Method to request the number of items in this container
	 * @return	The number of items in this container
	 */
	public int getNumberOfItems()
	{
		return items.size();
	}

	/**
	 * The begin time of a time frame container can be changed if the new
	 * begin time comes before the end time, and if the new begin time comes
	 * before the begin time of the first sub item in the container
	 * @param time 	The new begin time for this time frame
	 * @return 		True iff the time frame can be started at this time 
	 */
	@Override
	public boolean canBegin(long time)
	{
		boolean itemCanBegin = super.canBegin(time);
		boolean containerCanBegin = !items.peekFirst().timeInRange(time);
		return itemCanBegin && containerCanBegin;
	}

	/**
	 * The end time of a time frame container can be changed if the new end
	 * time comes after the begin time, and if the new end time comes
	 * after the end time of the last sub item in this container
	 * @param time 	The new end time for this time frame
	 * @return 		True iff the time frame can be ended at this time
	 */
	@Override
	public boolean canEnd(long time)
	{
		boolean itemCanEnd = super.canEnd(time);
		boolean containerCanEnd = items.size() == 0 || items.peekLast().getEnd() == -1 ||items.peekLast().getEnd() < time;
		return itemCanEnd && containerCanEnd;
	}

	/**
	 * Checks if a new item can be created at the time stamp time
	 * This is the case if the item falls within the range of this time frame 
	 * and the given time doesn't fall within the same range as any of the 
	 * existing items.
	 * @param time	The time at which the item should be created in milliseconds
	 * @return		The index of where the item can be added if it can be added
	 * 				and -1 otherwise
	 */
	public int canAddItem(long time)
	{
		Integer canAdd = null;

		if (!this.timeInRange(time)) {
			canAdd = -1;
		} else {
			for(int i = 0; i < items.size(); i++)
			{
				AbstractTimeFrame tf = items.get(i);
				if(tf.getBegin() > time)
				{
					canAdd = i-1;
					break;
				} else if (tf.getEnd() < time || tf.getEnd() == -1)
				{
					continue;
				} else {
					canAdd = -1;
				}
			}
		}
		
		if(canAdd == null)
		{
			canAdd = items.size();
		}
		
		return canAdd;
	}

	/**
	 * Checks if the begin time of an item can be changed to a new time stamp.
	 * This is the case if the item exists, the time falls within the range
	 * of this time frame and no other of the items in this container has the
	 * same time in its range
	 * @param item		The item number of the item to be affected
	 * @param time		The new begin time in milliseconds
	 * @return			True if the item can be changed for this time
	 */
	public boolean canBeginItem(int item, long time)
	{
		// Default state is that the time can be changed
		boolean canBegin = true;
		
		if(!this.timeInRange(time) || !this.itemExists(item))
		{
			// The new begin time falls outside the scope of this time frame,
			// or the item that has to be changed does not exist
			canBegin = false;
		} else {
			int index = this.getIndex(item);
			AbstractTimeFrame victem = items.get(index);
			if(!victem.canBegin(time))
			{
				// The new start time falls after the end time of the item
				canBegin = false;
			} else if (index > 0) {
				if(items.get(index-1).timeInRange(time))
				{
					// The new start time falls before the end time of the
					// previous item, resulting in overlap
					canBegin = false;
				}
			}
		}
		
		return canBegin;
	}

	/**
	 * Checks if the end time of an item can be changed to a new time stamp.
	 * This is the case if the item exists, the time falls within the range
	 * of this time frame and no other of the items in this container has the
	 * same time in its range.
	 * @param item		The item number of the item to be affected
	 * @param time		The new end time in milliseconds
	 * @return			True if the item can be changed for this time
	 */
	public boolean canEndItem(int item, long time)
	{
		boolean canEnd = true;
		
		if(!this.timeInRange(time) || !this.itemExists(item))
		{
			// The new begin time falls outside the scope of this time frame,
			// or the item that has to be changed does not exist
			canEnd = false;
		} else {
			int index = this.getIndex(item);
			AbstractTimeFrame victem = items.get(index);
			if(!victem.canEnd(time))
			{
				// The new end time falls before the begin time of the item
				canEnd = false;
			} else if (index < items.size()) {
				if(items.get(item).timeInRange(time))
				{
					// The new end time falls after the begin time of the
					// next item, resulting in overlap
					canEnd = false;
				}
			}
		}
		
		return canEnd;
	}

	/**
	 * This function should instantiate a new item at the right begintime and
	 * then call hiddenAddItem with that time and the new object
	 * @param time	The start time for the item
	 */
	abstract public void addItem(long time);
	
	/**
	 * This abstract class can't instantiate the right types, so this function
	 * handles adding a new item, as long as the abstract addItem() function
	 * creates the instantiation that needs to be added
	 * @param time		The start time of the sub item
	 * @param tf		The instantiated class to be added
	 */
	protected void hiddenAddItem(long time, AbstractTimeFrame tf)
	{
		int canAdd = this.canAddItem(time);
		if(canAdd < 0)
		{
			String e = String.format("A new item could not be created "
					+ "at the time %d ms", 
					time
				);
			throw new IllegalStateException(e);
		} else {
			if(items.size() > 0 && items.peekLast().getEnd() < 0)
			{
				items.peekLast().setEnd(time);
			}
			items.add(canAdd, tf);
		}
	}
	
	/**
	 * Removes an item from the list
	 * @param item	The item number for the item that is to be removed
	 */
	public void removeItem(int item)
	{
		if(!itemExists(item))
		{
			String e = String.format("The item %d could not be "
					+ "removed because it does not exist.", item);
			throw new IllegalStateException(e);
		} else {
			items.remove(getIndex(item));
		}
	}

	/**
	 * Returns the index of an item, where the item starts with one
	 * @param item	The item number
	 * @return	The index of the item if the item exists, 0 otherwise.
	 */
	public int getIndex(int item)
	{
		if(itemExists(item))
			return item - 1;
		else
			return -1;
	}
	
	/**
	 * Checks if an item exists, where the item number starts with one
	 * @param item	Item number that should exist
	 * @return		True iff the item exists
	 */
	private boolean itemExists(int item)
	{
		return item > 0 && item <= items.size();
	}
	
	/**
	 * Returns the item number that has the time in its range.
	 * If no such an item can be found, the last item before the time stamp
	 * is found and the number if this item is returned as a negative integer
	 * @param time	The time for which an item has to be found
	 * @return		The item number if such an item exists, the negative of
	 * 				that number of no such item exists and 0 if no items exist
	 */
	public int getItemForTime(long time)
	{
		for(int i = 0; i < items.size(); i++)
		{
			AbstractTimeFrame tf = items.get(i);
			if(tf.getEnd() >= 0 && tf.getEnd() < time) {
				if(i == items.size()-1)
					return 0 - i - 1;
				continue;
			} else if (tf.getBegin() >= time || tf.getEnd() == -1) {
				return i + 1;
			} else {
				return 0 - i - 1;
			}
		}
		
		return 0;
	}
	
	/**
	 * Method to get the total duration of all items in this time container
	 * @return	Total duration of all items in this time container in milliseconds
	 */
	public long getTotalTimeForItems()
	{
		long time = 0L;
		for(AbstractTimeFrame tf : items)
		{
			time += tf.getDuration();
		}
		return time;
	}
}
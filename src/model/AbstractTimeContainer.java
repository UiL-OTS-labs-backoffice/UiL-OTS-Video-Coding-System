package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import controller.Globals;
import model.AbstractTimeFrame;
import model.TimeObserver.ITimeContainerObserver;
import model.TimeObserver.ITimeContainerSubject;
import model.TimeObserver.ITimeFrameObserver;

/**
 * This abstract class extends the time frame
 * In this class, a time frame can contain one or more sub time frames that
 * need to fall within the range if this time frame
 */
public abstract class AbstractTimeContainer extends AbstractTimeFrame implements ITimeContainerSubject
{
	/**
	 * SerialVersion UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The list of sub-items that this container contains
	 */
	protected LinkedList<AbstractTimeFrame> items;
	private transient Object ITEMS_MUTEX;
	
	/**
	 * Observer pattern
	 */
	private transient List<ITimeContainerObserver> containerObservers;
	private transient Object CONTAINER_MUTEX;
	
	/**
	 * The constructor for Time container calls the super constructor from
	 * Time frame and creates a new LinkedList that will contain the sub items
	 * @param time
	 */
	public AbstractTimeContainer(long time, int type)
	{
		super(time, type);
		this.CONTAINER_MUTEX = new Object();
		this.ITEMS_MUTEX = new Object();
		this.containerObservers = new ArrayList<ITimeContainerObserver>();
		this.timeout = -1L;
		this.items = new LinkedList<AbstractTimeFrame>();
	}

	/**
	 * Method to get all the sub items
	 * @return	LinkedList of type Time frame that contains all sub items of this
	 * 					container
	 */
	public LinkedList<AbstractTimeFrame> getItems()
	{
		LinkedList<AbstractTimeFrame> localItems;
		synchronized(ITEMS_MUTEX){
			localItems = new LinkedList<AbstractTimeFrame>(items);
		}
		return localItems;
	}
	
	/**
	 * Get a read only copy of a specific item in this container
	 * @param item		The item number
	 * @return			The item with item number 'item'
	 */
	public AbstractTimeFrame getItem(int item)
	{
		synchronized (ITEMS_MUTEX) {
			return items.get(getIndex(item));
		}
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
		boolean containerCanEnd;
		synchronized (ITEMS_MUTEX) {
			containerCanEnd = items.size() == 0 || !items.peekLast().hasEnded() ||items.peekLast().getEnd() <= time;
		}
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
		if (!this.timeInRange(time) || getEnd() == time) {
			canAdd = -1;
		} else {
			LinkedList<AbstractTimeFrame> itemsLocal;
			synchronized (ITEMS_MUTEX) {
				itemsLocal = new LinkedList<AbstractTimeFrame>(this.items);
			}
			for(int i = 0; i < itemsLocal.size(); i++)
			{
				AbstractTimeFrame tf = itemsLocal.get(i);
				if(tf.getBegin() > time)
				{
					canAdd = i;
					break;
				} else if (tf.getEnd() < time || !tf.hasEnded())
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
			AbstractTimeFrame victim = items.get(index);
			if(!victim.canBegin(time))
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
	abstract public AbstractTimeFrame addItem(long time);
	
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
			if(canAdd > 0 && (items.get(canAdd-1).getEnd() >= tf.getBegin() || items.get(canAdd-1).getEnd() < 0))
			{
				synchronized (ITEMS_MUTEX){
					items.get(canAdd-1).setEnd(tf.getBegin()-1);
				}
			}
			if (canAdd < items.size()){
				synchronized (ITEMS_MUTEX) {
					tf.setEnd(items.get(canAdd).getBegin()-1);
					tf.ended = AbstractTimeFrame.ENDED_FALSE;
				}
			} else {
				tf.setEnd(getEnd()-1);
				tf.ended = AbstractTimeFrame.ENDED_FALSE;
			}
			synchronized (ITEMS_MUTEX) {
				items.add(canAdd, tf);
			}
			
			tf.registerFrameListener(new ITimeFrameObserver(){

				@Override
				public void timeChanged(AbstractTimeFrame tf) {
					childTimeChanged();
				}

				@Override
				public void commentChanged(AbstractTimeFrame tf, String comment) { }
				
			});
			itemAdded(tf, canAdd + 1);
		}
		calculateTimeout();
		
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
			AbstractTimeFrame it;
			synchronized(ITEMS_MUTEX)
			{
				it = items.get(getIndex(item));
				items.remove(getIndex(item));
			}
			itemRemoved(it);
		}
		calculateTimeout();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public long getMinimumEndTime(){
		long minimumEndTime;
		if(items.isEmpty()){
			minimumEndTime = getBegin();
		} else {
			AbstractTimeFrame last = items.peekLast();
			minimumEndTime = last.hasEnded() ? last.getEnd() : last.getBegin();
		}
		return minimumEndTime + 1;
	}
	
	/**
	 * Method to remove all abstract time frames from this container
	 */
	public void removeAll()
	{
		LinkedList<AbstractTimeFrame> itemsLocal;
		synchronized (ITEMS_MUTEX) {
			itemsLocal = new LinkedList<AbstractTimeFrame>(items);
		}
		for(AbstractTimeFrame t : itemsLocal)
		{
			itemRemoved(t);
		}
		items = new LinkedList<AbstractTimeFrame>();
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
		synchronized (ITEMS_MUTEX) {
			return item > 0 && item <= items.size();
		}
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
		LinkedList<AbstractTimeFrame> itemsLocal;
		synchronized (ITEMS_MUTEX) {
			itemsLocal = new LinkedList<AbstractTimeFrame>(items);
		}
		
		for(int i = 0; i < itemsLocal.size(); i++)
		{
			AbstractTimeFrame tf = itemsLocal.get(i);
			if(tf.hasEnded() && tf.getEnd() < time) {
				if(i == itemsLocal.size()-1)
					return 0 - i - 1;
				continue;
			} else if (tf.getBegin() <= time || !tf.hasEnded()) {
				return i + 1;
			} else {
				return 0 - i;
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
		model.Experiment em = Globals.getInstance().getExperimentModel();
		long time = 0L;
		long last_end = -1;
		LinkedList<AbstractTimeFrame> itemsLocal;
		synchronized (ITEMS_MUTEX) {
			itemsLocal = new LinkedList<AbstractTimeFrame>(items);
		}
		for(AbstractTimeFrame tf : itemsLocal)
		{
			if(!em.getUseTimeout() || tf.getBegin() - last_end < em.getTimeout() || last_end < 0)
			{
				time += tf.getDuration();
				last_end = tf.getEnd();
			} else if(em.getUseTimeout()) {
				break;
			}
		}
		return time;
	}
	
	/**
	 * Get the item number for a certain item
	 * @param tf 	Item AbstractTimeFrame to be checked
	 */
	@Override
	public int getNumberForItem(AbstractTimeFrame tf)
	{
		int number = -1;
		LinkedList<AbstractTimeFrame> itemsLocal;
		synchronized (ITEMS_MUTEX) {
			itemsLocal = new LinkedList<AbstractTimeFrame>(items);
		}
		if(itemsLocal.contains(tf))
		{
			number = itemsLocal.indexOf(tf) + 1;
		} else {
			for(AbstractTimeFrame item : itemsLocal)
			{
				int maybeNumber = item.getNumberForItem(tf);
				if(maybeNumber > 0)
				{
					number = maybeNumber;
					break;
				}
			}
		}
		
		return number;
	}
	
	/**
	 * Method to calculate the time out for this abstract time frame
	 */
	public void calculateTimeout()
	{
		Experiment m = Globals.getInstance().getExperimentModel();
		LinkedList<AbstractTimeFrame> itemsLocal;
		synchronized (ITEMS_MUTEX) {
			itemsLocal = new LinkedList<AbstractTimeFrame>(items);
		}
		if(itemsLocal.size() == 0)
		{
			this.timeout = -1l;
		} else {
			this.timeout = items.getFirst().getEnd();	
			for(AbstractTimeFrame tf : itemsLocal)
			{
				if(tf.getBegin() < this.timeout)
				{
					this.timeout = (tf.hasEnded()) ? tf.getEnd() + m.getTimeout() : -1l;
				}
				tf.setTimeout(this.timeout);
			}
		}
	}
	
	/**
	 * Disabled for time containers
	 */
	protected void setTimeout(long timeout) { }
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public void registerContainerListener(ITimeContainerObserver obj) {
        if(obj == null) throw new NullPointerException("Null Observer");
        synchronized (CONTAINER_MUTEX) {
        	if(!containerObservers.contains(obj)) containerObservers.add(obj);
        }
    }
	
	/**
	 * {@inheritDoc}
	 */
    @Override
    public void unregisterContainerListener(ITimeContainerObserver obj) {
        synchronized (CONTAINER_MUTEX) {
        	containerObservers.remove(obj);
        }
    }
    
    /**
	 * {@inheritDoc}
	 */
    @Override
    public void itemAdded(AbstractTimeFrame item, int itemNumber){
    	List<ITimeContainerObserver> observersLocal = null;
    	synchronized(CONTAINER_MUTEX) {
    		observersLocal = new ArrayList<ITimeContainerObserver>(this.containerObservers);
    	}
    	for(ITimeContainerObserver obj : observersLocal)
    	{
    		obj.itemAdded(this, item, itemNumber);
    		obj.numberOfItemsChanged(this);
    	}
    }
    
    /**
	 * {@inheritDoc}
	 */
    @Override
	public void itemRemoved(AbstractTimeFrame item){
    	List<ITimeContainerObserver> observersLocal = null;
    	synchronized(CONTAINER_MUTEX) {
    		observersLocal = new ArrayList<ITimeContainerObserver>(this.containerObservers);
    	}
    	for(ITimeContainerObserver obj : observersLocal)
    	{
    		obj.itemRemoved(this, item);
    		obj.numberOfItemsChanged(this);
    	}
    }
    
    /**
     * {@inheritDoc}
     */
    public void childTimeChanged(){
    	List<ITimeContainerObserver> observersLocal = null;
    	synchronized(CONTAINER_MUTEX) {
    		observersLocal = new ArrayList<ITimeContainerObserver>(this.containerObservers);
    	}
    	for(ITimeContainerObserver obj : observersLocal)
    	{
    		obj.childTimeChanged(this);
    	}
    }
    
    private void readObject (final ObjectInputStream s ) throws ClassNotFoundException, IOException
    {
        s.defaultReadObject( );
        this.CONTAINER_MUTEX = new Object();
        this.ITEMS_MUTEX = new Object();
        this.containerObservers = new ArrayList<ITimeContainerObserver>();
        controller.Globals.getInstance().getExperimentModel().registerTimeFrameListener(this);
        controller.Globals.getInstance().getExperimentModel().registerContainerListener(this);
    }
	
}
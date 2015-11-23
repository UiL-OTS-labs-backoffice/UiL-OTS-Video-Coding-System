package model;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import model.TimeObserver.ITimeFrameObserver;
import model.TimeObserver.ITimeFrameSubject;

/**
 * A timeframe is an object that has a begin time and an end time
 * These times can be changed if necessary and the duration of a time frame
 * can be requested
 */
public abstract class AbstractTimeFrame implements Serializable, ITimeFrameSubject
{
	/**
	 * SerialVersion UID
	 */
	private static final long serialVersionUID = 2L;
	
	public static final int TYPE_LOOK = 0;
	public static final int TYPE_TRIAL = 1;
	public static final int TYPE_EXPERIMENT = 2;
	
	/**
	 * Fields for begin- and end time, and duration
	 * which are given as longs and based on the
	 * media player time and given in milliseconds
	 */
	protected long duration, begintime, endtime = -1L;
	
	protected String comment;
	
	protected JPanel panel;
	
	protected int type;
	
	protected long timeout;
	
	private List<ITimeFrameObserver> observers;
	private boolean changed;
	private final Object MUTEX = new Object();
	
	/**
	 * Constructor for this class
	 * Creates a new time frame with begin time set to
	 * the time parameter and end time set to -1 if it's the last
	 * time frame in a set, or to the begin time of the next time frame - 1
	 * if not
	 * @param time	The begin time of this time frame in milliseconds
	 */
	public AbstractTimeFrame(long time, int type)
	{
		this.observers=new ArrayList<ITimeFrameObserver>();
		this.begintime = time;
		this.type = type;
	}

	/**
	 * Checks if a certain time stamp falls within the range of this
	 * time frame.
	 * @param 	time 	The time stamp that needs to be checked in milliseconds
	 * @return 	True iff it falls within this range (inclusive)
	 */
	public boolean timeInRange(long time)
	{
		return time >= begintime && (endtime == -1L || time <= endtime);
	}
	
	/**
	 * Method to get the begin time of this time frame
	 * @return	The begin time of this time frame in milliseconds
	 */
	public long getBegin()
	{
		return this.begintime;
	}

	/**
	 * Method to get the end time of this time frame
	 * @return	End time of this time frame in milliseconds if it is set;
	 * 			-1 otherwise
	 */
	public long getEnd()
	{
		return this.endtime;
	}

	/**
	 * Method to change the begin time of this time frame.
	 * Throws an IlligalStateException of the new begin time comes
	 * after the end time of this frame
	 * @param time	The new begin time in milliseconds for this time frame
	 */
	public void setBegin(long time)
	{
		if(canBegin(time))
		{
			this.begintime = time;
			this.changed = true;
			calculateDuration();
			timeChanged();
		} else {
			String e = String.format("Requested new begin time is %d, but the "
					+ "current end time is %d. The begin time should be less "
					+ "than the end time",
					time,
					endtime);
			throw new IllegalStateException(e);
		}
		
	}

	/**
	 * Method to change the end time of this time frame.
	 * Throws an IlligalStateException of the new end time comes
	 * before the current begin time of this frame
	 * @param time	The new end time in milliseconds for this frame
	 */
	public void setEnd(long time)
	{
		if(canEnd(time))
		{
			this.endtime = time;
			this.changed = true;
			calculateDuration();
			timeChanged();
		} else {
			String e = String.format("Requested new end time is %d, but the "
					+ "current begin time is %d. The end time should "
					+ "always come after the begin time",
					time,
					begintime);
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Method to check if the begin time of this frame can be changed to a 
	 * certain time stamp
	 * @param time	The time stamp in milliseconds that needs to be checked
	 * @return	True iff the time stamp comes before the current end time
	 */
	public boolean canBegin(long time)
	{
		return endtime == -1L || time < endtime;
	}

	/**
	 * Method to check if the end time of this frame can be changed to a 
	 * certain time stamp
	 * @param time	The time stamp in milliseconds that needs to be checked
	 * @return	True iff the time stamp comes after the current begin time
	 */
	public boolean canEnd(long time)
	{
		return time > begintime;
	}

	/**
	 * Method to automatically calculate the duration of the time frame
	 * after the begin- or end time is changed
	 */
	private void calculateDuration()
	{
		duration = endtime - begintime;
	}

	/**
	 * Method to get the duration of this time frame
	 * @return	Duration of this time frame in milliseconds
	 */
	public long getDuration()
	{
		return duration;
	}
	
	/**
	 * Method to get the user comment for this time frame
	 * @return		User comment
	 */
	public String getComment()
	{
		return comment;
	}
	
	/**
	 * Method to change the user comment for this time frame
	 * @param comment	The new user comment
	 */
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	
	/**
	 * Method to change the size and position of the panel that
	 * show up on the view for this time frame. 
	 * @param r 	Rectangle for the bounds
	 */
	public void setSize(Rectangle r)
	{
		if(panel != null)
			panel.setBounds(r);
	}
	
	public int getType()
	{
		return this.type;
	}
	
	/**
	 * By default, an abstract time frame cannot contain any items.
	 * @param tf	The time frame to get the number for
	 * @return		-1 if time frame can't be found, number otherwise
	 */
	public int getNumberForItem(AbstractTimeFrame tf)
	{
		return -1;
	}
	
	public boolean hasEnded()
	{
		return this.endtime >= 0;
	}
	
	/**
	 * Method to get the time where a timeout starts for this abstract time container
	 * @return	-1 if no timeout is present, a long containing the timeout starttime
	 * 			otherwise
	 */
	public long getTimeout()
	{
		return this.timeout;
	}
	
	/**
	 * Protected method to set the timeout. This can be called from an abstract time container
	 * this time frame is an item of and should be set to the timeout of that container
	 * @param timeout	The timeout time
	 */
	protected void setTimeout(long timeout)
	{
		this.timeout = timeout;
	}
	
	@Override
	public void registerFrameListener(ITimeFrameObserver obj)
	{
		if(obj == null) throw new NullPointerException("Null Observer");
		synchronized (MUTEX) {
			if(!observers.contains(obj)) observers.add(obj);
		}
	}
	
	@Override
	public void unregisterFrameListener(ITimeFrameObserver obj)
	{
		synchronized (MUTEX) {
			observers.remove(obj);
		}
	}
	
	@Override
	public void timeChanged()
	{
		List<ITimeFrameObserver> observersLocal = null;
		synchronized(MUTEX) {
			if (!changed)
				return;
			observersLocal = new ArrayList<ITimeFrameObserver>(this.observers);
			this.changed=false;
		}
		for(ITimeFrameObserver obj : observersLocal)
		{
			obj.timeChanged(this);
		}
	}
	
}
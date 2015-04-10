package model;
import java.io.Serializable;

/**
 * A timeframe is an object that has a begin time and an end time
 * These times can be changed if necessary and the duration of a time frame
 * can be requested
 */
public abstract class AbstractTimeFrame implements Serializable
{
	/**
	 * SerialVersion UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Fields for begin- and end time, and duration
	 * which are given as longs and based on the
	 * media player time and given in milliseconds
	 */
	protected long duration, begintime, endtime = -1L;
	
	/**
	 * Constructor for this class
	 * Creates a new time frame with begin time set to
	 * the time parameter and end time set to -1 if it's the last
	 * time frame in a set, or to the begin time of the next time frame - 1
	 * if not
	 * @param time	The begin time of this time frame in milliseconds
	 */
	public AbstractTimeFrame(long time)
	{
		this.begintime = time;
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
			calculateDuration();
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
			calculateDuration();
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
}
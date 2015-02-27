package model;

import java.io.Serializable;

/**
 * A look object contains the information about a look
 */
public class Look implements Comparable<Look>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Begin and endtime of the look
	private long beginTime, endTime = -1L;
	
	// Duration of the look in milliseconds
	private long duration;
	
	/**
	 * Constructor of the look
	 * @param beginTime		The timestamp of the beginning of the look
	 */
	public Look(long beginTime)
	{
		this.beginTime = beginTime;
	}
	
	/**
	 * Get method for begin time of the look
	 * @return		The begin time of this look
	 */
	public long getBeginTime()
	{
		return beginTime;
	}
	
	/**
	 * Set method for the begin time of this look
	 * @param beginTime		The new begin time of this look
	 */
	public boolean setBeginTime(long beginTime)
	{
		if(endTime == -1L || beginTime < endTime)
		{
			this.beginTime = beginTime;
			duration = endTime - this.beginTime;
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Get method for the end time of this look
	 * @return		The end time of this look
	 */
	public long getEndTime()
	{
		return endTime;
	}
	
	/**
	 * Set method for the end time of this look
	 * @param endTime		The new end time of this look
	 */
	public boolean setEndTime(long endTime)
	{
		if(endTime > beginTime)
		{
			this.endTime = endTime;
			duration = this.endTime - beginTime;
			return true;
		}
		else 
			return false;
		
	}
	
	/**
	 * Method to check if a timestamp falls within the current look range
	 * @param time		The timestamp to be checked
	 * @return			True iff the timestamp falls within the current look
	 */
	public boolean hasTime(long time)
	{
		return (beginTime <= time && (endTime == -1L || time < endTime));
	}
	
	/**
	 * Returns the duration of this look
	 * @return		Duration in milliseconds
	 */
	public long getDuration()
	{
		return duration;
	}	
	
	/**
	 * compareTo for Look objects
	 * Sees objects that have overlap as equal
	 * @param l		Look object this look needs to be compared to
	 * @return 		0 iff this and l are equal (or the fall partly in the same 
	 * 					range)
	 * 				1 iff this > l
	 * 			   -1 iff this < l
	 */
	@Override
	public int compareTo(Look l) {
		if(this.endTime == -1L)
		{
			if(beginTime == l.beginTime) return 0;
			if(beginTime > l.getBeginTime()) return 1;
			else return -1;
		}
		else
		{
			if(l.getBeginTime() >= this.endTime && this.endTime != -1L)
			{
				return -1;
			}
			else if(l.getEndTime() <= this.beginTime)
			{
				return 1;
			}
			else return 0; // Ranges match
		}
	}
	
	
}

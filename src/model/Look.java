package model;

/**
 * Look class saves the information for a certain look
 * @author mooij006
 *
 */
public class Look {
	private int lookNumber; // The number of the current look in the trial
	private int beginTime, endTime;
	private int duration; // in miliseconds
	
	/**
	 * Constructor for look object.
	 * Sets the begintime of the look, as that's when the
	 * new object is created. The endtime is not yet known
	 * at this point.
	 * @param beginTime		the begintime of the look
	 */
	public Look(int beginTime)
	{
		this.beginTime = beginTime;
	}
	
	public int getLookNumber()
	{
		return lookNumber;
	}
	
	/**
	 * Set method for begin time. Used to alter the begintime
	 * if a mistake has been made
	 * @param beginTime		new begintime of the look
	 */
	public void setBeginTime(int beginTime)
	{
		this.beginTime = beginTime;
		this.duration = endTime - beginTime;
	}
	
	/**
	 * Get method for begintime
	 * @return	begintime
	 */
	public int getBeginTime()
	{
		return this.beginTime;
	}
	
	/**
	 * Set method for enddtime
	 * @param endTime	endtime of look
	 */
	public void setEndTime(int endTime)
	{
		this.endTime = endTime;
		this.duration = endTime - beginTime;
	}
	
	/**
	 * Get method for enddtime
	 * @return	endTime
	 */
	public int getEndTime()
	{
		return this.endTime;
	}
	
	/**
	 * Method to get the duration of a look
	 * @return	Duration of look in miliseconds
	 */
	public int getDuration()
	{
		return this.duration;
	}
}

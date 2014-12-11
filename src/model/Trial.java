package model;

/**
 * Trial class keeps track of all the information in the trials
 * @author mooij006
 *
 */
public class Trial {
	private int trialNumber;
	private Look[] looks;
	private int lookCurIndex; // Keeps track of the current look
	private int beginTime, endTime;
	
	/**
	 * Constructor for a new trial
	 * @param trial			Trial number
	 * @param beginTime		Begintime of trial
	 */
	public Trial(int trialNumber, int beginTime)
	{
		this.trialNumber = trialNumber;
		looks = new Look[0];
		lookCurIndex = -1;
		this.beginTime = beginTime;
	}
	
	/**
	 * Returns the trial number
	 * @return	Trial number of this trial
	 */
	public int getTrialNumber()
	{
		return this.trialNumber;
	}
	
	/**
	 * Returns the current look number of the current trial
	 * @return	Look number of current look in trial
	 */
	public int getCurrentLookNumber()
	{
		if(lookCurIndex == -1)
			return -1;
		return looks[lookCurIndex].getLookNumber();
	}
	
	/**
	 * Set method for begin time. Used to alter the begintime
	 * if a mistake has been made
	 * @param beginTime		new begintime of the trial
	 */
	public void setBeginTime(int beginTime)
	{
		this.beginTime = beginTime;
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
	 * @param endTime	endtime of trial
	 */
	public void setEndTime(int endTime)
	{
		this.endTime = endTime;
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
	 * Gets the array of looks
	 * @return	array of looks
	 */
	public Look[] get_looks()
	{
		return this.looks;
	}
	
	/**
	 * Adds a new look to the looks in this trial
	 * @param look	The look to be added
	 */
	public void add_look(Look look)
	{
		Look[] old_looks = this.looks;
		this.looks = new Look[old_looks.length + 1];
		for(int i = 0; i < old_looks.length; i++)
		{
			this.looks[i] = old_looks[i];
		}
		this.looks[this.looks.length - 1] = look;
	}
	
	/**
	 * Returns the total time looked in this trial
	 * @return	Total time looked
	 */
	public int getTotalLookTime()
	{
		int total = 0;
		for (Look l : this.looks)
		{
			total += l.getDuration();
		}
		return total;
	}
	
}

package model;

import java.util.LinkedList;

public class Trial implements Comparable<Trial>{
	
	// Reference to all the looks
	private LinkedList<Look> looks;
	
	// References to begin and end time
	private long beginTime, endTime = -1L;
	
	/**
	 * Constructor for a new Trial
	 * @param beginTime		The begintime of the trial
	 */
	public Trial(long beginTime)
	{
		this.beginTime = beginTime;
		looks = new LinkedList<Look>();
	}
	
	/**
	 * Get method for the begin time of this trial
	 * @return	Begin time of this trial
	 */
	public long getBeginTime()
	{
		return beginTime;
	}
	
	/**
	 * Set method for the begin time of this trial
	 * @param beginTime		The new begin time for this trial
	 * @return				True iff change could be made, i.e. if the new 
	 * 						begin time is larger than the end time
	 */
	public boolean setBeginTime(long beginTime)
	{
		if(endTime == -1L || endTime > beginTime)
		{
			this.beginTime = beginTime;
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Get method for the end time of this trial
	 * @return		The end time of this trial
	 */
	public long getEndTime()
	{
		return endTime;
	}
	
	/**
	 * Set method for the end time of this trial
	 * @param endTime	The new end time for this trial
	 * @return			True iff changes could be made, i.e. if the new end
	 * 					time is later than the begin time of this trial
	 */
	public boolean setEndTime(long endTime)
	{
		// TODO: either disallow if looks extend beyong this point, or remove said looks
		if(endTime >= beginTime)
		{
			this.endTime = endTime;
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Method to add a look to the list of looks in the right place, so
	 * the order is maintained and only if the begintime of the look isn't
	 * in the range of other looks
	 * @param beginTime		The begintime of the look
	 * @return				True iff begintime is not in range of other looks
	 */
	public boolean addLook(long beginTime)
	{
		Look last = looks.peekLast();
		if(last != null && last.getEndTime() == -1L)
			last.setEndTime(beginTime-1);
		Look newLook = new Look(beginTime);
		for(int i = 0; i < looks.size(); i++)
		{
			int compare = newLook.compareTo(looks.get(i));
			if( compare > 0)
				continue;
			else if(compare == 0)
			{
				System.out.println("Same range");
				return false;
			}
				
			else if(compare < 0)
			{
				newLook.setEndTime(looks.get(i).getBeginTime()-1);
				looks.add(i, newLook);
				return true;
			}
		}
		
		looks.addLast(newLook);
		return true;
	}
	
	/**
	 * Tries to remove the trial from the list
	 * @param trialNumber	The trial number of the trial to be removed
	 * @return				True iff remove succesful
	 */
	public boolean removeLook(int lookNumber)
	{
		lookNumber--;
		if(lookNumber < 0 || lookNumber > looks.size())
			return false;
		else
			return looks.remove(looks.get(lookNumber - 1));
	}
	
	/**
	 * Get method for the total number of looks
	 * @return	Number of looks
	 */
	public int getNumberOfLooks()
	{
		return looks.size();
	}
	
	public long getTotalLookTime()
	{
		long time = 0;
		for(Look l : looks)
		{
			time += l.getDuration();
		}
		
		return time;
	}
	
	/**
	 * Method to check if a timestamp falls within the current trial
	 * @param time		The timestamp to be checked
	 * @return			True iff the timestamp falls within the current trial
	 */
	public boolean hasTime(long time)
	{
		return (beginTime < time && (endTime == -1L || time < endTime));
	}
	
	/**
	 * Method to get the look number of the look in which the passed time
	 * stamp falls
	 * @param time		Timestamp to be checked
	 * @return			Look number of the trial in which the timestamp falls
	 * 					or -1 if no such look exists
	 */
	public int getLookByTime(long time)
	{
		for(int i = 0; i < looks.size(); i++)
		{
			if (looks.get(i).hasTime(time))
				return i+1;
		}
		return -1;
	}
	
	public int getLastLookByTime(long time)
	{
		for(int i = 0; i < looks.size(); i++)
		{
			if(looks.get(i).getBeginTime() > time)
				return i;
		}
		
		return -1;
	}
	
	/**
	 * Method to set the end time of a look in this trial
	 * @param look		The number of the look you wish to change
	 * @param time		The new time for this look
	 * @return			True if succesful, i.e. if the new end time for this
	 * 					look comes after the begin time of the look
	 */
	public boolean setLookEndTime(int look, long time)
	{
		look--;
		if(look < 0 || look > looks.size())
			return false;
		else
			return looks.get(look).setEndTime(time);
	}
	
	/**
	 * compareTo for Trial objects
	 * Sees objects that have overlap as equal
	 * @param t		Trial object this trial needs to be compared to
	 * @return 		0 iff this and t are equal (or the fall partly in the same 
	 * 					range)
	 * 				1 iff this > t
	 * 			   -1 iff this < t
	 */
	@Override
	public int compareTo(Trial t) {
		if(this.endTime == -1L)
		{
			if(beginTime == t.beginTime) return 0;
			if(beginTime > t.getBeginTime()) return 1;
			else return -1;
		}
		else
		{
			if(t.getBeginTime() >= this.endTime && this.endTime != -1L)
			{
				return -1;
			}
			else if(t.getEndTime() <= this.beginTime)
			{
				return 1;
			}
			else return 0; // Ranges match
		}
		
	}

}

package model;

import java.util.LinkedList;

import controller.Globals;

public class Experiment {
	// Current video file url
	private String url;
		
	// Global Experiment Settings
	private String exp_name, exp_id, res_id, pp_id;
	private boolean show_exp_name, show_exp_id, show_res_id, show_pp_id;
	
	// Linked list of all the trials
	private LinkedList<Trial> trials;
	
	/**
	 * Constructor method
	 */
	public Experiment()
	{
		trials = new LinkedList<Trial>();
	}
	
	/**
	 * Get method for the list of trials
	 * @return LinkedList<Trial>  list of trials
	 */
	public LinkedList<Trial> getTrials()
	{
		return trials;
	}
	
	public boolean canAddTrial(long time)
	{
		if(!Globals.getVideoController().IsLoaded())
			return false;
		if(trials.size() == 0)
			return true;
		for(Trial t : trials)
		{
			if(t.getBeginTime() > time)
				return true;
			if(t.getEndTime() < time)
				continue;
			else
				return false;
		}
		
		return true;
	}
	
	/**
	 * Method to add a trial to the list of trials in the right place, so
	 * the order is maintained and only if the begintime of the trial isn't
	 * in the range of other trials
	 * @param beginTime		The begintime of the trial
	 * @return				True iff begintime is not in range of other trial
	 */
	public boolean addTrial(long beginTime)
	{
		Trial last = trials.peekLast();
		if(last != null && last.getEndTime() == -1L)
			last.setEndTime(beginTime-1);
		Trial newTrial = new Trial(beginTime);
		for(int i = 0; i < trials.size(); i++)
		{
			int compare = newTrial.compareTo(trials.get(i));
			if( compare > 0)
				continue;
			else if(compare == 0)
			{
				System.out.println("Same range");
				return false;
			}
			else if(compare < 0)
			{
				newTrial.setEndTime(trials.get(i).getBeginTime()-1);
				trials.add(i, newTrial);
				return true;
			}
		}
		
		trials.addLast(newTrial);
		return true;
	}
	
	/**
	 * Method to add a look if you don't know which trial the look should be in
	 * @param beginTime		The begintime of the look
	 * @return				True iff look falls within the range of a trial
	 * 							but not in the range of another look
	 */
	public boolean addLook(long beginTime)
	{
		int trial = getTrialByTime(beginTime);
		if(trial == -1)
			return false;
		else
			return trials.get(trial-1).addLook(beginTime);
	}
	
	public boolean canAddLook(int trial, long time)
	{
		trial--;
		if(trial < 0 || trial > trials.size())
			return false;
		else
			return trials.get(trial).canAddLook(time);
	}
	
	/**
	 * Method to add a new look to an existing trial
	 * @param trial			The trial number where the look should be added to
	 * @param beginTime		The begintime of the look
	 * @return				True if the trial exists, the look falls within
	 * 							the range of the trial and not in the range
	 * 							of another look.
	 */
	public boolean addLook(int trial, long beginTime)
	{
		trial--;
		if(trial < 0 || trial > trials.size() || 
				!trials.get(trial).hasTime(beginTime))
		{
			return false;
		}
		else
		{
			return trials.get(trial).addLook(beginTime);
		}
	}
	
	/**
	 * Tries to remove the trial from the list
	 * @param trialNumber	The trial number of the trial to be removed
	 * @return				True iff remove succesful
	 */
	public boolean removeTrial(int trialNumber)
	{
		trialNumber--;
		if(trialNumber < 0 || trialNumber > trials.size())
			return false;
		else
			return trials.remove(trials.get(trialNumber));
	}
	
	public int getNumberOfTrials()
	{
		return trials.size();
	}
	
	public int getNumberOfLooks(int trial)
	{
		trial--;
		if(trial < 0 || trial > trials.size())
			return -1;
		return trials.get(trial).getNumberOfLooks();
	}
	
	public long getLookTime(int trial)
	{
		trial--;
		if(trial < 0 || trial > trials.size())
			return 0L;
		else
			return trials.get(trial).getTotalLookTime();
	}
	
	/**
	 * Method to get the trial number of the trial in which the passed time
	 * stamp falls
	 * @param time		Timestamp to be checked
	 * @return			Trial number of the trial in which the timestamp falls
	 * 					or -1 if no such trial exists
	 */
	public int getTrialByTime(long time)
	{
		for(int i = 0; i < trials.size(); i++)
		{
			if (trials.get(i).hasTime(time))
				return i+1;
		}
		
		return -1;
	}
	
	public int getLastTrialByTime(long time)
	{
		int trial = getTrialByTime(time);
		if (trial > 0) return trial;
		
		// If not inside a valid trial, return the last valid value
		// for this time
		for(int i = 0; i < trials.size(); i++)
		{
			if(trials.get(i).getBeginTime() > time)
				return i;
		}
		
		return -1;
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
		int trial = getTrialByTime(time);
		if(trial > 0)
		{
			Trial t = trials.get(getTrialByTime(time) - 1);
			return t.getLookByTime(time);
		} else
			return -1;
	}
	
	public int getLastLookByTime(long time)
	{
		int trial = getTrialByTime(time);
		return (trial != -1) ? trials.get(trial-1).getLastLookByTime(time) : -1;
	}
	
	/**
	 * Method to get the look number of the look in which the passed time
	 * stamp falls
	 * @param time		Timestamp to be checked
	 * @return			Look number of the trial in which the timestamp falls
	 * 					or -1 if no such look exists
	 */
	public int getLookByTime(int trial, long time)
	{
		trial--;
		if(trial < 0 || trial > trials.size())
		{
			return -1;
		}
		else
		{
			Trial t = trials.get(trial);
			return t.getLookByTime(time);
		}
	}
	
	public int getLastLookByTime(int trial, long time)
	{
		trial--;
		if(trial < 0 || trial > trials.size())
		{
			return -1;
		}
		else
		{
			Trial t = trials.get(trial);
			return t.getLastLookByTime(time);
		}
	}
	
	public boolean endTrial(int trial, long time)
	{
		trial--;
		if(trial < 0 || trial > trials.size())
			return false;
		else
		{
			return trials.get(trial).setEndTime(time);
		}
	}
	
	/**
	 * Check if the trial can be ended at time t
	 * @param trial		The trial to be ended
	 * @param time		The new end time of the trial
	 * @return			True iff trial can be ended
	 */
	public boolean canEndTrial(int trial, long time)
	{
		trial--;
		if(trial < 0 || trial > trials.size())
			return false;
		
		if(trials.get(trial).getBeginTime() == time)
			return false;
		
		return true;
	}
	
	public boolean canEndLook(int trial, int look, long time)
	{
		trial--;
		if(trial < 0 || trial > trials.size())
			return false;
		else
			return trials.get(trial).canEndLook(look, time);
	}
	
	public boolean endLook(int trial, int look, long time)
	{
		trial--;
		if(trial < 0 || trial > trials.size())
			return false;
		else
		{
			return trials.get(trial).setLookEndTime(look, time);
		}
	}
	
	/**
	 * Sets the settings of the settings view window to the Globals
	 * @param exp_name				Experiment name
	 * @param exp_id				Experiment ID
	 * @param res_id				Researcher ID
	 * @param pp_id					Participant ID
	 * @param show_exp_name			Show exp_name in output
	 * @param show_exp_id			Show exp_id in output
	 * @param show_res_id			Show res_id in output
	 * @param show_pp_id			Show pp_id in output
	 */
	public void setSettings(
			String exp_name, String exp_id, String res_id, String pp_id,
			boolean show_exp_name, boolean show_exp_id, 
			boolean show_res_id, boolean show_pp_id
		)
	{
		this.exp_name = exp_name;
		this.exp_id = exp_id;
		this.res_id = res_id;
		this.pp_id = pp_id;
		this.show_exp_name = show_exp_name;
		this.show_exp_id = show_exp_id;
		this.show_res_id = show_res_id;
		this.show_pp_id = show_pp_id;
	}
	
	/**
	 * Getfunction for video URL
	 * @return	Current video URL
	 */
	public String getUrl()
	{
		return this.url;
	}
	
	/**
	 * Setfunction for video URL
	 * @param url	New url for video
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}
	
	/**
	 * Get method for experiment name from global settings
	 * @return	Experiment name as set in global settings
	 */
	public String getExp_name()
	{
		return exp_name;
	}
	
	/**
	 * Get method for experiment ID from global settings
	 * @return	Experiment ID as set in global settings
	 */
	public String getExp_id()
	{
		return exp_id;
	}
	
	/**
	 * Get method for researcher ID from global settings
	 * @return	Researcher ID as set in global settings
	 */
	public String getRes_id()
	{
		return res_id;
	}
	
	/**
	 * Get method for participant ID from global settings
	 * @return	Participant ID as set in global settings
	 */
	public String getPP_id()
	{
		return pp_id;
	}
	
	/**
	 * Get method for the boolean value for if to show experiment name
	 * in the final output csv
	 * @return	If to show experiment name as set in global settings
	 */
	public boolean getShow_exp_name()
	{
		return show_exp_name;
	}
	
	/**
	 * Get method for the boolean value for if to show experiment ID
	 * in the final output csv
	 * @return	If to show experiment ID as set in global settings
	 */
	public boolean getShow_exp_id()
	{
		return show_exp_id;
	}
	
	/**
	 * Get method for the boolean value for if to show researcher ID
	 * in the final output csv
	 * @return	If to show researcher ID as set in global settings
	 */
	public boolean getShow_res_id()
	{
		return show_res_id;
	}
	
	/**
	 * Get method for the boolean value for if to show participant ID
	 * in the final output csv
	 * @return	If to show participant ID as set in global settings
	 */
	public boolean getShow_pp_id()
	{
		return show_pp_id;
	}
	
	
}

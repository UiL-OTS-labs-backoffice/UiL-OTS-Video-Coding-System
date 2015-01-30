package model;

/**
 * Keeps track of an experiment
 * @author mooij006
 *
 */
public class Experiment {
	// Current video file url
	private String url;
		
	// Global Experiment Settings
	private String exp_name, exp_id, res_id, pp_id;
	private boolean show_exp_name, show_exp_id, show_res_id, show_pp_id;
	
	// Trial information
	Trial trials[];
	int curTrialIndex; // Current index of trial
	
	/**
	 * Constructor for experiment
	 * Creates a new experiment object with starting values
	 */
	public Experiment()
	{
		trials = new Trial[0];
		curTrialIndex = -1;
	}
	
	public int getCurrentTrialNumber()
	{
		if(curTrialIndex == -1)
			return -1;
		Trial curTrial = trials[curTrialIndex];
		return curTrial.getTrialNumber();
	}
	
	public int getCurrentLookNumber()
	{
		if(curTrialIndex == -1)
			return -1;
		Trial curTrial = trials[curTrialIndex];
		return curTrial.getCurrentLookNumber();
			
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

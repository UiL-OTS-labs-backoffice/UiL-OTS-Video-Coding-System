package model;

import controller.Globals;

public class Experiment extends AbstractTimeContainer{
	
	/**
	 * SerialVersion UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Current video file url
	 */
	private String url;
	
	/**
	 * Project file location
	 */
	private String SaveURL;
		
	/**
	 * Global Experiment Settings
	 */
	private String exp_name, exp_id, res_id, pp_id;
	private boolean show_exp_name, show_exp_id, show_res_id, show_pp_id;
	
	/**
	 * Reference to the Globals object
	 */
	transient private Globals g;
	
	/**
	 * Constructor that saves a reference to the globals
	 * @param g		Globals instance
	 */
	public Experiment(Globals g) {
		super(0);
		this.g = g;
	}
	
	/**
	 * Method to change the reference to the globals
	 * @param g		New Globals instance
	 */
	public void setGlobals(Globals g)
	{
		this.g = g;
	}
	
	@Override
	public int canAddItem(long time)
	{
		if( !g.getVideoController().IsLoaded() ) {
			return -1;
		} else {
			return super.canAddItem(time);
		}
	}

	@Override
	public void addItem(long time) {
		Trial nt = new Trial(time);
		hiddenAddItem(time, nt);
	}

	/*****************************************************************
	 * ***************************************************************
	 * *****************EXPERIMENT INFORMATION ***********************
	 * ***************************************************************
	 * ***************************************************************
	 */
	
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
	 * The current location of the project file
	 * @return		URL to the project file location
	 */
	public String getSaveURL()
	{
		return SaveURL;
	}
	
	/**
	 * Set function for the project file location
	 * @param url		The url to the project file
	 */
	public void setSaveURL(String url)
	{
		this.SaveURL = url;
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

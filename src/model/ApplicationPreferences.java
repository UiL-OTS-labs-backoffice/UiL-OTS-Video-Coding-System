package model;
import java.io.File;
import java.util.prefs.Preferences;

/**
 * Preferences are saved through this class
 */
public class ApplicationPreferences {
	private Preferences prefs;
	
	/**
	 * Define the node the data is stored in
	 */
	public ApplicationPreferences()
	{
		prefs = Preferences.userRoot().node(this.getClass().getName());
	}
	
	/**
	 * Method to store the url to the vlc dll in the preferences
	 * @param url	The vlc dll url
	 */
	public void setVLCUrl(String url)
	{
		prefs.put("vlcURL", url);
	}
	
	/**
	 * Method to request vlc dll url from preferences
	 * @return		vlc dll url if set, null otherwise
	 */
	public String getVLCUrl()
	{
		return prefs.get("vlcURL", new File(".").getAbsolutePath());
	}
	
	/**
	 * Method to store the key of an action key assigned to an
	 * action.
	 * @param TupleID 	String containing unique ID of an action
	 * @param TupleKey	Int of the action key
	 */
	public void setKeyTuple(String TupleID, int TupleKey)
	{
		prefs.putInt(convertKeyTupleKey(TupleID), TupleKey);
	}
	
	/**
	 * Method to get the key of an action key that was assigned to an
	 * action and saved to the preferences earlier
	 * @param TupleID			String containing unique ID of an action
	 * @param defaultKey		Default value for key if no key was stored yet
	 * @return					int with the saved action key
	 */
	public int getKeyTuple(String TupleID, int defaultKey)
	{
		return prefs.getInt(convertKeyTupleKey(TupleID), defaultKey);
	}
	
	/**
	 * Method to add prefix to Key Tuple key
	 * @param TupleID 		Original key ID
	 * @return				Prefixed key ID
	 */
	private String convertKeyTupleKey(String TupleID)
	{
		return String.format("ActionKey_%s", TupleID); 
	}
	
	/**
	 * Method to get the last visited video directory
	 * @param default_dir	Directory to use if no video dir was used 
	 * 						before
	 * @return				Last used video dir if available, otherwise
	 * 						default
	 */
	public String getLastVideoDirectory(String default_dir)
	{
		return prefs.get("lastVideoDir", default_dir);
	}
	
	/**
	 * Set method for last visited video directory
	 * @param lastVideoDirectory 	Directory
	 */
	public void setLastVideoDirectory(String lastVideoDirectory)
	{
		prefs.put("lastVideoDir", lastVideoDirectory);
	}
	
	/**
	 * Get method for last visited CSV directory
	 * @param default_dir	Directory to use if no CSV dir was used 
	 * 						before
	 * @return				Last used CSV dir if available, otherwise
	 * 						default
	 */
	public String getLastCSVDirectory(String default_dir)
	{
		return prefs.get("lastCSVDir", default_dir);
	}
	
	/**
	 * Set method for last visited CSV dir
	 * @param LastCsvDirectory 		directory
	 */
	public void setLastCSVDirectory(String LastCsvDirectory)
	{
		prefs.put("lastCSVDir", LastCsvDirectory);
	}
	
	/**
	 * Get method for last visited project directory
	 * @param default_dir	Directory to use if no project dir was used 
	 * 						before
	 * @return				Last used project dir if available, otherwise
	 * 						default
	 */
	public String getLastProjectDirectory(String default_dir)
	{
		return prefs.get("lastProjectDir", default_dir);
	}
	
	/**
	 * Set method for last visited project dir
	 * @param LastCsvDirectory 		directory
	 */
	public void setLastProjectDirectory(String LastCsvDirectory)
	{
		prefs.put("lastProjectDir", LastCsvDirectory);
	}
	
	public void setClosed(){
		prefs.putBoolean("closedProperly", true);
	}
	
	public void setOpened(){
		prefs.putBoolean("closedProperly", false);
	}
	
	public boolean getClosedProperly(){
		return prefs.getBoolean("closedProperly", true);
	}
}

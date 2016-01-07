package model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import model.TimeObserver.IExperimentListener;
import model.TimeObserver.IExperimentSubject;
import model.TimeObserver.ITimeContainerObserver;
import model.TimeObserver.ITimeContainerSubject;
import model.TimeObserver.ITimeFrameObserver;
import model.TimeObserver.ITimeFrameSubject;
import controller.Globals;

public class Experiment extends AbstractTimeContainer implements IExperimentSubject{
	
	/**
	 * SerialVersion UID
	 */
	private static final long serialVersionUID = 2L;
	
	private transient List<IExperimentListener> observers;
	private transient Object MUTEX;
	
	/**
	 * Current video file url
	 */
	private String url;
	
	/**
	 * Project file location
	 */
	private String SaveURL;
	
	/**
	 * Project file name
	 */
	private String saveName;
	
	/**
	 * Keeps track of whether the file was saved or not
	 */
	private boolean backupState = false;
	private boolean savedState = false;
		
	/**
	 * Global Experiment Settings
	 */
	private String exp_name, exp_id, res_id, pp_id;
	private boolean show_exp_name, show_exp_id, show_res_id, show_pp_id;
	private long timeout = 2000;
	private boolean useTimeout;
	
	/**
	 * Reference to the Globals object
	 */
	transient private Globals g;
	
	/**
	 * Constructor that saves a reference to the globals
	 * @param g		Globals instance
	 */
	public Experiment(Globals g) {
		super(0L, AbstractTimeFrame.TYPE_EXPERIMENT);
		this.observers = new ArrayList<IExperimentListener>();
		this.MUTEX = new Object();
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
	public AbstractTimeFrame addItem(long time) {
		Trial nt = new Trial(time);
		hiddenAddItem(time, nt);
		return nt;
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
		stateChanged();
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
		stateChanged();
		this.SaveURL = url;
	}
	
	/**
	 * Return the current name of the project file
	 * @return 		Name of the current file
	 */
	public String getSaveName()
	{
		return this.saveName;
	}
	
	/**
	 * Set the new name of the current project file
	 * @param name		Current name of current project file
	 */
	public void setSaveName(String name)
	{
		stateChanged();
		this.saveName = name;
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
	
	public void setTimeout(long timeout)
	{
		stateChanged();
		this.timeout = timeout;
	}
	
	public long getTimeout()
	{
		return timeout;
	}
	
	public void setUseTimeout(boolean useTimeout)
	{
		stateChanged();
		this.useTimeout = useTimeout;
	}
	
	public boolean getUseTimeout()
	{
		return useTimeout;
	}
	
	/**
	 * Returns the save state of the current experiment
	 * @return true if no changes have been made since last save
	 */
	public boolean isSaved(){
		return this.savedState;
	}
	
	/**
	 * Returns the backup state of the current experiment
	 * @return True if no changes have been made since last backup
	 */
	public boolean isBackupSaved(){
		return this.backupState;
	}
	
	public void registerContainerListener(ITimeContainerSubject s){
		s.registerContainerListener(new ITimeContainerObserver(){

			@Override
			public void itemAdded(AbstractTimeContainer container,
					AbstractTimeFrame tf, int itemNumber) { }

			@Override
			public void itemRemoved(AbstractTimeContainer container,
					AbstractTimeFrame tf) { }
			@Override
			public void numberOfItemsChanged(AbstractTimeContainer container) {
				stateChanged();
			}
		});
	}
	
	public void registerTimeFrameListener(ITimeFrameSubject s)
	{
		s.registerFrameListener(new ITimeFrameObserver(){

			@Override
			public void timeChanged(AbstractTimeFrame tf) {
				stateChanged();
			}

			@Override
			public void commentChanged(AbstractTimeFrame tf, String comment) {
				stateChanged();
			}
		});
	}
	
	private void stateChanged(){
		this.savedState = false;
		this.backupState = false;
		notifyBackupChange();
		notifySavedChange();
	}
	
	public void isBackedUp(){
		this.backupState = true;
		notifyBackupChange();
	}
	
	public void saved(){
		this.savedState = true;
		notifySavedChange();
	}
	
	private void notifyBackupChange(){
		List<IExperimentListener> observersLocal = null;
		synchronized (MUTEX) {
			observersLocal = new ArrayList<IExperimentListener>(this.observers);
		}
		for(IExperimentListener obj : observersLocal)
		{
			obj.backupstateChanged(this.backupState);
		}
	}
	
	private void notifySavedChange(){
		List<IExperimentListener> observersLocal = null;
		synchronized (MUTEX) {
			observersLocal = new ArrayList<IExperimentListener>(this.observers);
		}
		for(IExperimentListener obj : observersLocal)
		{
			obj.saveStateChanged(this.savedState);
		}
	}

	@Override
	public void addExperimentListener(IExperimentListener obj) {
		if(obj == null) throw new NullPointerException("Null Observer");
		synchronized (MUTEX) {
			if(!observers.contains(obj)) observers.add(obj);
		}		
	}

	@Override
	public void removeExperimentListener(IExperimentListener obj) {
		synchronized (MUTEX) {
			observers.remove(obj);
		}
	}
	
	/**
	 * Method to get a backup of all registered listeners to the
	 * current instance of the experiment
	 * @return	List of listeners
	 */
	public List<IExperimentListener> getObservers(){
		List<IExperimentListener> observersLocal = null;
		synchronized (MUTEX) {
			observersLocal = new ArrayList<IExperimentListener>(this.observers);
		}
		return observersLocal;
	}
	
	/**
	 * If the model is replaced, use this to register all previously registered
	 * experiment listeners
	 * @param newObservers	List of listeners
	 */
	public void replaceObservers(List<IExperimentListener> newObservers)
	{
		for(IExperimentListener el : newObservers){
			addExperimentListener(el);
		}
	}
	
	private void readObject (final ObjectInputStream s ) throws ClassNotFoundException, IOException
    {
        s.defaultReadObject();
        this.MUTEX = new Object();
        this.observers = new ArrayList<IExperimentListener>();
        saved();
        isBackedUp();
    }
}

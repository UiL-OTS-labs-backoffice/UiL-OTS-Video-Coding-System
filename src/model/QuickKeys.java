package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import view.event.IQuickKeyListener;

/**
 * Contains references to quick keys
 * Can be set by user
 */
public class QuickKeys implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instance reference
	 */
	private static QuickKeys instance;
	
	/**
	 * Reference to globals class
	 */
	private ApplicationPreferences prefs;
	
	private List<IQuickKeyListener> observers;
	private final Object MUTEX = new Object();
	
	/**
	 * List of available actions
	 */	
	private Collection<Tuple> actionMap = Arrays.asList(
			new Tuple("play", "Play"), 
			new Tuple("prevFrame", "Previous Frame"),
			new Tuple("nextFrame", "Next Frame"),
			new Tuple("prevTrial", "Previous Trial"),
			new Tuple("nextTrial", "Next Trial"), 
			new Tuple("prevLook", "Previous Look"),
			new Tuple("nextLook", "Next Look"),
			new Tuple("newTrial", "New Trial"),
			new Tuple("endTrial", "End Trial"),
			new Tuple("newLook", "New Look"),
			new Tuple("endLook", "End Look")
		);
	
	/**
	 * Get method for instance
	 * @return	instance of QuickKeys model
	 */
	public static QuickKeys getInstance(ApplicationPreferences prefs)
	{
		if (instance == null)
			instance = new QuickKeys(prefs);
		return instance;
	}
	
	/**
	 * Method to register a QuickKeyListener
	 * @param observer	The listener object to be registered
	 */
	public void addQuickKeyListener(IQuickKeyListener observer){
		synchronized(MUTEX){
			if(!observers.contains(observer)){
				observers.add(observer);
			}
		}
	}
	
	/**
	 * Method to deregister a QuickKeyListener
	 * @param observer	The listener object to be deregistered
	 */
	public void removeQuickKeyListener(IQuickKeyListener observer){
		synchronized(MUTEX){
			observers.remove(observer);
		}
	}
	
	/**
	 * Private constructor to ensure singleton
	 * Sets default keys
	 */
	private QuickKeys(ApplicationPreferences prefs)
	{	
		this.observers = new ArrayList<IQuickKeyListener>();
		this.prefs = prefs;
		
		isk("play", 32);
		
		isk("prevFrame", 37);
		isk("nextFrame", 39);
		
		isk("prevTrial", -1);
		isk("nextTrial", -1);
		
		isk("prevLook", 91);
		isk("nextLook", 93);
		
		isk("newTrial", -1);
		isk("endTrial", -1);
		
		isk("newLook", 78);
		isk("endLook", -1);
	}
	
	/**
	 * Internal Set Keys (isk)
	 * @param ID
	 * @param key
	 */
	private void isk(String ID, int key)
	{
		setKey(ID, prefs.getKeyTuple(ID, key));
	}
	
	/**
	 * Get the assigned key for an action
	 * @param action	Action ID
	 * @return			Assigned key if key is assigned to action.
	 * 					0 otherwise
	 * 					-1 if action doesn't exist
	 */
	public int getKey(String action)
	{
		for(Tuple t : actionMap)
		{
			if (t.getID() == action)
				return t.getKey();
		}
		
		return -1;
	}
	
	/**
	 * Get the readable name for an action
	 * @param action	Action ID
	 * @return			Readable name of action, null if action doesn't exist
	 */
	public String getName(String action)
	{
		for(Tuple t : actionMap)
		{
			if (t.getID() == action)
				return t.getName();
		}
		
		return null;
	}
	
	/**
	 * Assign a new key to an existing action
	 * @param action	Name of the action
	 * @param key		number of the new key
	 */
	public void setKey(String action, int key)
	{
		int oldKey = -1;
		for(Tuple t : actionMap)
		{
			if(t.getID() == action) {
				oldKey = t.getKey();
				t.setKey(key);
				prefs.setKeyTuple(action, key);
			} else if (t.getKey() == key) {
				oldKey = t.getKey();
				t.setKey(0);
			}
		}
		
		if(oldKey != -1){
			List<IQuickKeyListener> localObservers;
			synchronized(MUTEX){
				localObservers = new ArrayList<IQuickKeyListener>(this.observers);
			}
			for(IQuickKeyListener o : localObservers){
				o.actionUpdated(action, oldKey);
			}
		}
	}
	
	/**
	 * Get a TreeSet of all available action
	 * @return	TreeSet containing all actions
	 */
	public Collection<String> getActions()
	{
		Collection<String> actions = new LinkedList<String>();
		for (Tuple t : actionMap)
			actions.add(t.getID());
		
		return actions;
	}
	
	/**
	 * Method to check if an action exists
	 * @param action	Action ID
	 * @return			True iff action exists
	 */
	public boolean isValidAction(String action)
	{
		for(Tuple t : actionMap)
		{
			if(t.getID().equals(action)) return true;
		}
		
		return false;
	}
	
	/**
	 * Action map element being an action/key (string/int) tuple.
	 * Name is the action name
	 * Key is the number of the assigned action key
	 * If key == 0, no action is executed.
	 */
	public class Tuple
	{
		private String id;
		private String name;
		private int key;
		
		/**
		 * Constructor of action/key tuple
		 * @param name	Action name
		 */
		public Tuple(String id,String name)
		{
			this.id = id;
			this.name = name;
			this.key = 0;
		}
		
		/**
		 * Get Method for the action name
		 * @return		Action name
		 */
		public String getName()
		{
			return name;
		}
		
		/**
		 * Get method for the ID of this action
		 * @return		ID of this action
		 */
		public String getID()
		{
			return id;
		}
		
		/**
		 * Get method for the assigned key
		 * @return		The assigned key of this method
		 */
		public int getKey()
		{
			return key;
		}
		
		/**
		 * Set method for the method to set the assigned key
		 * @param key		The key to be assigned to this method
		 */
		public void setKey(int key)
		{
			this.key = key;
		}

	}
	
}

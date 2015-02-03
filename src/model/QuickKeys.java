package model;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Contains references to quick keys
 * Can be set by user
 */
public class QuickKeys {
	
	/**
	 * Instance reference
	 */
	private static QuickKeys instance;
	
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
	public static QuickKeys getInstance()
	{
		if (instance == null)
			instance = new QuickKeys();
		return instance;
	}
	
	/**
	 * Private constructor to ensure singleton
	 * Sets default keys
	 */
	private QuickKeys()
	{		
		setKey("play", 32);
		setKey("prevFrame", 37);
		setKey("nextFrame", 39);
		setKey("prevLook", 91);
		setKey("nextLook", 93);
		setKey("newLook", 78);
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
		for(Tuple t : actionMap)
		{
			if(t.getID() == action) t.setKey(key);
			else if (t.getKey() == key) t.setKey(0);
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

package view.event;

public interface IQuickKeyListener {
	
	/**
	 * Method to notify observers of a key change for an action.
	 * The new key can be requested through the observed class
	 * @param action	Action that was changed
	 * @param oldKey	The key before the change
	 */
	public void actionUpdated(String action, int oldKey);
}

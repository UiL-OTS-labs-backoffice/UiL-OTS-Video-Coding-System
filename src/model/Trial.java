package model;

import java.util.LinkedList;

public class Trial extends AbstractTimeContainer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new trial at the time given as the argument
	 * @param time	Starttime of the trial in milliseconds
	 */
	public Trial(long time) {
		super(time, AbstractTimeFrame.TYPE_TRIAL);
	}

	@Override
	public AbstractTimeFrame addItem(long time) {
		Look nl = new Look(time);
		hiddenAddItem(time, nl);
		return nl;
	}
	
	public void removeAll()
	{
		this.items = new LinkedList<AbstractTimeFrame>();
	}
}
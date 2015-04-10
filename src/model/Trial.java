package model;

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
		super(time);
	}

	@Override
	public void addItem(long time) {
		Look nl = new Look(time);
		hiddenAddItem(time, nl);
	}

}
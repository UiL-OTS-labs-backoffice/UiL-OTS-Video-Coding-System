package model;

public class Look extends AbstractTimeFrame
{
	/**
	 * SerialVersion UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new look at the time given as an argument
	 * @param time	Start time of the look in milliseconds
	 */
	public Look(long time) {
		super(time, AbstractTimeFrame.TYPE_LOOK);
	}
	
}
package model.TimeObserver;
import model.AbstractTimeFrame;

public interface ITimeFrameObserver {
	
	/**
	 * Method to update the observer of the changed time
	 * @param tf		Time frame for which comment was changed
	 */
	public void timeChanged(AbstractTimeFrame tf);
	
	/**
	 * Method to notify the observer of a change in the comment
	 * for the time frame
	 * @param tf		Time frame for which comment was changed
	 * @param comment	The new comment
	 */
	public void commentChanged(AbstractTimeFrame tf, String comment);
	
}

package model.TimeObserver;
import model.AbstractTimeFrame;

public interface ITimeFrameObserver {
	
	/**
	 * Method to update the observer of the changed time
	 */
	public void timeChanged(AbstractTimeFrame tf);
	
}

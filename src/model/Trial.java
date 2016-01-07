package model;

public class Trial extends AbstractTimeContainer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new trial at the time given as the argument
	 * @param time	Start time of the trial in milliseconds
	 */
	public Trial(long time) {
		super(time, AbstractTimeFrame.TYPE_TRIAL);
		controller.Globals.getInstance().getExperimentModel().registerTimeFrameListener(this);
		controller.Globals.getInstance().getExperimentModel().registerContainerListener(this);
	}

	@Override
	public AbstractTimeFrame addItem(long time) {
		Look nl = new Look(time);
		hiddenAddItem(time, nl);
		return nl;
	}
}
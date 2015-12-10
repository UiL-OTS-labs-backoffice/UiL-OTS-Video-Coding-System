package view.timeline.utilities;

public interface INavbarObserver {
	public void visibleAreaChanged(long begin, long end, long visibleTime, float visiblePercentage);
}

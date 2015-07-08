package view.menu;

import model.AbstractTimeFrame;

public class TrialNavigator extends AbstractTrialListMenu {

	public TrialNavigator(String name) {
		super(name);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void actionPerformer(long ltime, AbstractTimeFrame f, String tt)
	{
		vc.setMediaTime(ltime);
		c.updateLabels(ltime);
	}

}

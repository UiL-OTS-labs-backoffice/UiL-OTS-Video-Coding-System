package view.menu;

import javax.swing.SwingUtilities;

import model.AbstractTimeFrame;

public class TrialNavigator extends AbstractTrialListMenu {

	public TrialNavigator(String name) {
		super(name);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void actionPerformer(final long ltime, AbstractTimeFrame f, String tt)
	{
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				vc.setMediaTime(ltime);
			}
		});
	}

}

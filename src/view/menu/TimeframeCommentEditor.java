package view.menu;

import javax.swing.SwingUtilities;

import view.panels.CommentEditor;
import model.AbstractTimeFrame;

public class TimeframeCommentEditor extends AbstractTrialListMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TimeframeCommentEditor(String name) {
		super(name);
	}

	@Override
	public void actionPerformer(final long ltime, final AbstractTimeFrame fr, final String trialText) {
		SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				new CommentEditor(ltime, fr, trialText);	
			}
		});
	}

}

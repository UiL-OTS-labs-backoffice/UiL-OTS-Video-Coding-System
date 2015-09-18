package view.menu;

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
	public void actionPerformer(long ltime, AbstractTimeFrame fr, String trialText) {
		new CommentEditor(ltime, fr, trialText);	
	}

}

package view.menu;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import controller.Globals;
import model.AbstractTimeFrame;

public class TimeframeCommentEditor extends AbstractTrialListMenu {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TimeframeCommentEditor(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void actionPerformer(long ltime, AbstractTimeFrame fr, String trialText) {
		String comment = (String) JOptionPane.showInputDialog(
				null, 
				String.format("Change the comment for %s",trialText), 
				"Comment", 
				JOptionPane.PLAIN_MESSAGE,
				new ImageIcon(Globals.getIcons().get(4)),
				null,
				fr.getComment()
			);
		
		if((comment != null && comment.length() > 0))
		{
			if(comment.indexOf(";") > 0)
			{
				comment.replaceAll(":,", ":");
				JOptionPane.showMessageDialog(
						null,
						"A semicolon was found in the comment. The semicolon "
						+ "has been removed to ensure compatibility with "
						+ "CSV export", 
						"No semicolons allowed", 
						JOptionPane.ERROR_MESSAGE);
			}
			fr.setComment(comment);
			return;
		}
	
	}

}

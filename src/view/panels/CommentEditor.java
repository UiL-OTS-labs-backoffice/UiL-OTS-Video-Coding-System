package view.panels;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import model.AbstractTimeFrame;
import controller.Globals;

public class CommentEditor extends JOptionPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommentEditor(AbstractTimeFrame fr, String trialText)
	{
		String comment = (String) showInputDialog(
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
		} else {
			fr.setComment(null);
		}
	}

}

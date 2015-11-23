package view.navbar.paneltimeframe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import controller.Controller;
import controller.Globals;
import controller.IVideoControls;
import model.AbstractTimeFrame;
import view.panels.CommentEditor;

/**
 * The context menu for a panel time frame
 */
class ContextMenu extends JPopupMenu
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Globals g;
	private final IVideoControls vc;
	private final Controller c;
	private final AbstractTimeFrame tf;
	
	public ContextMenu(Globals g, AbstractTimeFrame tf)
	{
		this.g = g;
		this.vc = g.getVideoController();
		this.c = g.getController();
		this.tf = tf;
		
		addCommentMenu();
		addGoToBeginMenu();
		addGoToEndMenu();
		add(new JSeparator());
		addRemoveLookMenu();
		addManipulateTrialMenus();
	}
	
	private void addCommentMenu()
	{
		JMenuItem addComment = new JMenuItem("Add comment");
		addComment.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				int number = c.getNumber(tf);
				String type = (tf.getType() == AbstractTimeFrame.TYPE_LOOK) ? "Look %d" : "Trial %d";
				new CommentEditor(tf, String.format(type, number));
			}
		});
		add(addComment);
	}
	
	private void addGoToBeginMenu()
	{
		JMenuItem goToBegin = new JMenuItem("Go to start time");
		goToBegin.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				vc.setMediaTime(tf.getBegin());					
			}
			
		});
		add(goToBegin);
	}
	
	private void addGoToEndMenu()
	{
		JMenuItem goToEnd = new JMenuItem("Go to end time");
		goToEnd.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				vc.setMediaTime(tf.getEnd());
			}
		});
		add(goToEnd);
	}
	
	private void addRemoveLookMenu()
	{
		JMenuItem removeLook = new JMenuItem("Remove this look");
		removeLook.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread() {
					public void run()
					{
						int tnr = g.getExperimentModel().getItemForTime(tf.getBegin());
						System.out.println("Tnr is " + tnr);
						int lnr = g.getExperimentModel().getItem(tnr).getNumberForItem(tf);
						System.out.println("lnr is " + lnr);
						g.getController().removeLook(tnr, lnr);
					}
				}.start();
			}
		});
		
		if(tf.getType() == AbstractTimeFrame.TYPE_LOOK)
			add(removeLook);
	}
	
	private void addManipulateTrialMenus()
	{
		JMenuItem removeTrial = new JMenuItem("Remove this trial");
		removeTrial.setToolTipText("WARNING: This will remove all looks in this trial as well!");
		removeTrial.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				new Thread() {
					public void run()
					{
						int tnr = g.getExperimentModel().getNumberForItem(tf);
						g.getController().removeTrial(tnr);
					}
				}.start();
			}
		});
		
		JMenuItem removeLooks = new JMenuItem("Remove looks");
		removeLooks.setToolTipText("WARNING: This will remove all looks in this trial!");
		removeLooks.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				new Thread() {
					public void run()
					{
						int tnr = g.getExperimentModel().getNumberForItem(tf);
						g.getController().removeLooksInTrial(tnr);
					}
				}.start();
			}
		});
		
		if(tf.getType() == AbstractTimeFrame.TYPE_TRIAL) {
			add(removeTrial);
			add(removeLooks);
		}
	}
	
}

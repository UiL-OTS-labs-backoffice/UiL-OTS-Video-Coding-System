package view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import controller.Controller;
import controller.Globals;
import controller.IVideoControls;
import model.AbstractTimeFrame;
import model.Trial;

public abstract class AbstractTrialListMenu extends JMenu {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected IVideoControls vc;
	protected Controller c;
	
	/**
	 * Constructor method
	 * @param name
	 */
	public AbstractTrialListMenu(String name)
	{
		super(name);
		vc = Globals.getInstance().getVideoController();
		c = Globals.getInstance().getController();
		addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
				generateTrialMenuItems();
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}
		});
	}
	
	/**
	 * Generates a menu that contains a list of all trials and looks
	 */
	private void generateTrialMenuItems()
	{
		while(getItemCount() > 0)
		{
			remove(0);
		}
		
		for(int i = 1; i <= c.getNumberOfTrials(); i++)
		{
			final Trial t = c.getTrial(i);
			final int tnr = i;
			
			final long time = t.getBegin();
			final String trialText = String.format(
					"Trial %d (%s)", 
					i, 
					view.bottombar.PlayerControlsPanel.formatTime(time)
				);
			
			JMenuItem item;
			if (t.getNumberOfItems() > 0)
			{
				item = new JMenu(trialText);
				item.addMouseListener(new MouseListener(){
					public void mouseClicked(MouseEvent e) {
						actionPerformer(time, t, trialText);
					}

					public void mouseEntered(MouseEvent e) {
					}

					public void mouseExited(MouseEvent e) {
					}

					public void mousePressed(MouseEvent e) {
					}

					public void mouseReleased(MouseEvent e) {
					}
					
				});
			}
			else
			{
				item = new JMenuItem(trialText);
				item.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						actionPerformer(time, t, trialText);
					}
				});
			}
			
			for(int j = 1; j <= t.getNumberOfItems(); j++)
			{
				final int lnr = j;
				final AbstractTimeFrame l = t.getItem(j);
				final long ltime = l.getBegin();
				final String lookText = String.format(
						"Look %d (%s)", 
						j, 
						view.bottombar.PlayerControlsPanel.formatTime(ltime)
					);
				JMenuItem look = new JMenuItem(lookText);
				look.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						actionPerformer(ltime, l, String.format("Trial %d look %d", tnr, lnr));
					}
				});
				
				item.add(look);
			}
			
			add(item);
		}
	}
	
	public abstract void actionPerformer(long ltime, AbstractTimeFrame frame, String trialText);
}

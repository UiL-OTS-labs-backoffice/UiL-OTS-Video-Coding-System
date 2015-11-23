package view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import view.formatter.Time;
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
			
			/**
			 * Update the trial menu when the menu is requested
			 */
			public void mouseEntered(MouseEvent e) {
				Thread menuItemGeneratorThread = new Thread()
				{
					public void run(){
						generateTrialMenuItems();
					}
				};
				menuItemGeneratorThread.start();
			}
			
			public void mouseClicked(MouseEvent e) { }
			public void mouseExited(MouseEvent e) { }
			public void mousePressed(MouseEvent e) { }
			public void mouseReleased(MouseEvent e) { }
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
					Time.format(time)
				);
			
			final JMenuItem item;
			if (t.getNumberOfItems() > 0)
			{
				item = new JMenu(trialText);
				item.addMouseListener(new MouseListener(){
					public void mouseClicked(MouseEvent e) {
						Thread actionPerformerThread = new Thread(){
							public void run(){
								actionPerformer(time, t, trialText);
							}
						};
						actionPerformerThread.start();
						
					}

					public void mouseEntered(MouseEvent e) { }
					public void mouseExited(MouseEvent e) { }
					public void mousePressed(MouseEvent e) { }
					public void mouseReleased(MouseEvent e) { }
					
				});
			}
			else
			{
				item = new JMenuItem(trialText);
				item.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						Thread actionPerformerThread  = new Thread()
						{
							public void run(){
								actionPerformer(time, t, trialText);
							}
						};
						actionPerformerThread.start();
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
						Time.format(ltime)
					);
				final JMenuItem look = new JMenuItem(lookText);
				look.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						Thread actionPerformerThread = new Thread()
						{
							public void run(){
								actionPerformer(ltime, l, String.format("Trial %d look %d", tnr, lnr));
							}
						};
						actionPerformerThread.start();
					}
				});
				
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						item.add(look);
					}
				});
			}
			
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					add(item);
				}
			});
		}
	}
	
	public abstract void actionPerformer(long ltime, AbstractTimeFrame frame, String trialText);
}

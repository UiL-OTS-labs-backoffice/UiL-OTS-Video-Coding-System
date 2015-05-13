package view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import model.Trial;
import controller.*;

public class MainMenu extends JMenuBar {

	private static final long serialVersionUID = 5103817458709866267L;
	
	private static Controller c;
	private static IVideoControls vc;
	
	private JMenuItem removeTrial, removeLook, removeLooks;
	
	public MainMenu(Globals g)
	{
		c = g.getController();
		vc = g.getVideoController();
		addFileMenu();
		addTrialMenu();
		addSettingsMenu();
		addHelpMenu();
	}
	
	/**
	 * Adds the file menu
	 * Contains:
	 * 		- save
	 * 		- save as
	 * 		- open project
	 * 		- open video file
	 */
	private void addFileMenu()
	{
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		add(fileMenu);
		
		JMenuItem save = new JMenuItem("Save");
		save.setMnemonic('S');
		save.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
                java.awt.Event.CTRL_MASK));
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (!c.save()) {
					JOptionPane.showMessageDialog(new JPanel(), "Sorry! Looks like the file couldn't be saved!", "Save failed", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		JMenuItem saveas = new JMenuItem("Save As");
		saveas.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				c.saveAs();
			}
		});
		saveas.setMnemonic('A');
//		JMenuItem openProject = new JMenuItem("Open project");
//		openProject.setMnemonic('O');
		
		JMenuItem exportProject = new JMenuItem("Export project to CSV");
		exportProject.setMnemonic('E');
		exportProject.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if (!c.export())
				{
					JOptionPane.showMessageDialog(new JPanel(), "Sorry! Looks like the file couldn't exported!", "Exporting failed", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		fileMenu.add(save);
		fileMenu.add(saveas);
//		fileMenu.add(openProject);
		fileMenu.addSeparator();
		fileMenu.add(exportProject);
	}
	
	/**
	 * Sets the state of the remove trial, remove look and remove all look
	 * options in the menu.
	 * @param rmt	True iff currently in trial
	 * @param rml	True iff currently in look
	 */
	public void updateButtons(boolean rmt, boolean rml){
		removeTrial.setEnabled(rmt);
		removeLook.setEnabled(rml);
		removeLooks.setEnabled(rmt);
	}
	
	/**
	 * Adds the settings menu
	 * Contains:
	 * 		- Experiment settings
	 */
	private void addSettingsMenu()
	{
		JMenu settingsMenu = new JMenu("Settings");
		settingsMenu.setMnemonic('E');
		add(settingsMenu);
		
		JMenuItem setExpInfo = new JMenuItem("Experiment Information");
		setExpInfo.setMnemonic('I');
		
		setExpInfo.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		c.openSettings();
        	}
        });
		
		settingsMenu.add(setExpInfo);
	}
	
	/**
	 * Adds the trial menu
	 * Contains:
	 * 		- Edit current trial number
	 * 		- Remove current trial
	 * 		- Remove current look
	 * 		- Remove looks (removes all looks in current trial)
	 * 		- Go to trial:
	 * 			- List of trials
	 * 		- Go to look:
	 * 			- List of looks
	 * 		- Overview (of currently set trials and looks)
	 */
	private void addTrialMenu()
	{
		JMenu trialMenu = new JMenu("Trial");
		trialMenu.setMnemonic('T');
		add(trialMenu);
		
		removeTrial = new JMenuItem("Remove current trial");
		removeTrial.setToolTipText("Removes the current trial");
		removeTrial.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				c.removeCurrentTrial();
			}
			
		});
		
		removeLook = new JMenuItem("Remove current look");
		removeLook.setToolTipText("Removes the current look");
		removeLook.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				c.removeCurrentLook();
			}
			
		});
		
		removeLooks = new JMenuItem("Remove looks in trial");
		removeLooks.setToolTipText("Removes all the looks from the current "
				+ "trial");
		removeLooks.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				c.removeAllCurrentLooks();
			}
			
		});
		
		final JMenu goToTrial = new JMenu("Go to trial");
		
		goToTrial.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e) {
			}

			public void mouseEntered(MouseEvent e) {
				generateTrialMenuItems(goToTrial);
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}
			
		});
		
		JMenuItem overview = new JMenuItem("Show overview");
		overview.setToolTipText("Show an overview of the experiment so far");
		overview.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		view.panels.ExperimentOverview kdp = new view.panels.ExperimentOverview();
        		kdp.setVisible(true);
        	}
        });
		
		trialMenu.add(removeTrial);
		trialMenu.add(removeLook);
		trialMenu.add(removeLooks);
		trialMenu.add(goToTrial);
		trialMenu.add(overview);
		
		removeTrial.setEnabled(false);
		removeLook.setEnabled(false);
		removeLooks.setEnabled(false);
	}
	
	private void generateTrialMenuItems(JMenu menu)
	{
		while(menu.getItemCount() > 0)
		{
			menu.remove(0);
		}
		
		for(int i = 1; i <= c.getNumberOfTrials(); i++)
		{
			Trial t = c.getTrial(i);
			
			final long time = t.getBegin();
			String trialText = String.format(
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
						vc.setMediaTime(time);
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
						vc.setMediaTime(time);
						c.updateLabels(time);
					}
				});
			}
			
			for(int j = 1; j <= t.getNumberOfItems(); j++)
			{
				
				final long ltime = t.getItem(j).getBegin();
				String lookText = String.format(
						"Look %d (%s)", 
						j, 
						view.bottombar.PlayerControlsPanel.formatTime(ltime)
					);
				JMenuItem look = new JMenuItem(lookText);
				look.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e)
					{
						vc.setMediaTime(ltime);
						c.updateLabels(ltime);
					}
				});
				
				item.add(look);
			}
			
			menu.add(item);
		}
	}
	
	
	/**
	 * Adds the help menu
	 * Contains:
	 * 		- Quick Keys list
	 * 		- Version and contact information
	 */
	private void addHelpMenu()
	{
		JMenu help = new JMenu("Help");
		add(help);
		
		JMenuItem shortKeys = new JMenuItem("Quick keys");
		shortKeys.setToolTipText("Show a list of quick keys");
		shortKeys.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		view.panels.QuickKeys kdp = new view.panels.QuickKeys();
        		kdp.setVisible(true);
        	}
        });
		
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new view.panels.About().setVisible(true);
			}
			
		});
		
		help.add(shortKeys);
		help.add(about);
	}
	
}

package view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import controller.*;

public class MainMenu extends JMenuBar {

	private static final long serialVersionUID = 5103817458709866267L;
	
	private static Controller c;
	
	private JMenuItem removeTrial, removeLook, removeLooks;
	
	public MainMenu(Globals g)
	{
		c = g.getController();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				addFileMenu();
				addTrialMenu();
				addSettingsMenu();
				addHelpMenu();
			}
		});
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
		final JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		
		final JMenuItem save = new JMenuItem("Save");
		save.setMnemonic('S');
		save.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
                java.awt.Event.CTRL_MASK));
		save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Thread saveThread = new Thread(){ 
					public void run(){
						c.save();
					}
				};
				saveThread.start();
			}
		});
		final JMenuItem saveas = new JMenuItem("Save As");
		saveas.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Thread saveAsThread = new Thread(){
					public void run(){
						c.saveAs();
					}
				};
				saveAsThread.start();
			}
		});
		saveas.setMnemonic('A');
		
		final JMenuItem exportProject = new JMenuItem("Export project to CSV");
		exportProject.setMnemonic('E');
		exportProject.setToolTipText("This will export all the trials and the total of the time of all the looks in each of those trials");
		exportProject.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Thread exportThread = new Thread()
				{
					public void run(){
						if (!c.export())
						{
							JOptionPane.showMessageDialog(
									new JPanel(), 
									"Sorry! Looks like the file couldn't exported!", 
									"Exporting failed", 
									JOptionPane.ERROR_MESSAGE
								);
						}
					}
				};
				exportThread.start();
			}
		});
		
		final JMenuItem exportOverview = new JMenuItem("Export overview to CSV");
		exportOverview.setToolTipText("This will export an extended overview of the project, including the begin- and end times for all trials and looks");
		exportOverview.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Thread exportThread = new Thread()
				{
					public void run(){
						if (!c.exportOverview())
						{
							JOptionPane.showMessageDialog(
									new JPanel(), 
									"Sorry! Looks like the file couldn't exported!", 
									"Exporting failed", 
								JOptionPane.ERROR_MESSAGE
							);
						}
					}
				};
				exportThread.start();
			}
		});
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				fileMenu.add(save);
				fileMenu.add(saveas);
//				fileMenu.add(openProject);
				fileMenu.addSeparator();
				fileMenu.add(exportProject);
				fileMenu.add(exportOverview);
				add(fileMenu);
			}
		});
		
	}
	
	/**
	 * Sets the state of the remove trial, remove look and remove all look
	 * options in the menu.
	 * @param rmt	True iff currently in trial
	 * @param rml	True iff currently in look
	 */
	public void updateButtons(final boolean rmt, final boolean rml){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				removeTrial.setEnabled(rmt);
				removeLook.setEnabled(rml);
				removeLooks.setEnabled(rmt);
			}
		});
	}
	
	/**
	 * Adds the settings menu
	 * Contains:
	 * 		- Experiment settings
	 */
	private void addSettingsMenu()
	{
		final JMenu settingsMenu = new JMenu("Settings");
		settingsMenu.setMnemonic('E');
		
		final JMenuItem setExpInfo = new JMenuItem("Experiment Information");
		setExpInfo.setMnemonic('I');
		
		setExpInfo.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Thread settingsOpener = new Thread(){
        			public void run(){
        				c.openSettings();
        			}
        		};
        		settingsOpener.start();
        	}
        });
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				settingsMenu.add(setExpInfo);
				add(settingsMenu);
			}
		});
		
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
		final JMenu trialMenu = new JMenu("Trial");
		trialMenu.setMnemonic('T');
		
		
		removeTrial = new JMenuItem("Remove current trial");
		removeTrial.setToolTipText("Removes the current trial");
		removeTrial.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread removeTrialThread = new Thread(){
					public void run(){
						c.removeCurrentTrial();
					}
				};
				removeTrialThread.start();
			}
			
		});
		
		removeLook = new JMenuItem("Remove current look");
		removeLook.setToolTipText("Removes the current look");
		removeLook.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread removeLookThread = new Thread(){
					public void run(){
						c.removeCurrentLook();
					}
				};
				removeLookThread.start();
			}
			
		});
		
		removeLooks = new JMenuItem("Remove looks in trial");
		removeLooks.setToolTipText("Removes all the looks from the current "
				+ "trial");
		removeLooks.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Thread removeAllLooks = new Thread(){
					public void run(){
						c.removeAllCurrentLooks();
					}
				};
				removeAllLooks.start();
			}
			
		});
		
		final JMenu goToTrial = new TrialNavigator("Go to trial");
		final JMenu addCommentMenu = new TimeframeCommentEditor("Add a comment");
		
		final JMenuItem overview = new JMenuItem("Show overview");
		overview.setToolTipText("Show an overview of the experiment so far");
		overview.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		view.panels.ExperimentOverview kdp = new view.panels.ExperimentOverview();
        		kdp.setVisible(true);
        	}
        });
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				trialMenu.add(removeTrial);
				trialMenu.add(removeLook);
				trialMenu.add(removeLooks);
				trialMenu.add(goToTrial);
				trialMenu.add(addCommentMenu);
				trialMenu.add(overview);
				add(trialMenu);
				
				removeTrial.setEnabled(false);
				removeLook.setEnabled(false);
				removeLooks.setEnabled(false);
			}
		});
		
	}
	
	/**
	 * Adds the help menu
	 * Contains:
	 * 		- Quick Keys list
	 * 		- Version and contact information
	 */
	private void addHelpMenu()
	{
		final JMenu help = new JMenu("Help");
		
		final JMenuItem shortKeys = new JMenuItem("Quick keys");
		shortKeys.setToolTipText("Show a list of quick keys");
		shortKeys.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		view.panels.QuickKeys kdp = new view.panels.QuickKeys();
        		kdp.setVisible(true);
        	}
        });
		
		final JMenuItem about = new JMenuItem("About");
		about.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new view.panels.About().setVisible(true);
			}
			
		});
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				help.add(shortKeys);
				help.add(about);
				add(help);
			}
		});
		
	}
	
}

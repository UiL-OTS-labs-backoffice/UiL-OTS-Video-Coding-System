package view.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import controller.*;

public class MainMenu extends JMenuBar {

	private static final long serialVersionUID = 5103817458709866267L;
	
	private static Controller c = Controller.getInstance();
	
	public MainMenu()
	{
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
		JMenuItem saveas = new JMenuItem("Save As");
		saveas.setMnemonic('A');
		JMenuItem openProject = new JMenuItem("Open project");
		openProject.setMnemonic('O');
		JMenuItem openVideo = new JMenuItem("Op video file");
		openVideo.setMnemonic('V');
		
		openVideo.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		c.videoUrlChooser();
        	}
        });
		
		fileMenu.add(save);
		fileMenu.add(saveas);
		fileMenu.add(openProject);
		fileMenu.add(new JSeparator());
		fileMenu.add(openVideo);
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
		
		JMenuItem removeTrial = new JMenuItem("Remove current trial");
		removeTrial.setToolTipText("Removes the current trial, but doesn't"
				+ " change the trial numbers of the remaining trials");
		
		JMenuItem removeLook = new JMenuItem("Remove current look");
		removeLook.setToolTipText("Removes the current look and changes "
				+ "the numbers of the remaining looks");
		
		JMenuItem removeLooks = new JMenuItem("Remove looks in trial");
		removeLooks.setToolTipText("Removes all the looks from the current "
				+ "trial");
		
		// TODO: generate list of current trials
		JMenu goToTrial = new JMenu("Go to trial");
		
		// TODO: generate list of current looks
		JMenu goToLook = new JMenu("Go to look");
		
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
		trialMenu.add(goToLook);
		trialMenu.add(overview);
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
		
		help.add(shortKeys);
		help.add(about);
	}
}

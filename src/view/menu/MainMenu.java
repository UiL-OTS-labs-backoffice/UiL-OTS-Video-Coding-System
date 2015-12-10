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

import view.panels.CSVExportSelector;
import controller.*;

public class MainMenu extends JMenuBar {

	private static final long serialVersionUID = 5103817458709866267L;
	
	private static final KeyStroke SAVE_ACCELERATOR = KeyStroke.getKeyStroke("control S");
	private static final KeyStroke SAVE_AS_ACCELERATOR = KeyStroke.getKeyStroke("control alt S");
//	private static final KeyStroke EXPORT_OVERVIEW_ACCELERATOR = KeyStroke.getKeyStroke("control alt O");
	private static final KeyStroke EXPORT_PROJECT_ACCELERATOR = KeyStroke.getKeyStroke("control alt P");
	private static final KeyStroke OPEN_OVERVIEW_ACCELERATOR = KeyStroke.getKeyStroke("control O");
	private static final KeyStroke EXPERIMENT_INFORMATION_ACCELERATOR = KeyStroke.getKeyStroke("control E");
	
	private static Controller c;
	
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
		
		final JMenuItem save = new JMenuItem("Save");
		save.setAccelerator(SAVE_ACCELERATOR);
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
		saveas.setAccelerator(SAVE_AS_ACCELERATOR);
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
		
		final JMenuItem exportProject = new JMenuItem("Export project to CSV");
		exportProject.setAccelerator(EXPORT_PROJECT_ACCELERATOR);
		exportProject.setToolTipText("This will export all the trials and the total of the time of all the looks in each of those trials");
		exportProject.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Thread exportThread = new Thread()
				{
					public void run(){
						CSVExportSelector exporter = new CSVExportSelector();
						if(exporter.isApproved() || exporter.getFilePath() != null){
							if (!c.export(exporter.getFilePath(), exporter.getExporterMethod()))
							{
								JOptionPane.showMessageDialog(
										new JPanel(), 
										"Sorry! Looks like the file couldn't exported!", 
										"Exporting failed", 
										JOptionPane.ERROR_MESSAGE
									);
							}
						}
					}
				};
				exportThread.start();
			}
		});
		
//		final JMenuItem exportOverview = new JMenuItem("Export overview to CSV");
//		exportOverview.setAccelerator(EXPORT_OVERVIEW_ACCELERATOR);
//		exportOverview.setToolTipText("Export the project to CSV");
//		
//		exportOverview.addActionListener(new ActionListener(){
//			
//			
//			
//			public void actionPerformed(ActionEvent e){
//				Thread exportThread = new Thread()
//				{
//					public void run(){
//						CSVExportSelector exporter = new CSVExportSelector();
//						if(exporter.isApproved() || exporter.getName() != null){
//							
//						}
//						
//						if (!c.exportOverview())
//						{
//							JOptionPane.showMessageDialog(
//									new JPanel(), 
//									"Sorry! Looks like the file couldn't exported!", 
//									"Exporting failed", 
//								JOptionPane.ERROR_MESSAGE
//							);
//						}
//					}
//				};
//				exportThread.start();
//			}
//		});
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				fileMenu.add(save);
				fileMenu.add(saveas);
				fileMenu.addSeparator();
				fileMenu.add(exportProject);
//				fileMenu.add(exportOverview);
				add(fileMenu);
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
		
		final JMenuItem setExpInfo = new JMenuItem("Experiment Information");
		setExpInfo.setAccelerator(EXPERIMENT_INFORMATION_ACCELERATOR);
		
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
		final JMenu experimentMenu = new JMenu("Project");
		
		final JMenuItem overview = new JMenuItem("Show overview");
		overview.setAccelerator(OPEN_OVERVIEW_ACCELERATOR);
		overview.setToolTipText("Show an overview of the experiment so far");
		overview.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Globals.getInstance().showExperimentOverview();
        	}
        });
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				experimentMenu.add(overview);
				add(experimentMenu);
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
        		view.panels.QuickKeysPanel kdp = new view.panels.QuickKeysPanel();
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

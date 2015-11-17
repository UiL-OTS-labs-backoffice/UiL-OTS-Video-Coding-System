package view.navbar;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import controller.IVideoControls;
import view.bottombar.PlayerControlsPanel;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DebugInfo extends JFrame {
	
	Navbar navbar;
	IVideoControls v;
	
	private final ScheduledExecutorService executorService = 
			Executors.newSingleThreadScheduledExecutor();
	
	private UpdateRunnable updateRunnable;
	
	public DebugInfo(Navbar navbar, IVideoControls v) {
		this.navbar = navbar;
		this.v = v;
		addAll();
		
		updateRunnable = new UpdateRunnable();
		executorService.scheduleAtFixedRate(
					updateRunnable,
					0L,
					1L,
					TimeUnit.MILLISECONDS
				);
		
		this.setVisible(true);
	}
	
	private void addAll()
	{
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblStarttime = new JLabel("Starttime (visible)");
		GridBagConstraints gbc_lblStarttime = new GridBagConstraints();
		gbc_lblStarttime.insets = new Insets(0, 0, 5, 5);
		gbc_lblStarttime.gridx = 0;
		gbc_lblStarttime.gridy = 0;
		getContentPane().add(lblStarttime, gbc_lblStarttime);
		
		starttimelbl = new JLabel("<time>");
		GridBagConstraints gbc_starttimelbl = new GridBagConstraints();
		gbc_starttimelbl.insets = new Insets(0, 0, 5, 5);
		gbc_starttimelbl.gridx = 1;
		gbc_starttimelbl.gridy = 0;
		getContentPane().add(starttimelbl, gbc_starttimelbl);
		
		startlonglbl = new JLabel("<long time>");
		GridBagConstraints gbc_startlonglbl = new GridBagConstraints();
		gbc_startlonglbl.insets = new Insets(0, 0, 5, 0);
		gbc_startlonglbl.gridx = 2;
		gbc_startlonglbl.gridy = 0;
		getContentPane().add(startlonglbl, gbc_startlonglbl);
		
		JLabel lblEndTimevisible = new JLabel("end time (visible)");
		GridBagConstraints gbc_lblEndTimevisible = new GridBagConstraints();
		gbc_lblEndTimevisible.insets = new Insets(0, 0, 5, 5);
		gbc_lblEndTimevisible.gridx = 0;
		gbc_lblEndTimevisible.gridy = 1;
		getContentPane().add(lblEndTimevisible, gbc_lblEndTimevisible);
		
		endtimelbl = new JLabel("<time>");
		GridBagConstraints gbc_endtimelbl = new GridBagConstraints();
		gbc_endtimelbl.insets = new Insets(0, 0, 5, 5);
		gbc_endtimelbl.gridx = 1;
		gbc_endtimelbl.gridy = 1;
		getContentPane().add(endtimelbl, gbc_endtimelbl);
		
		endlonglbl = new JLabel("<long time>");
		GridBagConstraints gbc_endlonglbl = new GridBagConstraints();
		gbc_endlonglbl.insets = new Insets(0, 0, 5, 0);
		gbc_endlonglbl.anchor = GridBagConstraints.WEST;
		gbc_endlonglbl.gridx = 2;
		gbc_endlonglbl.gridy = 1;
		getContentPane().add(endlonglbl, gbc_endlonglbl);
		
		JLabel lblVisibleTime = new JLabel("Visible time");
		GridBagConstraints gbc_lblVisibleTime = new GridBagConstraints();
		gbc_lblVisibleTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblVisibleTime.gridx = 0;
		gbc_lblVisibleTime.gridy = 2;
		getContentPane().add(lblVisibleTime, gbc_lblVisibleTime);
		
		visibletimelbl = new JLabel("<long time>");
		GridBagConstraints gbc_visibletimelbl = new GridBagConstraints();
		gbc_visibletimelbl.insets = new Insets(0, 0, 5, 5);
		gbc_visibletimelbl.gridx = 1;
		gbc_visibletimelbl.gridy = 2;
		getContentPane().add(visibletimelbl, gbc_visibletimelbl);
		
		visiblepctlbl = new JLabel("<pct>");
		GridBagConstraints gbc_visiblepctlbl = new GridBagConstraints();
		gbc_visiblepctlbl.insets = new Insets(0, 0, 5, 0);
		gbc_visiblepctlbl.gridx = 2;
		gbc_visiblepctlbl.gridy = 2;
		getContentPane().add(visiblepctlbl, gbc_visiblepctlbl);
		
		JLabel lblCurrentTime = new JLabel("Current time");
		GridBagConstraints gbc_lblCurrentTime = new GridBagConstraints();
		gbc_lblCurrentTime.insets = new Insets(0, 0, 5, 5);
		gbc_lblCurrentTime.gridx = 0;
		gbc_lblCurrentTime.gridy = 3;
		getContentPane().add(lblCurrentTime, gbc_lblCurrentTime);
		
		currenttimelbl = new JLabel("<time>");
		GridBagConstraints gbc_currenttimelbl = new GridBagConstraints();
		gbc_currenttimelbl.insets = new Insets(0, 0, 5, 5);
		gbc_currenttimelbl.gridx = 1;
		gbc_currenttimelbl.gridy = 3;
		getContentPane().add(currenttimelbl, gbc_currenttimelbl);
		
		currentlonglbl = new JLabel("<long time>");
		GridBagConstraints gbc_currentlonglbl = new GridBagConstraints();
		gbc_currentlonglbl.insets = new Insets(0, 0, 5, 0);
		gbc_currentlonglbl.gridx = 2;
		gbc_currentlonglbl.gridy = 3;
		getContentPane().add(currentlonglbl, gbc_currentlonglbl);
		
		lblVideoEndTime = new JLabel("Video end time");
		GridBagConstraints gbc_lblVideoEndTime = new GridBagConstraints();
		gbc_lblVideoEndTime.insets = new Insets(0, 0, 0, 5);
		gbc_lblVideoEndTime.gridx = 0;
		gbc_lblVideoEndTime.gridy = 4;
		getContentPane().add(lblVideoEndTime, gbc_lblVideoEndTime);
		
		videoEndTimelbl = new JLabel("<time>");
		GridBagConstraints gbc_videoEndTimelbl = new GridBagConstraints();
		gbc_videoEndTimelbl.insets = new Insets(0, 0, 0, 5);
		gbc_videoEndTimelbl.gridx = 1;
		gbc_videoEndTimelbl.gridy = 4;
		getContentPane().add(videoEndTimelbl, gbc_videoEndTimelbl);
		
		videoEndLonglbl = new JLabel("<long time>");
		GridBagConstraints gbc_videoEndLonglbl = new GridBagConstraints();
		gbc_videoEndLonglbl.gridx = 2;
		gbc_videoEndLonglbl.gridy = 4;
		getContentPane().add(videoEndLonglbl, gbc_videoEndLonglbl);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel currentlonglbl;
	private JLabel currenttimelbl;
	private JLabel visiblepctlbl;
	private JLabel visibletimelbl;
	private JLabel endlonglbl;
	private JLabel endtimelbl;
	private JLabel startlonglbl;
	private JLabel starttimelbl;
	private JLabel lblVideoEndTime;
	private JLabel videoEndTimelbl;
	private JLabel videoEndLonglbl;
	
	private final class UpdateRunnable implements Runnable
	{

		@Override
		public void run() {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					currentlonglbl.setText(Long.toString(v.getMediaTime()));
					currenttimelbl.setText(PlayerControlsPanel.formatTime(v
							.getMediaTime()));
					visiblepctlbl.setText(String.format("%f%%",
							navbar.getVisiblePercentage()));
					visibletimelbl.setText(Long.toString(navbar
							.getVisibleTime()));
					endlonglbl.setText(Long.toString(navbar
							.getCurrentEndVisibleTime()));
					endtimelbl.setText(PlayerControlsPanel.formatTime(navbar
							.getCurrentEndVisibleTime()));
					startlonglbl.setText(Long.toString(navbar
							.getCurrentStartVisibleTime()));
					starttimelbl.setText(PlayerControlsPanel.formatTime(navbar
							.getCurrentStartVisibleTime()));
					videoEndTimelbl.setText(PlayerControlsPanel.formatTime(v.getMediaDuration()));
					videoEndLonglbl.setText(Long.toString(v.getMediaDuration()));
				}
			});
		}
		
	}

}

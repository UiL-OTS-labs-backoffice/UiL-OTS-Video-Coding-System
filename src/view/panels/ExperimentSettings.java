package view.panels;

import java.awt.BorderLayout;
//import java.awt.EventQueue;



import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Dialog.ModalExclusionType;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;




import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

import controller.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Point;

import javax.swing.JSpinner;

/**
 * JFrame containing settings menu for the experiment
 * @author mooij006
 *
 */
public class ExperimentSettings{
	
	private Controller c;

	// Fields
	private JFrame frmExperimentSettings;
	private JPanel contentPane;
	private JTextField exp_name;
	private JTextField exp_id;
	private JTextField res_id;
	private JTextField pp_id;
	private JCheckBox show_exp_name, show_exp_id, show_res_id, show_pp_id;
	
	// Keeps track if checkbox should be enabled automatically on text change
	private boolean exp_name_checked = false, exp_id_checked = false, 
		res_id_checked = false, pp_id_checked = false;
	private JLabel lblTimeoutms;
	private JCheckBox use_timeout;
	private JSpinner spinner_timeout;

	/**
	 * Create the frame.
	 */
	public ExperimentSettings(Globals g) {
		c = g.getController();
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createLayout();
			}
		});
	}
	
	private void createLayout()
	{
		frmExperimentSettings = new JFrame();
		frmExperimentSettings.setIconImages(Globals.getIcons());
		frmExperimentSettings.setResizable(false);
		frmExperimentSettings.setLocation(new Point(500, 500));
		frmExperimentSettings.setTitle("Experiment Settings");
		frmExperimentSettings.setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
		frmExperimentSettings.setAlwaysOnTop(true);
		frmExperimentSettings.setBounds(100, 100, 450, 254);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		frmExperimentSettings.setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.rowHeights = new int[] {0, 0, 0, 0, 0};
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		panel.setLayout(gbl_panel);
		
		JLabel lblExperimentName = new JLabel("Experiment name");
		GridBagConstraints gbc_lblExperimentName = new GridBagConstraints();
		gbc_lblExperimentName.insets = new Insets(0, 0, 5, 5);
		gbc_lblExperimentName.gridx = 0;
		gbc_lblExperimentName.gridy = 0;
		panel.add(lblExperimentName, gbc_lblExperimentName);
		
		exp_name = new JTextField();
		exp_name.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(exp_name.getText().length() == 0)
				{
					exp_name_checked = !show_exp_name.isSelected();
					show_exp_name.setSelected(false);
					show_exp_name.setEnabled(false);
				}
				else
				{
					if(!exp_name_checked)
						show_exp_name.setSelected(true);
					show_exp_name.setEnabled(true);
				}
			}
		});
		GridBagConstraints gbc_exp_name = new GridBagConstraints();
		gbc_exp_name.insets = new Insets(0, 0, 5, 5);
		gbc_exp_name.fill = GridBagConstraints.HORIZONTAL;
		gbc_exp_name.gridx = 1;
		gbc_exp_name.gridy = 0;
		panel.add(exp_name, gbc_exp_name);
		exp_name.setColumns(10);
		
		show_exp_name = new JCheckBox("Include in csv");
		show_exp_name.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exp_name_checked = true;
			}
		});
		show_exp_name.setEnabled(false);
		show_exp_name.setToolTipText("If checked, a column \"exp_name\" will appear in the output file after exporting");
		GridBagConstraints gbc_show_exp_name = new GridBagConstraints();
		gbc_show_exp_name.insets = new Insets(0, 0, 5, 0);
		gbc_show_exp_name.gridx = 2;
		gbc_show_exp_name.gridy = 0;
		panel.add(show_exp_name, gbc_show_exp_name);
		
		JLabel lblExperimentId = new JLabel("Experiment ID");
		GridBagConstraints gbc_lblExperimentId = new GridBagConstraints();
		gbc_lblExperimentId.anchor = GridBagConstraints.EAST;
		gbc_lblExperimentId.insets = new Insets(0, 0, 5, 5);
		gbc_lblExperimentId.gridx = 0;
		gbc_lblExperimentId.gridy = 1;
		panel.add(lblExperimentId, gbc_lblExperimentId);
		
		exp_id = new JTextField();
		exp_id.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(exp_id.getText().length() == 0)
				{
					exp_id_checked = !show_exp_id.isSelected();
					show_exp_id.setSelected(false);
					show_exp_id.setEnabled(false);
				}
				else
				{
					if(!exp_id_checked)
						show_exp_id.setSelected(true);
					show_exp_id.setEnabled(true);
				}
			}
		});
		GridBagConstraints gbc_exp_id = new GridBagConstraints();
		gbc_exp_id.insets = new Insets(0, 0, 5, 5);
		gbc_exp_id.fill = GridBagConstraints.HORIZONTAL;
		gbc_exp_id.gridx = 1;
		gbc_exp_id.gridy = 1;
		panel.add(exp_id, gbc_exp_id);
		exp_id.setColumns(10);
		
		show_exp_id = new JCheckBox("Include in csv");
		show_exp_id.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exp_id_checked = true;
			}
		});
		show_exp_id.setEnabled(false);
		show_exp_id.setToolTipText("If checked, a column \"exp_id\" will appear in the output file after exporting");
		GridBagConstraints gbc_show_exp_id = new GridBagConstraints();
		gbc_show_exp_id.insets = new Insets(0, 0, 5, 0);
		gbc_show_exp_id.gridx = 2;
		gbc_show_exp_id.gridy = 1;
		panel.add(show_exp_id, gbc_show_exp_id);
		
		JLabel lblResearchLeader = new JLabel("Researcher ID");
		GridBagConstraints gbc_lblResearchLeader = new GridBagConstraints();
		gbc_lblResearchLeader.anchor = GridBagConstraints.EAST;
		gbc_lblResearchLeader.insets = new Insets(0, 0, 5, 5);
		gbc_lblResearchLeader.gridx = 0;
		gbc_lblResearchLeader.gridy = 2;
		panel.add(lblResearchLeader, gbc_lblResearchLeader);
		
		res_id = new JTextField();
		res_id.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(res_id.getText().length() == 0)
				{
					res_id_checked = !show_res_id.isSelected();
					show_res_id.setSelected(false);
					show_res_id.setEnabled(false);
				}
				else
				{
					if(!res_id_checked)
						show_res_id.setSelected(true);
					show_res_id.setEnabled(true);
				}
			}
		});
		GridBagConstraints gbc_res_id = new GridBagConstraints();
		gbc_res_id.insets = new Insets(0, 0, 5, 5);
		gbc_res_id.fill = GridBagConstraints.HORIZONTAL;
		gbc_res_id.gridx = 1;
		gbc_res_id.gridy = 2;
		panel.add(res_id, gbc_res_id);
		res_id.setColumns(10);
		
		show_res_id = new JCheckBox("Include in csv");
		show_res_id.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				res_id_checked = true;
			}
		});
		show_res_id.setEnabled(false);
		show_res_id.setToolTipText("If checked, a column \"red_id\" will appear in the output file after exporting");
		GridBagConstraints gbc_show_res_id = new GridBagConstraints();
		gbc_show_res_id.insets = new Insets(0, 0, 5, 0);
		gbc_show_res_id.gridx = 2;
		gbc_show_res_id.gridy = 2;
		panel.add(show_res_id, gbc_show_res_id);
		
		JLabel lblParticipantId = new JLabel("Participant ID");
		GridBagConstraints gbc_lblParticipantId = new GridBagConstraints();
		gbc_lblParticipantId.anchor = GridBagConstraints.EAST;
		gbc_lblParticipantId.insets = new Insets(0, 0, 5, 5);
		gbc_lblParticipantId.gridx = 0;
		gbc_lblParticipantId.gridy = 3;
		panel.add(lblParticipantId, gbc_lblParticipantId);
		
		pp_id = new JTextField();
		pp_id.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(pp_id.getText().length() == 0)
				{
					pp_id_checked = !show_pp_id.isSelected();
					show_pp_id.setSelected(false);
					show_pp_id.setEnabled(false);
				}
				else
				{
					if(!pp_id_checked)
						show_pp_id.setSelected(true);
					show_pp_id.setEnabled(true);
				}
			}
		});
		GridBagConstraints gbc_pp_id = new GridBagConstraints();
		gbc_pp_id.insets = new Insets(0, 0, 5, 5);
		gbc_pp_id.fill = GridBagConstraints.HORIZONTAL;
		gbc_pp_id.gridx = 1;
		gbc_pp_id.gridy = 3;
		panel.add(pp_id, gbc_pp_id);
		pp_id.setColumns(10);
		
		show_pp_id = new JCheckBox("Include in csv");
		show_pp_id.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pp_id_checked = true;
			}
		});
		show_pp_id.setEnabled(false);
		show_pp_id.setToolTipText("If checked, a column \"pp_id\" will appear in the output file after exporting");
		GridBagConstraints gbc_show_pp_id = new GridBagConstraints();
		gbc_show_pp_id.insets = new Insets(0, 0, 5, 0);
		gbc_show_pp_id.gridx = 2;
		gbc_show_pp_id.gridy = 3;
		panel.add(show_pp_id, gbc_show_pp_id);
		
		lblTimeoutms = new JLabel("Timeout (ms)");
		GridBagConstraints gbc_lblTimeoutms = new GridBagConstraints();
		gbc_lblTimeoutms.anchor = GridBagConstraints.EAST;
		gbc_lblTimeoutms.insets = new Insets(0, 0, 0, 5);
		gbc_lblTimeoutms.gridx = 0;
		gbc_lblTimeoutms.gridy = 4;
		panel.add(lblTimeoutms, gbc_lblTimeoutms);
		
		use_timeout = new JCheckBox("Use timeout   ");
		use_timeout.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				spinner_timeout.setEnabled(use_timeout.isSelected());
			}
		});
		use_timeout.setSelected(Globals.getInstance().getExperimentModel().getUseTimeout());
		
		spinner_timeout = new JSpinner();
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.fill = GridBagConstraints.HORIZONTAL;
		gbc_spinner.insets = new Insets(0, 0, 0, 5);
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 4;
		spinner_timeout.setEnabled(Globals.getInstance().getExperimentModel().getUseTimeout());
		spinner_timeout.setModel(new SpinnerNumberModel());
		spinner_timeout.getModel().setValue(Globals.getInstance().getExperimentModel().getTimeout());
		panel.add(spinner_timeout, gbc_spinner);
		GridBagConstraints gbc_chckbxDontUse = new GridBagConstraints();
		gbc_chckbxDontUse.gridx = 2;
		gbc_chckbxDontUse.gridy = 4;
		panel.add(use_timeout, gbc_chckbxDontUse);
		
		JPanel buttons = new JPanel();
		contentPane.add(buttons, BorderLayout.SOUTH);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.setSettings();
				hide();
			}
		});
		buttons.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hide();
			}
		});
		buttons.add(btnCancel);		
	}
	
	/**
	 * Shows the settings window
	 */
	public void show()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frmExperimentSettings.setVisible(true);
			}
		});
	}
	
	/**
	 * Hides the settings window
	 */
	public void hide()
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frmExperimentSettings.setVisible(false);
			}
		});
	}
	
	/**
	 * Get method for experiment name
	 * @return	Input value of experiment name text box
	 */
	public String getExp_name()
	{
		return exp_name.getText();
	}
	
	/**
	 * Get method for experiment ID
	 * @return	Input value for experiment ID text box
	 */
	public String getExp_id()
	{
		return exp_id.getText();
	}
	
	/**
	 * Get method for researcher ID
	 * @return	Input value for researcher ID text box
	 */
	public String getRes_id()
	{
		return res_id.getText();
	}
	
	/**
	 * Get method for participant ID
	 * @return	Input value for participant ID text box
	 */
	public String getPP_id()
	{
		return pp_id.getText();
	}
	
	public boolean getUseTimeout()
	{
		return use_timeout.isSelected();
	}
	
	public long getTimeout()
	{
		return (Integer) spinner_timeout.getValue();
	}
	
	public boolean getShow_exp_name()
	{
		return show_exp_name.isSelected();
	}
	
	public boolean getShow_exp_id()
	{
		return show_exp_id.isSelected();
	}
	
	public boolean getShow_res_id()
	{
		return show_res_id.isSelected();
	}
	
	public boolean getShow_pp_id()
	{
		return show_pp_id.isSelected();
	}
	
	/**
	 * Sets the settings as provided to the view
	 * @param exp_name			Experiment name
	 * @param exp_id			Experiment ID
	 * @param res_id			Researcher ID
	 * @param pp_id				Participant ID
	 * @param show_exp_name		True iff to show experiment name in csv
	 * @param show_exp_id		True iff to show experiment ID in csv
	 * @param show_res_id		True iff to show researcher ID in csv
	 * @param show_pp_id		True iff to show participant ID in csv
	 */
	public void setSettings(final String exp_name, final String exp_id, final String res_id,
			final String pp_id, final boolean show_exp_name, final boolean show_exp_id,
			final boolean show_res_id, final boolean show_pp_id)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ExperimentSettings.this.exp_name.setText(exp_name);
				ExperimentSettings.this.exp_id.setText(exp_id);
				ExperimentSettings.this.res_id.setText(res_id);
				ExperimentSettings.this.pp_id.setText(pp_id);
				ExperimentSettings.this.show_exp_name
						.setSelected(show_exp_name);
				ExperimentSettings.this.show_exp_id.setSelected(show_exp_id);
				ExperimentSettings.this.show_res_id.setSelected(show_res_id);
				ExperimentSettings.this.show_pp_id.setSelected(show_pp_id);
			}
		});
	}
	
	/**
	 * Sets the timeout settings as provided to the view
	 * @param useTimeout 	Wether or not to use the timeout variable
	 * @param timeout		The number of ms after which a look times out
	 */
	public void setTimeout(final Boolean useTimeout, final Long timeout)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ExperimentSettings.this.use_timeout.setSelected(useTimeout);
				ExperimentSettings.this.spinner_timeout.setValue(timeout
						.intValue());
				ExperimentSettings.this.spinner_timeout.setEnabled(useTimeout);
			}
		});
	}

}

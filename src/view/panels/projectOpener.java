package view.panels;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSeparator;
import javax.swing.JCheckBox;
import javax.swing.border.Border;

import controller.Controller;
import controller.Globals;

import java.awt.Dialog.ModalExclusionType;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.awt.Toolkit;

public class projectOpener extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller c;
	
	private JTextField text_videoURL;
	private JFormattedTextField text_projectName;
	private JTextField text_projectLocation;
	private JTextField text_part_id;
	private JTextField text_res_id;
	private JTextField text_exp_id;
	private JTextField text_exp_name;

	private JLabel lblParticipantId;
	private JLabel lblResearcherId;
	private JLabel lblExperimentId;
	private JLabel lblExperimentName;
	private JLabel lblVideo;
	private JLabel lblProjectLocation;
	private JLabel lblProjectName;

	private JCheckBox include_part_id;
	private JCheckBox include_res_id;
	private JCheckBox include_exp_id;
	private JCheckBox include_exp_name;

	private JButton button_open;
	private JButton button_create;
	private JButton button_cancel;

	private JPanel panel;
	
	private boolean videoSelected = false;
	private boolean nameEntered = false;
	private boolean saveSet = false;
	
	private boolean exp_name_checked, exp_id_checked, res_id_checked, pp_id_checked;
	
	/**
	 * Constructor of the project opener frame
	 */
	public projectOpener(Globals g)
	{
		setIconImage(Toolkit.getDefaultToolkit().getImage(projectOpener.class.getResource("/img/favicon.png")));
		c = g.getController();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setTitle("New Project");
		this.setSize(700, 310);
		this.setLocation(450, 200);
		
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		
		createLabels();
		try {
		createTextFields(); } catch (Exception e) {System.out.println(e);}
		createCheckBoxes();
		createButtons();
		createLayout();
	}
	
	/**
	 * Enables the create button if the required fields are set
	 */
	private void enableCreateButton()
	{
		button_create.setEnabled(videoSelected && nameEntered && saveSet);
	}
	
	/**
	 * Creates the labels
	 */
	private void createLabels()
	{
		lblParticipantId = new JLabel("Participant ID:");
		lblResearcherId = new JLabel("Researcher ID:");
		lblExperimentId = new JLabel("Experiment ID:");
		lblExperimentName = new JLabel("Experiment name:");
		lblVideo = new JLabel("Video: *");
		lblProjectLocation = new JLabel("Project location: *");
		lblProjectName = new JLabel("Project name: *");
	}
	
	/**
	 * Creates the text fields
	 */
	private void createTextFields() throws ParseException
	{
		// Borders
		final Border invalid = BorderFactory.createLineBorder(Color.red);
		
		text_videoURL = new JTextField();
		text_videoURL.setEditable(false);
		text_videoURL.setForeground(Color.GRAY);
		text_videoURL.setText(" Click to select the video you want to code");
		text_videoURL.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String url = VideoSelector.show();
				if(url != null)
				{
					text_videoURL.setForeground(Color.black);
					text_videoURL.setText(url);
					videoSelected = true;
					enableCreateButton();
				}
			}
		});
		
		text_projectName = new JFormattedTextField();
		final Border valid = text_projectName.getBorder();
		
		text_projectName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				String text = text_projectName.getText();
				text = text.replaceFirst("^\\W", "")
						.replaceAll("\\W", "_")
						.replaceAll("_{2,}", "_");
				text_projectName.setText(text);
				nameEntered = text.length() >= 5 && text.length() <= 55;
				text_projectName.setBorder(nameEntered ? valid : invalid);
				enableCreateButton();
			}
		});
		
		text_projectLocation = new JTextField();
		text_projectLocation.setEditable(false);
		text_projectLocation.setForeground(Color.GRAY);
		text_projectLocation.setText(" Click to select a directory where the project should be saved");
		
		text_projectLocation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String text = SaveDialog.show();
				if (text != null)
				{
					text_projectLocation.setForeground(Color.black);
					text_projectLocation.setText(text);
					saveSet = true;
					enableCreateButton();
				}
			}
		});
		
		text_part_id = new JTextField();
		text_part_id.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(text_part_id.getText().length() == 0)
				{
					pp_id_checked = !include_part_id.isSelected();
					include_part_id.setSelected(false);
					include_part_id.setEnabled(false);
				}
				else
				{
					if(!pp_id_checked)
						include_part_id.setSelected(true);
					include_part_id.setEnabled(true);
				}
			}
		});
		
		text_res_id = new JTextField();
		text_res_id.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(text_res_id.getText().length() == 0)
				{
					res_id_checked = !include_res_id.isSelected();
					include_res_id.setSelected(false);
					include_res_id.setEnabled(false);
				}
				else
				{
					if(!res_id_checked)
						include_res_id.setSelected(true);
					include_res_id.setEnabled(true);
				}
			}
		});
		
		text_exp_id = new JTextField();
		text_exp_id.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(text_exp_id.getText().length() == 0)
				{
					exp_id_checked = !include_exp_id.isSelected();
					include_exp_id.setSelected(false);
					include_exp_id.setEnabled(false);
				}
				else
				{
					if(!exp_id_checked)
						include_exp_id.setSelected(true);
					include_exp_id.setEnabled(true);
				}
			}
		});
		
		text_exp_name = new JTextField();
		text_exp_name.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(text_exp_name.getText().length() == 0)
				{
					exp_name_checked = !include_exp_name.isSelected();
					include_exp_name.setSelected(false);
					include_exp_name.setEnabled(false);
				}
				else
				{
					if(!exp_name_checked)
						include_exp_name.setSelected(true);
					include_exp_name.setEnabled(true);
				}
			}
		});
	}
	
	/**
	 * Creates the checkboxes
	 */
	private void createCheckBoxes()
	{
		include_part_id = new JCheckBox("Include in csv");
		include_part_id.setEnabled(false);
		include_part_id.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				pp_id_checked = true;
			}
		});
		
		include_res_id = new JCheckBox("Include in csv");
		include_res_id.setEnabled(false);
		include_res_id.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				res_id_checked = true;
			}
		});
		
		include_exp_id = new JCheckBox("Include in csv");
		include_exp_id.setEnabled(false);
		include_exp_id.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				exp_id_checked = true;
			}
		});
		
		include_exp_name = new JCheckBox("Include in csv");
		include_exp_name.setEnabled(false);
		include_exp_name.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				exp_name_checked = true;
			}
		});
		
	}
	
	/**
	 * Creates the buttons
	 */
	private void createButtons()
	{
		button_open = new JButton("Open existing");
		button_open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				String url = view.panels.ProjectSelector.show();
				
				if (url != null)
				{
					if(c.open(url))
					{
						dispose();
					}
					else {
						JOptionPane.showMessageDialog(new JPanel(), "Sorry! It looks like the project couldn't be opened", "Opening project failed", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		button_create = new JButton("Create new project");
		button_create.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				String saveURL = String.format(
						"%s\\%s", 
						text_projectLocation.getText(),
						text_projectName.getText()
					);
				
				if(new java.io.File(
						saveURL + ".UiL").exists())
				{
					if(JOptionPane.showConfirmDialog(
							null, 
							"This file already exists. Continuing will overwrite this file permanently. "
							+ "Are you sure you want to continue?", "This file alreay exists", 
							JOptionPane.YES_NO_OPTION) == 1)
					{
						return;
					}
				}
				
				Globals g = Globals.getInstance();
				
				// Save the settings
				g.getExperimentModel().setSettings(
						text_exp_name.getText(), text_exp_id.getText(), 
						text_res_id.getText(), text_part_id.getText(), 
						include_exp_name.isSelected(), include_exp_id.isSelected(), 
						include_res_id.isSelected(), include_part_id.isSelected()
					);
				
				c.setVideo(text_videoURL.getText());
				
				g.getExperimentModel().setSaveURL(saveURL);
				
				c.save();
				
				dispose();
			}
		});
		button_create.setEnabled(false);
		
		button_cancel = new JButton("Cancel");
		button_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}
	
	private void createLayout() {
		JSeparator separator = new JSeparator();
		JSeparator separator_1 = new JSeparator();
		
		JLabel lblIndicatesA = new JLabel("* indicates a required field");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(separator, GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(separator_1, GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(59)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblParticipantId)
								.addComponent(lblResearcherId)
								.addComponent(lblExperimentId)
								.addComponent(lblExperimentName)
								.addComponent(lblVideo)
								.addComponent(lblProjectLocation)
								.addComponent(lblProjectName)
								.addComponent(lblIndicatesA))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(text_videoURL)
										.addComponent(text_projectName, GroupLayout.PREFERRED_SIZE, 432, GroupLayout.PREFERRED_SIZE)
										.addComponent(text_projectLocation, GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE))
									.addGroup(gl_panel.createSequentialGroup()
										.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING, false)
											.addComponent(text_part_id, Alignment.LEADING)
											.addComponent(text_res_id, Alignment.LEADING)
											.addComponent(text_exp_id, Alignment.LEADING)
											.addComponent(text_exp_name, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
											.addComponent(include_part_id)
											.addComponent(include_res_id)
											.addComponent(include_exp_id)
											.addComponent(include_exp_name))))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(button_create)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(button_open)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(button_cancel)))))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProjectName)
						.addComponent(text_projectName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProjectLocation)
						.addComponent(text_projectLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 3, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblVideo)
						.addComponent(text_videoURL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 8, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblExperimentName)
						.addComponent(text_exp_name, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(include_exp_name))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblExperimentId)
						.addComponent(text_exp_id, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(include_exp_id))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblResearcherId)
						.addComponent(text_res_id, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(include_res_id))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblParticipantId)
						.addComponent(text_part_id, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(include_part_id))
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(button_cancel)
								.addComponent(button_create)
								.addComponent(button_open)))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(18)
							.addComponent(lblIndicatesA)))
					.addContainerGap(14, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
	}

	
}

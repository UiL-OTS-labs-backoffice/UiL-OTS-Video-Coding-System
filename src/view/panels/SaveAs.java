package view.panels;

import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.Border;
import javax.swing.JTextField;
import javax.swing.JButton;

import controller.Globals;

public class SaveAs extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static SaveAs instance;
	
	private JTextField text_projectName;
	private JTextField text_projectLocation;
	private JButton btnSave;
	private JButton btnCancel;
	private JLabel lblProjectName;
	private JLabel lblProjectLocation;
	
	private boolean nameEntered = false;
	private boolean saveSet = false;
	
	public SaveAs() {
		setIconImages(Globals.getIcons());
		instance = this;
		createLayout();
		try {
			createTextFields();
		} catch (Exception e) {
			e.printStackTrace();
		}
		createButtons();
		createGroups();
	}
	
	private void createLayout()
	{
		setIconImages(Globals.getIcons());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		
		setTitle("Save project as");
		
		this.setSize(500, 210);
		this.setLocation(450, 200);
		
		lblProjectName = new JLabel("Project Name");
		lblProjectLocation = new JLabel("Project Location");
	}
	
	private void createTextFields()
	{
		// Borders
		final Border invalid = BorderFactory.createLineBorder(Color.red);
				
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
				enableSaveButton();
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
					enableSaveButton();
				}
			}
		});
	}
	
	private void createButtons()
	{
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				
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
						instance.dispose();
					}
				}
				
				Globals g = Globals.getInstance();
				g.getExperimentModel().setSaveURL(saveURL);
				g.getController().save();
				instance.dispose();
			}
		});
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				instance.dispose();
			}
			
		});
	}
	
	private void enableSaveButton()
	{
		btnSave.setEnabled(nameEntered && saveSet);
	}
	
	private void createGroups()
	{
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblProjectName)
						.addComponent(lblProjectLocation))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnSave)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnCancel))
						.addComponent(text_projectLocation)
						.addComponent(text_projectName, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
					.addContainerGap(56, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(32)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProjectName)
						.addComponent(text_projectName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblProjectLocation)
						.addComponent(text_projectLocation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSave)
						.addComponent(btnCancel))
					.addGap(26))
		);
		getContentPane().setLayout(groupLayout);
	}
}

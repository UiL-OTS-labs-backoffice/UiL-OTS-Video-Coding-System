package view.panels;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import view.event.ChangeKeyListener;
import controller.Controller;
import controller.Globals;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.LinkedList;

public class QuickKeysPanel extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * GridbagLayout
	 */
	GridBagLayout gridBagLayout;
	
	/**
	 * Default row height
	 */
	private static final int ROW_HEIGHT = 30;
	
	/**
	 * List of fields
	 */
	List<Field> fields = new LinkedList<Field>();
	
	/**
	 * Controller reference
	 */
	private Controller c;
	
	private JTextField currentSelection;

	/**
	 * Constructor
	 */
	public QuickKeysPanel() {
		c = Globals.getInstance().getController();
		
		// Frame options
		setTitle("Quick key commands");
		setResizable(false);
		setIconImages(Globals.getIcons());
		
		// Add content
		createLayout();
		addActions();
	}
	
	/**
	 * Update method to update the hot key codes
	 */
	public void update()
	{
		for(Field field : fields)
		{
			field.getField().setText(c.codeToString(field.getID()));
		}
	}
	
	private void removeCurrentSelection(){
		if(this.currentSelection != null){
			currentSelection.setFocusable(false);
			currentSelection.setBackground(null);
			currentSelection = null;
		}
	}
	
	private void select(JTextField field){
		if(field != this.currentSelection) {
			removeCurrentSelection();
			currentSelection = field;
		}
		field.setFocusable(true);
		field.requestFocusInWindow();
	}
	
	/**
	 * Creates the grid bag layout and adds the header labels to the frame
	 */
	private void createLayout()
	{
		// Grid bag layout
		// Ensures fit
		
		gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{ROW_HEIGHT};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		
		/*
		 * Header labels in bold 
		 */
		
		// Action label
		JLabel lblAction = new JLabel("Action", SwingConstants.RIGHT);
		lblAction.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblAction = new GridBagConstraints();
		gbc_lblAction.insets = new Insets(0, 0, 0, 5);
		gbc_lblAction.gridx = 0;
		gbc_lblAction.gridy = 0;
		gbc_lblAction.fill = 2;
		getContentPane().add(lblAction, gbc_lblAction);
		
		// Assignment label
		JLabel lblKeyAssignment = new JLabel("Key Assignment", 2);
		lblKeyAssignment.setFont(new Font("Tahoma", Font.BOLD, 14));
		GridBagConstraints gbc_lblKeyAssignment = new GridBagConstraints();
		gbc_lblKeyAssignment.insets = new Insets(0, 30, 0, 5);
		gbc_lblKeyAssignment.gridx = 1;
		gbc_lblKeyAssignment.gridy = 0;
		gbc_lblKeyAssignment.fill = 2;
		getContentPane().add(lblKeyAssignment, gbc_lblKeyAssignment);
		
		this.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				removeCurrentSelection();
			}
			@Override
			public void mousePressed(MouseEvent e) { }
			@Override
			public void mouseReleased(MouseEvent e) { }
			@Override
			public void mouseEntered(MouseEvent e) { }
			@Override
			public void mouseExited(MouseEvent e) { }
			
		});
	}
	
	// Add the actions to the layout
	private void addActions()
	{
		
		Collection<String> actions = c.getActions();
		
		// Set height based on number of rows
		setBounds(600, 200, 400, (actions.size() + 3) * ROW_HEIGHT);
		
		int row = 1;
		
		for (String action : actions)
		{
			// Action label
			JLabel lblActionName = new JLabel(c.getName(action), SwingConstants.RIGHT);
			GridBagConstraints gbc_lblActionName = new GridBagConstraints();
			gbc_lblActionName.insets = new Insets(0, 0, 0, 5);
			gbc_lblActionName.gridx = 0;
			gbc_lblActionName.gridy = row;
			gbc_lblActionName.fill = 2;
			getContentPane().add(lblActionName, gbc_lblActionName);
			
			// Key Label 
			final JTextField txtKey = new JTextField(c.codeToString(action));
			fields.add(new Field(txtKey, action));
			txtKey.setHorizontalAlignment(SwingConstants.CENTER);
			txtKey.setEditable(false);
			txtKey.setFocusable(false);
			txtKey.addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent e) {
					select(txtKey);
				}

				@Override
				public void mouseEntered(MouseEvent e) { }

				@Override
				public void mouseExited(MouseEvent e) { }

				@Override
				public void mousePressed(MouseEvent e) { }
				@Override
				public void mouseReleased(MouseEvent e) { }
			});
			
			txtKey.addFocusListener(new ChangeKeyListener(txtKey, action, this));
			
			GridBagConstraints gbc_lblKey = new GridBagConstraints();
			gbc_lblKey.insets = new Insets(0, 30, 0, 5);
			gbc_lblKey.gridx = 1;
			gbc_lblKey.gridy = row;
			gbc_lblKey.fill = 2;
			getContentPane().add(txtKey, gbc_lblKey);
			
			addRowToGridbagLayout();
			
			row++;
		}
	}
	
	private void addRowToGridbagLayout()
	{
		int[] rows = new int[gridBagLayout.rowHeights.length + 1];
		for(int i = 0; i < rows.length-1; i++)
			rows[i] = gridBagLayout.rowHeights[i];
		rows[rows.length-1] = ROW_HEIGHT;
		gridBagLayout.rowHeights = rows;
	}
	
	/**
	 * Tuple class for text field and hot key ID
	 */
	private class Field
	{
		/**
		 * Fields
		 */
		private JTextField field;
		private String ID;
		
		/**
		 * Constructor for field
		 * @param field		JTextField
		 * @param ID		Hot key ID
		 */
		public Field(JTextField field, String ID)
		{
			this.field = field;
			this.ID = ID;
		}
		
		/**
		 * Getter for the field
		 * @return	JTextField
		 */
		public JTextField getField()
		{
			return field;
		}
		
		/**
		 * Getter for ID
		 * @return	Hot key ID for field
		 */
		public String getID()
		{
			return ID;
		}
	}

}

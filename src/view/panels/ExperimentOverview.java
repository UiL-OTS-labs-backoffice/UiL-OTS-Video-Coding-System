package view.panels;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import controller.*;

public class ExperimentOverview extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Controller c;
	
	// Linked list of all the trials
	private LinkedList<model.Trial> trials;
	
	private static final int COLUMNS = 5;
	private static final int COL_WIDTH = 50;
	
	private static final Color HEADER_COLOR = new Color(79,129,189);
	private static final Color ROW1_COLOR = new Color(220, 230, 241);
	private static final Color ROW2_COLOR = new Color(255,255,255);
	
	private GridBagLayout gridBagLayout;
	
	private JPanel container;

	public ExperimentOverview() {
		c = Globals.getInstance().getController();
		this.trials = c.getTrials();
		setTitle("Experiment Overview");
		setType(Type.UTILITY);
		setResizable(false);
		this.setBounds(300, 200, 700, 500);
		createLayout();
		addOverview();
	}
	
	/**
	 * Creates the gridbaglayout and adds it to the frame
	 */
	private void createLayout()
	{
		container = new JPanel();
		JScrollPane scrPane = new JScrollPane(container,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrPane, BorderLayout.CENTER);
		
		gridBagLayout = new GridBagLayout();
		int[] columnWidths = new int[COLUMNS];
		for(int i = 0; i < COLUMNS; i++)
		{
			columnWidths[i] = COL_WIDTH;
		}
		gridBagLayout.columnWidths = columnWidths;
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		
		GridBagConstraints layoutConstraint = new GridBagConstraints();
		layoutConstraint.anchor = GridBagConstraints.NORTH;
		layoutConstraint.fill = GridBagConstraints.NONE;
		layoutConstraint.weighty = 1d;
		
		container.setLayout(gridBagLayout);
		
		JLabel lblH1 = new JLabel("Trial");
		JLabel lblH2 = new JLabel("Look");
		JLabel lblH3 = new JLabel("Begin time");
		JLabel lblH4 = new JLabel("End time");
		JLabel lblH5 = new JLabel("Total look time");
		
		LinkedList<JLabel> labels = new LinkedList<JLabel>(Arrays.asList(lblH1, lblH2, lblH3, lblH4, lblH5));
		
		int col = 0;
		for(JLabel l : labels)
		{
			l.setFont(new Font("Tahoma", Font.BOLD, 18));
			l.setHorizontalAlignment(SwingConstants.LEFT);
			GridBagConstraints c = new GridBagConstraints();
			c.insets = new Insets(0,0,0,0);
			c.gridx = col;
			c.gridy = 0;
			c.anchor = GridBagConstraints.NORTH;
			c.weighty = 1;
			c.fill = 2;
			container.add(l, c);
			col++;
		}
		
	}
	
	/**
	 * Add all the trials and look
	 */
	private void addOverview()
	{
		int row = 1;
		int trial = 1;
		for(model.Trial t : trials)
		{
			row = addSeparator(row);
			
			addLabel("Trial " + trial, row, 0, HEADER_COLOR);
			addLabel(" ", row, 1, HEADER_COLOR);
			addLabel(timeToString(t.getBeginTime()), row, 2, HEADER_COLOR);
			addLabel(timeToString(t.getEndTime()), row, 3, HEADER_COLOR);
			addLabel(t.getTotalLookTime() + " ms", row, 4, HEADER_COLOR);
			row++;
			int look = 1;
			for(model.Look l : t.getLooks())
			{
				Color color = (row % 2 == 0) ? ROW1_COLOR : ROW2_COLOR;
				addLabel(" ", row, 0, color);
				addLabel("Look " + look, row, 1, color);
				addLabel(timeToString(l.getBeginTime()), row, 2, color);
				addLabel(timeToString(l.getEndTime()), row, 3, color);
				addLabel(l.getDuration() + " ms", row, 4, color);
				row++;
				look++;
			}
			
			trial++;
		}
	}
	
	/**
	 * Adds a separator to the requested row and returns the next
	 * writable row
	 * @param row	Row number where separator should go
	 * @return		row + 1
	 */
	private int addSeparator(int row)
	{
		JSeparator s = new JSeparator();
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = row;
		c.weightx = 1;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5,0,5,0);
		container.add(s, c);
		return row + 1;
	}
	
	/**
	 * Converts a long of milliseconds into a String
	 * @param	time	The timestamp in milliseconds
	 * @return	String		Readable time stamp
	 */
	private String timeToString(long time)
	{
		if(time == -1L)
				return "--";
		String s = String.format("%02d:%02d:%02d.%03d", 
        		TimeUnit.MILLISECONDS.toHours(time), 
        		
        		TimeUnit.MILLISECONDS.toMinutes(time) - 
        		TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)), 
        		
        		TimeUnit.MILLISECONDS.toSeconds(time) - 
        		TimeUnit.MINUTES.toSeconds(
        				TimeUnit.MILLISECONDS.toMinutes(time)
        		),
        		time - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(time))
        	);
		return s;
	}
	
	/**
	 * Method to quickly add a label
	 * @param title		The text of the label
	 * @param row		The row number where the label should be added
	 * @param col		The column number where the label should be added
	 */
	private void addLabel(String title, int row, int col, Color color)
	{
		JLabel l = new JLabel(title);
		l.setBackground(color);
		if(color == HEADER_COLOR)
		{
			// Title row
			l.setFont(new Font("Tahoma", Font.BOLD, 14));
			l.setForeground(ROW2_COLOR);
		}
		l.setOpaque(true);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = col;
		c.gridy = row;
		c.anchor = GridBagConstraints.NORTH;
		c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		container.add(l, c);
	}
	

}

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
	
	private static final int COLUMNS = 5;
	private static final int COL_WIDTH = 50;
	
	private static final Color HEADER_COLOR = new Color(79,129,189);
	private static final Color[] ROW1_COLOR = {new Color(220, 230, 241), new Color(255,191,191)};
	private static final Color[] ROW2_COLOR = {new Color(255,255,255),new Color(250,215,215)};
	
	private GridBagLayout gridBagLayout;
	
	private JPanel container;

	public ExperimentOverview() {
		c = Globals.getInstance().getController();
		setTitle("Experiment Overview");
		setIconImages(Globals.getIcons());
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
		setIconImages(Globals.getIcons());
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
		Color[] row_color = ROW1_COLOR;
		
		for(int i = 1; i <= c.getNumberOfTrials(); i++)
		{
			model.Trial t = c.getTrial(i);
			
			addSeparator(row);
			row++;
			
			addLabel("Trial " + i, row, 0, HEADER_COLOR);
			addLabel(" ", row, 1, HEADER_COLOR);
			addLabel(timeToString(t.getBegin()), row, 2, HEADER_COLOR);
			addLabel(timeToString(t.getEnd()), row, 3, HEADER_COLOR);
			addLabel(t.getTotalTimeForItems() + " ms", row, 4, HEADER_COLOR);
			row++;
			if(t.getComment() != null && t.getComment().length() > 0)
			{
				addComment(t.getComment(), row, HEADER_COLOR);
				row++;
			}
			
			model.Experiment em = Globals.getInstance().getExperimentModel();
			long last_end = -1;
			for(int j = 1; j <= t.getNumberOfItems(); j++)
			{
				model.Look l = (model.Look) t.getItem(j);
				int index = (!em.getUseTimeout() || l.getBegin() - last_end < em.getTimeout() || last_end < 0) ? 0 : 1;
				last_end = l.getEnd();
				Color color = row_color[index];
				
				addLabel(" ", row, 0, color);
				addLabel("Look " + j, row, 1, color);
				addLabel(timeToString(l.getBegin()), row, 2, color);
				addLabel(timeToString(l.getEnd()), row, 3, color);
				addLabel(l.getDuration() + " ms", row, 4, color);
				row++;
				
				if(l.getComment() != null && l.getComment().length() > 0)
				{
					addComment(l.getComment(), row, color);
					row++;
				}
				
				row_color = (row_color == ROW1_COLOR) ? ROW2_COLOR : ROW1_COLOR;
			}
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
			l.setForeground(ROW2_COLOR[0]);
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
	
	private void addComment(String comment, int row, Color color)
	{
		JLabel l = new JLabel(String.format("<html><body><p style=\"width: 500px; margin-left: 10px; margin-right: 10px;\">%s</p></body></html>", comment));
		l.setBackground(color);
		if(color == HEADER_COLOR)
		{
			l.setForeground(ROW2_COLOR[0]);
		}
		l.setOpaque(true);
		GridBagConstraints c = new GridBagConstraints();
		c.gridwidth = COLUMNS;
		c.gridx = 0;
		c.gridy = row;
		c.anchor = GridBagConstraints.NORTH;
		c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		container.add(l, c);
	}
	

}

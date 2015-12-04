package view.panels;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Arrays;

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
	
	private JPanel container;

	public ExperimentOverview() {
		c = Globals.getInstance().getController();
		setTitle("Experiment Overview");
		setIconImages(Globals.getIcons());
		setResizable(false);
		this.setBounds(300, 200, 700, 500);
		createLayout();
		addOverview();
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowListener(){

			@Override
			public void windowOpened(WindowEvent e) { }

			@Override
			public void windowClosing(WindowEvent e) { }

			@Override
			public void windowClosed(WindowEvent e) { 
				Globals.getInstance().disposeExperimentOverview();
			}

			@Override
			public void windowIconified(WindowEvent e) { }

			@Override
			public void windowDeiconified(WindowEvent e) { }

			@Override
			public void windowActivated(WindowEvent e) { }

			@Override
			public void windowDeactivated(WindowEvent e) { }
			
		});
	}
	
	
	/**
	 * Creates the grid bag layout and adds it to the frame
	 */
	private void createLayout()
	{
		setIconImages(Globals.getIcons());
		container = new JPanel();
		JScrollPane scrPane = new JScrollPane(container,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrPane, BorderLayout.CENTER);
		
		String layout = "";
		for(int i = 0; i < COLUMNS; i++)
		{
			layout += String.format("[%d,grow,fill]", COL_WIDTH);
		}
		
		MigLayout migLayout = new MigLayout("gap 0 0",layout);
		container.setLayout(migLayout);
		
		JLabel lblH1 = new JLabel("Trial");
		JLabel lblH2 = new JLabel("Look");
		JLabel lblH3 = new JLabel("Begin time");
		JLabel lblH4 = new JLabel("End time");
		JLabel lblH5 = new JLabel("Total look time");
		
		ArrayList<JLabel> labels = new ArrayList<JLabel>(Arrays.asList(lblH1, lblH2, lblH3, lblH4, lblH5));
		
		int col = 0;
		for(JLabel l : labels)
		{
			l.setFont(new Font("Tahoma", Font.BOLD, 18));
			l.setHorizontalAlignment(SwingConstants.LEFT);
			container.add(l, String.format("cell %d 0", col));
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
			addLabel(view.formatter.Time.formatDetail(t.getBegin()), row, 2, HEADER_COLOR);
			addLabel(view.formatter.Time.formatDetail(t.getEnd()), row, 3, HEADER_COLOR);
			addLabel(t.getTotalTimeForItems() + " ms", row, 4, HEADER_COLOR);
			row++;
			if(t.getComment() != null && t.getComment().length() > 0)
			{
				addComment(t.getComment(), row, HEADER_COLOR);
				row++;
			}
			
			for(int j = 1; j <= t.getNumberOfItems(); j++)
			{
				model.Look l = (model.Look) t.getItem(j);
				int index = t.getTimeout() >= 1 && t.getTimeout() <= l.getBegin() ? 1 : 0;
				Color color = row_color[index];
				
				addLabel(" ", row, 0, color);
				addLabel("Look " + j, row, 1, color);
				addLabel(view.formatter.Time.formatDetail(l.getBegin()), row, 2, color);
				addLabel(view.formatter.Time.formatDetail(l.getEnd()), row, 3, color);
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
		container.add(s, String.format("gap 0 0 5 0, cell 0 %d %d 1", row, COLUMNS));
		return row + 1;
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
		container.add(l, String.format("cell %d %d", col, row));
		
	}
	
	/**
	 * Method to add a comment row
	 * @param comment		The comment for the row
	 * @param row			The row at which t insert the comment
	 * @param color			The background color of the row
	 */
	private void addComment(String comment, int row, Color color)
	{
		JLabel l = new JLabel(String.format("<html><body><p style=\"margin-right: 10px;\">%s</p></body></html>", comment));
		l.setBackground(color);
		
		JLabel commentLabel = new JLabel(" ");
		commentLabel.setBackground(color);
		
		if(color == HEADER_COLOR)
		{
			l.setForeground(ROW2_COLOR[0]);
		}
		l.setOpaque(true);
		commentLabel.setOpaque(true);
		
		container.add(commentLabel, String.format("cell 0 %d 2 1", row));
		container.add(l, String.format("cell %d %d %d 1", 2, row, COLUMNS - 2));
	}
	

}

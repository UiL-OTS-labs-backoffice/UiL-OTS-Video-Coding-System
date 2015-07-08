package view.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSpinner;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import controller.Globals;
import controller.IVideoControls;


public class TimeSelector extends JFrame {
	
	private IVideoControls vc;
	
	private static long video_duration;
	private long current_time;
	private int[] devisions = {3600000, 60000, 1000, 1};
	private int[] maxes = {0, 60, 60, 1000};
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JSpinner hour_spinner;
	private JSpinner minute_spinner;
	private JLabel lblMs;
	private JLabel lblS;
	private JLabel lblM;
	private JLabel lblH;
	private JSpinner millisecond_spinner;
	private JSpinner second_spinner;

	private JButton btnGo;
	
	public TimeSelector(long current_time) {
		this.current_time = current_time;
		
		vc = Globals.getInstance().getVideoController();
		video_duration = vc.getMediaDuration();
		
		// Frame options
		setTitle("Go to time");
		setResizable(false);
		setIconImages(Globals.getIcons());
		setSize(250, 140);
		
		this.setLocationByPlatform(true);
		
		createElements();
		createLayout();
		
		this.setVisible(true);
	}
	
	private SpinnerModel[] getSpinnerModels()
	{
		SpinnerModel[] spinnermodel = new SpinnerModel[4];
		
		long ct = current_time;
		for(int i = 0; i < 4; i++)
		{			
			int max = (int) Math.floor(video_duration / devisions[i]);
			max = max < maxes[i] || max == 0 ? max : maxes[i];
			int cur = 0;
			while(ct > devisions[i])
			{
				ct -= devisions[i];
				cur++;
			}
			spinnermodel[i] = new SpinnerNumberModel(cur,0, max, 1);
		}
		
		return spinnermodel;
	}
	
	private long getSelectedTime()
	{
		long t = 0;
		t += ((Integer) (hour_spinner.getValue()) * devisions[0]);
		t += ((Integer) (minute_spinner.getValue()) * devisions[1]);
		t += ((Integer) (second_spinner.getValue()) * devisions[2]);
		t += ((Integer) (millisecond_spinner.getValue()) * devisions[3]);
		
		return t;
	}
	
	private void createElements()
	{
		SpinnerModel[] models = getSpinnerModels();
		
		hour_spinner = new JSpinner(models[0]);
		minute_spinner = new JSpinner(models[1]);
		second_spinner = new JSpinner(models[2]);
		millisecond_spinner = new JSpinner(models[3]);
		
		lblH = new JLabel("H");
		lblM = new JLabel("M");
		lblS = new JLabel("S");
		lblMs = new JLabel("mS");
		
		btnGo = new JButton("Go to time");
		btnGo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				vc.setMediaTime(getSelectedTime());				
			}
			
		});
	}
	
	private void createLayout()
	{
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(hour_spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(minute_spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(second_spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(11)
									.addComponent(lblH)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(lblM)
									.addGap(32)
									.addComponent(lblS)
									.addGap(12)))
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(millisecond_spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(17)
									.addComponent(lblMs))))
						.addComponent(btnGo))
					.addContainerGap(58, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(lblMs)
						.addComponent(lblS)
						.addComponent(lblM)
						.addComponent(lblH))
					.addGap(6)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(hour_spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(minute_spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(second_spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(millisecond_spinner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnGo)
					.addContainerGap(27, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
	}

	
}

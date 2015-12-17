package controller.export;

import view.formatter.Time;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;




import controller.Globals;
import model.*;

public class CSVExport {
	
	Experiment exp;
	
	public CSVExport()
	{
		exp = Globals.getInstance().getExperimentModel();
	}
	
	public boolean exportTotalLookTime(String filename)
	{
		ArrayList<String> list = prepareList();
		return this.writeCsv(filename, list);
	}
	
	public boolean exportExtendedInformation(String filename)
	{
		ArrayList<String> list = prepareFullList();
		return this.writeCsv(filename, list);
	}
	
	/**
	 * Prepares a linked list of linked list that can be written to a 
	 * csv file easily
	 * @return	Linked list of linked list containing csv information
	 */
	private ArrayList<String> prepareList()
	{
		String expName = (exp.getShow_exp_name()) ? exp.getExp_name() : null;
		String expID = (exp.getShow_exp_id()) ? exp.getExp_id() : null;
		String resID = (exp.getShow_res_id()) ? exp.getRes_id() : null;
		String ppID = (exp.getShow_pp_id()) ? exp.getPP_id() : null;
		
		ArrayList<String> csv = new ArrayList<String>();
		
		String header = "";
		if (expName != null) header += "\"Experiment Name\";";
		if (expID != null) header += "\"Experiment ID\";";
		if (resID != null) header += "\"Researcher ID\";";
		if (ppID != null) header += "\"Participant ID\";";
		header += "\"Trial\";\"Number of Looks\";\"Total looktime (ms)\";\"Comment\"";
		
		csv.add(header);
		
		LinkedList<AbstractTimeFrame> trials = exp.getItems();
		for(int t = 0; t < trials.size(); t++)
		{
			String row = "";
			
			AbstractTimeContainer trial = (AbstractTimeContainer) trials.get(t);
			
			if (expName != null) row += expName + ";";
			if (expID != null) row += expID + ";";
			if (resID != null) row += resID + ";";
			if (ppID != null) row += ppID + ";";
			
			row += Integer.toString(t+1) + ";";
			row += Integer.toString(trial.getNumberOfItems()) + ";";
			row += Long.toString(trial.getTotalTimeForItems()) + ";";
			row += (trial.getComment() != null && trial.getComment().length() > 0) ? trial.getComment() : "";
			
			csv.add(row);
		}
		
		return csv;
	}
	
	private ArrayList<String> prepareFullList()
	{
		model.Experiment em = Globals.getInstance().getExperimentModel();
		String expName = (exp.getShow_exp_name()) ? exp.getExp_name() : null;
		String expID = (exp.getShow_exp_id()) ? exp.getExp_id() : null;
		String resID = (exp.getShow_res_id()) ? exp.getRes_id() : null;
		String ppID = (exp.getShow_pp_id()) ? exp.getPP_id() : null;
		
		ArrayList<String> csv = new ArrayList<String>();
		
		String header = "";
		if (expName != null) header += "\"Experiment Name\";";
		if (expID != null) header += "\"Experiment ID\";";
		if (resID != null) header += "\"Researcher ID\";";
		if (ppID != null) header += "\"Participant ID\";";
		header += String.format("\"%s\";\"%s\";\"%s\";\"%s\";\"%s\";\"%s\"",
				"Trial", "Look", "Begintime", "Endtime", "(Total) Looktime (ms)", "Comment");
		
		if(em.getUseTimeout())
		{
			header += ";\"Look validity\"";
		}
		
		csv.add(header);
		
		LinkedList<AbstractTimeFrame> trials = exp.getItems();
		for(int t = 0; t < trials.size(); t++)
		{
			String row = "";
			
			AbstractTimeContainer trial = (AbstractTimeContainer) trials.get(t);
			
			if (expName != null) row += expName + ";";
			if (expID != null) row += expID + ";";
			if (resID != null) row += resID + ";";
			if (ppID != null) row += ppID + ";";
			
			// Trial
			row += Integer.toString(t+1) + ";";
			
			// Look
			row += ";";
			
			// begintime
			row += Time.format(trial.getBegin()) + ";";
			
			// endtime
			row += Time.format(trial.getEnd()) + ";";
			
			// Total look time
			row += Long.toString(trial.getTotalTimeForItems()) + ";";
			
			row += (trial.getComment() != null && trial.getComment().length() > 0) ? trial.getComment() : "";
			
			// Look validity
			if(em.getUseTimeout())
			{
				row += ";";
			}
			
			csv.add(row);
			
			LinkedList<AbstractTimeFrame> looks = trial.getItems();
			
			
			long last_end = -1;
			for(int l = 0; l < looks.size(); l++)
			{
				AbstractTimeFrame look = (AbstractTimeFrame) looks.get(l);
				
				row = "";
				
				if (expName != null) row += expName + ";";
				if (expID != null) row += expID + ";";
				if (resID != null) row += resID + ";";
				if (ppID != null) row += ppID + ";";
				
				// Trial
				row += Integer.toString(t+1) + ";";
				
				// Look
				row += Integer.toString(l+1) + ";";
				
				// begintime
				row += Time.format(look.getBegin()) + ";";
				
				// endtime
				row += Time.format(look.getEnd()) + ";";
				
				// Total look time
				row += Long.toString(look.getDuration()) + ";";
				
				row += (look.getComment() != null && look.getComment().length() > 0) ? look.getComment() : "";
				
				if(em.getUseTimeout())
				{
					row += (look.getBegin() - last_end < em.getTimeout() || last_end < 0) ? ";good" : ";timeout";
					last_end = look.getEnd();
				}
				
				csv.add(row);
				
			}
		}
		
		return csv;
	}
	
	/**
	 * Exports the experiment to a csv
	 * @param filename
	 * @return	True if successful
	 */
	private boolean writeCsv(String filename, ArrayList<String> list)
	{
//		ArrayList<String> list = prepareList();
		
		try
		{
			FileWriter writer = new FileWriter(filename);
			
			for(String row: list)
			{
				writer.append(row + "\n");
			}
			
			writer.flush();
			writer.close();
			
			return true;
		}
		catch(IOException e)
		{
			return false;
		}
	}

}

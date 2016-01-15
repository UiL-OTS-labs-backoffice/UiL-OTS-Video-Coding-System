package controller.serializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Experiment;
import model.TimeObserver.IExperimentListener;
import controller.Globals;

public class Serializer {
	
	public static boolean save(){
		Experiment exp = Globals.getInstance().getExperimentModel();
		String uri = getFullPath(exp.getSaveURL(), exp.getSaveName());
		if(writeExperimentModel(uri)){
			exp.saved();
			exp.isBackedUp();
			removePreviousAutosavesForExperiment();
			return true;
		} else return false;
	}
	
	public static boolean backup(){
		Experiment exp = Globals.getInstance().getExperimentModel();
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
		String uri = getFullPath(
				Globals.getBackupLocation().getAbsolutePath(), 
				String.format("~%s_%s", d.format(new Date()), exp.getSaveName())
			);
		if(writeExperimentModel(uri)){
			exp.isBackedUp();
			return true;
		} else return false;
	}
	
	public static boolean writeExperimentModel(String uri)
	{
		System.out.println("Saving to " + uri);
		try{
			FileOutputStream fout = new FileOutputStream(uri);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(Globals.getInstance().getExperimentModel());
			oos.close();
			return true;
			
		} catch (Exception e)
		{
			JOptionPane.showMessageDialog(new JFrame(),
				    "File could not be saved!\nCheck if you have writing "
				    + "permissions, or if another program is using the file.",
				    "File saving error",
				    JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	public static model.Experiment readExperimentModel(String url)
	{
		List<IExperimentListener> experimentObservers = Globals.getInstance().getExperimentModel().getObservers();
		
		model.Experiment experimentModel;
		
		try {
			FileInputStream fin = new FileInputStream(url);
			ObjectInputStream ois = new ObjectInputStream(fin);
			experimentModel = (model.Experiment) ois.readObject();
			ois.close();
			experimentModel.saved();
			experimentModel.isBackedUp();
			experimentModel.replaceObservers(experimentObservers);
			return experimentModel;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(),
				    "The file could not be opened!\n"
				    + "Please check if the file is in use by another file,"
				    + "whether the video file was removed or renamed or"
				    + "if something was changed inside the file",
				    "File could not be opened",
				    JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	/**
	 * Returns the path to the file with the system separator
	 * @param url		The URL to the location of the file
	 * @param name		The name of the file itself
	 * @return			Full path including location and filename
	 */
	public static String getFullPath(String url, String name)
	{
		return String.format("%s%s%s.UiL", url, File.separator, name);
	}
	
	public static void removePreviousAutosavesForExperiment(){
		
		
		Experiment exp = Globals.getInstance().getExperimentModel();
		Pattern p = Pattern.compile(String.format("~\\d{4}-\\d{2}-\\d{2}_\\d{2}-\\d{2}_%s\\.UiL", exp.getSaveName()));
		File fullPath = Globals.getBackupLocation();
		File[] listOfFiles = fullPath.listFiles();
		
		for(File f : listOfFiles){
			if(p.matcher(f.getName()).matches()){
				f.delete();
			}
		}
	}
}

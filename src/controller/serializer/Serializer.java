package controller.serializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import model.Experiment;
import controller.Globals;

public class Serializer {
	
	public static boolean writeExperimentModel()
	{
		Experiment exp = Globals.getInstance().getExperimentModel();
		String uri = getFullPath(exp.getSaveURL(), exp.getSaveName());
		
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
		model.Experiment experimentModel;
		
		try {
			FileInputStream fin = new FileInputStream(url);
			ObjectInputStream ois = new ObjectInputStream(fin);
			
			experimentModel = (model.Experiment) ois.readObject();
			
			ois.close();
			
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
	 * Returns the path to the file with the system seperator
	 * @param url		The url to the location of the file
	 * @param name		The name of the file itself
	 * @return			Full path including location and filename
	 */
	public static String getFullPath(String url, String name)
	{
		return String.format("%s%s%s.UiL", url, File.separator, name);
	}
	
}

package controller.serializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import controller.Globals;

public class Serializer {
	
	public static boolean writeExperimentModel()
	{
		String uri = controller.Globals.getInstance()
				.getExperimentModel().getSaveURL() + ".UiL";
		
		try{
			FileOutputStream fout = new FileOutputStream(uri);
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(Globals.getInstance().getExperimentModel());
			oos.close();
			
			return true;
			
		} catch (Exception e)
		{
			JOptionPane.showMessageDialog(new JFrame(),
				    "File could not be saved!",
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
}

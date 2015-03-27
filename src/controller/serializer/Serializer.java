package controller.serializer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
			// TODO: add error window
			System.out.println("Error" + e);
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
			// TODO: handle this nicely
			System.out.println(e);
			return null;
		}
	}
}

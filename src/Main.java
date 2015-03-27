import com.sun.jna.NativeLibrary;

import controller.Globals;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Main {
	public static void main(String[] args) {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files (x86)\\VideoLAN\\VLC\\sdk\\lib");
		NativeLibrary.addSearchPath("libvlc", "C:\\Program Files\\VideoLAN\\VLC");
		Globals.getInstance();
		
		/*SwingUtilities.invokeLater(new Runnable(){
			public void run()
			{
				view.panels.projectOpener opener = new view.panels.projectOpener();
				opener.setVisible(true);
			}
		});*/
	}
}

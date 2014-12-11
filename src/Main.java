import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.NativeLibrary;

import controller.Globals;

public class Main {
	
	public static void main(String[] args) {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files (x86)\\VideoLAN\\VLC\\sdk\\lib");
		NativeLibrary.addSearchPath("libvlc", "C:\\Program Files\\VideoLAN\\VLC");
        Globals global = Globals.getInstance();
        global.editorView.show();
	}
}

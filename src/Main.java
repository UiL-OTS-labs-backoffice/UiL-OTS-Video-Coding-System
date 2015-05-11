import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import controller.Globals;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Main {
	public static void main(String[] args) {
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files (x86)\\VideoLAN\\VLC\\sdk\\lib");
		NativeLibrary.addSearchPath("libvlc", "C:\\Program Files\\VideoLAN\\VLC");
		// A settable property: -Dnl.mpi.elan.vlcj=/Applications/VLC.app/Contents/MacOS/lib
    	String path = System.getProperty("nl.mpi.elan.vlcj");
    	if (path != null) {
	        NativeLibrary.addSearchPath(
	                RuntimeUtil.getLibVlcLibraryName(), path);
    	}
    	
    	
    	// Another reasonable default location to install VLC
        NativeLibrary.addSearchPath(
                RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib");
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        
		Globals.getInstance();
		
		
	}
}

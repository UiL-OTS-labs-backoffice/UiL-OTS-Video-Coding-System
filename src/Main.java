import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import controller.Globals;
import model.ApplicationPreferences;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.version.LibVlcVersion;

public class Main {
	
	static ApplicationPreferences prefs;
	
	public static void main(String[] args) {
		prefs = new ApplicationPreferences();
		
		searchDefaultPaths();
		
		searchPreferencedPath();
		
		if(!vlcFound())
		{
			view.panels.VLCNotFound vlcError = new view.panels.VLCNotFound(prefs);
			vlcError.setVisible(true);
			
		} else {
			// Only starts the main application after VLC has been found
	        Globals.getInstance();
		}
	}
	
	/**
	 * Adds the most common VLC install locations to the search path
	 */
	private static void searchDefaultPaths()
	{
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
		
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
	}
	
	/**
	 * Adds the VLC path in the preference file to the search path
	 */
	private static void searchPreferencedPath()
	{
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), prefs.getVLCUrl());
	}
	
	/**
	 * Checks the search paths for a valid VLC version
	 * @return		True if the required VLC dll's can be found
	 */
	private static boolean vlcFound()
	{
		boolean found = false;
		try{
			LibVlcVersion.getVersion();
			found = true;
		} catch(java.lang.UnsatisfiedLinkError e) {
		} catch(java.lang.NoClassDefFoundError e){
		} catch(Error e) {
		}
		
		return found;
	}	
	
}


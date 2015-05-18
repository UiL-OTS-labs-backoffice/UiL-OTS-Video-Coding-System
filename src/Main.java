import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

import controller.Globals;
import model.ApplicationPreferences;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.version.LibVlcVersion;
import view.panels.VLCLibSelector;


public class Main {
	
	static ApplicationPreferences prefs;
	
	public static void main(String[] args) {
		prefs = new ApplicationPreferences();
		
		searchDefaultPaths();
		
		searchPreferencedPath();
		
		while(!vlcFound())
		{
			System.out.println("Path not found!\n\n");
			String newPath = VLCLibSelector.show();
			
			prefs.setVLCUrl(newPath);
			searchUserSelectedPath(newPath);
		}
		
		// Only starts the main application after VLC has been found
        Globals.getInstance();
		
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
		System.out.println(String.format("Trying to add %s", prefs.getVLCUrl()));
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), prefs.getVLCUrl());
	}
	
	/**
	 * Adds the path that was just selected to the preference file
	 * @param path		Path that was just selected
	 */
	private static void searchUserSelectedPath(String path)
	{
		System.out.println(NativeLibrary.getProcess());
		System.out.println(String.format("Trying newly added path %s", path));
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), path);
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
			e.printStackTrace();
		} catch(java.lang.NoClassDefFoundError e){
			found = true;
			e.printStackTrace();
		} catch(Error e) {
			e.printStackTrace();
		}
		
		return found;
	}
	
	
	
}


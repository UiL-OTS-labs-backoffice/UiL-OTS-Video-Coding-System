import com.sun.jna.NativeLibrary;

import controller.Globals;
import model.ApplicationPreferences;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Main {
	
	static boolean debug = false;
	
	static ApplicationPreferences prefs;
	
	public static void main(String[] args) {
		prefs = new ApplicationPreferences();
		
		try{
			searchDefaultPaths();
			if(debug) System.out.println("Default paths searched");
		} catch (java.lang.UnsatisfiedLinkError e) {
			if(debug) System.out.println("Not in default");
		} catch ( Error e) {
			if(debug) System.out.println("Invocation exception ofzo");
		}
		
		try{
			searchPreferencedPath();
			if(debug) System.out.println("PreferencedPaths searched");
		} catch (java.lang.UnsatisfiedLinkError e) {
			if(debug) System.out.println("Not in prefs");
		} catch (Error e)
		{
			
		}
		
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
		if(com.sun.jna.Native.POINTER_SIZE == 8)
		{
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files\\VideoLAN\\VLC");
		} else {
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:\\Program Files (x86)\\VideoLAN");
		}
    	
    	// Another reasonable default location to install VLC
        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "/Applications/VLC.app/Contents/MacOS/lib");
	}
	
	/**
	 * Adds the VLC path in the preference file to the search path
	 */
	private static void searchPreferencedPath()
	{
		if(debug) System.out.println(prefs.getVLCUrl());
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
			MediaPlayerFactory fac = new MediaPlayerFactory();
			fac.release();
			found = true;
			if(debug) System.out.println("Media Player Factory started");
		} catch(java.lang.UnsatisfiedLinkError e) {
			if(debug) System.out.println("Unsatisfied link");
		} catch(java.lang.NoClassDefFoundError e){
			if(debug) System.out.println("No class def found");
		} catch(java.lang.RuntimeException e){
			if(debug) System.out.println("Runtime exception (this is good. Means VLC wasn't found at all");
		} catch(Error e) {
			if(debug) System.out.println("Other error:");
		}
		
		return found;
	}	
	
}


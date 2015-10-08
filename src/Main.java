import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
		} catch (java.lang.UnsatisfiedLinkError e) {
			vlcErrorMessage("Not in default");
		} catch ( Error e) {
			vlcErrorMessage("Invocation exception ofzo");
		}
		
		try{
			searchPreferencedPath();
		} catch (java.lang.UnsatisfiedLinkError e) {
			vlcErrorMessage("Not in prefs");
		} catch (Error e)
		{
			vlcErrorMessage("General error in preference path");
		}
		
		if(vlcFound())
		{
			// Only starts the main application after VLC has been found
	        Globals.getInstance();
		} else {
			view.panels.VLCNotFound vlcError = new view.panels.VLCNotFound(prefs);
			vlcError.setVisible(true);
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
    	
		// Best guess for Ubuntu. Doesn't read the path, except when in preferences.
		if(prefs.getVLCUrl() == null) prefs.setVLCUrl("usr/bin/vlc");
		
    	// Best guess for OSX
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
	
	private static void vlcErrorMessage(String error)
	{
		if(debug) System.out.println(error);
		JOptionPane.showMessageDialog(new JFrame(),
			    "An error occured while looking for VLC. \nThis could mean VLC "
			    + "is not installed on your computer, that VLC could not be \n"
			    + "found on your computer or, more likely, that something else \n"
			    + "has gone horribly wrong. \n\n"
			    + "It would seem this application will not run on your computer.\n"
			    + "Please try another machine",
			    "Internal Error",
			    JOptionPane.ERROR_MESSAGE);
	}
	
}


import java.io.File;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.sun.jna.NativeLibrary;

import java.util.regex.Matcher;

import controller.Globals;
import model.ApplicationPreferences;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;


public class Main {
	
	static private final boolean DEBUG = false;
	
	static private ApplicationPreferences prefs;
	
	static private boolean fail_find_vlc = false;
	static private String vlc_location = null; //Make debugging on different machines easier
	/**
	 * Main method tries to add VLC to the search path of this application
	 * If this fails, a dialog is opened, asking the user to specify where
	 * VLC is installed. Otherwise, the application itself is started
	 * 
	 * @param args 		-f: fails finding VLC automatically, to test a new
	 * 						path while the program would otherwise recognise
	 * 						VLC
	 * 
	 * 					-vlc="<path>": Specify the VLC path. Useful for installers
	 * 
	 * 					String: Specify the project that the user would like to open
	 * 							Required for automatic file association
	 */
	public static void main(String[] args) {
		
		prefs = new ApplicationPreferences();
		
		if(args.length > 0) handleArguments(args);
		
		if(vlc_location != null)
			
		searchDefaultPaths();
		searchPreferencedPath();
		
		if(vlcFound() && !fail_find_vlc)
		{
			// Only starts the main application after VLC has been found
			Globals.getInstance();
		} else {
			SwingUtilities.invokeLater(new Runnable(){
				public void run(){
					view.panels.VLCNotFound vlcError = new view.panels.VLCNotFound(prefs);
					vlcError.setVisible(true);
				}
			});
		}
	}
	
	/**
	 * Method to handle command line arguments
	 * @param args	command line arguments
	 */
	static private void handleArguments(String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			if (args[i].equals("-f"))
			{
				fail_find_vlc = true;
			} else if(args[i].startsWith("vlc="))
			{
				Pattern p = Pattern.compile("-?[\"\']?vlc=(.*)[\"\']?");
				Matcher m = p.matcher(args[i]);
				if(m.find()) {
					File f = new File(m.group(1));
					if (f.isDirectory()) {
						vlc_location = m.group(1);
						trySearchPath(vlc_location);
					}
				}
			} else {
				File f = new File(args[i]);
				if (f.exists()) {
					// TODO open file?
					System.out.println("Should be opening " + f);					
				}
			}
		}
	}
	
	/**
	 * Adds the most common VLC install locations to the search path
	 */
	private static void searchDefaultPaths()
	{
		if(com.sun.jna.Native.POINTER_SIZE == 8)
		{
			trySearchPath("C:\\Program Files\\VideoLAN\\VLC");
		} else {
			trySearchPath("C:\\Program Files (x86)\\VideoLAN");
		}
    	
		// Best guess for Ubuntu. Doesn't read the path, except when in preferences.
		if(prefs.getVLCUrl() == null) prefs.setVLCUrl("usr/bin/vlc");
		
    	// Best guess for OSX
        trySearchPath("/Applications/VLC.app/Contents/MacOS/lib");
	}
	
	/**
	 * Adds the VLC path in the preference file to the search path
	 */
	private static void searchPreferencedPath()
	{
		if(DEBUG) System.out.println(prefs.getVLCUrl());
		trySearchPath(prefs.getVLCUrl());
	}
	
	/**
	 * Private method that tries to search a path for VLC and handles the errors
	 * @param p
	 */
	private static void trySearchPath(String p)
	{
		try{
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), p);
		} catch (java.lang.UnsatisfiedLinkError e) {
			vlcErrorMessage("Not in prefs");
		} catch (Error e)
		{
			vlcErrorMessage("General error in preference path");
		}
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
			if(DEBUG) System.out.println("Media Player Factory started");
		} catch(java.lang.UnsatisfiedLinkError e) {
			if(DEBUG) System.out.println("Unsatisfied link");
		} catch(java.lang.NoClassDefFoundError e){
			if(DEBUG) System.out.println("No class def found");
		} catch(java.lang.RuntimeException e){
			if(DEBUG) System.out.println("Runtime exception (this is good. Means VLC wasn't found at all");
		} catch(Error e) {
			if(DEBUG) System.out.println("Other error:");
		}
		
		return found;
	}
	
	/**
	 * Method to print a general vlc error message if debug is enabled
	 * @param error		Error message
	 */
	private static void vlcErrorMessage(String error)
	{
		if(DEBUG) System.out.println(error);
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


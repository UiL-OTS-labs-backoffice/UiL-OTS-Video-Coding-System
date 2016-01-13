package view.timeline.utilities;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import controller.Globals;
import controller.Globals.OsType;

/**
 * Code used from:
 * http://www.coderanch.com/t/581592/java/java/Calculating-video-file-duration
 */
public class VideoDurationExtractor {
	public static String getDuration(final String sVideoInput){
		OsType osType = Globals.getOs();
		final String exe;
		String res = null;
		
		if(osType == OsType.Windows) {
			exe = "lib/ffmpeg.exe";
		} else if(osType == OsType.Linux) {
			exe = "avconv";
		} else {
			System.out.println("Selected MAC");
			exe = "lib/ffmpeg";
		}
		
		try {
			String processName = String.format("%s -i \"%s\"", exe, sVideoInput);
			
			System.out.println(processName);
			
			Process child;
			
			if(osType == OsType.Windows){
				child = Runtime.getRuntime().exec(processName);
			} else {
				File cmd = File.createTempFile("aaa", "sh");
				if(osType == OsType.Mac){
					String tempDir = cmd.getParent();
					try {
						ExportResource(tempDir, exe);
					} catch (Exception e) {
						System.out.println("Couldn't extract to temp dir");
						System.out.println(e);
					}
				}
				
                Writer w =  new BufferedWriter(new FileWriter(cmd)); 
                w.write(processName);
                w.close();
                cmd.canExecute();
                cmd.canRead();
                cmd.deleteOnExit();
                String cmdPath = cmd.getAbsolutePath();
                child = Runtime.getRuntime().exec(new String[]{"/bin/sh", cmdPath});
			}
			
			
			InputStream lsOut = child.getErrorStream();
			InputStreamReader isr = new InputStreamReader(lsOut);
			BufferedReader in = new BufferedReader(isr);
			
			String line;
            while ((line = in.readLine()) != null)
            {
            	System.out.println(line);
                if(line.contains("Duration:"))
                {
                    line = line.replaceFirst("Duration: ", ""); 
                    line = line.trim();
                     
                    res = line.substring(0, 11);
                    break;
                }
            }
            
		} catch (IOException e) {
			System.out.println("Exception checking duration: " + e);
			return null;
		}
		
		return res;
	}
	
	static public String ExportResource(String targetDir, String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try {
            stream = VideoDurationExtractor.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = "";
            resStreamOut = new FileOutputStream(jarFolder + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            stream.close();
            resStreamOut.close();
        }

        return jarFolder + resourceName;
    }

	public static long getDurationAsLong(final String sVideoInput){
		String res = getDuration(sVideoInput);
		return parseDurationAsLong(res);
	}
	
	public static long parseDurationAsLong(String res){
		String[] parts = res.split(":");
		int hrs = Integer.parseInt(parts[0]);
		int mins = Integer.parseInt(parts[1]);
		int secs = Integer.parseInt(parts[2].substring(0,2));
		int millis = Integer.parseInt(parts[2].substring(3,5));
		long duration = 10 * millis + 1000 * secs + mins * 60000 + hrs * 3600000;
		return duration;
	}
}

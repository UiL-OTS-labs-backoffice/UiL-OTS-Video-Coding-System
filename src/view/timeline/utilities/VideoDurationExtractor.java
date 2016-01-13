package view.timeline.utilities;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

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
			Process child;
			
			if(osType == OsType.Windows){
				child = Runtime.getRuntime().exec(processName);
			} else {
				File cmd = File.createTempFile("aaa", "sh");
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
                if(line.contains("Duration:"))
                {
                    line = line.replaceFirst("Duration: ", ""); 
                    line = line.trim();
                     
                    res = line.substring(0, 11);
                    break;
                }
            }
            
		} catch (IOException e) {
			return null;
		}
		
		return res;
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

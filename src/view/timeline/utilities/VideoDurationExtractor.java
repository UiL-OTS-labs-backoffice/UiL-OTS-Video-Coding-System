package view.timeline.utilities;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import controller.Globals;
import controller.Globals.OsType;

public class VideoDurationExtractor {
	public static String getDuration(final String sVideoInput){
		OsType osType = Globals.getOs();
		final String exe;
		String res = null;
		
		if(osType == OsType.Windows) {
			exe = "lib/ffmpeg.exe";
		} else {
			exe = "";
		}
		
		try {
			Process child = null;
			if(osType == OsType.Windows){
					child = Runtime.getRuntime().exec(exe + " -i \"" + sVideoInput + "\"");
			} else {
				System.out.println("Not windows. Not implemented");
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

package view.timeline.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
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
	private static boolean debug;
	
	public static String getDuration(final String sVideoInput){
		debug = Globals.getInstance().debug();
		OsType osType = Globals.getOs();
		String exe;
		String res = null;
		
		if(osType == OsType.Windows) {
			String tempDir = System.getProperty("java.io.tmpdir");
			try {
				exe = ExportResource(tempDir, "/ffmpeg.exe");
			} catch (Exception e) {
				if(debug)e.printStackTrace();
				exe = "lib/ffmpeg.exe";
			}
		} else if(osType == OsType.Linux) {
			exe = "avconv";
		} else {
			exe = "ffmpeg";
		}
		
		try {
			String processName = String.format("%s -i \"%s\"", exe, sVideoInput);
			
			if(debug){
				System.out.println("Process name: " + processName);
			}
			
			Process child;
			
			if(osType == OsType.Windows){
				child = Runtime.getRuntime().exec(processName);
			} else {
				File cmd = File.createTempFile("aaa", "sh");
				if(osType == OsType.Mac){
					String tempDir = cmd.getParent();
					try {
						String outPath = ExportResource(tempDir, "/ffmpeg");
						processName = String.format("%s -i \"%s\"", outPath, sVideoInput);
						File outFile = new File(outPath);
						outFile.setReadable(true);
						outFile.setExecutable(true);
						outFile.deleteOnExit();
					} catch (Exception e) {
					}
				}
                Writer w =  new BufferedWriter(new FileWriter(cmd));
                w.write(processName);
                w.close();
                cmd.setExecutable(true);
                cmd.setReadable(true);
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
            	if(Globals.getInstance().debug()){
    				System.out.println(line);
    			}
                if(line.contains("Duration:"))
                {
                    line = line.replaceFirst("Duration: ", ""); 
                    line = line.trim();
                     
                    res = line.substring(0, 11);
                    break;
                }
            }
		} catch (IOException e) {
			if(debug) System.out.println(e);
			return null;
		}
		
		return res;
	}
	
	public static String ExportResource(String targetDir, String resourceName) throws Exception {
		InputStream stream = VideoDurationExtractor.class.getResourceAsStream(resourceName);
		FileOutputStream fos = null;
		
		try{
			fos = new FileOutputStream(targetDir + resourceName);
			byte[] buf = new byte[2048];
			int r = stream.read(buf);
			while(r != -1){
				fos.write(buf, 0, r);
				r = stream.read(buf);
			}
		} finally {
			if(fos != null){
				fos.close();
			}
		}
		
		return targetDir + resourceName;
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

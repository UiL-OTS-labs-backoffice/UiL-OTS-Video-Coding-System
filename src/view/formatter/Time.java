package view.formatter;

import java.util.concurrent.TimeUnit;

public class Time {
	/**
     * Formats the time in a nice format
     * @param millis	The time string to be encoded
     * @return			String displaying the time nicely
     */
    public static String format(long millis)
    {
    	return String.format("%02d:%02d:%02d", 
        		TimeUnit.MILLISECONDS.toHours(millis), 
        		TimeUnit.MILLISECONDS.toMinutes(millis) - 
        		TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), 
        		TimeUnit.MILLISECONDS.toSeconds(millis) - 
        		TimeUnit.MINUTES.toSeconds(
        				TimeUnit.MILLISECONDS.toMinutes(millis)
        		)        			
        	);
    }
    
    /**
     * Formats a time in a nice detailed format (including milliseconds)
     * @param millis	The number of milliseconds more than time 0
     * @return			A string displaying the time nicely, including milliseconds
     */
    public static String formatDetail(long millis){
    	if(millis == -1L)
			return "--";
		String s = String.format("%s.%03d", 
	    		format(millis),
	    		millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis))
	    	);
		return s;
    }
    
}

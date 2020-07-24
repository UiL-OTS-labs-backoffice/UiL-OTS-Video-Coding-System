package view.player;

import java.awt.Dimension;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import controller.Globals;

import uk.co.caprica.vlcj.media.MediaRef;
import uk.co.caprica.vlcj.media.TrackType;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import view.timeline.utilities.VideoDurationExtractor;

public class VLCMediaPlayer implements IMediaPlayer{

	/**
     * Player reference
     */
    private EmbeddedMediaPlayer player;
    
    /**
     * Media URL
     */
    private String mediaURL;
    
    /**
     * Media length is figured out by the hidden media player
     */
    private long mediaLength;
    
    private List<IMediaPlayerListener> observers;
    
    private final Object MUTEX = new Object();
    
    /**
     * Display component
     */
    private EmbeddedMediaPlayerComponent playerComponent;
    
    private Dimension videoSize;
   	private static final Dimension fallbackVideoSize = new Dimension(352, 288);
   	private float aspectRatio, origAspectRatio;
   	private double msPerFrame = 0.0d; 
   	private double stdev = 0.0d;
   	private long offset = 0L;
   	
   	public VLCMediaPlayer(String url)
   	{
   		this.observers = new ArrayList<IMediaPlayerListener>();
   		mediaURL = url;
		
		internalCreateNewVisualComponent(null);

        // At this point, no usable info is actually available yet.
        // seekable, playable, #video outputs, video size... all false or 0.
        // Therefore, we play a small bit of the media in a second media player,
        // which doesn't display anything on the screen, to find out the video size. 
		tryHiddenPlayer(mediaURL);

		Dimension dim = (videoSize != null) ? videoSize
                                            : fallbackVideoSize;
		origAspectRatio = (float)dim.getWidth() / (float)dim.getHeight();
		aspectRatio = origAspectRatio;
   	}
    
	
	/* (non-Javadoc)
	 * @see view.IMediaPlayer#getMediaPlayer()
	 */
	@Override
	public EmbeddedMediaPlayer getMediaPlayer()
	{
		return player;
	}
	
	private void internalCreateNewVisualComponent(String [] prepareOptions) {
        playerComponent = new EmbeddedMediaPlayerComponent();
        player = playerComponent.mediaPlayer();
        player.events().addMediaPlayerEventListener(infoListener);
        
        playerComponent.addHierarchyListener(new HierarchyListener() {
			// This makes sure that when the player window becomes displayable
			// (which is a subset of visible) the player shows some frame in it. 
			// This can't be done before that time.
            public void hierarchyChanged(HierarchyEvent e) {
            	long flags = e.getChangeFlags() & (HierarchyEvent.PARENT_CHANGED);
                if ((flags != 0) &&	e.getComponent().isDisplayable()) {
                    player.controls().start();
                   	player.controls().pause();
            	}
            }
        });
        
        // configure the player with the given media file
        player.media().prepare(mediaURL, prepareOptions);
        player.media().parsing().parse();

        player.input().enableKeyInputHandling(false);
        player.input().enableMouseInputHandling(false);
	}

    private EmbeddedMediaPlayer hiddenMediaPlayer;
    private Semaphore semaphore;
    
    /*
     * Play media invisibly, to find out the size.
     * NOTE: THIS WILL CAUSE VLC ERRORS. They can be ignored, the original programmer employed a hack to find out
     * the video size. I'm not in the mood to fix it, as it's not really a functional problem. It's just evil code.
     */
    private synchronized void tryHiddenPlayer(String media) {
    	MediaPlayerFactory factory = new MediaPlayerFactory();
    	TestRenderCallback render = new TestRenderCallback();
		hiddenMediaPlayer = factory.mediaPlayers().newEmbeddedMediaPlayer();
    	VideoSurface surface = factory.videoSurfaces().newVideoSurface(new TestBufferFormatCallback(), render, false);
		hiddenMediaPlayer.videoSurface().set(surface);
    	hiddenMediaPlayer.audio().setVolume(0);
        semaphore = new Semaphore(0);
        hiddenMediaPlayer.media().play(media);

        try {
        	semaphore.tryAcquire(1000L, TimeUnit.MILLISECONDS);
        	wait(1000);
        } catch(InterruptedException ex) {
        	if(Globals.getInstance().debug())
        		System.out.println(ex);
        }
        mediaLength = hiddenMediaPlayer.status().length();

        checkMediaDuration(media, mediaLength);

        hiddenMediaPlayer.controls().pause();
        long trytime = hiddenMediaPlayer.status().length() / 2;
        hiddenMediaPlayer.controls().setTime(trytime);
        try{
        	wait(1000);
        } catch(InterruptedException ex){
        }
        this.offset = hiddenMediaPlayer.status().time() - trytime;

        hiddenMediaPlayer.controls().stop();
        hiddenMediaPlayer.release();
        hiddenMediaPlayer = null;
        semaphore = null;
    }
    
    private void checkMediaDuration(String media, long duration){
    	String ffmpegLength = VideoDurationExtractor.getDuration(media);
    	if(Globals.getInstance().debug()){
    		System.out.println("FFMPEG string: " + ffmpegLength);
    	}
    	
    	
    	if(ffmpegLength == null){
    		SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					JOptionPane.showMessageDialog(null,
							"WARNING: Video compatibility could not be checked.\n"
							+ "If the video is incompatible, results of coding this video might be\n"
							+ "off by a long way. Check video compatibility manually.", "Warning",
							JOptionPane.ERROR_MESSAGE);
				}
			});
    	} else {
	    	long ffmpegDuration = VideoDurationExtractor.parseDurationAsLong(ffmpegLength);
	    	if(Math.abs(ffmpegDuration - duration) > 50){
	    		final String videoIncompatibleWarning = "CRITICAL WARNING: THIS VIDEO IS INCOMPATIBLE\n"
	    				+ "Media time cannot be read properly from this video. This will cause the results of coding this video to be worthless!\n"
	    				+ "We recommend to use a video converter, to create a more suitable video format.\nContinue at your own risk.\n\n"
	    				+ "Do you want to close this project?";
	    		
	    		SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						int result = JOptionPane.showConfirmDialog(null,
								videoIncompatibleWarning, "VIDEO INCOMPATIBLE",
								JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION);
						if (result == JOptionPane.YES_OPTION) {
							Globals.getInstance().getPreferencesModel().setClosed();
							System.exit(0);
						}
					}
				});
	    	}
    	}
    }
    
    /**
     * Test render callback
     */
    private final class TestRenderCallback implements RenderCallback {
    	
        public TestRenderCallback() {
        }

        @Override
        public void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffer, BufferFormat bufferFormat) {
        }
    }
    
    /**
     * Test buffer format callback
     */
    private final class TestBufferFormatCallback implements BufferFormatCallback {
        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
        	videoSize = new Dimension(sourceWidth, sourceHeight);
        	// As soon as we're here, we might as well stop. We know what we wanted.
        	semaphore.release();
            return new RV32BufferFormat(352, 288);
        }

		@Override
		public void allocatedBuffers(ByteBuffer[] byteBuffers) {

		}
	}
    
    MediaPlayerEventListener infoListener = new MediaPlayerEventListener() {
		
    	public void playing(MediaPlayer mediaPlayer) {
    		// TODO: wtf is
//			if (msPerFrame <= 0) {
//			    float fps = mediaPlayer.getFps();
//			    if (fps > 1) {
//			    	msPerFrame = (1000d / fps);
//			    	stdev = msPerFrame / 2;
//		        }
//		    }
			List<IMediaPlayerListener> localObservers;
			synchronized(MUTEX) {
				localObservers = new ArrayList<IMediaPlayerListener>(observers);
			}
			for(IMediaPlayerListener l : localObservers){
				l.mediaStarted();
			}
		}
    	
		public void finished(MediaPlayer mediaPlayer) {
			VLCMediaPlayer.this.stop();
			VLCMediaPlayer.this.player.controls().stop();
		}
		
		public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
//			if (msPerFrame <= 0) {
//				float fps = mediaPlayer.getFps();
//				if (fps > 1) {
//					msPerFrame = (1000d / fps);
//					stdev = msPerFrame / 2;
//				}
//			}
			mediaTimeChanged();
		}

		@Override
		public void backward(MediaPlayer arg0) { }

		@Override
		public void buffering(MediaPlayer arg0, float arg1) { }

		@Override
		public void error(MediaPlayer arg0) { }

		@Override
		public void forward(MediaPlayer arg0) { }

		@Override
		public void lengthChanged(MediaPlayer arg0, long arg1) { }

		@Override
		public void opening(MediaPlayer arg0) { }

		@Override
		public void pausableChanged(MediaPlayer arg0, int arg1) { }

		@Override
		public void paused(MediaPlayer arg0) {
			List<IMediaPlayerListener> localObservers;
			synchronized(MUTEX) {
				localObservers = new ArrayList<IMediaPlayerListener>(observers);
			}
			for(IMediaPlayerListener l : localObservers){
				l.mediaPaused();
			}
		}

		@Override
		public void positionChanged(MediaPlayer arg0, float arg1) { }
	
		@Override
		public void seekableChanged(MediaPlayer arg0, int arg1) { }

		@Override
		public void snapshotTaken(MediaPlayer arg0, String arg1) { }

		@Override
		public void stopped(MediaPlayer arg0) { }

		@Override
		public void titleChanged(MediaPlayer arg0, int arg1) { }

		@Override
		public void videoOutput(MediaPlayer arg0, int arg1) { }

		@Override
		public void mediaChanged(MediaPlayer mediaPlayer, MediaRef mediaRef) {

		}

		@Override
		public void scrambledChanged(MediaPlayer mediaPlayer, int i) {

		}

		@Override
		public void elementaryStreamAdded(MediaPlayer mediaPlayer, TrackType trackType, int i) {

		}

		@Override
		public void elementaryStreamDeleted(MediaPlayer mediaPlayer, TrackType trackType, int i) {

		}

		@Override
		public void elementaryStreamSelected(MediaPlayer mediaPlayer, TrackType trackType, int i) {

		}

		@Override
		public void corked(MediaPlayer mediaPlayer, boolean b) {

		}

		@Override
		public void muted(MediaPlayer mediaPlayer, boolean b) {

		}

		@Override
		public void volumeChanged(MediaPlayer mediaPlayer, float v) {

		}

		@Override
		public void audioDeviceChanged(MediaPlayer mediaPlayer, String s) {

		}

		@Override
		public void chapterChanged(MediaPlayer mediaPlayer, int i) {

		}

		@Override
		public void mediaPlayerReady(MediaPlayer mediaPlayer) {

		}
	};
	
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#createNewVisualComponent()
	 */
    @Override
	public java.awt.Component createNewVisualComponent() {
        if (playerComponent != null && player != null) {
        	// Preserve the time, volume, play rate, aspect ratio.
        	long time = player.status().time();
        	int volume = player.audio().volume();
        	float rate = player.status().rate();
        	float ar = getAspectRatio();
        	
   			playerComponent.release();
   			playerComponent = null;
   			String opt1 = ":start-time=" + Float.toString(time / 1000f);
   			String[] opts = { opt1 };
            internalCreateNewVisualComponent(opts);
			
            player.controls().setRate(rate);
            player.audio().setVolume(volume);
            setAspectRatio(ar);
            
            return playerComponent;
        } else {
        	// This method should not have been called in this case.
            return null;
        }        
    }
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#getVisualComponent()
	 */
    @Override
	public java.awt.Component getVisualComponent() {
    	return playerComponent;
    }
    
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#getSourceHeight()
	 */
    @Override
	public int getSourceHeight() {
        if (videoSize != null) {
        	return videoSize.height;
        } else {
        	return fallbackVideoSize.height;
        }
    }
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#getSourceWidth()
	 */
    @Override
	public int getSourceWidth() {
        if (videoSize != null) {
        	return videoSize.width;
        } else {
        	return fallbackVideoSize.width;
        }
    }
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#getAspectRatio()
	 */
    @Override
	public float getAspectRatio() {
		return aspectRatio;
    }
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#setAspectRatio(float)
	 */
    @Override
	public void setAspectRatio(float aspectRatio) {
        String aspect = "";
        switch ((int)(100 * aspectRatio + 0.5)) {
	        case 100: aspect =   "1:1"; break;
	        case 125: aspect =   "5:4"; break;
	        case 133: aspect =   "4:3"; break;
	        case 160: aspect =  "16:10"; break;
	        case 177:
	        case 178: aspect =  "16:9"; break;
	        case 221: aspect = "221:100"; break;
	        case 234: /* bad floating point rounding! */
	        case 235: aspect = "235:100"; break;
	        case 239: aspect = "239:100"; break;
	        default: aspect  = ""; break;
        }
    	player.video().setAspectRatio(aspect);
    	this.aspectRatio = aspectRatio;
    }
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#start()
	 */
    @Override
	public void start() {
		if(player.status().isPlaying())
       	{
       		if(player.status().canPause())
       			player.controls().pause();
       	}
       	else
       		player.controls().play();	// async!
    }
	
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#stop()
	 */
    @Override
	public void stop() {
		if (player.status().isPlaying()) {
            if (player.status().canPause()) {
                player.controls().pause();
            } else {
                player.controls().stop();
            }
		}
    }
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#isPlaying()
	 */
    @Override
	public boolean isPlaying() {
        return player.status().isPlaying();
    }
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#getMilliSecondsPerSample()
	 */
    @Override
	public double getMilliSecondsPerSample() {
    	if (msPerFrame > 0) {
    		return msPerFrame;
    	}
    	
    	// Return an arbitrary value: 25 fps.
        return 1000 / 25;
    }
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#getStdev()
	 */
    @Override
    public double getStdev()
    {
    	return stdev;
    }
    
   /* (non-Javadoc)
    * @see view.IMediaPlayer#getMediaTime()
    */
   @Override
   public long getMediaTime() {
		return player.status().time();
   }
   
   /* (non-Javadoc)
    * @see view.IMediaPlayer#setMediaTime(long)
    */
   @Override
   public void setMediaTime(final long time) {
	   player.controls().setTime(time - offset);
	   mediaTimeChanged();
   }
   
   /* (non-Javadoc)
    * @see view.IMediaPlayer#nextFrame()
    */
   @Override
	public void nextFrame() {
		if (player.status().isPlaying()) {
			stop();
		}
		player.controls().nextFrame();
		mediaTimeChanged();
   }
	
	/* (non-Javadoc)
	 * @see view.IMediaPlayer#previousFrame()
	 */
    @Override
	public void previousFrame() {
		if (player != null) {
			if (player.status().isPlaying()) {
				stop();
			}
			
			double msecPerSample = getMilliSecondsPerSample();
			long curTime = player.status().time();
			
        	long newTime = (long) Math.floor(curTime - msecPerSample);
        	
	        if (newTime < 0) {
	        	newTime = 0;
	        }
	        setMediaTime(newTime);
		}
    }
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#getRate()
	 */
    @Override
	public float getRate() {
        return player.status().rate();
    }
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#setRate(float)
	 */
    @Override
	public void setRate(float rate) {
        player.controls().setRate(rate);
    }

    /* (non-Javadoc)
	 * @see view.IMediaPlayer#isFrameRateAutoDetected()
	 */
    @Override
	public boolean isFrameRateAutoDetected() {
        return false;
    }

    /* (non-Javadoc)
	 * @see view.IMediaPlayer#getMediaDuration()
	 */
    @Override
	public long getMediaDuration() {
    	return mediaLength;
    }


	@Override
	public float getPosition() {
		return player.status().position();
	}


	@Override
	public void setPosition(float position) {
		player.controls().setPosition(position);
	}
	
	@Override
	public void register(IMediaPlayerListener obj) {
		if(obj == null) throw new NullPointerException("Null Observer");
        synchronized (MUTEX) {
        	if(!observers.contains(obj)) observers.add(obj);
        }		
	}

	@Override
	public void deregister(IMediaPlayerListener obj) {
		synchronized (MUTEX) {
			observers.remove(obj);
		}
	}
	
	/**
	 * Notify observers of a change in the media time
	 */
	private void mediaTimeChanged() {
		List<IMediaPlayerListener> localObservers;
		synchronized(MUTEX) {
			localObservers = new ArrayList<IMediaPlayerListener>(observers);
		}
		for(IMediaPlayerListener l : localObservers){
			l.mediaTimeChanged();
		}
	}
 }

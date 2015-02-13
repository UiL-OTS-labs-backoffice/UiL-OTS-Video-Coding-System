package view.player;

import java.awt.Dimension;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

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
     * Display component
     */
    private EmbeddedMediaPlayerComponent playerComponent;
    
    private Dimension videoSize;
   	private static final Dimension fallbackVideoSize = new Dimension(352, 288);
   	private float aspectRatio, origAspectRatio;
   	private double msPerFrame = 0.0d; 
   	private double stdev = 0.0d;
   	
   	public VLCMediaPlayer(String url)
   	{
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
        player = playerComponent.getMediaPlayer();
        player.addMediaPlayerEventListener(infoListener);
        
        playerComponent.addHierarchyListener(new HierarchyListener() {
			// This makes sure that when the player window becomes displayable
			// (which is a subset of visible) the player shows some frame in it. 
			// This can't be done before that time.
            public void hierarchyChanged(HierarchyEvent e) {
            	long flags = e.getChangeFlags() & (HierarchyEvent.PARENT_CHANGED);
                if ((flags != 0) &&	e.getComponent().isDisplayable()) {
                    player.start();
                   	player.pause();
            	}
            }
        });
        
        // configure the player with the given media file
        player.prepareMedia(mediaURL, prepareOptions);
        player.parseMedia();
        
        player.setEnableMouseInputHandling(false);
        player.setEnableKeyInputHandling(false);
	}
	
    private DirectMediaPlayer hiddenMediaPlayer;
    private Semaphore semaphore;
    
    /*
     * Play media invisibly, to find out the size.
     */
    private void tryHiddenPlayer(String media) {
	    	MediaPlayerFactory factory = new MediaPlayerFactory();
	    	TestRenderCallback render = new TestRenderCallback();
	    	hiddenMediaPlayer = factory.newDirectMediaPlayer(new TestBufferFormatCallback(), render);
	        hiddenMediaPlayer.setVolume(0);
	        semaphore = new Semaphore(0);	        
	        hiddenMediaPlayer.playMedia(media);
	        try {
	        	semaphore.tryAcquire(1000L, TimeUnit.MILLISECONDS);
	        } catch(InterruptedException ex) {	        		
	        }
        	hiddenMediaPlayer.stop();
	        hiddenMediaPlayer.release();
	        hiddenMediaPlayer = null;
	        semaphore = null;
    }

    /**
     * Test render callback
     */
    private final class TestRenderCallback implements RenderCallback {
    	
        public TestRenderCallback() {
        }

        @Override
        public void display(DirectMediaPlayer mediaPlayer, com.sun.jna.Memory[] nativeBuffer, BufferFormat bufferFormat) {
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
    }
    
    MediaPlayerEventListener infoListener = new MediaPlayerEventListener() {
		
    	public void playing(MediaPlayer mediaPlayer) {
			if (msPerFrame <= 0) {
			    float fps = mediaPlayer.getFps();
			    if (fps > 1) {
			    	msPerFrame = (1000d / fps);
			    	stdev = msPerFrame / 2;
		        }
		    }
			
		}
    	
		public void finished(MediaPlayer mediaPlayer) {
			VLCMediaPlayer.this.stop();
			VLCMediaPlayer.this.player.stop();
		}
		
		public void timeChanged(MediaPlayer mediaPlayer,
			long newTime) {
				if (msPerFrame <= 0) {
					float fps = mediaPlayer.getFps();
					if (fps > 1) {
					msPerFrame = (1000d / fps);
					stdev = msPerFrame / 2;
				}
			}
		}

		@Override
		public void backward(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void buffering(MediaPlayer arg0, float arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void elementaryStreamAdded(MediaPlayer arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void elementaryStreamDeleted(MediaPlayer arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void elementaryStreamSelected(MediaPlayer arg0, int arg1,
				int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void endOfSubItems(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void error(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void forward(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void lengthChanged(MediaPlayer arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mediaChanged(MediaPlayer arg0, libvlc_media_t arg1,
				String arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mediaDurationChanged(MediaPlayer arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mediaFreed(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mediaMetaChanged(MediaPlayer arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mediaParsedChanged(MediaPlayer arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mediaStateChanged(MediaPlayer arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mediaSubItemAdded(MediaPlayer arg0, libvlc_media_t arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void newMedia(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void opening(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void pausableChanged(MediaPlayer arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void paused(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void positionChanged(MediaPlayer arg0, float arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void scrambledChanged(MediaPlayer arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void seekableChanged(MediaPlayer arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void snapshotTaken(MediaPlayer arg0, String arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void stopped(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void subItemFinished(MediaPlayer arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void subItemPlayed(MediaPlayer arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void titleChanged(MediaPlayer arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void videoOutput(MediaPlayer arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
    };
	
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#createNewVisualComponent()
	 */
    @Override
	public java.awt.Component createNewVisualComponent() {
        if (playerComponent != null && player != null) {
        	// Preserve the time, volume, play rate, aspect ratio.
        	long time = player.getTime();
        	int volume = player.getVolume();
        	float rate = player.getRate();
        	float ar = getAspectRatio();
        	
   			playerComponent.release(true);
   			playerComponent = null;
   			String opt1 = ":start-time=" + Float.toString(time / 1000f);
   			String[] opts = { opt1 };
            internalCreateNewVisualComponent(opts);
			
            player.setRate(rate);
            player.setVolume(volume);
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
    	player.setAspectRatio(aspect);
    	this.aspectRatio = aspectRatio;
    }
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#start()
	 */
    @Override
	public void start() {
       	if(player.isPlaying())
       	{
       		if(player.canPause())
       			player.pause();
       	}
       	else
       		player.play();	// async!
    }
	
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#stop()
	 */
    @Override
	public void stop() {
        if (player.isPlaying()) {
            if (player.canPause()) {
                player.pause();
            } else {
                player.stop();
            }
        }
    }
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#isPlaying()
	 */
    @Override
	public boolean isPlaying() {
        return player.isPlaying();
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
		return player.getTime();
   }
   
   /* (non-Javadoc)
    * @see view.IMediaPlayer#setMediaTime(long)
    */
   @Override
   public void setMediaTime(long time) {
	   player.setTime(time);
   }
   
   /* (non-Javadoc)
    * @see view.IMediaPlayer#nextFrame()
    */
   @Override
	public void nextFrame() {
		if (player.isPlaying()) {
			stop();
		}
		player.nextFrame();
   }
	
	/* (non-Javadoc)
	 * @see view.IMediaPlayer#previousFrame()
	 */
    @Override
	public void previousFrame() {
		if (player != null) {
			if (player.isPlaying()) {
				stop();
			}
	        
			double msecPerSample = getMilliSecondsPerSample();
			long curTime = player.getTime() - 190;
			
        	curTime = (long) Math.floor(curTime - msecPerSample);
        	
	        if (curTime < 0) {
	        	curTime = 0;
	        }
	        setMediaTime(curTime);
		}
    }
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#getRate()
	 */
    @Override
	public float getRate() {
        return player.getRate();
    }
    
    /* (non-Javadoc)
	 * @see view.IMediaPlayer#setRate(float)
	 */
    @Override
	public void setRate(float rate) {
        player.setRate(rate);
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
        return player.getLength();
    }


	@Override
	public float getPosition() {
		return player.getPosition();
	}


	@Override
	public void setPosition(float position) {
		player.setPosition(position);
	}

 }

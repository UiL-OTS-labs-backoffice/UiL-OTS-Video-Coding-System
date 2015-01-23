package view;

import java.awt.Canvas;
import java.awt.Color;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

public class VideoPlayer extends Canvas {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8226566722766038818L;

	// Create a media player factory
    private MediaPlayerFactory mediaPlayerFactory;

    // Create a new media player instance for the run-time platform
    private EmbeddedMediaPlayer mediaPlayer;

	public VideoPlayer()
	{
		paintCanvas();
		setSurface();
	}
	
	
	private void paintCanvas()
	{
		this.setBackground(Color.BLACK);
		this.revalidate();
		this.repaint();
	}
	
	public void rerender()
	{
		
	}
	
	private void setSurface()
	{
		mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.newEmbeddedMediaPlayer();
        
        CanvasVideoSurface videoSurface = 
        		mediaPlayerFactory.newVideoSurface(this);
        mediaPlayer.setVideoSurface(videoSurface);
	}
	
	
	
	public EmbeddedMediaPlayer getMediaPlayer()
	{
		return mediaPlayer;
	}
	
}

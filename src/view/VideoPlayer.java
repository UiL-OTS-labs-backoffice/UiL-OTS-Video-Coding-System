package view;

import java.awt.Canvas;
import java.awt.Color;

import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.RenderCallback;
import uk.co.caprica.vlcj.player.direct.RenderCallbackAdapter;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

@SuppressWarnings("serial")
public class VideoPlayer extends JPanel {
	
	 /**
     * Media player component.
     */
    private DirectMediaPlayerComponent mediaPlayerComponent;

    /**
     *
     */
    private int width = 720;

    /**
     *
     */
    private int height = 480;


    /**
     *
     */
    private BufferedImage image;
    
    BufferFormatCallback bufferFormatCallback;
    
    private float fps = 29;

    /**
     * Application entry point.
     *
     * @param args
     */
    public void startPlayer(String mrl)
    {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                start(mrl);
            }
        });
    }
    
    protected void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D)g;
    	g2.setColor(Color.black);
    	g2.fillRect(0, 0, getWidth(), getHeight());
    	g2.drawImage(image, null, 0, 0);
	}
    
    /**
     * Create a new test.
     */
	public VideoPlayer() {
               
        setBackground(Color.black);
        setOpaque(true);
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));

        image = GraphicsEnvironment.getLocalGraphicsEnvironment().
        		getDefaultScreenDevice().getDefaultConfiguration().
        		createCompatibleImage(width, height);

        bufferFormatCallback = new BufferFormatCallback() {
            @Override
            public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
                return new RV32BufferFormat(width, height);
            }
        };

        mediaPlayerComponent = new DirectMediaPlayerComponent(bufferFormatCallback) {
            @Override
            protected RenderCallback onGetRenderCallback() {
                return new TestRenderCallbackAdapter();
            }
        };
    }
	
	public void render()
	{
		repaint();
	}

    /**
     * Start playing a movie.
     *
     * @param mrl mrl
     */
    private void start(String mrl) {
        // One line of vlcj code to play the media...
        mediaPlayerComponent.getMediaPlayer().playMedia(mrl);

    }

    private class TestRenderCallbackAdapter extends RenderCallbackAdapter {

        private TestRenderCallbackAdapter() {
            super(new int[width * height]);
        }

        @Override
        protected void onDisplay(DirectMediaPlayer mediaPlayer, int[] rgbBuffer) {
            // Simply copy buffer to the image and repaint
            image.setRGB(0, 0, width, height, rgbBuffer, 0, width);
            repaint();
           
            float framerate = getMediaPlayer().getFps();
            fps = (framerate > 0.0) ? framerate : fps;
        }
    }
    
  
    public DirectMediaPlayer getMediaPlayer()
    {
    	return mediaPlayerComponent.getMediaPlayer();
    }
    
    public float getFps()
    {
    	return fps;
    }

}

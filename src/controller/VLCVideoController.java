package controller;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of IVideoController for the VLC Video Player Object
 * @author mooij006
 *
 */
public class VLCVideoController implements IVideoControls {
	
	/**
	 * Instance of the video Controller
	 */
	private Globals g;
	
	/**
	 * Observers
	 */
	private List<IVideoControllerObserver> observers;
	
	/**
	 * Immutable object
	 */
	private final Object MUTEX = new Object();
	
	/**
	 * Instance of media player
	 */
	private view.player.IMediaPlayer player;
	
	protected VLCVideoController(Globals globals){
		this.g = globals;
		this.observers = new ArrayList<IVideoControllerObserver>();
	}
	
	@Override
	public void setPlayer(view.player.IMediaPlayer player) {
		this.player = player;
	}

	@Override
	public view.player.IMediaPlayer getPlayer() {
		return player;
	}	

	@Override
	public boolean IsLoaded() {
		return (player != null);
	}

	@Override
	public void play() {
		player.start();
		ArrayList<IVideoControllerObserver> localObservers;
		synchronized (MUTEX) {
			localObservers = new ArrayList<IVideoControllerObserver>(this.observers);
		}
		if(player.isPlaying()){
			for(IVideoControllerObserver obj : localObservers){
				obj.playerStarted();
			}
		} else {
			for(IVideoControllerObserver obj : localObservers){
				obj.playerStarted();
			}
		}
	}

	@Override
	public void nextFrame() {
		player.nextFrame();
	}

	@Override
	public void prevFrame() {
		player.previousFrame();
	}

	@Override
	public void secondAhead() {
		long time = player.getMediaTime();
		long duration = player.getMediaDuration();
		long newTime = time + 1000l;
		if(newTime < duration)
			player.setMediaTime(time + 1000l);
	}

	@Override
	public void secondBack() {
		long time = player.getMediaTime();
		long newTime = time + 1000l;
		if(newTime > 0)
			player.setMediaTime(time + 1000l);
	}

	@Override
	public void nextTrial() {
		int tnr = Math.abs(g.getExperimentModel().getItemForTime(getMediaTime()));
		if(tnr > 0 && tnr < g.getExperimentModel().getNumberOfItems())
		{
			model.AbstractTimeFrame t = g.getExperimentModel().getItem(tnr+1);
			long time = t.getBegin();
			player.setMediaTime(time);
		} else {
			player.setMediaTime(player.getMediaDuration());
		}
	}		

	@Override
	public void prevTrial() {
		int tnr = Math.abs(g.getExperimentModel().getItemForTime(getMediaTime()));
		if (tnr > 1) tnr--;
		else if (tnr < 0) tnr = Math.abs(tnr);
		if(tnr > 0)
		{
			model.AbstractTimeFrame t = g.getExperimentModel().getItem(tnr);
			long time = t.getBegin();
			player.setMediaTime(time);
		} else {
			player.setMediaTime(0);
		}
	}

	@Override
	public void nextLook() {
		// TODO Auto-generated method stub

	}

	@Override
	public void prevLook() {
		// TODO Auto-generated method stub

	}

	@Override
	public void JumpToEnd() {
		player.setMediaTime(player.getMediaDuration());
	}

	@Override
	public void JumpToStart() {
		player.setMediaTime(0l);
	}

	@Override
	public Component getVisualComponent() {
		return player.getVisualComponent();
	}

	@Override
	public int getSourceHeight() {
		return player.getSourceHeight();
	}

	@Override
	public int getSourceWidth() {
		return player.getSourceWidth();
	}

	@Override
	public float getAspectRatio() {
		return player.getAspectRatio();
	}

	@Override
	public void setAspectRatio(float aspectRatio) {
		player.setAspectRatio(aspectRatio);
	}

	@Override
	public void stop() {
		player.stop();
	}

	@Override
	public boolean isPlaying() {
		return player.isPlaying();
	}

	@Override
	public double getMilliSecondsPerSample() {
		return player.getMilliSecondsPerSample();
	}
	
	@Override
	public double getStdev()
	{
		if (IsLoaded())
			return player.getStdev();
		else return 0;
	}

	@Override
	public long getMediaTime() {
		return player.getMediaTime();
	}

	@Override
	public void setMediaTime(long time) {
		player.setMediaTime(time);
		ArrayList<IVideoControllerObserver> localObservers;
		synchronized(MUTEX) {
			localObservers = new ArrayList<IVideoControllerObserver>(this.observers);
		}
		for(IVideoControllerObserver obj : localObservers) {
			obj.mediaTimeChanged(time);
		}
	}

	@Override
	public float getRate() {
		return player.getRate();
	}

	@Override
	public void setRate(float rate) {
		player.setRate(rate);
	}

	@Override
	public boolean isFrameRateAutoDetected() {
		return player.isFrameRateAutoDetected();
	}

	@Override
	public long getMediaDuration() {
		return player.getMediaDuration();
	}

	@Override
	public float getPosition() {
		return player.getPosition();
	}

	@Override
	public void setPosition(float position) {
		player.setPosition(position);
	}

	@Override
	public void register(IVideoControllerObserver obj) {
		if(obj == null) throw new NullPointerException("Null Observer");
        synchronized (MUTEX) {
        	if(!observers.contains(obj)) observers.add(obj);
        }		
	}

	@Override
	public void deregister(IVideoControllerObserver obj) {
		synchronized (MUTEX) {
			observers.remove(obj);
		}
	}

	@Override
	public void videoInstantiated() {
		ArrayList<IVideoControllerObserver> localObservers;
		synchronized(MUTEX) {
			localObservers = new ArrayList<IVideoControllerObserver>(this.observers);
		}
		for(IVideoControllerObserver obj : localObservers) {
			obj.videoInstantiated();
		}
	}

}

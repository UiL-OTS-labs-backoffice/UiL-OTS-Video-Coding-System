package model.TimeObserver;

public interface IExperimentListener {
	
	public void saveStateChanged(boolean newSavedState);
	
	public void backupstateChanged(boolean newBackupState);
}

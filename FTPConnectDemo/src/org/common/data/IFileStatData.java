package org.common.data;

import java.io.File;

public interface IFileStatData {
	public File getFile();
	public void setFile(File file);
	
	public long getStartTime();
	public String getStartTimeFormatted();

	public void setStartTime(long startTime);
	public long getEndTime();

	public String getEndTimeFormatted();
	public void setEndTime(long endTime);

	public double getDuration();
	public String getDurationFormatted();

	public double getBitrate();
	public String getBitrateFormatted();

	public void setFileUploaded(boolean fileUploaded);
	public boolean getFileUploaded();

}

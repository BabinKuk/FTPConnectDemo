package org.common.data;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * class that holds statistic data of uploaded files
 * 
 * @author nbabic
 * 
 */
public class FileStatData implements IFileStatData {

	private File file;
	private long startTime;
	private long endTime;
	private boolean fileUploaded;

	SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-yyyy HH:mm:ss");
	DecimalFormat df = new DecimalFormat("#.##");

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public long getStartTime() {
		return startTime;
	}

	public String getStartTimeFormatted() {
		Date resultDate = new Date(startTime);
		// System.out.println(sdf.format(resultDate));
		return sdf.format(resultDate);
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public String getEndTimeFormatted() {
		Date resultDate = new Date(endTime);
		// System.out.println(sdf.format(resultDate));
		return sdf.format(resultDate);
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public double getDuration() {
		return (double) (endTime - startTime) / 1000;
	}

	public String getDurationFormatted() {
		// This should print a number which is rounded to 2 decimal places.
		String str = df.format(getDuration());
		return str;
	}

	public double getBitrate() {
		return (double) (file.length() / 1024) / getDuration();
	}

	public String getBitrateFormatted() {
		// This should print a number which is rounded to 2 decimal places.
		String str = df.format(getBitrate());
		return str;
	}

	public void setFileUploaded(boolean fileUploaded) {
		this.fileUploaded = fileUploaded;
	}

	public boolean getFileUploaded() {
		return fileUploaded;
	}

}

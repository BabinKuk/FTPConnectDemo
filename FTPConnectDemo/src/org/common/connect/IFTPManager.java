package org.common.connect;

public interface IFTPManager {
	
	/**
	 * create thread pool and start
	 */
	public void go();
	
	/**
	 * loop while all uploads are not finished
	 */
	public boolean checkFileStatus();
	
	/**
	 * get total upload time
	 * 
	 * @return
	 */
	public double getTotalUploadTime();

	/**
	 * get total upload time rounded to 2 decimal places
	 * 
	 * @return
	 */
	public String getTotalUploadTimeFormatted();
	
	/**
	 * get average bit rate
	 * 
	 * @return
	 */
	public double getAverageBitrate();

	/**
	 * get average bit rate rounded to 2 decimal places
	 * 
	 * @return
	 */
	public String getAverageBitrateFormatted();

}

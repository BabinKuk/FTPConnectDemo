package org.common.connect;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.common.connect.FTPManager;
import org.common.data.FileStatData;
import org.common.data.IFileStatData;
import org.common.process.Processor;

public class FTPManager implements IFTPManager {

	private String user = new String();
	private String pass = new String();
	private String server = new String();
	private ArrayList<String> listFiles = new ArrayList<String>();
	private ArrayList<String> listErrorFiles = new ArrayList<String>();
	private ArrayList<IFileStatData> fileStatList = new ArrayList<IFileStatData>();
	private ArrayList<Long> minTimeList = new ArrayList<Long>();
	private ArrayList<Long> maxTimeList = new ArrayList<Long>();
	double sumBitRate = 0;
	long minTime;
	long maxTime;
	DecimalFormat df = new DecimalFormat("#.##");
	
	public FTPManager(String user, String pass, String server,
			ArrayList<String> listFiles) {
		this.user = user;
		this.pass = pass;
		this.server = server;
		this.listFiles = listFiles;
	}

	/**
	 * create thread pool and start
	 */
	public void go() {
		// System.out.println("FTPConnect.go()");

		// create thread pool of 5 threads
		ExecutorService executor = Executors.newFixedThreadPool(5);
		
		// upload counter
		int countUpload = 0;

		// iterate over listFiles
		Iterator<String> it = listFiles.iterator();
		while (it.hasNext()) {
			// uploads file using an InputStream File
			File localFile = new File((String) it.next());

			// check if local file exists on local machine
			if (localFile.isFile()) {
				countUpload++;
				// max 5 files can be uploaded
				if (countUpload <= 5) {
					//System.out.println(localFile.getName() + " countUpload " + countUpload);
					// File statistics object
					IFileStatData fileStatData = new FileStatData();
					fileStatData.setFile(localFile);
					// add object to list
					fileStatList.add(fileStatData);

					executor.submit(new Processor(user, pass, server, fileStatData));
				} else {
					System.out.println("File " + localFile.getName()
							+ " not uploaded. Maximum number of uploads already reached.");
					// add to list of not uploaded files
					listErrorFiles.add(localFile.getName());
				}
			} else {
				System.out.println("File " + localFile.getName()
						+ " does not exist.");
				// add to list of not uploaded files
				listErrorFiles.add(localFile.getName());
			}
		}

		// when one thread finish one task, go and start another task
		executor.shutdown();

		System.out.println("All files submitted for upload.");

		// wait to terminate task
		try {
			executor.awaitTermination(60, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// check if all uploads are finished
		boolean checkUploads = checkFileStatus();
		// System.out.println(checkUploads);

		if (checkUploads) {
			System.out.println("All tasks executed. File transfer statistics:");

			// iterate over fileStatList
			Iterator<IFileStatData> itStatData = fileStatList.iterator();
			while (itStatData.hasNext()) {
				IFileStatData tempData = itStatData.next();
				System.out.println("File " + tempData.getFile().getName()
						+ ". Upload time " + tempData.getDurationFormatted()
						+ " seconds. Bitrate " + tempData.getBitrateFormatted()
						+ " KB/s.");
				// add start time to minTimeList
				minTimeList.add(tempData.getStartTime());
				// add end time to maxTimeList
				maxTimeList.add(tempData.getEndTime());
				// sum bit rates
				sumBitRate += tempData.getBitrate();
			}
		}

		// print total upload time and average bit rate
		System.out.println("\nNumber of files uploaded : "
				+ fileStatList.size());
		// System.out.println("minTimeList : " + minTimeList);
		// System.out.println("maxTimeList : " + maxTimeList);
		minTime = Collections.min(minTimeList);
		maxTime = Collections.max(maxTimeList);
		/*
		 * SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-yyyy HH:mm:ss");
		 * System.out.println("minTime : " + minTime + ", formatted : " +
		 * sdf.format(new Date(minTime))); System.out.println("maxTime : " +
		 * maxTime + ", formatted : " + sdf.format(new Date(maxTime)));
		 */
		String totalUploadTime = getTotalUploadTimeFormatted();
		String averageBitRate = getAverageBitrateFormatted();

		System.out.println("Total upload time : " + totalUploadTime
				+ " seconds.");
		System.out.println("Average bit rate : " + averageBitRate + " KB/s.");

		// print files that are not uploaded, if exist
		if (listErrorFiles.size() > 0) {
			System.out.println("\nFiles not uploaded : " + listErrorFiles);
		}

	}

	/**
	 * loop while all uploads are not finished
	 */
	public boolean checkFileStatus() {
		boolean upload = true;

		// iterate over fileStatList
		do {
			try {
				upload = true;
				Thread.sleep(10000);

				Iterator<IFileStatData> itStatData = fileStatList.iterator();
				while (itStatData.hasNext()) {
					IFileStatData tempData = itStatData.next();
					// check if upload is finished
					if (!tempData.getFileUploaded()) {
						upload = false;
					}
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (upload == false);

		return upload;
	}

	/**
	 * get total upload time
	 * 
	 * @return
	 */
	public double getTotalUploadTime() {
		return (double) (maxTime - minTime) / 1000;
	}

	/**
	 * get total upload time rounded to 2 decimal places
	 * 
	 * @return
	 */
	public String getTotalUploadTimeFormatted() {
		// This should print a number which is rounded to 2 decimal places.
		String str = df.format(getTotalUploadTime());
		return str;
	}

	/**
	 * get average bit rate
	 * 
	 * @return
	 */
	public double getAverageBitrate() {
		return (double) sumBitRate / fileStatList.size();
	}

	/**
	 * get average bit rate rounded to 2 decimal places
	 * 
	 * @return
	 */
	public String getAverageBitrateFormatted() {
		// This should print a number which is rounded to 2 decimal places.
		String str = df.format(getAverageBitrate());
		return str;
	}

}

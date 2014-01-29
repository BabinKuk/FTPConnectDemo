package org.common.process;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.common.data.IFileStatData;

/**
 * class with connection and uploading methods
 * 
 * @author nbabic
 * 
 */
public class Processor implements Runnable, IProcessor {

	private String user = new String();
	private String pass = new String();
	private String server = new String();
	private IFileStatData fileData;
	private URLConnection clientConnection;

	public Processor(String user, String pass, String server,
			IFileStatData fileStatData) {
		super();
		this.user = user;
		this.pass = pass;
		this.server = server;
		this.fileData = fileStatData;
	}

	@Override
	public void run() {
		// System.out.println("Processor.run()");
		// try to connect to server
		try {
			// local file object
			File localFile = fileData.getFile();

			boolean connectSuccessfull = connect(server, user, pass, localFile);
			if (connectSuccessfull) {
				// try to upload file
				//System.out.println("Starting upload the file " + localFile);
				sendFile(localFile);
			} else {
				System.out
						.println("Error when trying to establish the connection...");
			}
		} catch (Exception e) {
			System.out
					.println("File upload error. Please try later or check your settings...");
		}
	}

	/**
	 * try to connect to server
	 * 
	 * @param server
	 * @param user
	 * @param password
	 * @return
	 */
	@Override
	public boolean connect(String server, String user, String password,
			File file) {
		// try to connect to server
		// create url object using connection string:
		// ftp://user:password@ipaddress/path/file.ext
		try {
			String connectionString = "ftp://" + user + ":" + password + "@"
					+ server + "/" + file.getName();
			// System.out.println("connectionString - " + connectionString);

			// create url object
			URL url = new URL(connectionString);
			// connection to the remote object referred to by the URL
			clientConnection = url.openConnection();
			return true;
		} catch (Exception ex) {
			System.out
					.println("Error when trying to establish the connection...");
			return false;
		}

	}

	/**
	 * try to upload the file
	 * 
	 * @param file
	 * @return
	 */
	@Override
	public boolean sendFile(File localFile) throws Exception {
		// System.out.println("Processor.sendFile()");
		System.out.println("Start uploading file " + localFile.getName());
		try {
			// start and end time, and processing in between
			long startTime = System.currentTimeMillis();
			// set start time
			fileData.setStartTime(startTime);
			// set upload flg
			fileData.setFileUploaded(false);

			InputStream input = new FileInputStream(localFile);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					input);

			// uploads file using an OutputStream File
			OutputStream output = clientConnection.getOutputStream();
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
					output);
			byte[] buffer = new byte[4096];
			int readed;

			while ((readed = bufferedInputStream.read(buffer)) > 0) {
				bufferedOutputStream.write(buffer, 0, readed);
			}
			bufferedOutputStream.close();
			bufferedInputStream.close();

			// set end time
			long endTime = System.currentTimeMillis();
			fileData.setEndTime(endTime);
			// set upload flg
			fileData.setFileUploaded(true);
			// print data
			System.out.println("File " + fileData.getFile().getName()
					+ " uploaded successfully. Start time "
					+ fileData.getStartTimeFormatted() + ". End time "
					+ fileData.getEndTimeFormatted() + ". Upload time "
					+ fileData.getDurationFormatted() + " seconds. Bitrate "
					+ fileData.getBitrateFormatted() + " KB/s.\n");

			return true;
		} catch (Exception ex) {
			System.out.println("Error when uploading file...");
			return false;
		}
	}

}

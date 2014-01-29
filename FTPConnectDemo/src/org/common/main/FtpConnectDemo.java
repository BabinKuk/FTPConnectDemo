package org.common.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.common.connect.FTPManager;
import org.common.connect.IFTPManager;

/**
 * A program that uploads files from local computer to a remote FTP server
 * using connection string: ftp://user:password@ipaddress/path/file.ext.
 * 
 * @author nbabic
 * 
 */
public class FtpConnectDemo {

	static String user = "";
	static String pass = "";
	static String server = "";
	static String files = "";
	static ArrayList<String> listFiles = new ArrayList<String>();

	/**
	 * 
	 */
	public FtpConnectDemo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * check and organize input parameters
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// parse input parameters
		// format: -u ftpuser -p ftppass -server ipaddress -files
		// filename1;filename2;...filename5
		/*
		 * for (int i = 0; i < args.length; i++) { System.out.println("arg[" + i
		 * + "].name " + args[i] + "; arg[" + (i + 1) + "].value " + args[i +
		 * 1]); i++; }
		 */
		// check if certain parameters exist
		// -u, -p and server are optional (predefined values are
		// user/pass/localhost)
		// searching element on unsorted Java array
		// searching java array using ArrayList
		List<String> inputList = Arrays.asList(args);

		int userIndex = checkInputParams(inputList, "-u");
		if (userIndex != -1) {
			// System.out.println("User found ");
			user = inputList.get(userIndex + 1);
			// System.out.println("User: " + user);
		} else {
			// System.out.println("Use default username");
			user = "user";
		}

		int passIndex = checkInputParams(inputList, "-p");
		if (passIndex != -1) {
			// System.out.println("Password found");
			pass = inputList.get(passIndex + 1);
			// System.out.println("Pass: " + pass);
		} else {
			// System.out.println("Use default username");
			pass = "pass";
		}

		int serverIndex = checkInputParams(inputList, "-server");
		if (serverIndex != -1) {
			// System.out.println("Server found");
			server = inputList.get(serverIndex + 1);
			// System.out.println("Server: " + server);
		} else {
			// System.out.println("Use default local server");
			server = "localhost";
		}

		int fileIndex = checkInputParams(inputList, "-files");
		if (fileIndex != -1) {
			// System.out.println("Files found");
			// check if there are white spaces in file paths
			files = checkFileNames(fileIndex, args);
			// System.out.println(files);

			// check if file paths consist of "\" and change to "/"
			files = files.replace("\\", "/");
		} else {
			System.out.println("Files not entered. Exit application...");
			System.exit(1);
		}

		// parse fileNames
		listFiles = new ArrayList<String>(Arrays.asList(files.split(";")));
		// System.out.println("listFiles: " + listFiles);

		// got all parameters
		// System.out.println("Username: " + user + "\nPassword: " + pass
		// + "\nServer: " + server + "\nFiles: " + listFiles);

		// connect to server and start uploading
		IFTPManager connect = new FTPManager(user, pass, server, listFiles);
		connect.go();

	}

	/**
	 * check if input parameters exist
	 * 
	 * @param list
	 * @param param
	 * @return
	 */
	public static int checkInputParams(List<String> list, String param) {
		int index = -1;
		if (list.contains(param)) {
			index = list.indexOf(param);
			// System.out.println("Element found " + index);
		}
		return index;
	}

	/**
	 * check if file path/name consists of white spaces
	 * 
	 * @param fileIndex
	 * @param args
	 * @return
	 */
	public static String checkFileNames(int fileIndex, String[] args) {
		String fileNames = args[fileIndex + 1];
		// System.out.println("fileIndex + 1 : " + (fileIndex + 1)
		// + ", args.length : " + args.length);

		if ((fileIndex + 1) < (args.length - 1)) {
			// System.out.println("there are blanks...");
			for (int i = fileIndex + 1; i < args.length - 1; i++) {
				fileNames = fileNames + " " + args[i + 1];
			}
		}
		/*
		 * else { //System.out.println("no blanks..."); }
		 */

		return fileNames;
	}

}

package org.common.process;

import java.io.File;

public interface IProcessor {
	/**
	 * try to connect to server
	 * @param server
	 * @param user
	 * @param password
	 * @return
	 */
	boolean connect(String server, String user, String password, File file);
	
	/**
	 * try to upload the file
	 * 
	 * @param file
	 * @return
	 */
	boolean sendFile(File localFile) throws Exception;

}

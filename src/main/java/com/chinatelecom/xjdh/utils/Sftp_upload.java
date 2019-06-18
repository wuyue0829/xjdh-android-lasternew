package com.chinatelecom.xjdh.utils;//package com.chinatelecom.xjdh.utils;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Date;
//import java.util.Properties;
//import java.util.Vector;
//
//import com.jcraft.jsch.Channel;
//import com.jcraft.jsch.ChannelExec;
//import com.jcraft.jsch.ChannelSftp;
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.Session;
//import com.jcraft.jsch.SftpException;
//import com.jcraft.jsch.UserInfo;
//
//import android.text.format.DateFormat;
//
//public class Sftp_upload {
//
//	/**
//	 * 连接sftp服务器
//	 * 
//	 * @param host
//	 *            主机
//	 * @param port
//	 *            端口
//	 * @param username
//	 *            用户名
//	 * @param password
//	 *            密码
//	 * @return
//	 */
//	public void connect(String host, int port, String username, String password, String sourceFilename,String destFilename) {
//		try {
//			JSch jsch = new JSch();
//			Session session = jsch.getSession(username, host, port);
//			UserInfo userInfo = new MyUserInfo();
//			((MyUserInfo) userInfo).setPassword(password);
//			session.setPassword(password);
//
//			Properties config = new Properties();
//			config.put("StrictHostKeyChecking", "no");
//			session.setConfig(config);
//			session.connect();
//			System.out.println("Connected to " + host + ".");
//			L.d("><><<<<<<888888888888888", "Connected to " + host + ".");
//			String command = "scp -p -t " + destFilename;
//			Channel channel = session.openChannel("exec");
//
//			((ChannelExec) channel).setCommand(command);
//
//			OutputStream out = channel.getOutputStream();
//			InputStream in = channel.getInputStream();
//
//			channel.connect();
//
//			if (checkAck(in) != 0) {
//				System.exit(0);
//			}
//
//			// send \"T timestamp timestamp\" to preserve the timestamp
//			File sourceFile = new File(sourceFilename);
//
//			command = "T " + (sourceFile.lastModified() / 1000) + " 0";
//			// The access time should be sent here,
//			// but it is not accessible with JavaAPI ;-<
//			command += (" " + (sourceFile.lastModified() / 1000) + " 0\\n");
//			out.write(command.getBytes());
//			out.flush();
//			if (checkAck(in) != 0) {
//				System.exit(0);
//			}
//
//			// send \"C0644 filesize filename\" where filename doesn\'t contain
//			// a /
//			long filesize = (new File("d:/print.tx")).length();
//			command = "C0644 " + filesize + " ";
//			if (sourceFilename.lastIndexOf('/') > 0) {
//				command += sourceFilename.substring(sourceFilename.lastIndexOf('/') + 1);
//			} else {
//				command += sourceFilename;
//			}
//
//			command += "\\n";
//
//			out.write(command.getBytes());
//			out.flush();
//
//			if (checkAck(in) != 0) {
//				System.exit(0);
//			}
//
//			// send the contents of the source file
//			FileInputStream fis = new FileInputStream(sourceFilename);
//			byte[] buf = new byte[1024];
//			while (true) {
//				int len = fis.read(buf, 0, buf.length);
//
//				if (len <= 0) {
//					break;
//				}
//
//				out.write(buf, 0, len);
//			}
//
//			fis.close();
//			fis = null;
//
//			// send \'\\0\' to end it
//			buf[0] = 0;
//			out.write(buf, 0, 1);
//			out.flush();
//
//			out.close();
//
//			channel.disconnect();
//			session.disconnect();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	private int checkAck(InputStream in) throws IOException {
//		int b = in.read();
//		// b may be 0 for success,
//		// 1 for error,
//		// 2 for fatal error,
//		// -1
//		if (b == 0)
//			return b;
//		if (b == -1)
//			return b;
//
//		if (b == 1 || b == 2) {
//			StringBuffer sb = new StringBuffer();
//			int c;
//			do {
//				c = in.read();
//				sb.append((char) c);
//			} while (c != '\n');
//			if (b == 1) { // error
//				System.out.print(sb.toString());
//			}
//			if (b == 2) { // fatal error
//				System.out.print(sb.toString());
//			}
//		}
//		return b;
//
//	}
//
//	/**
//	 * 上传文件
//	 * 
//	 * @param directory
//	 *            上传的目录
//	 * @param uploadFile
//	 *            要上传的文件
//	 * @param sftp
//	 */
//	public void upload(String directory, String uploadFile, ChannelSftp sftp) {
//
//		try {
//			sftp.cd(directory);
//			File file = new File(uploadFile);
//
//			String currentTime = DateFormat.format("yyyy_MM_dd_hh_mm_ss", new Date()).toString(); // 获取时间
//			String filename = currentTime + ".wav"; // 文件名为当前时间来保存
//			sftp.put(new FileInputStream(file), filename);
//
//			System.out.println("上传成功！");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 下载文件
//	 * 
//	 * @param directory
//	 *            下载目录
//	 * @param downloadFile
//	 *            下载的文件
//	 * @param saveFile
//	 *            存在本地的路径
//	 * @param sftp
//	 */
//	public void download(String directory, String downloadFile, String saveFile, ChannelSftp sftp) {
//
//		try {
//			sftp.cd(directory);
//			File file = new File(saveFile);
//			sftp.get(downloadFile, new FileOutputStream(file));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 删除文件
//	 * 
//	 * @param directory
//	 *            要删除文件所在目录
//	 * @param deleteFile
//	 *            要删除的文件
//	 * @param sftp
//	 */
//	public void delete(String directory, String deleteFile, ChannelSftp sftp) {
//
//		try {
//			sftp.cd(directory);
//			sftp.rm(deleteFile);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 列出目录下的文件
//	 * 
//	 * @param directory
//	 *            要列出的目录
//	 * @param sftp
//	 * @return
//	 * @throws SftpException
//	 */
//	public Vector listFiles(String directory, ChannelSftp sftp) throws SftpException {
//
//		return sftp.ls(directory);
//
//	}
//
//	// 上传文件
//
//	public static void Sftp_server() {
//
//		// String Imsi = imsi;
//		Sftp_upload sf = new Sftp_upload();
//		String host = "192.168.56.100";
//		int port = 22;
//		String username = "root";
//		String password = "2016jimglobal";
//		String directory = "/data/test/wav";
//		// String uploadFile = file;
//		sf.connect(host, port, username, password, directory);
//
//		// try{
//		// sftp.cd(directory);
//		// sftp.mkdir(Imsi); //创建目录
//		// System.out.println("finished");
//		// }catch(Exception e){
//		// e.printStackTrace();
//		// }
//		// directory = "/data/test/wav/"+imsi;
//		// sf.upload(directory, uploadFile, sftp);//上传文件到服务器
//		//
//	}
//
//	public static class MyUserInfo implements UserInfo {
//		public String password = null;
//
//		public void setPassword(String password) {
//			this.password = password;
//		}
//
//		public String getPassword() {
//			return this.password;
//		}
//
//		public boolean promptYesNo(String str) {
//			return true;
//		}
//
//		public String getPassphrase() {
//			return null;
//		}
//
//		public boolean promptPassphrase(String message) {
//			return true;
//		}
//
//		public void showMessage(String message) {
//
//		}
//
//		public boolean promptPassword(String message) {
//			return true;
//		}
//	}
//
//}
package com.chinatelecom.xjdh.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

/**
 * 文件处理工具包
 * 
 * @author peter
 * @version 1.1
 * @created 2015-07-08
 */
public class FileUtils {
	// 创建文件夹myImage
	public static String SDPATH = Environment.getExternalStorageDirectory() + "/myImage/";

	private static final String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

	public static String getFromAssets(Context ctx, String fileName) {
		String Result = "";
		try {
			InputStreamReader inputReader = new InputStreamReader(ctx.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result;
	}

	public static boolean setToData(Context ctx, String fileName, byte[] data) {
		try {
			FileOutputStream fos = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write(data);
			fos.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getCanonical(File f) {
		if (f == null)
			return null;

		try {
			return f.getCanonicalPath();
		} catch (IOException e) {
			return f.getAbsolutePath();
		}
	}
	public static String getPath(String uri) {
		Log.i("FileUtils#getPath(%s)", uri);
		if (TextUtils.isEmpty(uri))
			return null;
		if (uri.startsWith("file://") && uri.length() > 7)
			return Uri.decode(uri.substring(7));
		return Uri.decode(uri);
	}
	public static byte[] getFileData(Context ctx, String fileName) {
		try {
			FileInputStream fis = ctx.openFileInput(fileName);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = fis.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			return outStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "".getBytes();
	}

	public static Uri getTakePhotoUri() {
		SimpleDateFormat imageDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
		StringBuilder pathBuilder = new StringBuilder();
		pathBuilder.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
		pathBuilder.append('/');
		pathBuilder.append("Camera");
		pathBuilder.append('/');
		pathBuilder.append("IMG_" + imageDateFormat.format(new Date()) + ".jpg");
		Uri uri = Uri.parse("file://" + pathBuilder.toString());
		File file = new File(uri.toString());
		file.getParentFile().mkdirs();
		return uri;
	}

	public static String getImagePath(String picName) {
		File dir = new File(SDPATH);
		if (! dir.isDirectory()) {
			dir.mkdir();
		}
		return SDPATH + picName + ".png";
	}
	public static String PHOTOPATH = Environment.getExternalStorageDirectory() + File.separator ;
	public static String getImagePath_(String picName) {
		// /sdcard/myImage/
		File file = new File("/sdcard/myImage/");
		if (!file.exists()) {
			file.mkdirs();// 创建文件夹
		}
		return file + picName + ".JPEG";
	}

	public static String imageURI() {
		String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
		File file = new File("IMAGE_PATH");
		file.mkdirs();// 创建文件夹
		String fileName = file + name;
		return fileName;

	}

	// 在SD卡上创建目录
	public File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}
	// 在SD卡上创建文件
		public File creatSDfile(String fileName) throws IOException {
			File file = new File(SDPATH + fileName);
			file.createNewFile();
			return file;
		}

	public File writeToSDFromIuput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = creatSDfile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			while ((input.read(buffer)) != -1) {
				output.write(buffer);
			}
			output.flush();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}
		}
		return file;
	}
	
	public void writeToSDFromStr(String path,String fileName,String str){
		File file=null;
		FileOutputStream fos=null;
		try {
			file=new File(path, fileName);
			fos=new FileOutputStream(file);
			
//			fos.write(str.getBytes());
//			fos.write("\r\n".getBytes());
//			fos.write("I am lilu".getBytes());
//			fos.close();
			PrintWriter pw=new PrintWriter(fos,true);
			pw.println(str);;
			pw.close();
			Log.i("TAG", "====保存成功====:");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	//保存照片
	public static void saveBitmap(Bitmap b) {
		String jpegName = SDPATH + "/" + getTime() + ".jpg";
		try {
			FileOutputStream fout = new FileOutputStream(jpegName);
			BufferedOutputStream bos = new BufferedOutputStream(fout);
			b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//获取视频存储路径
	public static String getMediaOutputPath() {
		return SDPATH + "/" + getTime() + ".mp4";
	}

	private static String getTime() {
		return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis()));
	}

	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}
}

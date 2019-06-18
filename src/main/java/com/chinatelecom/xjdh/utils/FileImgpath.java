package com.chinatelecom.xjdh.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateFormat;

public class FileImgpath {
	// 创建文件夹myImage
	public static String SDPATH = Environment.getExternalStorageDirectory() + "/myImage/";

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
}
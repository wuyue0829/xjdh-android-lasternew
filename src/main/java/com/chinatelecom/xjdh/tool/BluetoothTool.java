package com.chinatelecom.xjdh.tool;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BluetoothTool extends Thread {
	public static BluetoothSocket bluetoothSocket;
	public static BufferedWriter bw;
	public static BufferedReader br;

	public static boolean setup(BluetoothDevice device) {
		// for (int i = 1; i <= 30; i++) {
		try {
			if (bluetoothSocket != null) {
				bluetoothSocket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		try {
			if (bluetoothSocket != null) {
				bluetoothSocket.close();
			}
			bluetoothSocket = initSocket(device, 3);
			bluetoothSocket.connect();
			bw = new BufferedWriter(new OutputStreamWriter(bluetoothSocket.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(bluetoothSocket.getInputStream()));
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		// }
		return false;
	}

	public static void Close() {
		if (bluetoothSocket != null) {
			try {
				bluetoothSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			bluetoothSocket = null;
		}
	}

	/**
	 * 开始配对
	 * 
	 */
	private static BluetoothSocket initSocket(BluetoothDevice device, int channel) {
		BluetoothSocket temp = null;
		try {
			Method m = device.getClass().getMethod("createRfcommSocket", new Class[] { int.class });
			temp = (BluetoothSocket) m.invoke(device, channel);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return temp;
	}

	public static boolean SendCmd(String cmd) {
		if (bluetoothSocket != null) {
			if (!bluetoothSocket.isConnected()) {
				return false;
			}
			try {
				bw.write(cmd);
				bw.flush();// 刷新
				return true;
			} catch (IOException e) {
				// e.printStackTrace();
			}
		}
		return false;
	}

	public static String readMsg() {
		if (!bluetoothSocket.isConnected()) {
			return "";
		}
		String msg;
		try {
			msg = br.readLine();
			return msg;
		} catch (IOException e) {
			// e.printStackTrace();
		} // 读取一行数据
		return "";
	}
}
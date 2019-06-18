package com.chinatelecom.xjdh.ui;//package com.chinatelecom.xjdh.ui;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//import org.androidannotations.annotations.Background;
//import org.androidannotations.annotations.Click;
//import org.androidannotations.annotations.EActivity;
//import org.androidannotations.annotations.UiThread;
//import org.androidannotations.annotations.ViewById;
//
//import com.chinatelecom.xjdh.R;
//import com.chinatelecom.xjdh.utils.L;
//import com.chinatelecom.xjdh.utils.T;
//import com.jcraft.jsch.Channel;
//import com.jcraft.jsch.ChannelExec;
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.Session;
//import com.jcraft.jsch.UserInfo;
//
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//@EActivity(R.layout.wifi_modify_view)
//public class PingActivity extends BaseActivity {
//	@ViewById
//	EditText ping1_edt, ping2_edt;
//	@ViewById
//	EditText info, up_ip, up_netmask, up_gateway,board_ip,smd_user;
//	@ViewById
//	TextView result;
//	String ip,password,ping2,login_ip,username;
//	String count = "4", size = "64", time = "1";
//	String y = "\"";
//	private static JSch jsch;
//	private static int PORT=22;
//	private static Session session;
//	ProgressDialog pDialog;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		pDialog = new ProgressDialog(this);
//	}
//
//	@Click(R.id.up_loads)
//	void scpClick() {
//		password = info.getText().toString();
//		String IPADRESS = up_ip.getText().toString();
//		String NETMASK = up_netmask.getText().toString();
//		String GATEWAY = up_gateway.getText().toString();
//		String ipaddress = y + IPADRESS + y;
//		String netmask = y + NETMASK + y;
//		String gateway = y + GATEWAY + y;
//		login_ip =board_ip.getText().toString();
//		if (TextUtils.isEmpty(IPADRESS) && TextUtils.isEmpty(NETMASK)&& TextUtils.isEmpty(GATEWAY)) {
//			T.showShort(this, "请输入修改内容");
//			return;
//		}else if (TextUtils.isEmpty(IPADRESS)&& TextUtils.isEmpty(NETMASK)) {
//			T.showShort(this, "请输入修改内容");
//			return;
//		} else if (TextUtils.isEmpty(IPADRESS)&& TextUtils.isEmpty(GATEWAY)) {
//			T.showShort(this, "请输入修改内容");
//			return;
//		}else if (TextUtils.isEmpty(NETMASK)&& TextUtils.isEmpty(GATEWAY)) {
//			T.showShort(this, "请输入修改内容");
//			return;
//		}else if (TextUtils.isEmpty(IPADRESS)) {
//			T.showShort(this, "请输入修改内容");
//			return;
//		}else if (TextUtils.isEmpty(NETMASK)) {
//			T.showShort(this, "请输入修改内容");
//			return;
//		}else if (TextUtils.isEmpty(GATEWAY)) {
//			T.showShort(this, "请输入修改内容");
//			return;
//		}
//		username = smd_user.getText().toString();
//		if (TextUtils.isEmpty(username)) {
//			T.showShort(this, "请输入登录用户");
//			return;
//		}
//		if (TextUtils.isEmpty(password) && TextUtils.isEmpty(login_ip)) {
//			T.showShort(this, "请输入密码和登录IP");
//			return;
//		}else if (TextUtils.isEmpty(login_ip)) {
//			T.showShort(this, "请输入登录ip");
//			return;
//		} else if (TextUtils.isEmpty(password)) {
//			T.showShort(this, "请输入登录密码");
//			return;
//		}
//		connect_smd("sed -i -e '/IPADDR0/c export IPADDR0=" + ipaddress + "' -e'" + "/NETMASK0/c export NETMASK0="
//				+ netmask + "' -e '" + "/GATEWAY0/cexport GATEWAY0=" + gateway + "' /etc/rc.d/rc.conf", password,login_ip,username);
//		pDialog.setMessage("正在执行，请稍后...");
//		pDialog.show();
//		up_ip.setText("");
//		up_netmask.setText("");
//		up_gateway.setText("");
//	}
//
//	@Click(R.id.look)
//	void lookClick() {
//		password = info.getText().toString();
//		login_ip =board_ip.getText().toString();
//		username = smd_user.getText().toString();
//		if (TextUtils.isEmpty(username)) {
//			T.showShort(this, "请输入登录用户");
//			return;
//		}
//		if (TextUtils.isEmpty(password) && TextUtils.isEmpty(login_ip)) {
//			T.showShort(this, "请输入密码和登录IP");
//			return;
//		}else if (TextUtils.isEmpty(login_ip)) {
//			T.showShort(this, "请输入登录ip");
//			return;
//		} else if (TextUtils.isEmpty(password)) {
//			T.showShort(this, "请输入登录密码");
//			return;
//		}
//		pDialog.setMessage("正在执行，请稍后...");
//		pDialog.show();
//		connect("/bin/tail /etc/rc.d/rc.conf", password,login_ip,username);
//		
//	}
//
//	@Click(R.id.ping2)
//	void ping2Click() {
//		password = info.getText().toString();
//		login_ip =board_ip.getText().toString();
//		ping2 = ping2_edt.getText().toString();
//		if (TextUtils.isEmpty(ping2)) {
//			T.showShort(this, "请输入IP");
//			return;
//		}
//		username = smd_user.getText().toString();
//		if (TextUtils.isEmpty(username)) {
//			T.showShort(this, "请输入登录用户");
//			return;
//		}
//		if (TextUtils.isEmpty(password) && TextUtils.isEmpty(login_ip)) {
//			T.showShort(this, "请输入密码和登录IP");
//			return;
//		}else if (TextUtils.isEmpty(login_ip)) {
//			T.showShort(this, "请输入登录ip");
//			return;
//		} else if (TextUtils.isEmpty(password)) {
//			T.showShort(this, "请输入登录密码");
//			return;
//		}
//		pDialog.setMessage("正在执行，请稍后...");
//		pDialog.show();
//		
//		connect("ping -c 4 -W 2 " + ping2, password,login_ip,username);
//		
//	}
//
//	@Click(R.id.restart)
//	void restartClick() {
//		password = info.getText().toString();
//		login_ip =board_ip.getText().toString();
//		username = smd_user.getText().toString();
//		if (TextUtils.isEmpty(username)) {
//			T.showShort(this, "请输入登录用户");
//			return;
//		}
//		if (TextUtils.isEmpty(password) && TextUtils.isEmpty(login_ip)) {
//			T.showShort(this, "请输入密码和登录IP");
//			return;
//		}else if (TextUtils.isEmpty(login_ip)) {
//			T.showShort(this, "请输入登录ip");
//			return;
//		} else if (TextUtils.isEmpty(password)) {
//			T.showShort(this, "请输入登录密码");
//			return;
//		}
//		connect_smd("/sbin/reboot", password,login_ip,username);
//		pDialog.setMessage("正在执行，请稍后...");
//		
//		pDialog.show();
//	}
//
//	
//	@Click(R.id.ping_fh)
//	void ping_fhClick(){
//		password = info.getText().toString();
//		login_ip =board_ip.getText().toString();
//		String fenghuo = "194.0.0.1";
//		username = smd_user.getText().toString();
//		if (TextUtils.isEmpty(username)) {
//			T.showShort(this, "请输入登录用户");
//			return;
//		}
//		if (TextUtils.isEmpty(password) && TextUtils.isEmpty(login_ip)) {
//			T.showShort(this, "请输入密码和登录IP");
//			return;
//		}else if (TextUtils.isEmpty(login_ip)) {
//			T.showShort(this, "请输入登录ip");
//			return;
//		} else if (TextUtils.isEmpty(password)) {
//			T.showShort(this, "请输入登录密码");
//			return;
//		}
//		pDialog.setMessage("正在执行，请稍后...");
//		pDialog.show();
//		connect("ping -c 4 -W 2 " + fenghuo, password,login_ip,username);
//	}
//	
//	@Click(R.id.restart_ip)
//	void restart_ipClick() {
//		password = info.getText().toString();
//		login_ip =board_ip.getText().toString();
//		username = smd_user.getText().toString();
//		if (TextUtils.isEmpty(username)) {
//			T.showShort(this, "请输入登录用户");
//			return;
//		}
//		if (TextUtils.isEmpty(password) && TextUtils.isEmpty(login_ip)) {
//			T.showShort(this, "请输入密码和登录IP");
//			return;
//		}else if (TextUtils.isEmpty(login_ip)) {
//			T.showShort(this, "请输入登录ip");
//			return;
//		} else if (TextUtils.isEmpty(password)) {
//			T.showShort(this, "请输入登录密码");
//			return;
//		}
//		pDialog.setMessage("正在执行，请稍后...");
//		pDialog.show();
//		connect("/sbin/ifconfig eth0", password,login_ip,username);
//	}
//
//	@Background
//	void connect_smd(String command, String passwd,String host,String username ) {
//		Channel channel = null;
//		try {
//			jsch = new JSch();
//			
//			session = jsch.getSession(username, host, PORT);
//			session.setPassword(passwd);
//
//			java.util.Properties config = new java.util.Properties();
//			config.put("StrictHostKeyChecking", "no");
//			session.setConfig(config);
//			session.connect();
//			L.d("888888888888888", "Connected to " + host + ".");
//			channel = session.openChannel("exec");
//			((ChannelExec) channel).setCommand(command);
//			channel.setInputStream(null);
//			((ChannelExec) channel).setErrStream(System.err);
//
//			channel.connect();
//			// }
//			if (pDialog != null && pDialog.isShowing()){
//				pDialog.dismiss();}
//			sendMsg();
//		} catch (JSchException e) {
//			e.printStackTrace();
//		} finally {
//			channel.disconnect();
//			session.disconnect();
//		}
//	}
//
//	@UiThread
//	void sendMsg(){
//		T.showShort(this, "发送成功");
//	}
//	/**
//	 * 连接到指定的IP
//	 * 
//	 * @throws JSchException
//	 */
//	@Background
//	void connect(String command, String passwd,String host,String user) {
//		BufferedReader reader = null;
//		Channel channel = null;
//		try {
//			jsch = new JSch();
//			session = jsch.getSession(user, host, PORT);
//			session.setPassword(passwd);
//
//			java.util.Properties config = new java.util.Properties();
//			config.put("StrictHostKeyChecking", "no");
//			session.setConfig(config);
//			session.connect();
//			L.d("888888888888888", "Connected to " + host + ".");
//
//			channel = session.openChannel("exec");
//			((ChannelExec) channel).setCommand(command);
//
//			channel.setInputStream(null);
//			((ChannelExec) channel).setErrStream(System.err);
//
//			channel.connect();
//			InputStream in = channel.getInputStream();
//			StringBuffer sb = new StringBuffer();
//			reader = new BufferedReader(new InputStreamReader(in));
//			String buf = null;
//			while ((buf = reader.readLine()) != null) {
//				System.out.println(buf);
//				sb.append(buf + "\n");
//
//			}
//			textShow(sb.toString());
//			// }
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (JSchException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				reader.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			channel.disconnect();
//			session.disconnect();
//		}
//	}
//
//	@UiThread
//	void textShow(String con) {
//		if (pDialog != null && pDialog.isShowing()){
//			pDialog.dismiss();}
//		final Dialog mDialog = new Dialog(this, R.style.CustomDialogTheme);
//		// 获取要填充的布局
//		View v = LayoutInflater.from(this).inflate(R.layout.smdevice_info, null);
//		// 设置自定义的dialog布局
//		mDialog.setContentView(v);
//		TextView result = (TextView) v.findViewById(R.id.result);
//		ImageView back = (ImageView) v.findViewById(R.id.back);
//		result.setText(con);
//		mDialog.setCanceledOnTouchOutside(false);
//		mDialog.show();
//		
//		back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				mDialog.dismiss();
//			}
//		});
//	}
//
//	@Click(R.id.ping1)
//	void ping1Click() {
//		ip = ping1_edt.getText().toString();
//		String countCmd = " -c " + count + " ";
//		String sizeCmd = " -s " + size + " ";
//		String timeCmd = " -i " + time + " ";
//		String ip_adress = ip;
//		String ping = "ping" + countCmd + timeCmd + sizeCmd + ip_adress;
//		if (TextUtils.isEmpty(ip)) {
//			T.showLong(this, "请输入正确的能耗板IP");
//			return;
//		}
//		Intent intent = new Intent();
//		intent.setClass(PingActivity.this, PingResult1_.class);
//		// new一个Bundle对象，并将要传递的数据传入
//		Bundle bundle = new Bundle();
//		bundle.putString("ping", ping);
//		bundle.putString("ip", ip);
//		bundle.putString("count", count);
//		bundle.putString("size", size);
//		bundle.putString("time", time);
//		intent.putExtras(bundle);
//		startActivity(intent);
//	}
//
//	static int checkAck(InputStream in) throws IOException {
//		int b = in.read();
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

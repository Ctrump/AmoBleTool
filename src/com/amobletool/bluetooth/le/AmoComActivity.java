package com.amobletool.bluetooth.le;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.amoheartrate.bluetooth.le.R;

public class AmoComActivity extends Activity implements View.OnClickListener {
	private final static String TAG = "DeviceScanActivity";// DeviceScanActivity.class.getSimpleName();

	static TextView Text_Recv;
	static String Str_Recv;

	static String ReciveStr;
	static ScrollView scrollView;
	static Handler mHandler = new Handler();
	static boolean ifDisplayInHexStringOnOff = true;
	static boolean ifDisplayTimeOnOff = true;
	static TextView textview_recive_send_info;
	static int Totol_Send_bytes = 0;
	static int Totol_recv_bytes = 0;
	static String SendString = "AmoMcu.com";

	ToggleButton toggleHexStr;
	ToggleButton toggleTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other);
		getActionBar().setTitle("串口助手  版本V1.02(2014.01.26)");

		findViewById(R.id.button_clear).setOnClickListener(this);
		findViewById(R.id.button_send).setOnClickListener(this);
		findViewById(R.id.button_about).setOnClickListener(this);
		findViewById(R.id.button_detail).setOnClickListener(this);

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String mac_addr = bundle.getString("mac_addr");
		String char_uuid = bundle.getString("char_uuid");
		TextView tv_mac_addr = (TextView) this
				.findViewById(R.id.textview_mac_addr);
		TextView tv_char_uuid = (TextView) this
				.findViewById(R.id.textview_char_uuid);

		tv_mac_addr.setText("设备地址:" + mac_addr);
		tv_char_uuid.setText("特征值UUID:" + char_uuid);

		textview_recive_send_info = (TextView) this
				.findViewById(R.id.textview_recive_send_info);

		Text_Recv = (TextView) findViewById(R.id.device_address);
		Text_Recv.setGravity(Gravity.CLIP_VERTICAL | Gravity.CLIP_HORIZONTAL);
		ReciveStr = "";
		Text_Recv.setMovementMethod(ScrollingMovementMethod.getInstance());
		scrollView = (ScrollView) findViewById(R.id.scroll);

		TextView text2 = (TextView) this.findViewById(R.id.edit_text);
		text2.setText(SendString);

		Totol_Send_bytes = 0;
		Totol_recv_bytes = 0;
		update_display_send_recv_info(Totol_Send_bytes, Totol_recv_bytes);

		ifDisplayInHexStringOnOff = true;
		ifDisplayTimeOnOff = true;

		toggleHexStr = (ToggleButton) findViewById(R.id.togglebutton_hex_str);
		toggleTime = (ToggleButton) findViewById(R.id.togglebutton_time_onoff);

		toggleHexStr.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				Log.i(TAG, "onCheckedChanged  arg1= " + arg1);
				ifDisplayInHexStringOnOff = arg1;
				ToggleButton toggleTime = (ToggleButton) findViewById(R.id.togglebutton_time_onoff);
				if (ifDisplayInHexStringOnOff == true) { // 字符串显�?
					toggleTime.setChecked(true);
					if (Text_Recv.length() > 0) {
						String hexString = Text_Recv.getText().toString();
						byte[] hexdata = Utils.hexStringToBytes(hexString);

						String HexStr = Utils.bytesToString(hexdata);

						Text_Recv.setText("");
						Text_Recv.append(HexStr);
					}

					toggleTime.setEnabled(true);
				} else { // hex 显示
					toggleTime.setChecked(false);
					if (Text_Recv.length() > 0) {
						String content = Text_Recv.getText().toString();
						byte[] midbytes = content.getBytes();
						String HexStr = Utils.bytesToHexString(midbytes);

						Text_Recv.setText("");
						Text_Recv.append(HexStr);
					}

					toggleTime.setEnabled(false);
				}

				scrollView.fullScroll(ScrollView.FOCUS_DOWN);// 滚动到底
			}
		});

		toggleTime.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				Log.i(TAG, "onCheckedChanged  arg1= " + arg1);
				ifDisplayTimeOnOff = arg1;

				if (ifDisplayInHexStringOnOff == false) {
					ToggleButton toggleTime = (ToggleButton) findViewById(R.id.togglebutton_time_onoff);
					toggleTime.setEnabled(false);
				}

				Text_Recv.setText("");
				scrollView.fullScroll(ScrollView.FOCUS_UP);// 滚动到底�?
				Totol_Send_bytes = 0;
				Totol_recv_bytes = 0;
				update_display_send_recv_info(Totol_Send_bytes,
						Totol_recv_bytes);
			}
		});

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.button_clear:
			Text_Recv.setText("");
			ReciveStr = "";
			Totol_Send_bytes = 0;
			Totol_recv_bytes = 0;
			update_display_send_recv_info(Totol_Send_bytes, Totol_recv_bytes);

			scrollView.fullScroll(ScrollView.FOCUS_UP);// 滚动到顶
			break;
		case R.id.button_send:
			TextView text2 = (TextView) this.findViewById(R.id.edit_text);
			if (text2.length() > 0) {
				String s1 = text2.getText().toString();
				DeviceScanActivity.writeChar6(s1);

				Totol_Send_bytes += s1.length();
				update_display_send_recv_info(Totol_Send_bytes,
						Totol_recv_bytes);

				SendString = text2.getText().toString();

			}
			break;

		case R.id.button_about:
			startActivity(new Intent(AmoComActivity.this, AboutActivity.class));
			// Uri uri = Uri.parse("www.amomcu.com");
			// Intent it = new Intent(Intent.ACTION_VIEW, uri);
			// startActivity(it);
			//
			// Intent it = new Intent(Intent.ACTION_VIEW,
			// Uri.parse("http://www.baidu.com"));
			// it.setClassName("com.android.browser",
			// "com.android.browser.BrowserActivity");
			// getBaseContext().startActivity(it);
			break;
		case R.id.button_detail:
			startActivity(new Intent(AmoComActivity.this, DetailActivity.class));
			// startActivity(new Intent (AmoComActivity.this,
			// AboutActivity.class) );
			break;
		}
	}

	public static synchronized void char6_display(String str, byte[] data,
			String uuid) {
		Log.i(TAG, "char6_display str = " + str);

		if (uuid.equals(DeviceScanActivity.UUID_HERATRATE)) {
			SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss ");
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String TimeStr = formatter.format(curDate);
			byte[] ht = new byte[str.length()];
			// System.arraycopy(ht, Totol_Send_bytes,
			// Utils.hexStringToBytes(str), 0, str.length());

			String DisplayStr = "[" + TimeStr + "] " + "HeartRate : " + data[0]
					+ "=" + data[1];
			// Text_Recv.append(DisplayStr + "\r\n");
			Str_Recv = DisplayStr + "\r\n";
		} else if (uuid.equals(DeviceScanActivity.UUID_TEMPERATURE)) // 温度测量
		{
			byte[] midbytes = str.getBytes();
			String HexStr = Utils.bytesToHexString(midbytes);
			// Text_Recv.append(HexStr);
			Str_Recv = HexStr;
		} else if (uuid.equals(DeviceScanActivity.UUID_CHAR6)) // amomcu 的串口透传
		{
			if (ifDisplayInHexStringOnOff == true)// 字符串显�?
			{
				if (ifDisplayTimeOnOff == true) {
					SimpleDateFormat formatter = new SimpleDateFormat(
							"HH:mm:ss ");
					Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
					String TimeStr = formatter.format(curDate);

					String DisplayStr = "[" + TimeStr + "] " + str;
					// Text_Recv.append(DisplayStr + "\r\n");
					Str_Recv = DisplayStr + "\r\n";
				} else {
					Str_Recv = str;
					// Text_Recv.setText(str);
				}
			} else// hex 显示
			{
				byte[] midbytes = str.getBytes();
				String HexStr = Utils.bytesToHexString(midbytes);
				// Text_Recv.append(HexStr);
				Str_Recv = HexStr;
			}

		} else // 默认显示 hex
		{
			// byte[] midbytes = str.getBytes();
			// String HexStr = Utils.bytesToHexString(midbytes);
			// // Text_Recv.append(HexStr);
			// Str_Recv = HexStr;

			if (ifDisplayInHexStringOnOff == true)// 字符串显�?
			{
				if (ifDisplayTimeOnOff == true) {
					SimpleDateFormat formatter = new SimpleDateFormat(
							"HH:mm:ss ");
					Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
					String TimeStr = formatter.format(curDate);

					String DisplayStr = "[" + TimeStr + "] " + str;
					// Text_Recv.append(DisplayStr + "\r\n");
					Str_Recv = DisplayStr + "\r\n";
				} else {
					Str_Recv = str;
					// Text_Recv.setText(str);
				}
			} else// hex 显示
			{
				byte[] midbytes = str.getBytes();
				String HexStr = Utils.bytesToHexString(midbytes);
				// Text_Recv.append(HexStr);
				Str_Recv = HexStr;
			}
		}

		Totol_recv_bytes += str.length();

		mHandler.post(new Runnable() {
			@Override
			public synchronized void run() {
				scrollView.fullScroll(ScrollView.FOCUS_DOWN);// 滚动到底
				Text_Recv.append(Str_Recv);
				update_display_send_recv_info(Totol_Send_bytes,
						Totol_recv_bytes);
			}
		});
	}

	public synchronized static String GetLastData() {
		String string = Str_Recv;
		return string;
	}

	public synchronized static void update_display_send_recv_info(int send,
			int recv) {
		String info1 = String.format("发送%4d,接收%4d [字节]", send, recv);
		textview_recive_send_info.setText(info1);
	}
}
package com.amobletool.bluetooth.le;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import com.amoheartrate.bluetooth.le.R;

public class DetailActivity extends Activity {
	private final static String TAG = DetailActivity.class.getSimpleName();
	public static final int REFRESH = 0x000001;
	private Handler mHandler = null;
	public static TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);
		getActionBar().setTitle("详情显示");

		tv = (TextView) findViewById(R.id.detail_text_help);

		// Intent intent = getIntent();
		// String value = intent.getStringExtra("testIntent");

		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == REFRESH) {
					// Log.i(TAG, "handleMessage");
					String str = AmoComActivity.GetLastData();
					tv.setText("当前环境温度 ：\r\n" + str);

				}
				super.handleMessage(msg);
			}
		};

		new MyThread().start();

	}

	public class MyThread extends Thread {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {

				Message msg = new Message();
				msg.what = REFRESH;
				mHandler.sendMessage(msg);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void onClick(View v) {
		// TODO 自动生成的方法存根
	}

}
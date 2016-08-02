package com.amobletool.bluetooth.le;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import com.amoheartrate.bluetooth.le.R;

public class AboutActivity extends Activity {
	private final static String TAG = AboutActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		getActionBar().setTitle("关于AmoMcu");

		Intent intent = getIntent();
		String value = intent.getStringExtra("testIntent");

		String help = "新产品预告：RGB灯泡即将推出，配套Android Apk与源码"
				+ "字符串显示与hex显示切换，时间可开关，表示收到数据的时间，点击“清除数据”把当前的数据清除掉,"
				+ "“详情”可显示单独某一项数据，比如ds18b20的温度计.\r\n"
				+ "	AmoMcu 现专注蓝牙4.0低功耗开发板，" + "由三人团队组成，均拥有八至十年的工作经验，"
				+ "擅长CC2540等单片机的硬、软件开发，" + "十年间负责过的产品主要有：基于S3C44B0的电能质量分析仪、"
				+ "发动机点火分析仪、汽车故障检测仪，NRF9E5无线电池数据采集系统，"
				+ "2009年开始介入基于S3C6410的产品开发，对wince系统较为熟悉，"
				+ "2012年开始深入基于Exynos4412的设备开发，"
				+ "以及基于联发科MTK6575等Android手机终端以及平板开发；"
				+ "软件上，多年从事Android底层Linux驱动和HAL等驱动开发以及APK开发......";

		TextView tv = (TextView) findViewById(R.id.about_text_help);
		tv.setText(help);
	}

	public void onClick(View v) {
		// TODO 自动生成的方法存根
	}

}
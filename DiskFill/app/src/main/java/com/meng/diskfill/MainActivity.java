package com.meng.diskfill;

import android.*;
import android.app.*;
import android.content.pm.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;

public class MainActivity extends Activity {

	private Button btn;
	private TextView tv;
	private int i=0;
	private int rest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		btn = (Button) findViewById(R.id.mainButton);
		tv = (TextView) findViewById(R.id.mainTextView);
		btn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					new Thread(new Runnable(){

							@Override
							public void run() {
								writerSD();
							}
						}).start();

				}
			});
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
		}
		tv.setText(String.format("剩余%dM", rest = getRestM()));
    }

	public void writerSD() {
        File file =new File(Environment.getExternalStorageDirectory() + "/1.fill");
        try {
			FileOutputStream outputStream = new FileOutputStream(file);
			byte[] bs=new byte[1048576];
			int target = getRestM() - 200;
			for (i = 0;i < target;++i) {
				outputStream.write(bs);
				runOnUiThread(new Runnable(){

						@Override
						public void run() {
							tv.setText(String.format("目标:%dM\n已写入%dM", rest - 200, i));
						}
					});
			}
        } catch (Exception e) {
            e.printStackTrace();
        } 
	}
	private int getRestM() {
		File datapath = Environment.getDataDirectory();
		StatFs dataFs=new StatFs(datapath.getPath());
		long sizes=(long)dataFs.getFreeBlocks() * (long)dataFs.getBlockSize();
		return (int) (sizes / ((1024 * 1024)));
	}

}

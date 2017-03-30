package com.ider.launcherpackage.launcher;

import java.util.ArrayList;
import java.util.List;

import com.ider.launcherpackage.R;
import com.ider.launcherpackage.common.ApplicationUtil;
import com.ider.launcherpackage.views.AppOpWindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AppListActivity extends FullscreenActivity {
	private static final String TAG = "AppListActivity";
	Context context = AppListActivity.this;
	GridView grid;
	AppAdapter adapter;
	List<PackageHolder> apps;
	TextView tip;
	private int selectPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_list);

		adapter = new AppAdapter(context, R.layout.applist_item, initApps());
		initViews();
		setListeners();
		registReceivers();
	}
	
	
	public void registReceivers() {
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PACKAGE_ADDED");
		filter.addAction("android.intent.action.PACKAGE_REMOVED");
		filter.addAction("android.intent.action.PACKAGE_CHANGED");
		filter.addDataScheme("package");
		registerReceiver(packageReceiver, filter);
	}
	
	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		try {
			unregisterReceiver(packageReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
	
	public List<PackageHolder> initApps() {
		apps = ApplicationUtil.queryApplication(this);
		return apps;
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStop() {
		super.onStop();

	}
	
	
	public void initViews() {
		grid = (GridView) findViewById(R.id.app_grid);
		grid.setAdapter(adapter);
		tip = (TextView) findViewById(R.id.uninstall_tip);


	}

	public void setListeners() {
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = getPackageManager().getLaunchIntentForPackage(apps.get(position).getPackageName());
				if(intent != null) {
					startActivity(intent);
				}
			}
		});

		grid.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
										   int position, long id) {
				uninstallApp(apps.get(position).getPackageName());
				return true;
			}
		});

		grid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
				AppListActivity.this.selectPosition = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {
			}
		});
	}

	public void uninstallApp(String packagename) {
		Uri uri = Uri.parse("package:" + packagename);
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		startActivity(intent);
	}

	BroadcastReceiver packageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			grid.setAdapter(new AppAdapter(context, R.layout.applist_item, initApps()));
		}
	};


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_MENU) {
			AppOpWindow.getInstance(getApplicationContext()).showAppOpWindow(getWindow().getDecorView(), apps.get(selectPosition).getPackageName());
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}

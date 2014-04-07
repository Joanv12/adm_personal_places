package com.upv.adm.adm_personal_shapes.classes;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import com.upv.adm.adm_personal_shapes.screens.screen01;
import com.upv.adm.adm_personal_shapes.screens.screen02;
import com.upv.adm.adm_personal_shapes.screens.screen03;
import com.upv.adm.adm_personal_shapes.screens.screen04;
import com.upv.adm.adm_personal_shapes.screens.screen05;
import com.upv.adm.adm_personal_shapes.screens.screen07;
import com.upv.adm.adm_personal_shapes.screens.screen18;
import com.upv.adm.adm_personal_shapes.screens.screen19;
import com.upv.adm.adm_personal_shapes.R;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CustomActionBarActivity extends ActionBarActivity implements IActivityGiver {

	public static final int ACTIVITYRESULT_FINISH = 11415;
	
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	
	@Override
	public Activity getCurrentActivity() {
		return this;
	}
	
	public void onCreate(Bundle savedInstanceState, int layout) {
		super.onCreate(savedInstanceState);
		setContentView(layout);
		GlobalContext.init(getApplicationContext());

		//--- begin of preparing options
		ArrayList<String[]> options = new ArrayList<String[]>();
		options.add(new String[]{"Inicio", "1"});
		if (!Utils.isUserLoggedIn()) {
			options.add(new String[]{"Login", "2"});
			options.add(new String[]{"Registro", "3"});
		}
		options.add(new String[]{"Perfil", "4"});
		options.add(new String[]{"Crear", "5"});
		options.add(new String[]{"Lista", "6"});
		options.add(new String[]{"Mapa", "7"});
		options.add(new String[]{"Amigos", "8"});
		options.add(new String[]{"Buscador", "9"});
		if (Utils.isUserLoggedIn())
			options.add(new String[]{"Logout", "10"});
		options.add(new String[]{"Salir", "11"});
		
		final String[] options_arr  = new String[options.size()];
		final Hashtable<String, Integer> options_ht = new Hashtable<String, Integer>();
		for (int i = 0; i < options.size(); i++) {
			options_arr[i] = options.get(i)[0];
			options_ht.put(options.get(i)[0], Integer.valueOf(options.get(i)[1]));
		}
		//--- end of preparing options
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);

		try {
			drawerList.setAdapter(
					new ArrayAdapter<String>(getSupportActionBar()
					.getThemedContext(),
					android.R.layout.simple_list_item_1,
					options_arr)
			);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		drawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int selected_option = options_ht.get(options_arr[position]);
				switch (selected_option) {
					case 1:
						Utils.startActivity(getCurrentActivity(), screen03.class, true);
						break;
					case 2:
						Utils.startActivity(getCurrentActivity(), screen01.class);
						break;
					case 3:
						Utils.startActivity(getCurrentActivity(), screen02.class);
						break;
					case 4:
						Intent in04 = new Intent(getApplicationContext(), screen02.class);
						startActivity(in04);
						break;
					case 5:
						Intent in07 = new Intent(getApplicationContext(), screen05.class);
						startActivity(in07);
						break;
					case 6:
						Intent in13 = new Intent(getApplicationContext(), screen04.class);
						startActivity(in13);
						break;
					case 7:
						Intent in12 = new Intent(getApplicationContext(), screen07.class);
						startActivity(in12);
						break;
					case 8:
						Intent in12a = new Intent(getApplicationContext(), screen18.class);
						startActivity(in12a);
						break;
					case 9:
						Intent in12abc = new Intent(getApplicationContext(), screen19.class);
						startActivity(in12abc);
						break;
					case 10:
						Utils.logoutUser();
						Utils.startActivity(getCurrentActivity(), screen01.class, true);
						break;
					case 11:
						Utils.confirmExit(getCurrentActivity());
						break;
				}
				drawerLayout.closeDrawer(drawerList);
			}
		});

		drawerToggle = new ActionBarDrawerToggle(
				this,
				drawerLayout,
				R.drawable.abc_ic_menu_moreoverflow_normal_holo_light,
				R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				ActivityCompat.invalidateOptionsMenu(CustomActionBarActivity.this);
			}

			public void onDrawerOpened(View drawerView) {
				ActivityCompat.invalidateOptionsMenu(CustomActionBarActivity.this);
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_generaltopright, menu);
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
			case R.id.menu_new:
				Log.i("ActionBar", "New!");
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
			if (
					taskList.get(0).numActivities == 1 &&
					taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())
			) {
				if (this.getClass() == screen03.class || this.getClass() == screen01.class) {
					Utils.confirmExit(this);
					return true;
				}
				else
					Utils.startActivity(getCurrentActivity(), screen03.class, true);
			}
		}
		return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // workaround to close all activities
    	if (resultCode == ACTIVITYRESULT_FINISH)
    		this.finish();
    	else
    		super.onActivityResult(requestCode, resultCode, data);
    }
}

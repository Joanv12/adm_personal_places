package com.upv.adm.adm_personal_shapes.classes;

import com.upv.adm.adm_personal_shapes.screens.screen02;
import com.upv.adm.adm_personal_shapes.screens.screen03;
import com.upv.adm.adm_personal_shapes.screens.screen04;
import com.upv.adm.adm_personal_shapes.screens.screen05;
import com.upv.adm.adm_personal_shapes.screens.screen07;
import com.upv.adm.adm_personal_shapes.screens.screen13;
import com.upv.adm.adm_personal_shapes.screens.screen19;
import com.upv.adm.adm_personal_shapes.R;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class CustomActionBarActivity extends ActionBarActivity implements IActivityGiver {

	private String[] menuOptions;
	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private CharSequence titleSection;
	private CharSequence appTitle;
	
	@Override
	public Activity getCurrentActivity() {
		return this;
	}
	
	public void onCreate(Bundle savedInstanceState, int layout) {
		super.onCreate(savedInstanceState);
		setContentView(layout);
		GlobalContext.init(getApplicationContext());

		menuOptions = new String[] {
				"Inicio",
				"Perfil",
				"Crear",
				"Lista",
				"Mapa",
				"Amigos",
				"Buscador"
		};
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);

		try {
			drawerList.setAdapter(new ArrayAdapter<String>(getSupportActionBar()
					.getThemedContext(), android.R.layout.simple_list_item_1,
					menuOptions));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		drawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				switch (position) {
					case 0:
						Intent in03 = new Intent(getApplicationContext(), screen03.class);
						in03.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(in03);
						break;
					case 1:
						Intent in02 = new Intent(getApplicationContext(), screen02.class);
						startActivity(in02);
						break;
					case 2:
						Intent in05 = new Intent(getApplicationContext(), screen05.class);
						GlobalContext.shape_id = null;
						startActivity(in05);
						break;
					case 3:
						Intent in04 = new Intent(getApplicationContext(), screen04.class);
						startActivity(in04);
						break;
					case 4:
						Intent in07 = new Intent(getApplicationContext(), screen07.class);
						startActivity(in07);
						break;
					case 5:
						Intent in13 = new Intent(getApplicationContext(), screen13.class);
						startActivity(in13);
						break;
					case 6:
						Intent in19 = new Intent(getApplicationContext(), screen19.class);
						startActivity(in19);
						break;
				}

				drawerList.setItemChecked(position, true);

				titleSection = menuOptions[position];
				getSupportActionBar().setTitle(titleSection);

				drawerLayout.closeDrawer(drawerList);
			}
		});

		titleSection = getTitle();
		appTitle = getTitle();

		drawerToggle = new ActionBarDrawerToggle(
				this,
				drawerLayout,
				R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark,
				R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(titleSection);
				ActivityCompat.invalidateOptionsMenu(CustomActionBarActivity.this);
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(appTitle);
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
}

package com.upv.adm.adm_personal_shapes.screens.debug;

import com.upv.adm.adm_personal_shapes.R;
import com.upv.adm.adm_personal_shapes.classes.BeanShape;
import com.upv.adm.adm_personal_shapes.classes.GlobalContext;
import com.upv.adm.adm_personal_shapes.classes.SQLite;
import com.upv.adm.adm_personal_shapes.classes.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

@SuppressLint("SetJavaScriptEnabled")
public class debug02_map extends Activity {
	
	WebView webview_map;
	Button
		button_showlayer,
		button_showplace,
		button_showplot,
		button_clearmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.debug02_map);
		GlobalContext.setContext(getApplicationContext());
		SQLite.staticInitialization(getApplicationContext());

		String url = "http://www.angeltools.tk/map.php";
		//String url = "http://google-developers.appspot.com/maps/documentation/javascript/examples/full/marker-simple";
		webview_map = (WebView) findViewById(R.id.webview_map);
		webview_map.getSettings().setJavaScriptEnabled(true);
		webview_map.loadUrl(url);
		
		button_showlayer = (Button) findViewById(R.id.button_showlayer);
		button_showplace = (Button) findViewById(R.id.button_showplace);
		button_showplot = (Button) findViewById(R.id.button_showplot);
		button_clearmap = (Button) findViewById(R.id.button_clearmap);
		
		button_showlayer.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.addLayerToMap("l01", webview_map);
			}
		});

		button_showplace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BeanShape place = new BeanShape(
						(long) 3,
						"Cantina Mariachi",
						"Nachos y Tapas a todas horas",
						"t01",
						"39.47169,-0.34711",
						"photoUri1"
				);
				Utils.addBeanPlaceToMap(getApplicationContext(), place, webview_map);
			}
		});

		button_showplot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String coords = "39.47214801806927,-0.34699738025665283;39.47217079342821,-0.34706711769104004;39.47218528683459,-0.34712880849838257;39.47220806218134,-0.3471797704696655;39.47222255557995,-0.34724414348602295;39.47223911946035,-0.34728705883026123;39.472195639265856,-0.3473058342933655;39.47215215904417,-0.34732192754745483;39.472110749283985,-0.34734874963760376;39.47206519851929,-0.34735947847366333;39.4720320706717,-0.34738630056381226;39.471988590347806,-0.3474023938179016;39.47194718049027,-0.34741848707199097;39.47190784110277,-0.3474453091621399;39.47186850169303,-0.34746140241622925;39.47183123275801,-0.3474801778793335;39.47178568181034,-0.34749627113342285;39.47174841283096,-0.3475204110145569;39.47170286182905,-0.347541868686676;39.4716655928053,-0.34756332635879517;39.471622112252376,-0.3475821018218994;39.47158277268109,-0.34760087728500366;39.47154964460378,-0.3476250171661377;39.47150409347175,-0.34764111042022705;39.47146268332583,-0.3476625680923462;39.47142127315528,-0.34768134355545044;39.471388145001086,-0.3476893901824951;39.47135294631997,-0.34767329692840576;39.4713301706933,-0.3476223349571228;39.471307395059156,-0.34756332635879517;39.47129704249572,-0.3475096821784973;39.47127840787767,-0.34746676683425903;39.47127012582358,-0.3474131226539612;39.47125356171247,-0.347367525100708;39.47124320914103,-0.34731924533843994;39.47122457450855,-0.3472656011581421;39.471210080902054,-0.34720927476882935;39.47119765780836,-0.3471609950065613;39.47118523471246,-0.3471073508262634;39.47116660006446,-0.34705907106399536;39.47115417696303,-0.3470054268836975;39.47119144626069,-0.34698665142059326;39.47122250399353,-0.3469651937484741;39.47126805530991,-0.3469464182853699;39.47131774762106,-0.34691959619522095;39.471369510407435,-0.346892774105072;39.4714109206088,-0.3468713164329529;39.47144611926058,-0.34684449434280396;39.471493740937575,-0.3468230366706848;39.47152893954748,-0.34680962562561035;39.47156620864441,-0.346793532371521;39.47160968923227,-0.34676671028137207;39.47164902878836,-0.34674525260925293;39.4716883683222,-0.34673184156417847;39.4717359898334,-0.3467103838920593;39.47178154081361,-0.3466916084289551;39.47181880977523,-0.34667015075683594;39.47187057218881,-0.346643328666687;39.47191198209197,-0.34662455320358276;39.471949250983734,-0.34659773111343384;39.47199066084005,-0.3465843200683594;39.47202585919853,-0.34656286239624023;39.47204656410698,-0.34662187099456787;39.47205691655889,-0.34669965505599976;39.47207762145808,-0.3467532992362976;39.47209625586209,-0.34680426120758057;39.47210867879533,-0.34687668085098267;39.47212524270285,-0.34691691398620605;";
				BeanShape plot = new BeanShape(
						(long) 5,
						"Plaza del Cedro",
						"Plaza en zona estudiantil de Valencia",
						null,
						coords,
						"photoUri2"
				);
				Utils.addBeanPlotToMap(plot, webview_map);
			}
		});
		
		button_clearmap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utils.clearMap(webview_map);
			}
		});
		
	}
		
}

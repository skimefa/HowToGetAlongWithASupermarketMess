package de.example.howtogetalongwithasupermarketmess;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class KompassAnzeige extends Activity implements SensorEventListener, android.view.View.OnClickListener{

	private static final String TAG = "SensorTest";
	private SensorManager sensorManager;
	private Sensor accS, magS;
	private float[] accF = {0,0,0};
	private float[] magF = {0,0,0};
	private float compass;
	
	private Button anfangButton;
    private Button beendenButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kompass_anzeige);
		
		this.sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		this.accS = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		this.magS = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		this.sensorManager.registerListener(this, accS, SensorManager.SENSOR_DELAY_UI);
		this.sensorManager.registerListener(this, magS, SensorManager.SENSOR_DELAY_UI);
		
		anfangButton = (Button)findViewById(R.id.zurueckButton);
		anfangButton.setOnClickListener(this);
		beendenButton = (Button)findViewById(R.id.button1);
		beendenButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kompass_anzeige, menu);
		return true;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		this.sensorManager.unregisterListener(this);
		
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void changeText() {
		TextView compasstext = (TextView) findViewById(R.id.compass);
		compasstext.setText(": "+this.compass);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
			this.magF = event.values.clone();
		} else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			this.accF = event.values.clone();
		}

			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, this.accF, this.magF);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				// Convert the azimuth to degrees in 0.5 degree resolution.
				this.compass = (float) Math.round(Math.toDegrees(orientation[0]));
				// Adjust the range: 0 < range <= 360 (from: -180 < range <= 180).
				this.compass = (this.compass + 360) % 360;
				// alternative: compass = compass>=0 ? compass : compass+360;

				this.changeText();
			}
			
			//aktuelle Daten senden


			Log.i(TAG, "Compass: "+compass);

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == anfangButton)
		{  
			Intent intent = new Intent(KompassAnzeige.this, Hauptmenue.class);
	        startActivity(intent);
		}
		
		if (v == beendenButton)
		{  
			finish();
		}
	}

}

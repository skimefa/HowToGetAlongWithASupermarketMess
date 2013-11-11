package de.example.howtogetalongwithasupermarketmess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Hauptmenue extends Activity implements OnClickListener {

	private Button nfcButton;
	private Button abschickenButton;
    private Button resetButton;
    Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hauptmenue);
		
		nfcButton = (Button)findViewById(R.id.button1);
		nfcButton.setOnClickListener(this);
		resetButton = (Button)findViewById(R.id.button2);
		resetButton.setOnClickListener(this);
		
		abschickenButton = (Button) findViewById(R.id.abschicken);
		abschickenButton.setOnClickListener(this);
		
		
		
		/*EditText nameField = (EditText) findViewById(R.id.textView1);
		String name = nameField.getText().toString();
		if (name.length() > 2) {
			//setContentView(R.layout.activity_anzeige);
		}*/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hauptmenue, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == nfcButton)
		{  
			intent = new Intent(Hauptmenue.this, Anzeige.class);
	        startActivity(intent);
		}
		if(v == abschickenButton){
			EditText et = (EditText) findViewById(R.id.editText1);
			String artikel = et.getText().toString();
			
			Intent intent = new Intent(Hauptmenue.this, de.example.howtogetalongwithasupermarketmess.data.ArtikelAnzeigen.class);
			intent.putExtra("artikel", artikel);
			startActivity(intent);
			
		}
		if(v == resetButton){
			EditText et = (EditText) findViewById(R.id.editText1);
			et.setText("");
		}
	}

}

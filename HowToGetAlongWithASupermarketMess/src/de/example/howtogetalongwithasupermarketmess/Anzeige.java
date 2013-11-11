package de.example.howtogetalongwithasupermarketmess;

import java.util.List;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.ndeftools.*;
import org.ndeftools.wellknown.TextRecord;

@SuppressLint("NewApi")
public class Anzeige extends Activity implements OnClickListener {

	//attribute wird vorerst auf feste Variable gesetzt
	private String attribute="Oliven";
	private String ort="";
	private Button hinwollenButton;
	public static final String TAG = "NfcDemo";
	private TextView mTextView;
	private NfcAdapter mNfcAdapter;
	Hauptmenue menue;
	PendingIntent mPendingIntent;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anzeige);

		hinwollenButton = (Button) findViewById(R.id.hinwollen);
		hinwollenButton.setOnClickListener(this);

		// Sehen, ob NFC ausgeschaltet ist, oder nicht.
		mTextView = (TextView) findViewById(R.id.NFC_View);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter == null) {
			// Wenn es keinen NFC-Adapter gibt, können wir die App nicht nutzen.
			Toast.makeText(this, "This device doesn't support NFC.",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		if (!mNfcAdapter.isEnabled()) {
			mTextView.setText("NFC is disabled.");
		} else {
			mTextView.setText(R.string.explanation);
		}
		handleIntent(getIntent());
		// Erstellen eines pending Intent, der dann an die
		// enableForeGroundDispatch()-Methode übergeben wird, damit der
		// Tagempfang
		// direkt verarbeitet werden kann
		// mPendingIntent = PendingIntent.getActivity(this,0,new Intent(this,
		// getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);
		// NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this,pint,null,null);

		Intent intent = getIntent();
		Log.i(TAG, "intent: " + intent.toString());
		resolveIntent(intent);
		// INTENT WOHER???
		// Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		Log.i(TAG, "create");
		
		//Setzen des Attributfeldes auf den Wert, der bei der Suche eingegeben wurde
		//	attribute= textRecord.getText();
			TextView text1 = (TextView) findViewById(R.id.GesuchterArtikel);
			text1.setText(attribute);
	}

	@Override
	public void onNewIntent(Intent intent) {
		Log.i(TAG, "new Intent");
		setIntent(intent);
		resolveIntent(intent);
	}

	void resolveIntent(Intent intent) {
		// Parse the intent
		String action = intent.getAction();
		// oder ACTION_TAG_DISCOVERED
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			// When a tag is discovered we send it to the service to be save. We
			// include a PendingIntent for the service to call back onto. This
			// will cause this activity to be restarted with onNewIntent(). At
			// that time we read it from the database and view it.
			Parcelable[] rawMsgs = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			NdefMessage[] msgs;
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			} else {
				// Unknown tag type
				byte[] empty = new byte[] {};
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN,
						empty, empty, empty);
				NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
				// Alles aus dem Tag steht hier drin
				msgs = new NdefMessage[] { msg };
			}
			// Setup the views
			// setTitle(R.string.title_scanned_tag);
			buildTagViews(msgs);
			Log.i(TAG, "msg: " + msgs.toString());
		} else {
			Log.e(TAG, "Unknown intent " + intent);
			// finish();
			return;
		}
	}

	void buildTagViews(NdefMessage[] msgs) {
		if (msgs == null || msgs.length == 0) {
			return;
		}
		LayoutInflater inflater = LayoutInflater.from(this);

		// LinearLayout content = mTagContent;
		// Clear out any old views in the content area, for example if you scan
		// two tags in a row.
		// content.removeAllViews();
		// Parse the first message in the list
		// Build views for all of the sub records
		List<Record> records;
		try {
			records = new Message(msgs[0]);
			final int size = records.size();
			for (int i = 0; i < size; i++) {
				Record record = records.get(i);
				getView(record);
				/*
				 * View view = getView(record, inflater, content, i); if(view !=
				 * null) { content.addView(view); }
				 * inflater.inflate(R.layout.tag_divider, content, true);
				 */
			}
		} catch (FormatException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get view for different types of records
	 */

	// private View getView(Record record, LayoutInflater inflater, LinearLayout
	// content, int offset) {
	private View getView(Record record) {
		if (record instanceof TextRecord) {
			TextRecord textRecord = (TextRecord) record;

			TextView text = (TextView) findViewById(R.id.NFCAnzeige);
		//	text.setText(textRecord.getText());
			ort="Test";
			//if(OrtungErkannt(ort)){
			text.setText("Danke, Ortung erfolgreich erkannt!");
		//	}
			return text;
		} /*
		 * else if(record instanceof SmartPosterRecord) { SmartPosterRecord
		 * smartPosterRecord = (SmartPosterRecord)record; if
		 * (smartPosterRecord.hasTitle()) { // Build a container to hold the
		 * title and the URI LinearLayout container = new LinearLayout(this);
		 * container.setOrientation(LinearLayout.VERTICAL);
		 * container.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
		 * LayoutParams.WRAP_CONTENT));
		 * container.addView(getView(smartPosterRecord.getTitle(), inflater,
		 * container, offset)); inflater.inflate(R.layout.tag_divider,
		 * container); container.addView(getView(smartPosterRecord.getUri(),
		 * inflater, container, offset)); return container; } else { // Just a
		 * URI, return a view for it directly return
		 * getView(smartPosterRecord.getUri(), inflater, content, offset); } }
		 * else if(record instanceof UriRecord) { UriRecord uriRecord =
		 * (UriRecord)record;
		 * 
		 * TextView text = (TextView) inflater.inflate(R.layout.tag_text,
		 * content, false); text.setText(uriRecord.getUri().toString()); return
		 * text; }
		 */

		return null;
	}

	// Aufrufen der Methode disableForgroundDispatch, wenn die App verlassen
	// wird
	@Override
	protected void onPause() {
		super.onPause();
		if (NfcAdapter.getDefaultAdapter(this) != null) {
			NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
		}
	}

	private void handleIntent(Intent intent) {
		// TODO: handle Intent
	}
	
	private boolean OrtungErkannt(String ort){
		boolean gesetzt=false;
		if(ort!=""){
			gesetzt=true;
		}
		return gesetzt;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.anzeige, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == hinwollenButton) {
			Intent intent = new Intent(Anzeige.this, KompassAnzeige.class);
			startActivity(intent);
		}
	}

}

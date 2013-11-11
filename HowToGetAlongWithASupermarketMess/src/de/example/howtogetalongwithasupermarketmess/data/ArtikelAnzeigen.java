package de.example.howtogetalongwithasupermarketmess.data;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;
import de.example.howtogetalongwithasupermarketmess.Anzeige;
import de.example.howtogetalongwithasupermarketmess.R;

// Klasse zum Anzeigen der Datensätze aus der Datenbank in einer Liste

public class ArtikelAnzeigen extends ListActivity {
	private ArtikelDb artikelDb;

	private Cursor cursor;
	private Adapter adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Die Eingabe aus der Hauptmenue-Klasse wird aus dem übergebennen Intent ausgelesen
		// und in die Variable artikel gespeichtert.
		String artikel = getIntent().getExtras().getString("artikel");
		artikelDb = new ArtikelDb(this);
		
		//Aufruf der Methode zum Anzeigen der Artikel
		artikelAnzeigen(artikel);

		registerForContextMenu(getListView());

	}

	private void artikelAnzeigen(String artikel) {
		// Aufruf der Methode insert aus der Klasse ArtikelDb zum Einfügen der Datensätze
		// in die Datenbank
		artikelDb.insert();
		// Es wird die Methode select aus der Klasse ArtikelDb aufgerufen mit dem Übergabe Paramter artikel, der
		// aus dem Intent ausgelesen wurde. Dies wird dem Cursor übergeben.
		cursor = artikelDb.select(artikel);
		
		adapter = new Adapter(this, cursor);
		setListAdapter(adapter);
	}

	// Methode zum Erstellen eines Context-Menues 
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		// Kontextmenue entfalten
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context, menu);
	}

	@Override
	// Methode die auf einen langen Tastendruck reagiert.
	// Anzeige des Menues.
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.delete_button:
			artikelDb.delete(info.id);
			updateList();
			return true;
		case R.id.deleteAll_button:
			artikelDb.deleteAll();
			updateList();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}
	// Methode die auf einen Klick in der ListView reagiert.
		@Override
		protected void onListItemClick(ListView l, View v, int position, long id) {
			
			super.onListItemClick(l, v, position, id);
			
			EditText et = (EditText) findViewById(R.id.textName);
			String artikelName = et.getText().toString();
			
			Intent intent = new Intent(this, Anzeige.class);
			intent.putExtra("artikelName",artikelName);
			startActivity(intent);
		}

	private void updateList() {
		// zunaechst Cursor, dann Liste aktualisieren
		cursor.requery();
		adapter.notifyDataSetChanged();
	}
}

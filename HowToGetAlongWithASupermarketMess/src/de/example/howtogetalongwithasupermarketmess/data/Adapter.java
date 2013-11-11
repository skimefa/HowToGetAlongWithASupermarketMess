package de.example.howtogetalongwithasupermarketmess.data;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.example.howtogetalongwithasupermarketmess.R;

public class Adapter extends CursorAdapter {

	private LayoutInflater layout;

	public Adapter(Context context, Cursor c) {
		super(context, c);
		layout = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor c) {
		// TODO Auto-generated method stub
		String name = c.getString(c.getColumnIndex(ArtikelDb.NAME));
		String cost = c.getString(c.getColumnIndex(ArtikelDb.COST));
		String quantity = c.getString(c.getColumnIndex(ArtikelDb.QUANTITY));

		TextView textName = (TextView) view.findViewById(R.id.textName);
		textName.setText(name);
		TextView textCost = (TextView) view.findViewById(R.id.textCost);
		textCost.setText(cost + " €");
		TextView textQuantity = (TextView) view.findViewById(R.id.textQuantity);
		textQuantity.setText(quantity);
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return layout.inflate(R.layout.activity_artikel_db, null);
	}
}

package com.walter.lesson17;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.UUID;

public class Database extends SQLiteOpenHelper {

	public Database(Context context) {
		super(context, "products", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql =
			    "CREATE TABLE IF NOT EXISTS products "+
                "(id INTEGER PRIMARY KEY AUTOINCREMENT,"+ 
                " uid TEXT NOT NULL, " +		
				" names TEXT NOT NULL, " +
				" quantity INTEGER NOT NULL," +				
				" status TEXT NOT NULL)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql="DROP TABLE IF EXISTS products";
		db.execSQL(sql);		
	}
   	/**
	 * Saves an item into sqlite database
	 * @param names
	 * @param quantity
	 */
	public void save(String names, int quantity)
	{
	  SQLiteDatabase db=this.getWritableDatabase();
	  ContentValues values=new ContentValues();
	  values.put("names", names);
	  values.put("quantity", quantity);
	  values.put("status", "no");
	  UUID uid= UUID.randomUUID();
	  values.put("uid", uid.toString());
	  db.insert("products", null, values);
	  db.close();
	}
	/**
	 * Fetches all unsynced records from the database
	 * Where the status is no
	 * @return
	 */
	public ArrayList<Product> fetch()
	{
		ArrayList<Product> data=new ArrayList<Product>();
		SQLiteDatabase db=this.getReadableDatabase();
		String sql="SELECT * FROM products WHERE status='no'";
		Cursor cursor= db.rawQuery(sql, null);
		if(cursor.moveToFirst())
		{
		  do
		  {
			String uid= cursor.getString(1); 
			String names= cursor.getString(2);
			int quantity= cursor.getInt(3);
			Product p=new Product();
			p.setNames(names);
			p.setQuantity(quantity);
			p.setUid(uid);
			data.add(p);			
		  }while(cursor.moveToNext());	
		}
		return data;
	}
	/**
	 * Updates the database with info received 
	 * from the server
	 */
	public void update(String uid, String status)
	{
	 SQLiteDatabase db=this.getWritableDatabase();
	 String sql="UPDATE products SET status='"+status+"' WHERE uid ='"+uid+"'";  
	 db.execSQL(sql);
	 db.close();		
	}
	/**
	 * Counts All Unsynced Records in sqlite
	 * @return
	 */
	public int count()
	{
		SQLiteDatabase db=this.getReadableDatabase();
		String sql="SELECT * FROM products WHERE status='no'";
		Cursor cursor =db.rawQuery(sql, null);		
		return cursor.getCount();
	}
	
	
	
	
	
	
}

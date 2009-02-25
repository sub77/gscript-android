package nl.rogro.GScript;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class GScriptEdit extends Activity {
	SQLiteDatabase mDatabase;
	EditText EditTextName;
	EditText EditTextScript;
	CheckBox CheckboxSu;
	
	protected static int EditScriptId;
	ContentValues updateValues;
	
	/** Database */
	protected static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, "gscript.sqlite", null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String ddlScripts = "create table scripts (_id integer primary key, name text, script text, su short);";
			db.execSQL(ddlScripts);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		}
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edititem);

		
		DatabaseHelper hlp = new DatabaseHelper(this);                 
        mDatabase = hlp.getWritableDatabase(); 

        EditTextName = (EditText)findViewById(R.id.EditName);
        EditTextScript = (EditText)findViewById(R.id.EditScript);
        CheckboxSu = (CheckBox)findViewById(R.id.EditSu);
       
        Cursor eCursor = mDatabase.query(false, "scripts", new String[] { "_id", "name", "script", "su" }, "_id="+String.valueOf(EditScriptId), null, null, null, null, null);
        eCursor.moveToFirst();
        
        EditTextName.setText(eCursor.getString(1).toString());
        EditTextScript.setText(eCursor.getString(2));
        if(eCursor.getShort(3)==0)
        {
        	CheckboxSu.setChecked(false);
        } else {
        	CheckboxSu.setChecked(true);
        }
        
		Button btnCancel = (Button) findViewById(R.id.ButtonEditCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setResult(android.app.Activity.RESULT_CANCELED);
				finish();
			}

		});
		
		Button btnSave = (Button) findViewById(R.id.ButtonEditSave);
		btnSave.setOnClickListener(

				new OnClickListener()
				{
					public void onClick(View v) {
						
						String Name = EditTextName.getText().toString();
						String Script = EditTextScript.getText().toString();
						
						boolean SU = CheckboxSu.isChecked();
						short NeedsSU = 0;

						if(SU)
						{
							NeedsSU = 1;
						}
						
				        ContentValues updateValues = new ContentValues();
				        updateValues.put("name", Name);
				        updateValues.put("script", Script);
				        updateValues.put("su", NeedsSU);
				        
				        mDatabase.update("scripts", updateValues, "_id="+String.valueOf(EditScriptId), null);

						Toast toast = Toast.makeText(GScriptEdit.this, "Script saved", Toast.LENGTH_LONG);
					    toast.show();
				        
				        setResult(android.app.Activity.RESULT_OK);
						finish();
					}
				}
		);

	}

}

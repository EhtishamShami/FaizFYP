package edu.apcoms.bse17;







import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class AndiText extends Activity {
	
	private static final String TAG = AndiText.class.getSimpleName();

	private ImageButton TakeImage;
	private ImageButton BrowseImage;
	private ImageButton setting;
	
	private SharedPreferences pref;
	private int PICK_IMAGE=2;
	public static Bitmap OPic;
	public static Uri picUriD;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_andi_text);
		
		defaultValues();
		
		TakeImage = (ImageButton) findViewById(R.id.cambtn);
		BrowseImage = (ImageButton) findViewById(R.id.browse);
		setting = (ImageButton) findViewById(R.id.sys);
		
        TakeImage.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View v) {
            	Log.d(TAG,"A Class: goTo Camera Button, onClick");
            	openCamera();            	
            }
        });
        
        BrowseImage.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
            	Log.d(TAG,"A Class: goTo Broowse Button, onClick");
            	final Intent intent = new Intent();
        		intent.setAction(Intent.ACTION_PICK);
        		intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
             	startActivityForResult(intent,PICK_IMAGE);
            	
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
          	Log.d(TAG,"A Class: goTo Setting Button, onClick");
          	Intent intent = new Intent(AndiText.this, PreferencesActivity.class);
          	startActivity(intent);
          	
          }
      });

	}

	  private static final int SETTINGS_ID = Menu.FIRST;
	  private static final int ABOUT_ID = Menu.FIRST + 1;
	  
	  
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    //    MenuInflater inflater = getMenuInflater();
	    //    inflater.inflate(R.menu.options_menu, menu);
	    super.onCreateOptionsMenu(menu);
	    menu.add(0, SETTINGS_ID, 0, "Settings").setIcon(android.R.drawable.ic_menu_preferences);
	    menu.add(0, ABOUT_ID, 0, "About").setIcon(android.R.drawable.ic_menu_info_details);
	    return true;
	  }
	 

	@Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	    Intent intent;
	    switch (item.getItemId()) {
	    case SETTINGS_ID: {
	      intent = new Intent().setClass(this, PreferencesActivity.class);
	      startActivity(intent);
	      	  break;
	    }
	    case ABOUT_ID: {
	      //intent = new Intent(this, HelpActivity.class);
	      //intent.putExtra(HelpActivity.REQUESTED_PAGE_KEY, HelpActivity.ABOUT_PAGE);
	      //startActivity(intent);
	      break;
	    }
	    }
	    return super.onOptionsItemSelected(item);
	  }

	static boolean  brow=false;
	
void openCamera(){
 	
	try{
		
		brow=false;
		startActivity(new Intent(AndiText.this, CameraPreview.class));
		
	}
	catch(ActivityNotFoundException anfe){ 
	    //display an error message     String errorMessage = "Whoops - your device doesn't support capturing images!";
	    Toast toast = Toast.makeText(this, "Error Accoured in Camera", Toast.LENGTH_SHORT);
	    toast.show(); 

}
}

public static final String DEFAULT_TARGET_LANGUAGE_CODE = "ur";
public static final boolean DEFAULT_TOGGLE_TRANSLATION = true;
public static final boolean DEFAULT_TOGGLE_BEEP = false;

void defaultValues()
{
	pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
	pref.edit().putString("targetLanguageCodeTranslationPref", DEFAULT_TARGET_LANGUAGE_CODE).commit();
	pref.edit().putBoolean("preference_translation_toggle_translation",DEFAULT_TOGGLE_TRANSLATION).commit();
	pref.edit().putBoolean("preferences_play_beep",DEFAULT_TOGGLE_BEEP).commit();
	
	
}
protected void onActivityResult(int requestCode, int resultCode, Intent data){
	
	if(requestCode==PICK_IMAGE && resultCode == RESULT_OK){
		

	    
		picUriD = data.getData();
     	String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(picUriD, filePathColumn, null, null, null);
        cursor.moveToFirst();
        cursor.close();
        brow=true;
        startActivity(new Intent(AndiText.this, camera.class));

	}
}






}

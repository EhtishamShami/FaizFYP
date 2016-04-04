package edu.apcoms.bse17;




import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;





import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract.Constants;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class camera extends Activity implements OnInitListener {

	
	private int PIC_CROP = 2;
	private Uri picUri=null;
	Bitmap CPic=null;
	ImageView picView1;
	Button btn,btn1,btn2;
	boolean BR =false;
	boolean internet=false;
	
	private TextToSpeech tts;
	
	protected static String _path;
	
	public static final String lang = "eng";
	public static final String DATA_PATH = Environment
			.getExternalStorageDirectory().toString() + "/DegreeProject/";
	private static final String TAG = "Extract.java";
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		
		
		
		
		
		
		
		
		setContentView(R.layout.camera);
		
		String[] paths = new String[] { DATA_PATH, DATA_PATH + "tessdata/" };

		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.v(TAG, "ERROR: Creation of directory " + path + " on sdcard failed");
					return;
				} else {
					Log.v(TAG, "Created directory " + path + " on sdcard");
				}
			}

		}
		
		
		if (!(new File(DATA_PATH + "tessdata/" + lang + ".traineddata")).exists()) {
			try {

				AssetManager assetManager = getAssets();
				InputStream in = assetManager.open("tessdata/eng.traineddata");
				//GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(DATA_PATH
						+ "tessdata/eng.traineddata");

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				//while ((lenf = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				//gin.close();
				out.close();
				
				Log.v(TAG, "Copied " + lang + " traineddata");
			} catch (IOException e) {
				Log.e(TAG, "Was unable to copy " + lang + " traineddata " + e.toString());
			}
		}
		
		
		
		
		
		
		
		 Intent checkTTSIntent = new Intent();
         checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
         startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
		
		
		
		
		
		
		picView1 = (ImageView)findViewById(R.id.imageView1);
		if(AndiText.brow){
			picUri = AndiText.picUriD;
			performCrop();
		}
		else{
			CPic = CameraPreview.bitmap;
			picView1.setImageBitmap(CPic);
		}
		
		btn1 = (Button)findViewById(R.id.extract);                
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
            	
            	
            	
               
            	extract_txt();
       
            	
            	
            	
            }
        });
        btn = (Button)findViewById(R.id.trans);                
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               
            	
            	
            
            		translator();
               
            	
            	
            	
            }
        });
        
        
        
        btn2 = (Button)findViewById(R.id.btnSpeek);
        
            
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               
            	
          
            
            	
            	 speakWords(recognizedText);
            	
            	
            	
            }
        });
             	
	}
	
	
	void speakout(){
		 Toast toast = Toast.makeText(this,recognizedText , Toast.LENGTH_SHORT); 
 	    toast.show();
		
	}
	
	public static String recognizedText="[DM]Shami";
	TextView tv;
	EditText et;
	 public void extract_txt(){

    	
    	try{
    		recognizedText= extractimg();
    		et = (EditText)findViewById(R.id.editT);
    		et.setText(recognizedText);
    		picView1 = (ImageView)findViewById(R.id.imageView1);
        	picView1.setImageBitmap(ocrResult.getBitmap());
    	

    	}
    	catch(ActivityNotFoundException anfe){ 
    	    //display an error message     String errorMessage = "Whoops - your device doesn't support the crop action!"; 
    	    Toast toast = Toast.makeText(this, "Extraction Error", Toast.LENGTH_SHORT); 
    	    toast.show(); 
    	}
    	
    	
    }

	 private TextToSpeech myTTS;
	 private int MY_DATA_CHECK_CODE = 0;
	 
	  private void speakWords(String speech) {
          //speak straight away
          myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
  }
protected void onActivityResult(int requestCode, int resultCode, Intent data){
	
	if (requestCode==PIC_CROP && resultCode==RESULT_OK){

		  Bundle extras = data.getExtras();
		  CPic = extras.getParcelable("data");
		  
		  picView1.setImageBitmap(CPic);
	}
	else if (requestCode == MY_DATA_CHECK_CODE) {
        if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
            //the user has the necessary data - create the TTS
        myTTS = new TextToSpeech(this, this);
        }
        else {
                //no data - install it now
            Intent installTTSIntent = new Intent();
            installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installTTSIntent);
        }
    }
	else
		this.finish();
		
}

private void performCrop(){
		try { 
    		
    		Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
    	 	cropIntent.setDataAndType(picUri, "image/*"); 
    	    cropIntent.putExtra("crop", "true");
    	    cropIntent.putExtra("aspectX", 1); 
    	    cropIntent.putExtra("aspectY", 0);
    	    
    	    //cropIntent.putExtra("aspectX", 0); 
    	    //cropIntent.putExtra("aspectY", 1);
    	    
   	    
    	    cropIntent.putExtra("return-data", true); 
    	    startActivityForResult(cropIntent, PIC_CROP); 
    	} 
    	catch(ActivityNotFoundException anfe){ 
    	    //display an error message     String errorMessage = "Whoops - your device doesn't support the crop action!"; 
    	    Toast toast = Toast.makeText(this, "Crop Error", Toast.LENGTH_SHORT); 
    	    toast.show(); 
    	}
	}
private String characterBlacklist = "";
private String characterWhitelist="!?@#$%&*()<>_-+=/.,:;'\"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
private int pageSegmentationMode = TessBaseAPI.PageSegMode.PSM_AUTO;











public void translator()
{
	
	

	
	class bgStuff extends AsyncTask<Void, Void, Void>{
		private ProgressDialog dialog = new ProgressDialog(camera.this);
        String translatedText = "";
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
            	 String text = recognizedText;
                		
                translatedText = translate(text);
                internet=true;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            	
                internet=false;
            	
         	  
            }
             
            return null;
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Translating Please wait ...");
            this.dialog.show();
        }
        @Override
        protected void onPostExecute(Void result) {
        	 if (dialog.isShowing()) {
                 dialog.dismiss();
             }
            // TODO Auto-generated method stub
            ((EditText) findViewById(R.id.TranslatedText)).setText(translatedText);
            
            super.onPostExecute(result);
            if(internet==false)
            displayExceptionMessage("No Internet Connection !!");
        }
         
    }
     
    new bgStuff().execute();

	
	
	
	
}
private boolean isTranslationActive;
String Code;
private SharedPreferences pref;

@Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	 pref = PreferenceManager.getDefaultSharedPreferences(this);
	 PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	  //obj.getString("targetLanguageCodeTranslationPref", "Urdu");
	 Code = pref.getString("targetLanguageCodeTranslationPref", "ur");
	 isTranslationActive = pref.getBoolean("preference_translation_toggle_translation", false);
	    if(isTranslationActive){
	    	 btn = (Button)findViewById(R.id.trans);
	    	 btn.setVisibility(View.VISIBLE);
	    	 ((EditText)findViewById(R.id.TranslatedText)).setVisibility(View.VISIBLE);
	    	 
	    }
	    else{
	    	 btn = (Button)findViewById(R.id.trans);
	    	 btn.setVisibility(View.GONE);
	    	 ((EditText)findViewById(R.id.TranslatedText)).setVisibility(View.GONE);
	    }
	    
	   

}


public void displayExceptionMessage(String msg)
{
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
}

public String translate(String text) throws Exception{
    
	
    // Set the Client ID / Client Secret once per JVM. It is set statically and applies to all services
       Translate.setClientId("BSSE-17"); //Change this
       Translate.setClientSecret("TPbMABLZuo853IUG7okXcAGRkRof5pamKkMWVGIcXVo"); //change
        
        
       String translatedText = "";
        
       translatedText = Translate.execute(text,Language.fromString(Code));
        
       return translatedText;
   }

private OcrResult ocrResult;

public String extractimg()
{
	 
	camera._path = DATA_PATH + "/ocr.jpg";
	
	      	CPic = toGrayscale(CPic);	
	       	CPic = CPic.copy(Bitmap.Config.ARGB_8888, true);
	         	Log.v(TAG, "Before baseApi");

	            	TessBaseAPI baseApi = new TessBaseAPI();
	            	baseApi.setDebug(true);
	            	baseApi.setPageSegMode(pageSegmentationMode);
	            	baseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, characterBlacklist);
	            	baseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, characterWhitelist);
	            	baseApi.init(DATA_PATH, camera.lang);
	            	
	            	baseApi.setImage(CPic);
	            	
	            	recognizedText = baseApi.getUTF8Text();
	            	
	            	 ocrResult = new OcrResult();
	                 ocrResult.setWordConfidences(baseApi.wordConfidences());
	                 ocrResult.setMeanConfidence( baseApi.meanConfidence());
	                 ocrResult.setRegionBoundingBoxes(baseApi.getRegions().getBoxRects());
	                 ocrResult.setTextlineBoundingBoxes(baseApi.getTextlines().getBoxRects());
	                 ocrResult.setWordBoundingBoxes(baseApi.getWords().getBoxRects());
	                 ocrResult.setCharacterBoundingBoxes(baseApi.getWords().getBoxRects());
	                 ocrResult.setBitmap(CPic);
	                 ocrResult.setText(recognizedText);
	            	
	            	baseApi.end();

	            	Log.v(TAG, "OCRED TEXT: " + recognizedText);

	            	
	                           
	            	
	         	  
return recognizedText;	
	   
			
}

public static Bitmap toGrayscale(Bitmap bmpOriginal)
{        
    int width, height;
    height = bmpOriginal.getHeight();
    width = bmpOriginal.getWidth();    

    Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    Canvas c = new Canvas(bmpGrayscale);
    Paint paint = new Paint();
    ColorMatrix cm = new ColorMatrix();
    cm.setSaturation(0);
    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
    paint.setColorFilter(f);
    c.drawBitmap(bmpOriginal, 0, 0, paint);
    return bmpGrayscale;
}


    //setup TTS

@Override
public void onInit(int status) {
	   if (status == TextToSpeech.SUCCESS) {
	        if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
	            myTTS.setLanguage(Locale.US);
	    }
	    else if (status == TextToSpeech.ERROR) {
	        Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
	    }	
	
}






}

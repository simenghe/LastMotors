package com.noteasy.simeng.motors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOError;


public class SettingsActivity extends AppCompatActivity {
    public String[] collectedAddresses; //initiate collected addresses
    public static boolean isSaved;
    Button btnSend;
    Button btnSave;
    Button btnLoad;
    public EditText[] addressEdits;
    String[] defaultAddresses = new String[]{"http://192.168.0.107:1234", "/forward", "/backward", "/right", "/left","/stop", "/hands", "/slow"}; //make a list of default addresses if user hasn't entered anyhting
    String[] list; //my list of addresses
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnSend = (Button) findViewById(R.id.btnSend); //intiate buttons
        btnSave = (Button) findViewById(R.id.btnSave);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        //Take from all the needed editTexts!
        addressEdits = new EditText[]{findViewById(R.id.editIP), findViewById(R.id.editUp), findViewById(R.id.editDown), findViewById(R.id.editRight), findViewById(R.id.editLeft),findViewById(R.id.editStop), findViewById(R.id.editHand), findViewById(R.id.editExtra)};
        list = new String[addressEdits.length]; //declare the size
        btnSave.setOnClickListener(new View.OnClickListener() { //Save the profile into the file.
            @Override
            public void onClick(View view) {
                boolean valuesReset=false;
                isSaved = true;
                for (int i = 0; i < addressEdits.length; i++) {
                    if (addressEdits[i].getText().toString().length() == 0) {
                        list[i] = defaultAddresses[i];
                        valuesReset=true;
                    } else {
                        list[i] = new String(addressEdits[i].getText().toString());
                    }
                }
                if(valuesReset){
                    Toast.makeText(getApplicationContext(),"Some/All values were reset!",Toast.LENGTH_SHORT).show();//show if you lost the values from pressing save on empty.
                }
                StringBuilder sb = new StringBuilder(); //append so it can read in later
                for (String s : list) {
                    sb.append(s);
                    sb.append(",");
                }
                SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE); //key value pair storeage using editor
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("list", sb.toString());
                editor.putBoolean("isSaved", isSaved);
                editor.apply(); //assync compared to commit.
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeIntent = new Intent(getApplicationContext(), MainActivity.class);
                try {
                    if (collectedAddresses!=null) { //if not null allow it to send to other activity.
                        if(!IsNull(collectedAddresses)){
                            changeIntent.putExtra("Addresses", collectedAddresses);
                            startActivity(changeIntent);
                        }
                    }
                    else{//other wise don't allow it.
                        Toast.makeText(getApplicationContext(),"Contents were null, try saving and loading before sending to the next activity!",Toast.LENGTH_LONG).show();
                    }
                } catch (IOError error) { //error catch
                    System.out.println("Error has occurred.");
                }
            }
        });
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);//get the saved items.
                boolean saved = settings.getBoolean("isSaved", false);
                if (isSaved || saved) {
                    String wordString = settings.getString("list", ""); //split them up from the appended
                    collectedAddresses = wordString.split(",");
                    if (!IsNull(collectedAddresses)) {
                        for (int i = 0; i < collectedAddresses.length; i++) { //you can set the text so it ios easier for usres
                            System.out.println("Collected address" + i + collectedAddresses[i]);
                            addressEdits[i].setText(collectedAddresses[i]);
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "The addresses are not setup.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean IsNull(String[] dataArray) {
        for (int i = 0; i < dataArray.length; i++) {
            if (dataArray[i].length() == 0 || dataArray[i] == null||dataArray[i].isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
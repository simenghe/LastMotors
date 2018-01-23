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

import org.w3c.dom.Text;

import java.io.IOError;
import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends AppCompatActivity {
    public static String[] collectedAddresses = new String[7];
    public static boolean isSaved;
    public Toast toast;
    EditText editIP;
    EditText editUp;
    Button btnSend;
    Button btnSave;
    Button btnLoad;
    public EditText[] addressEdits;
    String[] defaultAddresses = new String[]{"http://192.168.0.107:1234", "/forward", "/backward", "/right", "/left", "/hands", "/extras"};
    TextView txtTest;
    List<String> list;

    //public static String[] collectedAddresses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        txtTest = (TextView) findViewById(R.id.txtText);
        //Take from all the needed editTexts.
        addressEdits = new EditText[]{findViewById(R.id.editIP), findViewById(R.id.editUp), findViewById(R.id.editDown), findViewById(R.id.editRight), findViewById(R.id.editLeft), findViewById(R.id.editHand), findViewById(R.id.editExtra)};
        list = new ArrayList<>();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSaved = true;
                for (int i = 0; i < addressEdits.length; i++) {
                    if (addressEdits[i].getText().toString().length() == 0) {
                        list.add(defaultAddresses[i]);
                    } else {
                        list.add(addressEdits[i].getText().toString());
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (String s : list) {
                    sb.append(s);
                    sb.append(",");
                }
                SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("list", sb.toString());
                editor.apply();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeIntent = new Intent(getApplicationContext(), MainActivity.class);
                try {
                    if (collectedAddresses != null) {
                        changeIntent.putExtra("Addresses", collectedAddresses);
                    }
                } catch (IOError error) {
                    System.out.println("Error has occurred.");
                }
                startActivity(changeIntent);
            }
        });
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSaved) {
                    SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    String wordString = settings.getString("list", "");
                    collectedAddresses = wordString.split(",");
                    if (!IsNull(collectedAddresses)) {
                        for (int i = 0; i < collectedAddresses.length; i++) {
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
            if (dataArray[i].length() == 0 || dataArray[i] == null) {
                return true;
            }
        }
        return false;
    }
}

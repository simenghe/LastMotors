package com.noteasy.simeng.motors;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOError;
import java.util.List;


public class SettingsActivity extends AppCompatActivity {
    SharedPreferences localPrefs;
    EditText editIP=(EditText)findViewById(R.id.editIP);
    EditText editUp=(EditText)findViewById(R.id.editUp);
    Button btnSend=(Button)findViewById(R.id.btnSend);
    Button btnSave=(Button)findViewById(R.id.btnSave);
    Button btnLoad= (Button)findViewById(R.id.btnLoad);
    public static String[] collectedAddresses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
    public void saveInfo(View view){
        SharedPreferences sharedPref=getSharedPreferences("userInfo",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putString("IP",editIP.getText().toString());
        editor.putString("UP",editUp.getText().toString());
        editor.apply();

    }

}

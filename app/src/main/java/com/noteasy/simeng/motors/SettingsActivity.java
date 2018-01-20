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
    Button btnSend=(Button)findViewById(R.id.btnSend);
    Button btnSave=(Button)findViewById(R.id.btnSave);
    Button btnLoad= (Button)findViewById(R.id.btnLoad);
    public static String[] collectedAddresses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        localPrefs=this.getPreferences(Context.MODE_PRIVATE);
        String savedFav=localPrefs.getString("Big Cane","Unknown");
        TextView tv=(TextView) findViewById(R.id.testView);
        tv.setText("My cane is: "+savedFav);




    }
    private void storeData(){
        SharedPreferences myAddress=getSharedPreferences("Addresses",MODE_PRIVATE);
        SharedPreferences.Editor myEditor=myAddress.edit();
        myEditor.putString("1","Test Works 1");
        myEditor.apply();
    }
    private String getData(){
        SharedPreferences myAddress=getSharedPreferences("Addresses",MODE_PRIVATE);
        return myAddress.getString("1","Not easy");
    }
}

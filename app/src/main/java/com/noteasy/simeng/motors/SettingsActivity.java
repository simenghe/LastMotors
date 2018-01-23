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
    public static String[] collectedAddresses;
    EditText editIP;
    EditText editUp;
    Button btnSend;
    Button btnSave;
    Button btnLoad;
    TextView txtTest;
    List<String> list;
    //public static String[] collectedAddresses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        editIP = (EditText) findViewById(R.id.editIP);
        editUp = (EditText) findViewById(R.id.editUp);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnLoad = (Button) findViewById(R.id.btnLoad);
        txtTest = (TextView) findViewById(R.id.txtText);

        list=new ArrayList<>();
        list.add("cane");
        list.add("mans");
        list.add("Not Easy");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb=new StringBuilder();
                for(String s:list){
                    sb.append(s);
                    sb.append(",");
                }
                SharedPreferences settings=getSharedPreferences("PREFS",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=settings.edit();
                editor.putString("list",sb.toString());
                editor.apply();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changeIntent=new Intent(getApplicationContext(),MainActivity.class);
                changeIntent.putExtra("Addresses",collectedAddresses);
                startActivity(changeIntent);
            }
        });
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings=getSharedPreferences("PREFS",Context.MODE_PRIVATE);
                String wordString=settings.getString("list","");
                collectedAddresses=wordString.split(",");
                for(int i=0; i<collectedAddresses.length;i++){
                    System.out.println("Collected address"+i+collectedAddresses[i]);
                }
                /*String[]itemsWords=wordString.split(",");
                List<String> items=new ArrayList<String>();
                for(int i=0;i<itemsWords.length;i++){
                    items.add(itemsWords[i]);
                }
                for(int i=0; i<items.size();i++){
                    System.out.println(items.get(i));
                }*/
            }
        });
    }

}

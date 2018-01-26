package com.noteasy.simeng.motors;

import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;
import com.noteasy.simeng.motors.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView urlText;
    public String [] addresses;
    //public String baseAddress="http://10.145.145.140:5000";
    public String baseAddress="http://192.168.0.109:1234";
    public String forward="/forward";
    public String backward="/backward";
    public String right="/right";
    public String left="/left";
    public String stop="/stop";
    public String hands="/hands";
    //public static String baseAddress="http://10.145.158.52:1234/";
    //public String baseAddress="http://192.168.0.100:1234/";
    float offSetMax=0.75f;
    float offSetMid=0.5f;
    float offSetLow=0.25f;
    public static int curLevel;
    public static int curDomain=0;
    public int GetLevel(float offSet){
        if(offSet>offSetMax){
            return 3;
        }
        if(offSet>offSetMid){
            return 2;
        }
        if(offSet<offSetLow){
            return 1;
        }
        else{
            return 1;
        }
    }
    public int GetDomain(float degrees){
        //Return 1=UP,2=DOWN,3=LEFT,4=RIGHT
        if(degrees<=135&&degrees>=45){
            return 1;
        }
        if(degrees<-45&&degrees>-135){
            return 2;
        }
        if(degrees<45&&degrees>-45){
            return 3;
        }
        if(degrees<135&&degrees>-135){
            return 4;
        }else{
            return 4;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        urlText=(TextView)findViewById(R.id.urlText);
        Button btnKitty=(Button) findViewById(R.id.btnKitty);
        Button btnRestart=(Button)findViewById(R.id.btnRestart);
        final TextView txtStick=(TextView) findViewById(R.id.txtStick);
        Button btnSettings=(Button)findViewById(R.id.btnSettings);
        Button btnJoy=(Button) findViewById(R.id.btnJoy);
        final Joystick buggoStick=(Joystick)findViewById(R.id.buggoStick);
        //Collect the data
        //Change to other activity
        if(getIntent().hasExtra("Addresses")){
            addresses=getIntent().getExtras().getStringArray("Addresses");
            if (addresses[0]==null||addresses[0]=="null"){
                Toast.makeText(getApplicationContext(),"Address IS NULL!!!!!!! Reenter and Save Properly",Toast.LENGTH_SHORT).show();
            }
            System.out.println(addresses[0]+"is your base addresss");
            urlText.setText("Base address="+ addresses[0]);
            baseAddress=addresses[0];
            Toast.makeText(getApplicationContext(),"Base Address :"+baseAddress,Toast.LENGTH_SHORT).show();
        }
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(startIntent);
            }
        });
        btnKitty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONTask().execute(baseAddress+forward);
            }
        });
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();

            }
        });


        buggoStick.setJoystickListener(new JoystickListener() {
            @Override
            public void onDown() {
                //Events for on down.
            }
            @Override
            public void onDrag(float degrees, float offset) {
                txtStick.setText("Degrees: "+degrees+"\nOffset:  "+offset);
                System.out.println(GetDomain(degrees));
                boolean changed=GetLevel(offset)!=curLevel;
                boolean domainChanged=GetDomain(degrees)!=curDomain;
                if(changed||domainChanged){
                    curDomain=GetDomain(degrees);
                    if(curDomain==1){
                        curLevel=GetLevel(offset);
                        curDomain=1;
                        if(curLevel==3){
                            System.out.println(baseAddress+forward);
                            new JSONTask().execute(baseAddress+forward);
                            new JSONTask().execute(baseAddress+forward);

                        }
                        if(curLevel==2){
                            new JSONTask().execute(baseAddress+forward+"/slow");
                        }
                        if(curLevel==1){
                            //stop.
                            new JSONTask().execute(baseAddress+stop);
                        }
                    }
                    if((curDomain==2)){
                        curDomain=2;
                        curLevel=GetLevel(offset);
                        if(curLevel==3){
                            new JSONTask().execute(baseAddress+backward);
                            new JSONTask().execute(baseAddress+backward);
                        }
                        if(curLevel==2){
                            new JSONTask().execute(baseAddress+backward);
                        }
                        if(curLevel==1){
                            new JSONTask().execute(baseAddress+stop);
                            new JSONTask().execute(baseAddress+stop);
                        }
                    }
                    if((curDomain==3)){
                        curDomain=3;
                        curLevel=GetLevel(offset);
                        if(curLevel==3){
                            new JSONTask().execute(baseAddress+right);
                            new JSONTask().execute(baseAddress+right);
                        }
                        if(curLevel==2){
                            //Slower
                            //new JSONTask().execute(baseAddress+"right");
                        }
                        if(curLevel==1){
                            //stop.
                            new JSONTask().execute(baseAddress+stop);
                            new JSONTask().execute(baseAddress+stop);
                        }
                    }
                    if((curDomain==4)){
                        curDomain=4;
                        curLevel=GetLevel(offset);
                        if(curLevel==3){
                            //Go
                            new JSONTask().execute(baseAddress+left);
                            new JSONTask().execute(baseAddress+left);
                    }
                        if(curLevel==2){
                            //Slower
                        }
                        if(curLevel==1){
                            //stop.
                            new JSONTask().execute(baseAddress+stop);
                            new JSONTask().execute(baseAddress+stop);
                        }
                    }
                }
            }

            @Override
            public void onUp() {
                txtStick.setText("Degrees: "+0+"\nOffset: "+0);
                curLevel=0;
                curDomain=0;
                new JSONTask().execute(baseAddress+stop);
                new JSONTask().execute(baseAddress+stop);
                System.out.println("Relased domain: "+curDomain);
            }
        });
    }
    public class JSONTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "");
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            urlText.setText(result);
        }
    }
}



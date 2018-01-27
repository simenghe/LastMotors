package com.noteasy.simeng.motors;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable;
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

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jmedeisis.bugstick.Joystick;
import com.jmedeisis.bugstick.JoystickListener;


public class MainActivity extends AppCompatActivity {
    private TextView urlText;
    public String[] addresses;
    //public String baseAddress="http://10.145.145.140:5000";
    public String baseAddress;
    public String forward = "/forward";
    public String backward = "/backward";
    public String right = "/right";
    public String left = "/left";
    public String stop = "/stop";
    public String hands = "/hands";
    public String slow="/slow";
    //public static String baseAddress="http://10.145.158.52:1234/";
    //public String baseAddress="http://192.168.0.100:1234/";
    float offSetMax = 0.75f;
    float offSetMid = 0.5f;
    float offSetLow = 0.25f;
    public static int curLevel;
    public static int curDomain = 0;
    public int GetLevel(float offSet) {
        if (offSet > offSetMax) {
            return 3;
        }
        if (offSet > offSetMid) {
            return 2;
        }
        if (offSet < offSetLow) {
            return 1;
        } else {
            return 1;
        }
    }
    public int GetDomain(float degrees) {
        //Return 1=UP,2=DOWN,3=LEFT,4=RIGHT
        if (degrees <= 135 && degrees >= 45) {
            return 1;
        }
        if (degrees < -45 && degrees > -135) {
            return 2;
        }
        if (degrees < 45 && degrees > -45) {
            return 3;
        }
        if (degrees < 135 && degrees > -135) {
            return 4;
        } else {
            return 4;
        }
    }
    static RequestQueue mRequestQueue;
    StringRequest sr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_main);

        urlText = (TextView) findViewById(R.id.urlText);
        Button btnKitty = (Button) findViewById(R.id.btnKitty);
        final TextView txtStick = (TextView) findViewById(R.id.txtStick);
        Button btnSettings = (Button) findViewById(R.id.btnSettings);
        Button btnJoy = (Button) findViewById(R.id.btnJoy);
        final Joystick buggoStick = (Joystick) findViewById(R.id.buggoStick);

        Network network = new BasicNetwork(new HurlStack());
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        mRequestQueue = new RequestQueue(cache, network);
        mRequestQueue.start();
        if(getIntent().hasExtra("Addresses")){ //Try to get the address from the other activity.
            addresses=getIntent().getExtras().getStringArray("Addresses");
            if (addresses[0]==null||addresses[0]=="null"){
                Toast.makeText(getApplicationContext(),"Address IS NULL!!!!!!! Reenter and Save Properly",Toast.LENGTH_SHORT).show();
            }
            System.out.println(addresses[0]+"is your base addresss");
            urlText.setText("Base address="+ addresses[0]);
            baseAddress=addresses[0];
            Toast.makeText(getApplicationContext(),"Base Address :"+baseAddress,Toast.LENGTH_SHORT).show();
            forward=addresses[1]; backward=addresses[2];right=addresses[3];
        }
        else{
            baseAddress="http://192.168.0.100:1234";
            Toast.makeText(getApplicationContext(),"Default Base Address :"+baseAddress,Toast.LENGTH_SHORT).show();
        }

         final StringRequest stringRequest = new StringRequest(Request.Method.GET, baseAddress,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        urlText.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error boys", Toast.LENGTH_SHORT).show();
                        System.out.println("Error boys");
                    }
                });
        btnKitty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(baseAddress);
                mRequestQueue.add(stringRequest);
            }
        });
        //Collect the data
        //Change to other activity
            buggoStick.setJoystickListener(new JoystickListener() {
                @Override
                public void onDown() {
                    //Events for on down.
                    //mRequestQueue.add(stringRequest);
                }
                @Override
                public void onDrag(float degrees, float offset) {
                    String newUrl="http://192.168.0.100:1234/";
                    txtStick.setText("Degrees: " + degrees + "\nOffset:  " + offset);
                    System.out.println(GetDomain(degrees));
                    boolean changed = GetLevel(offset) != curLevel;
                    boolean domainChanged = GetDomain(degrees) != curDomain;
                    if (changed || domainChanged) {
                        curDomain = GetDomain(degrees);
                        if (curDomain == 1) {
                            curLevel = GetLevel(offset);
                            curDomain = 1;
                            if (curLevel == 3) {
                                newUrl+=forward;
                            }
                            if (curLevel == 2) {
                                newUrl+=forward;
                            }
                            if (curLevel == 1) {
                                newUrl+="lightoff";
                            }
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, newUrl,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // Do something with the response
                                            urlText.setText(response);
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(getApplicationContext(), "Error boys", Toast.LENGTH_SHORT).show();
                                            System.out.println("Error boys");
                                        }
                                    });
                            mRequestQueue.add(stringRequest);
                        }
                        if ((curDomain == 2)) {
                            curDomain = 2;
                            curLevel = GetLevel(offset);
                            if (curLevel == 3) {
                            }
                            if (curLevel == 2) {
                            }
                            if (curLevel == 1) {
                            }
                        }
                        if ((curDomain == 3)) {
                            curDomain = 3;
                            curLevel = GetLevel(offset);
                            if (curLevel == 3) {
                            }
                            if (curLevel == 2) {
                                //Slower
                                //new JSONTask().execute(baseAddress+"right");
                            }
                            if (curLevel == 1) {
                                //stop.
                            }
                        }
                        if ((curDomain == 4)) {
                            curDomain = 4;
                            curLevel = GetLevel(offset);
                            if (curLevel == 3) {
                                //Go
                            }
                            if (curLevel == 2) {
                                //Slower
                            }
                            if (curLevel == 1) {
                                //stop.
                            }
                        }
                    }
                }
                @Override
                public void onUp() {
                    txtStick.setText("Degrees: " + 0 + "\nOffset: " + 0);
                    curLevel = 0;
                    curDomain = 0;
                    System.out.println("Relased domain: " + curDomain);
                }
            });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //changing activities.
                Intent startIntent=new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(startIntent);
            }
        });
        }
        private void sendRequest(){
            mRequestQueue= Volley.newRequestQueue(this);




        }
    }






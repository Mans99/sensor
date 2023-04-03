package com.example.sensor;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.sensor.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.os.VibrationEffect;
import android.os.Vibrator;

import android.media.MediaPlayer;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private Accelerometer accelerometer1;
    private Accelerometer accelerometer2;
    private Accelerometer accelerometer3;
    private Button shakeButton;
    private Button fallButton;
    private Button accButton;
    DecimalFormat df = new DecimalFormat("###.###");
    private int k = 0;
    private int count = 0;
    private int fallCount = 0;
    private int shakeCount = 0;
    private TextView x,y,z;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        accelerometer1 = new Accelerometer(this);
        accelerometer2 = new Accelerometer(this);
        accelerometer3 = new Accelerometer(this);
        mp = MediaPlayer.create(MainActivity.this, R.raw.scream);
        x = (TextView) findViewById(R.id.textview_first);
        y = (TextView) findViewById(R.id.textView_Y);
        z = (TextView) findViewById(R.id.textView_Z);

    }

    public void shake() {
        createShakeListener();
    }

    private void createShakeListener() {
        shakeButton = (Button)findViewById(R.id.random_button);
        if (shakeCount == 0) {

            shakeButton.setText("Stop");
            shakeButton.setBackgroundColor(Color.RED);
            accelerometer1.setListener(new Accelerometer.Listener() {
                @Override
                public void onTranslation(float tx, float ty, float tz) {
                    if (tx > 12) {

                        x.setText("");
                        y.setText("I'm shaking");
                        z.setText("");
                        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        final VibrationEffect vibrationEffect1;
                        // this is the only type of the vibration which requires system version Oreo (API 26)
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                            // this effect creates the vibration of default amplitude for 1000ms(1 sec)
                            vibrationEffect1 = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE);

                            // it is safe to cancel other vibrations currently taking place
                            vibrator.cancel();
                            vibrator.vibrate(vibrationEffect1);
                        }
                    }
                }
            });
            shakeCount++;
        } else {
            //Color.argb(100,98,0,238)

            shakeButton.setText("Shake");
            shakeButton.setBackgroundColor(6422761);
            accelerometer1.setListener(null);
            shakeCount--;
        }
    }

    public void fall() {
        createFallListener();
    }
    private void createFallListener() {
        fallButton = (Button)findViewById(R.id.fall_button);
        if (fallCount == 0) {

            fallButton.setText("Stop");
            fallButton.setBackgroundColor(Color.RED);
            accelerometer2.setListener(new Accelerometer.Listener() {
                @Override
                public void onTranslation(float tx, float ty, float tz) {
                    if (tx == 0 && ty == 0 && tz == 0) {
                        x.setText("");

                        y.setText("I'm falling");
                        z.setText("");
                        k++;
                        if (k == 1) {
                            mp.start();
                        }
                    }
                }
            });
            fallCount++;
        } else {
            //Color.argb(100,98,0,238)
            k=0;
            fallButton.setText("Fall");
            fallButton.setBackgroundColor(6422761);
            accelerometer2.setListener(null);
            fallCount--;
        }
    }


    private void createListener() {
        accButton = (Button)findViewById(R.id.button);
        if (count == 1) {
            accButton.setText("Stop");
            accButton.setBackgroundColor(Color.RED);
            accelerometer3.setListener(new Accelerometer.Listener() {
                @Override
                public void onTranslation(float tx, float ty, float tz) {
                    x.setText("X:" + df.format(tx));
                    y.setText("Y:"+df.format(ty));
                    z.setText("Z:"+df.format(tz));
                }
            });

        } else {
            accButton.setText("Tilt");
            accButton.setBackgroundColor(6422761);
            accelerometer3.setListener(null);
        }
    }

    public void changeCount() {
        if (count == 0) {
            count++;
        } else {
            count--;
        }
            createListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        accelerometer1.register();
        accelerometer2.register();
        accelerometer3.register();
    }

    @Override
    protected void onPause() {
        super.onPause();
        accelerometer1.unregister();
        accelerometer2.unregister();
        accelerometer3.unregister();
    }
}
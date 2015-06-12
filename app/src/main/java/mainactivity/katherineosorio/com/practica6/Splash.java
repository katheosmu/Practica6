package mainactivity.katherineosorio.com.practica6;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;

/**
 * Created by user on 28/05/2015.
 */
public class Splash extends Activity{
    private static final long SPLASH_DELAY = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.splash);

        TimerTask task = new TimerTask(){
            public void run(){
                Intent mainIntent = new Intent().setClass(Splash.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, SPLASH_DELAY);
    }
}


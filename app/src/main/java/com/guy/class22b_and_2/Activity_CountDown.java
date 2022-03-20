package com.guy.class22b_and_2;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class Activity_CountDown extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private ExtendedFloatingActionButton main_FAB_action;
    private MaterialTextView main_LBL_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("ptttThread", "onCreate Thread = " + Thread.currentThread().getName());
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        main_FAB_action = findViewById(R.id.main_FAB_action);
        main_LBL_info = findViewById(R.id.main_LBL_info);

        main_FAB_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionClicked();
            }
        });
    }

    private static final int BACK_TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;
    @Override
    public void onBackPressed() {
        // you can override back pressed method
            if (mBackPressed + BACK_TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
            }
            else {
                mBackPressed = System.currentTimeMillis();
                Toast.makeText(this, "tap BACK again to exit", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);


        MenuItem menuItem = menu.findItem(R.id.app_bar_switch);
        SwitchMaterial mySwitch = (SwitchMaterial) menuItem.getActionView();

        mySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, "Switch Changed", Toast.LENGTH_SHORT).show();
        });

        //toolbar.setTitle("Timer");
        toolbar.setSubtitle("Typing...");
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
        } else if (id == R.id.action_about) {
            finish();
            return true;
        } else if (id == R.id.action_privacyPolicy) {
            //ggfffgf
            return true;
        } else if (id == R.id.action_send) {
            Toast.makeText(this, "Sending data", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }






    // ----------- ----------- ----------- ----------- ----------- -----------

    private final int DELAY = 1000;
    private enum TIMER_STATUS {
        OFF,
        RUNNING,
        PAUSE
    }
    private TIMER_STATUS timerStatus = TIMER_STATUS.OFF;
    private int counter = 0;

    private void actionClicked() {
        if (timerStatus == TIMER_STATUS.RUNNING) {
            stopTimer();
            timerStatus = TIMER_STATUS.OFF;
        } else {
            startTimer();
        }
    }

    private void tick() {
        Log.d("ptttTick", "Tick Thread A = " + Thread.currentThread().getName() + "   " + counter);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ++counter;
                main_LBL_info.setText("" + counter);
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (timerStatus == TIMER_STATUS.RUNNING) {
            stopTimer();
            timerStatus = TIMER_STATUS.PAUSE;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (timerStatus == TIMER_STATUS.PAUSE) {
            startTimer();
        }
    }



    private void updateTimerControls() {
        if (timerStatus == TIMER_STATUS.RUNNING) {
            main_FAB_action.setText("STOP");
            main_FAB_action.setIconResource(R.drawable.ic_stop);
        } else {
            main_FAB_action.setText("START");
            main_FAB_action.setIconResource(R.drawable.ic_play);
        }
    }



    // ----------- ----------- ----------- ----------- ----------- -----------



    CountDownTimer countDownTimer;

    private void startTimer() {
        timerStatus = TIMER_STATUS.RUNNING;
        updateTimerControls();

        CountDownTimer countDownTimer =
        new CountDownTimer(5000, DELAY) {

            public void onTick(long millisUntilFinished) {
                tick();
            }

            public void onFinish() {
                Log.d("ptttTick", "On Finish");
            }

        }.start();


    }

    private void stopTimer() {
        updateTimerControls();
        countDownTimer.cancel(); // or finish it
    }
}
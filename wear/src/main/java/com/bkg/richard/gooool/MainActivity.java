package com.bkg.richard.gooool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.wearable.view.WatchViewStub;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/* TODO: add in voice recognition
    add card fragments:
        a card to allow to show a timer with a pause and start button
        a card to show each teams' score
    add in a tutorial overlay on first time boot
        enable skip tutorial option

*/



public class MainActivity extends Activity {

    public int teamAscore = 0;
    public int teamBscore = 0;
    private Chronometer chronometer;
    private CountDownTimer countDown;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);;

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener()
        {
            @Override
            public void onLayoutInflated(WatchViewStub stub)
            {
                addButtonListener();
                setupTimer();
            }
        });
    }

    public void addButtonListener() {
        ImageButton imgButton1 = (ImageButton) findViewById(R.id.imageButton);
        ImageButton imgButton2 = (ImageButton) findViewById(R.id.imageButton2);

        final TextView team1Score = (TextView) findViewById(R.id.homeScore);
        final TextView team2Score = (TextView) findViewById(R.id.guestScore);

        team1Score.setText(Integer.toString(teamAscore));
        team2Score.setText(Integer.toString(teamBscore));

        imgButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamAscore++;
                team1Score.setText(Integer.toString(teamAscore));
            }
        });

        imgButton1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                teamAscore = 0;
                team1Score.setText(Integer.toString(teamAscore));
                return true;
            }
        });

        imgButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                teamBscore++;
                team2Score.setText(Integer.toString(teamBscore));
            }
        });

        imgButton2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                teamBscore = 0;
                team2Score.setText(Integer.toString(teamBscore));
                return true;
            }
        });
    }

    public void setupTimer() {

        chronometer = (Chronometer) findViewById(R.id.chronometer);

        chronometer.setOnClickListener(new View.OnClickListener() {
            Boolean isPaused = false;
            @Override public void onClick(View v) {

                if (countDown == null ||
                        chronometer.getText() == "done!" ||
                        chronometer.getText() == "00:00")
                {
                    Intent intent = new Intent(getApplicationContext(), DisplayTimerActivity.class);
                    startActivityForResult(intent, 0);
                }
                else if (isPaused)
                {
                    String time = (String) chronometer.getText();
                    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                    Date date = null;
                    try
                    {
                        date = sdf.parse(time);
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }

                    if (date != null)
                    {
                        startCountDown(date.getTime());
                        isPaused = false;
                    }
                }
                else
                {
                    countDown.cancel();
                    isPaused = true;
                }
            }
        });

        chronometer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DisplayTimerActivity.class);
                startActivityForResult(intent, 0);
                return true;
            }
        });
    }

    /*
        As soon as we get the result from the timer picker
        Cancel the previous timer if it exists
        get the new time value and create a new countdown timer
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (countDown != null) {
            countDown.cancel();
        }

        Long millis = data.getExtras().getLong("millisUntilFinished");
        startCountDown(millis);
    }

    public void startCountDown(long millis)
    {
        countDown = new CountDownTimer(millis, 1000) {

            public void onTick(long millisUntilFinished) {
                SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
                String dateString = formatter.format(new Date(millisUntilFinished));
                chronometer.setText(dateString);
            }

            public void onFinish()
            {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] vibrationPattern = {0, 500, 50, 300};
                //-1 - don't repeat
                final int indexInPatternToRepeat = -1;
                vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);
                chronometer.setText("done!");
            }
        }.start();
    }
}

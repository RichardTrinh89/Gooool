package com.bkg.richard.gooool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.util.concurrent.TimeUnit;


public class DisplayTimerActivity extends Activity {

    private NumberPicker numMinutes;
    private NumberPicker numSeconds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_timer);

        numMinutes = (NumberPicker) findViewById(R.id.numMinutes);
        numSeconds = (NumberPicker) findViewById(R.id.numSeconds);

        numMinutes.setMaxValue(60);
        numSeconds.setMaxValue(59);

        addStartButton();
    }

    private void addStartButton() {
        Button btnStart = (Button) findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer minutes = numMinutes.getValue();
                Integer seconds = numSeconds.getValue();

                // Convert the total time to seconds
                Integer totalTime = (minutes * 60) + (seconds);

                // Convert totalTime to milliseconds
                Long milliseconds = TimeUnit.SECONDS.toMillis(totalTime);

                Intent intent = getIntent();
                intent.putExtra("millisUntilFinished", milliseconds);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}

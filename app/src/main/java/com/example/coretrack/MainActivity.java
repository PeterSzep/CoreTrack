package com.example.coretrack;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class MainActivity extends AppCompatActivity {
    private CircularProgressBar stepsProgressBar;
    private TextView stepsTextview, caloriesTextView, kilometersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        stepsProgressBar = findViewById(R.id.stepCircularBar);
        stepsTextview = findViewById(R.id.stepsTextview);
        caloriesTextView = findViewById(R.id.caloriesTextView);
        kilometersTextView = findViewById(R.id.kilometersTextView);

        stepsProgressBar.setProgressMax(10000f);
        String currentSteps = "7500";
        stepsProgressBar.setProgress(Float.parseFloat(currentSteps));

        caloriesTextView.setText("500\nKcal");
        kilometersTextView.setText("5,5\nKm");
        stepsTextview.setText(currentSteps + "\nsteps");
    }
}
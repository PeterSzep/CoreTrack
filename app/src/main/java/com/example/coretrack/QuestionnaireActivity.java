package com.example.coretrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class QuestionnaireActivity extends AppCompatActivity {
    private EditText nameEditText, surnameEditText;
    private TextInputLayout nameInputLayout, surnameInputLayout;
    private Slider goalSlider;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_questionnaire);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nameInputLayout = findViewById(R.id.nameInputLayout);
        surnameInputLayout = findViewById(R.id.surnameInputLayout);
        nameEditText = findViewById(R.id.nameEditText);
        surnameEditText = findViewById(R.id.surnameEditText);
        goalSlider = findViewById(R.id.goalSlider);
        nextButton = findViewById(R.id.nextButton);

        handleClick();

    }
    public void handleClick(){
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processData();
            }
        });
    }

    public void processData(){
        nameInputLayout.setError(null);
        surnameInputLayout.setError(null);

        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        int stepGoal = (int) goalSlider.getValue();


        if(isValid(name) && isValid(surname)){
            addData(name, surname, stepGoal);

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }else{
            if(!isValid(name)){
                nameInputLayout.setError(getString(R.string.invalidName));
            }
            if(!isValid(surname)){
                surnameInputLayout.setError(getString(R.string.invalidSurname));
            }
        }
    }

    public boolean isValid(String input) {
        if(input.isEmpty() || input.length() <= 3){
            return false;
        }
        return input.matches("^[A-Za-zÀ-ž\\s'-]+$");
    }

    public void addData(String name, String surname, int stepGoal){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("surname", surname);
        user.put("stepGoal", stepGoal);
        user.put("steps", 0);

        db.collection("users")
                .add(user);
    }
}
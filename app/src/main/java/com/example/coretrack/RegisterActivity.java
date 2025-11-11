package com.example.coretrack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;



public class RegisterActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, reenterPasswordEditText;
    private TextInputLayout emailInputLayout, passwordInputLayout, reenterPasswordInputLayout;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        reenterPasswordEditText = findViewById(R.id.reenterPasswordEditText);

        registerButton = findViewById(R.id.registerButton);

        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        reenterPasswordInputLayout = findViewById(R.id.reenterPasswordInputLayout);

        handleClicks();
    }

    public void handleClicks(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegister();
            }
        });
    }

    //check if password is at least 8 chars long
    public boolean isValidPassword(String password){
        if(password == null){
            return false;
        }

        return password.length() >= 8;
    }

    //check for valid email using google's recommended email regex
    public boolean isValidEmail(String email) {
        if(email == null){
            return false;
        }

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //compares the re entered password if are the same
    public boolean comparePassword(String password, String reenterPassword){
        if(password == null || reenterPassword == null){
            return false;
        }

        return password.equals(reenterPassword);
    }

    public void handleRegister(){
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);
        reenterPasswordInputLayout.setError(null);

        String email = String.valueOf(emailEditText.getText());
        String password = String.valueOf(passwordEditText.getText());
        String reenterPassword = String.valueOf(reenterPasswordEditText.getText());
        boolean valid = true;

        if(!isValidEmail(email)){
            emailInputLayout.setError(getString(R.string.invalidEmail));
            valid = false;
        }

        if (!isValidPassword(password)) {
            passwordInputLayout.setError(getString(R.string.invalidPasswordLength));
            valid = false;
        }

        if(!comparePassword(password, reenterPassword)){
            passwordInputLayout.setError(getString(R.string.passwordsDoNotMatch));
            reenterPasswordInputLayout.setError(getString(R.string.passwordsDoNotMatch));
            valid = false;
        }

        if(valid){
            createUser(email,password);
        }
    }

    public void createUser(String email, String password){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(RegisterActivity.this, QuestionnaireActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast toast = Toast.makeText(RegisterActivity.this,getString(R.string.errorRegistering), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
    }
}
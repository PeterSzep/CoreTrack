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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText, reenterPasswordEditText, nameEditText;
    private TextInputLayout emailInputLayout, passwordInputLayout, reenterPasswordInputLayout, nameInputLayout;
    private Button registerButton;
    private SignInButton googleSignInButton;

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
        reenterPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        nameEditText = findViewById(R.id.nameEditText);

        registerButton = findViewById(R.id.createAccountButton);
        googleSignInButton = findViewById(R.id.googleSignInButton);

        nameInputLayout = findViewById(R.id.nameInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        reenterPasswordInputLayout = findViewById(R.id.confirmPasswordInputLayout);

        handleClicks();
    }

    public void handleClicks(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegister();
            }
        });


        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }


    //check if name has forbidden chars
    public boolean isValid(String input) {
        if (input == null) {
            return false;
        }

        input = input.trim();
        if (input.length() < 2) {
            return false;
        }
        return input.matches("^[\\p{L}\\s'-]+$");
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

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        //Log.d("email", email);
        String password = passwordEditText.getText().toString();
        String reenterPassword = reenterPasswordEditText.getText().toString();

        boolean valid = true;

        if(!isValid(name)){
            nameInputLayout.setError(getString(R.string.invalidName));
            valid = false;
        }

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
            createUser(email,password, name);
        }
    }

    public void createUser(String email, String password, String name){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            String uid = user.getUid();

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", name);
                            userData.put("email", email);

                            db.collection("users").document(uid).set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    });
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, getString(R.string.errorRegistering), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
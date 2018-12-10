package com.example.arkadio.firebase;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelStoreOwner;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        buttonRegister = (Button) findViewById(R.id.button);
        editTextEmail = (EditText) findViewById(R.id.editText);
        editTextPassword = (EditText) findViewById(R.id.editText2);
        textViewSignup = (TextView) findViewById(R.id.textView);
        buttonRegister.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);

        if(firebaseAuth.getCurrentUser() != null){
            //jeżeli użytkownik jest zalogowany
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
    }

    @Override
    public void onClick(View view) {
        if(view == buttonRegister){
            registerUser();
        }

        if(view == textViewSignup){
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }


    }

    private void registerUser() { //Rejestracja nowego uzytkownika
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //nie wpisano emaila
            Toast.makeText(this, "Wpisz email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            //nie wpisano hasla
            Toast.makeText(this, "Wpisz haslo",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Rejestrowanie uzytkownika...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                        //jesli tworzenie użytkownika powiodło się
                        finish();
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

                }else {
                    Toast.makeText(getApplicationContext(),
                            "Nie mozna zarejestrowacć",Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }
            }
        });
    }
}

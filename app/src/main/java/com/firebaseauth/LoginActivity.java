package com.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.sax.StartElementListener;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnClick;
    private EditText textMail;
    private EditText textPassword;
    private TextView textCmpName;
    private TextView textLogged;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        btnClick = (Button)findViewById(R.id.btnClick);
        textMail = (EditText)findViewById(R.id.textMail);
        textPassword = (EditText)findViewById(R.id.textPassword);
        textLogged = (TextView) findViewById(R.id.textLogged);
        progressDialog = new ProgressDialog (this);


        //if the user is already logged in


        btnClick.setOnClickListener(this);
        textLogged.setOnClickListener(this);

    }

    private void userLogin() {
        String email = textMail.getText().toString().trim();
        String password = textPassword.getText().toString().trim();

        if((TextUtils.isEmpty(email))&&(TextUtils.isEmpty(password))){
            //email is empty
            Toast.makeText(this, "Please enter e-mail and password",Toast.LENGTH_SHORT).show();
            //stop the function from executing further
            return;
        }

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter e-mail",Toast.LENGTH_SHORT).show();
            //stop the function from executing further
            return;
        }
        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter Password",Toast.LENGTH_SHORT).show();
            //stop the function from executing further
            return;
        }
        //if validations are okay let us show a progressDialog
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            //redirect to the create profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        }
                    }
                });


    }

    @Override
    public void onClick(View view) {
        if(view == btnClick){
            //call login method
            userLogin();
        }

        if(view == textLogged){
            //close this activity and redirect to signup activity
            finish();
            startActivity(new Intent(this, AuthActivity.class));
        }
    }


}

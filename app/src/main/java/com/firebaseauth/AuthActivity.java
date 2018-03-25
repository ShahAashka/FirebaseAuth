package com.firebaseauth;

import android.app.ProgressDialog;
import android.content.Intent;
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

import static android.widget.Toast.*;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonClick;
    private EditText textEmail;
    private EditText textPass;
    private TextView textName;
    private TextView textSignned;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        firebaseAuth = FirebaseAuth.getInstance();
        buttonClick = (Button)findViewById(R.id.buttonClick);
        textEmail = (EditText)findViewById(R.id.textEmail);
        textPass = (EditText)findViewById(R.id.textPass);
        textSignned = (TextView) findViewById(R.id.textSignned);
        progressDialog = new ProgressDialog (this);


        buttonClick.setOnClickListener(this);
        textSignned.setOnClickListener(this);

    }

    private void registerUser(){
        String email = textEmail.getText().toString().trim();
        String pass = textPass.getText().toString().trim();

        if((TextUtils.isEmpty(pass))&&(TextUtils.isEmpty(email))){
            //password is empty
            Toast.makeText(this, "Please enter e-mail and Password",Toast.LENGTH_SHORT).show();
            //stop the function from executing further
            return;
        }

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter e-mail",Toast.LENGTH_SHORT).show();
            //stop the function from executing further
            return;
        }
        if(TextUtils.isEmpty(pass)){
            //password is empty
            Toast.makeText(this, "Please enter Password",Toast.LENGTH_SHORT).show();
            //stop the function from executing further
            return;
        }





        //if validations are okay let us show a progressDialog
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        //Registering user
        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user is successfully created amd logged in
                            Toast.makeText(AuthActivity.this, "Registeration Successful", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        }
                        else{
                            Toast.makeText(AuthActivity.this, "Registeration Unsuccessful, try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });



    }

    @Override
    public void onClick(View view) {
        if(view == buttonClick){
            //invoke user's registration method
            registerUser();

        }

        if(view == textSignned){
            //close this activity and redirect to login  activity
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}

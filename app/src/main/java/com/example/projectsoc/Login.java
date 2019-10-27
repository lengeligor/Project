package com.example.projectsoc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private TextView backToAccount;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    private void initialize(){
        email  = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        backToAccount = findViewById(R.id.back_to_profile);
        login = findViewById(R.id.enter_button);
        login.setOnClickListener(this);
        backToAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == login){

        }
        if(v == backToAccount){

        }
    }
}

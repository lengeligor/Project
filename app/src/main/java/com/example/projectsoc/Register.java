package com.example.projectsoc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText nameSurname;
    private EditText email;
    private EditText password;
    private EditText rePassword;
    private EditText dogNumber;
    private Button register;
    private TextView backToAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
    }

    private void initialize(){
        nameSurname = findViewById(R.id.name_and_surname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        rePassword = findViewById(R.id.re_password);
        dogNumber = findViewById(R.id.number_of_his_dog);
        register = findViewById(R.id.enter_button);
        backToAccount = findViewById(R.id.back_to_profile);
        register.setOnClickListener(this);
        backToAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == register){

        }
        if (v == backToAccount){

        }
    }
}

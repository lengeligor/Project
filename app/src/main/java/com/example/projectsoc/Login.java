package com.example.projectsoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private TextView backToAccount;
    private Button login;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    private void initialize(){
        mAuth = FirebaseAuth.getInstance();
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
            userLogin();
        }
        if(v == backToAccount){
            startActivity(new Intent(Login.this,MainActivity.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
    }

    private void userLogin() {
        String mail = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        if(TextUtils.isEmpty(mail)){
            Toast.makeText(this,"Zadaj email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(Password)){
            Toast.makeText(this,"Zadaj heslo",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(mail,Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            finish();
                            startActivity(new Intent(Login.this,MainActivity.class));
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        }else{
                            Toast.makeText(getApplicationContext(),"Zlé prihlasovacie údaje",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

package com.example.projectsoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

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
public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText nameSurname;
    private EditText email;
    private EditText password;
    private EditText rePassword;
    private EditText dogNumber;
    private Button register;
    private TextView backToAccount;


    private FirebaseAuth mAuth;
   // DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
    }

    private void initialize(){
        mAuth = FirebaseAuth.getInstance();
       // databaseUsers = FirebaseDatabase.getInstance().getReference("users");
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
            registerPerson();
        }
        if (v == backToAccount){
            startActivity(new Intent(Register.this,MainActivity.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
    }

    private void registerPerson() {
        final String NameSurname = nameSurname.getText().toString();
        final String Email = email.getText().toString();
        final String Password = password.getText().toString();
        final String RePassword = rePassword.getText().toString();
        final String DogNumber = dogNumber.getText().toString();
        if(TextUtils.isEmpty(NameSurname)){
            Toast.makeText(this,"Vyplňte meno prosím",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(Email)){
            Toast.makeText(this,"Vyplňte email prosím",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(Password)){
            Toast.makeText(this,"Vyplňte pole s heslom prosím",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(RePassword)){
            Toast.makeText(this,"Zopakujte svoje heslo prosím",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!RePassword.equals(Password)){
            Toast.makeText(this,"Heslá sa nezhodujú",Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            Intent intent = new Intent(Register.this,MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        }else {
                            Toast.makeText(Register.this,"Registrácia nebola úspešná skúste to znova",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}

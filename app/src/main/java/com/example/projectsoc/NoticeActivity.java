package com.example.projectsoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NoticeActivity extends AppCompatActivity {

    private TextView textViewTitle, textViewDescription, textViewPhoneNUmber;
    private ImageView arrowBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        initialize();
        if(getIntent().getStringExtra("Intent")!= null && !getIntent().getStringExtra("Intent").isEmpty()
        && getIntent().getStringExtra("Intent").equals("Dashboard")){
            textViewTitle.setText(getIntent().getStringExtra("Title"));
            textViewDescription.setText(getIntent().getStringExtra("Description"));
            textViewPhoneNUmber.setText(getIntent().getStringExtra("PhoneNumber"));
        }
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoticeActivity.this,MainActivity.class);
                intent.putExtra("Intent","NoticeActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    public void initialize(){
        textViewTitle = findViewById(R.id.title);
        textViewDescription = findViewById(R.id.description);
        textViewPhoneNUmber = findViewById(R.id.phone_number);
        arrowBack = findViewById(R.id.arrow_back);
    }
}
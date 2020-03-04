package com.doggocity.projectsoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doggocity.projectsoc.R;

public class DoggocityPresov extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout layout;
    private Button enter;
    private TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doggocity_presov);
        initialize();
        enter.setOnClickListener(this);
    }

    private void initialize(){
        layout = findViewById(R.id.relativelayout_activityMain);
        appName = findViewById(R.id.first_text_appName);
        enter = findViewById(R.id.enter_button);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        layout.startAnimation(animation);
    }


    @Override
    public void onClick(View v) {
        if (v == enter){
            startActivity(new Intent(DoggocityPresov.this,MainActivity.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }
    }

    @Override
    public void finish(){
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

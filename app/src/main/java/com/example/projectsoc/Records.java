package com.example.projectsoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import es.dmoral.toasty.Toasty;

public class Records extends AppCompatActivity implements View.OnClickListener {

    private ImageView backToProfile;
    private Button save;
    private EditText editText;

    private final String FILE_NAME = "records.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        initialize();
        backToProfile.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    private void initialize(){
        backToProfile = findViewById(R.id.arrow_back);
        save = findViewById(R.id.save);
        editText = findViewById(R.id.editText);
        load();
    }

    @Override
    public void onClick(View v) {
        if(backToProfile.equals(v)){
            Intent intent = new Intent(Records.this,MainActivity.class);
            intent.putExtra("Intent","RecordsClass");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }else if(save.equals(v)){
            save();
            Intent intent = new Intent(Records.this,MainActivity.class);
            intent.putExtra("Intent","RecordsClass");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
    }

    private void save(){
        String text = editText.getText().toString();
        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
            fos.write(text.getBytes());
            Toasty.success(getApplicationContext(),"Záznamy sa uložili", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toasty.error(getApplicationContext(),"Nastala chyba pri ukladaní záznamu", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void load(){
        FileInputStream fis = null;
        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null){
                sb.append(text).append("\n");
            }
            editText.setText(sb.toString());
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

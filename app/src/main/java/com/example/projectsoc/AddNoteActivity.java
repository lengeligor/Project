package com.example.projectsoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class AddNoteActivity extends AppCompatActivity {

    private EditText phone, description;
    private CheckBox lostDogs, findDogs;
    private ImageView arrowBack;
    private Button saveNote;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initialize();
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() != null) {
                    saveNote();
                } else {
                    Toasty.warning(getApplicationContext(),"Na túto akciu musíš byť prihlásený",Toast.LENGTH_SHORT).show();
                }
            }
        });
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNoteActivity.this,MainActivity.class);
                intent.putExtra("Intent","AddNoteActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        findDogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    lostDogs.setChecked(false);
                    findDogs.setChecked(true);
            }
        });
        lostDogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findDogs.setChecked(false);
                lostDogs.setChecked(true);
            }
        });
    }

    private void initialize(){
        phone = findViewById(R.id.phone_number);
        description = findViewById(R.id.description);
        lostDogs = findViewById(R.id.lost_Dogs);
        findDogs = findViewById(R.id.find_Dogs);
        saveNote = findViewById(R.id.save_note);
        arrowBack = findViewById(R.id.arrow_back);
    }

    private void saveNote() {
        String title = "";
        String telephone, descriptionString;
        Calendar calendar = Calendar.getInstance();
        String  currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        if (lostDogs.isChecked()){
            title = "Stratil sa mi psík";
        }
        if(findDogs.isChecked()){
            title = "Našiel som psíka";
        }
        telephone = phone.getText().toString();
        descriptionString = description.getText().toString();

        if (!lostDogs.isChecked() && !findDogs.isChecked()){
            Toasty.warning(getApplicationContext(),"Zaškrtni aspoň jedno políčko", Toast.LENGTH_SHORT).show();
            return;
        }
        if (telephone.trim().isEmpty() || descriptionString.trim().isEmpty()) {
            Toasty.warning(getApplicationContext(),"Vyplň chýbajúce polia", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference notebookRef = FirebaseFirestore.getInstance().collection("dashboard");
        notebookRef.add(new Note(title,descriptionString, telephone, currentDate,firebaseAuth.getCurrentUser().getUid()));
        Toasty.success(getApplicationContext(),"Oznam bol pridaný", Toast.LENGTH_SHORT).show();
        finish();


    }


}

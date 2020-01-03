package com.example.projectsoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class AddNoteActivity extends AppCompatActivity {

    private EditText phone, description;
    private CheckBox lostDogs, findDogs;
    private ImageView arrowBack, addPhoto;
    private Button saveNote;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notePhotos");

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initialize();
        url = "@drawable/ic_pet";
        if(getIntent().getStringExtra("Intent") != null){
            if (!getIntent().getStringExtra("Intent").isEmpty()){
                if (getIntent().getStringExtra("Intent").equals("UploadPhoto")){
                    setImage();
                }
            }
        }
        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firebaseAuth.getCurrentUser() != null) {
                    saveNote();
                    Intent intent = new Intent(AddNoteActivity.this,MainActivity.class);
                    intent.putExtra("Intent","AddNoteActivity");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddNoteActivity.this,UploadPhoto.class);
                intent.putExtra("Intent","AddNoteActivity");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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
        addPhoto = findViewById(R.id.addPhoto);
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
        User user = new User();
        notebookRef.add(new Note(title,descriptionString, telephone, currentDate,firebaseAuth.getCurrentUser().getUid(),
                url));
        Toasty.success(getApplicationContext(),"Oznam bol pridaný", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setImage(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    Picasso.with(getApplicationContext()).load(Uri.parse(upload.getUrl())).fit().centerCrop().into(addPhoto);
                    url = upload.getUrl();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getApplicationContext(),"Nepodarilo sa načítať fotku psíka", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

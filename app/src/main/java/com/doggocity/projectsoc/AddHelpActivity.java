package com.doggocity.projectsoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import es.dmoral.toasty.Toasty;

public class AddHelpActivity extends AppCompatActivity {

    private EditText title, phone, description;
    private ImageView arrowBack, addPhoto;
    private Button saveNote;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("helpOrganization");

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_help);
        initialize();
        url = "@drawable/ic_local_hospital_24dp";
        System.out.println(getIntent().getStringExtra("Intent"));
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
                saveNote();
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("Intent","HelpOrganization");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("Intent","HelpOrganization");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UploadPhoto.class);
                intent.putExtra("Intent","HelpOrganization");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });
    }

    private void initialize(){
        title = findViewById(R.id.title);
        phone = findViewById(R.id.phone_number);
        description = findViewById(R.id.description);
        saveNote = findViewById(R.id.save_note);
        arrowBack = findViewById(R.id.arrow_back);
        addPhoto = findViewById(R.id.addPhoto);
    }

    private void saveNote() {
        String titleString = title.getText().toString();
        String telephone, descriptionString;

        telephone = phone.getText().toString();
        descriptionString = description.getText().toString();

        if (telephone.trim().isEmpty() || descriptionString.trim().isEmpty() || titleString.isEmpty()) {
            Toasty.warning(getApplicationContext(),"Vyplň chýbajúce polia", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference notebookRef = FirebaseFirestore.getInstance().collection("helpOrganization");
        Calendar calendar = Calendar.getInstance();
        String  currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        notebookRef.add(new HelpRequest(titleString,telephone, descriptionString,currentDate,firebaseAuth.getUid(),url));
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

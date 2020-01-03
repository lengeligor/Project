package com.example.projectsoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;

public class UploadPhoto extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST =1;
    private Button chooseFile, upload;
    private ImageView imageView;
    private ProgressBar progressBar;
    private ImageView backToAccount;

    private Uri ImageUri;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask uploadTask;
    private FirebaseAuth mAuth =FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        initialize();

        chooseFile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openFileChooser();
            }
        });
        upload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toasty.warning(getApplicationContext(),"Obrázok sa načítava",Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });
        backToAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(UploadPhoto.this,MainActivity.class);
                if (getIntent().getStringExtra("Intent").equals("AddNoteActivity")) {
                    intent = new Intent(UploadPhoto.this,AddNoteActivity.class);
                }
                intent.putExtra("Intent","UploadPhoto");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile() {
        if(ImageUri != null){
            //curent time + Uid
            StorageReference fileReference = storageReference.child(mAuth.getCurrentUser().getUid() + "." + getFileExtension(ImageUri));
            if (getIntent().getStringExtra("Intent").equals("AddNoteActivity")) {
                fileReference = storageReference.child(System.currentTimeMillis()+mAuth.getCurrentUser().getUid()
                        + "." + getFileExtension(ImageUri));
            }
            uploadTask = fileReference.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    }, 500);
                    if (taskSnapshot.getMetadata() != null) {
                        if (taskSnapshot.getMetadata().getReference() != null) {
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    Upload upload = new Upload(imageUrl,mAuth.getCurrentUser().getUid());
                                    String uploadID = databaseReference.push().getKey();
                                    databaseReference.child(uploadID).setValue(upload);
                                }
                            });
                        }
                    }
                    if (getIntent().getStringExtra("Intent").equals("AddNoteActivity")) {
                        Toasty.success(getApplicationContext(),"Fotka sa nahrala",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UploadPhoto.this,AddNoteActivity.class);
                        intent.putExtra("Intent", "UploadPhoto");
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        return;
                    }
                    Toasty.success(getApplicationContext(),"Profilová fotka bola uložená",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UploadPhoto.this,MainActivity.class);
                    intent.putExtra("Intent","UploadPhoto");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toasty.error(getApplicationContext(),"Nepodarilo sa nahrať profilovú fotku",Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });
        }else {
            Toasty.warning(getApplicationContext(),"Chýbajúci obrázok",Toast.LENGTH_SHORT).show();
        }
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            ImageUri = data.getData();
            Picasso.with(this).load(ImageUri).into(imageView);
        }
    }

    private void initialize(){
        chooseFile = findViewById(R.id.choose_file);
        backToAccount = findViewById(R.id.arrow_back);
        upload = findViewById(R.id.upload);
        imageView = findViewById(R.id.image_view);
        progressBar = findViewById(R.id.progress_bar);
        if (getIntent().getStringExtra("Intent").equals("AddNoteActivity")){
            storageReference = FirebaseStorage.getInstance().getReference("notePhotos");
            databaseReference = FirebaseDatabase.getInstance().getReference("notePhotos");
        }else if(getIntent().getStringExtra("Intent").equals("Profile")){
            storageReference = FirebaseStorage.getInstance().getReference("profilePhotos");
            databaseReference = FirebaseDatabase.getInstance().getReference("profilePhotos");
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

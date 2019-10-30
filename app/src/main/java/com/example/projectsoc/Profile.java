package com.example.projectsoc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Profile extends Fragment implements View.OnClickListener {

    private View layout;

    private ImageView imageOfPerson;
    private TextView nameOfPerson;
    private TextView or;
    private TextView registerOfPerson;

    private TextView logOut;

    private FirebaseAuth mAuth =FirebaseAuth.getInstance();
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.profile_fragment,container,false);
        initialize();
        if (mAuth.getCurrentUser() != null){
            FirebaseUser user = mAuth.getCurrentUser();
            readFromDB(user);
        }else {
            logOut.setVisibility(View.INVISIBLE);
            nameOfPerson.setOnClickListener(this);
            registerOfPerson.setOnClickListener(this);
        }
        logOut.setOnClickListener(this);
        return layout;
    }

    private void initialize(){
        imageOfPerson = layout.findViewById(R.id.image_of_person);
        nameOfPerson = layout.findViewById(R.id.name_of_person);
        or = layout.findViewById(R.id.or);
        registerOfPerson = layout.findViewById(R.id.register_of_person);
        logOut = layout.findViewById(R.id.logout_button);
    }

    @Override
    public void onClick(View v) {
        if (nameOfPerson.equals(v)) {
            startActivity(new Intent(layout.getContext(), Login.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (registerOfPerson.equals(v)) {
            startActivity(new Intent(layout.getContext(), Register.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if(logOut.equals(v)) {
            mAuth.signOut();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile()).commit();
        }
    }

    public void readFromDB(final FirebaseUser user1){
        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren()) {
                    User user = new User();
                    String id = user1.getEmail().replace("@","f");
                    id = id.replace(".","f");
                    id = id.replace("-","f");
                    try {
                        user.setNameSurname(ds.child(id).getValue(User.class).getNameSurname());        //vyvolanie z Firebase Realtime DB
                        user.setMail(ds.child(id).getValue(User.class).getMail());
                        user.setDogNumber(ds.child(id).getValue(User.class).getDogNumber());

                        nameOfPerson.setText(user.getNameSurname());
                        or.setText(user.getMail());
                        if(user.getDogNumber() == null || user.getDogNumber().equals("")){
                            registerOfPerson.setText("Nevlastníš registrovaného psa");
                        }else{
                            registerOfPerson.setText(user.getDogNumber());
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}

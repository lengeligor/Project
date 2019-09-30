package com.example.projectsoc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Profile extends Fragment implements View.OnClickListener {

    private View layout;
    private FirebaseAuth mAuth;

    private ImageView imageOfPerson;
    private TextView nameOfPerson;
    private TextView oldOfPerson;
    private TextView cityOfPerson;

    private TextView myDog;
    private ImageView imageOfDog;
    private TextView nameOfDog;
    private TextView breedOfDog;
    private TextView dangerousDog;
    private TextView dogHome;

    private ImageView addNote;
    private TextView addNoteText;
    private TextView logOut;

    private RelativeLayout dogLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.profile_fragment,container,false);

        initialize();

        if (mAuth.getCurrentUser() != null){

        }else {
            logOut.setVisibility(View.INVISIBLE);
            dogLayout.setVisibility(View.INVISIBLE);
            myDog.setVisibility(View.INVISIBLE);
            addNote.setVisibility(View.INVISIBLE);
            addNoteText.setVisibility(View.INVISIBLE);
            nameOfPerson.setOnClickListener(this);
            cityOfPerson.setOnClickListener(this);
        }
        logOut.setOnClickListener(this);
        return layout;
    }

    private void initialize(){
        mAuth =FirebaseAuth.getInstance();
        imageOfPerson = layout.findViewById(R.id.image_of_person);
        nameOfPerson = layout.findViewById(R.id.name_of_person);
        oldOfPerson = layout.findViewById(R.id.old_of_person);
        cityOfPerson = layout.findViewById(R.id.city_of_person);
        imageOfDog = layout.findViewById(R.id.dog_image);
        nameOfDog = layout.findViewById(R.id.name_of_dog);
        breedOfDog = layout.findViewById(R.id.breed_of_dog);
        dangerousDog = layout.findViewById(R.id.dangerous_dog);
        dogHome = layout.findViewById(R.id.dog_home);
        addNote = layout.findViewById(R.id.add_note);
        logOut = layout.findViewById(R.id.logout_button);
        dogLayout = layout.findViewById(R.id.dog_profile_layout);
        myDog = layout.findViewById(R.id.my_dog);
        addNoteText = layout.findViewById(R.id.text_for_add_note);
    }

    @Override
    public void onClick(View v) {
        if (nameOfPerson.equals(v)) {
            startActivity(new Intent(layout.getContext(), Login.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (cityOfPerson.equals(v)) {
            startActivity(new Intent(layout.getContext(), Register.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if(logOut.equals(v)) {
            mAuth.signOut();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Profile()).commit();
        }else if (addNote.equals(v)) {
        }
    }
}

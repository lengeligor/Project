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

    private TextView logOut;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.profile_fragment,container,false);

        initialize();

        if (mAuth.getCurrentUser() != null){

        }else {
            logOut.setVisibility(View.INVISIBLE);
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
        logOut = layout.findViewById(R.id.logout_button);
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
        }
    }
}

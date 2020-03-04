package com.doggocity.projectsoc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.doggocity.projectsoc.R;

public class Share extends Fragment {

    private Button share;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layoutInflater = inflater.inflate(R.layout.share_fragment,container,false);

        share = layoutInflater.findViewById(R.id.enter_button);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Stiahni si aj ty našu mobilnú aplikáciu, ktorá ti pomôže starať sa o tvojho psíka. Aplikácia je nápomocná aj pri nájdení strateného psíka. Sťahuj na Google Play.";
                String shareSubject = "Doggocity Prešov";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSubject);
                myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(myIntent, "Zdieľaj cez ..."));
            }
        });


        return layoutInflater;
    }
}

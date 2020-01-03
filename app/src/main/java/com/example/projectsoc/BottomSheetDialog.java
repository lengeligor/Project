package com.example.projectsoc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    private CheckBox findDogs, lostDogs;
    private Button ok;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.modal_bottom_sheet,container, false);

        findDogs = v.findViewById(R.id.find_Dogs);
        lostDogs = v.findViewById(R.id.lost_Dogs);
        ok = v.findViewById(R.id.potvrdit);

        if(findDogs.isChecked()){
            lostDogs.setChecked(false);
        }
        if (lostDogs.isChecked()){
            findDogs.setChecked(false);
        }

        findDogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lostDogs.setChecked(false);
            }
        });

        lostDogs.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                findDogs.setChecked(false);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lostDogs.isChecked()) {
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    intent.putExtra("Intent", "BottomSheetDialog");
                    intent.putExtra("Query", "lostDogs");
                    dismiss();
                    startActivity(intent);
                }else if (findDogs.isChecked()){
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    intent.putExtra("Intent", "BottomSheetDialog");
                    intent.putExtra("Query", "findDogs");
                    dismiss();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(),MainActivity.class);
                    intent.putExtra("Intent", "BottomSheetDialog");
                    dismiss();
                    startActivity(intent);
                }

            }
        });

        return v;
    }


}

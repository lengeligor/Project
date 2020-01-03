package com.example.projectsoc;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class Call extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layoutInflater = inflater.inflate(R.layout.call_fragment,container,false);


        Button call = layoutInflater.findViewById(R.id.enter_button);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                String number = "159";
                intent.setData(Uri.parse("tel: " + number));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)!=
                        PackageManager.PERMISSION_GRANTED){
                    requestPermission();
                }else {
                    startActivity(intent);
                }
            }
        });


        return layoutInflater;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},1);
    }
}

package com.example.projectsoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DogActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView cisloPsa;
    private TextView plemenoPsa;
    private TextView castMesta;
    private TextView ulica;
    private TextView nebezpecnyPes;
    private ImageView linktolist;
    private Button showLocate;
    private Button openBrowser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dog);
        initialize();
        setData();
        linktolist.setOnClickListener(this);
        showLocate.setOnClickListener(this);
        openBrowser.setOnClickListener(this);
    }

    public void initialize() {
        cisloPsa = findViewById(R.id.dog_number);
        plemenoPsa = findViewById(R.id.plemenoPsa);
        linktolist = findViewById(R.id.arrow_back);
        castMesta = findViewById(R.id.cityPart);
        ulica = findViewById(R.id.street);
        nebezpecnyPes = findViewById(R.id.dangerous);
        showLocate = findViewById(R.id.view_dog_location);
        openBrowser = findViewById(R.id.open_web_browser);
    }

    public void setData(){
        String cisloPsika = getIntent().getStringExtra("ZnamkaPsa");
        String plemenoPsika = getIntent().getStringExtra("plemenoPsa");
        String nebezpecnyPsik = getIntent().getStringExtra("nebezpecnyPes");
        String mestskaCast = getIntent().getStringExtra("mestskaCast");
        String ulicaHava = getIntent().getStringExtra("ulicaPsa");
        cisloPsa.setText(cisloPsika);
        plemenoPsa.setText(plemenoPsika);
        castMesta.setText(mestskaCast);
        ulica.setText(ulicaHava);
        nebezpecnyPes.setText("Nebezpečný pes: " + nebezpecnyPsik);
    }

    @Override
    public void onClick(View v) {
        if(v == linktolist) {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("Intent","DogActivity");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }if(v == showLocate) {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            String ulicaHava = "Presov "+(getIntent().getStringExtra("ulicaPsa"));
            intent.putExtra("Intent","DogActivityOM");      //OM open map
            intent.putExtra("PolohaPsa",ulicaHava);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }if(v == openBrowser) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/imghp?hl=en"));
            startActivity(intent);
        }
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

}

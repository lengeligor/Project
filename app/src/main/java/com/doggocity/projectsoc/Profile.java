package com.doggocity.projectsoc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doggocity.projectsoc.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import es.dmoral.toasty.Toasty;

public class Profile extends Fragment implements View.OnClickListener {

    private View layout;
    private RelativeLayout dogLayout;

    private ImageView imageOfPerson;
    private TextView nameOfPerson;
    private TextView or;
    private TextView registerOfPerson;
    private TextView rasaPsa;
    private TextView cisloZnamky;
    private TextView nebezpecnyPes;
    private String search = "";
    private Button zaznamy;

    private TextView logOut;

    private FirebaseAuth mAuth =FirebaseAuth.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("profilePhotos");
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.profile_fragment,container,false);
        initialize();
        dogLayout.setVisibility(View.INVISIBLE);
        zaznamy.setVisibility(View.INVISIBLE);
        if (mAuth.getCurrentUser() != null){
            FirebaseUser user = mAuth.getCurrentUser();
            imageOfPerson.setOnClickListener(this);
            zaznamy.setOnClickListener(this);
            readFromDB(user);
            setImage();
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
        rasaPsa = layout.findViewById(R.id.rasaPsa);
        cisloZnamky = layout.findViewById(R.id.cisloZnamky);
        nebezpecnyPes = layout.findViewById(R.id.nebezpecnyPes);
        dogLayout = layout.findViewById(R.id.dog_Rlayout);
        zaznamy = layout.findViewById(R.id.zaznamyPsa);
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
        } else if(imageOfPerson.equals(v)){
            Intent intent = new Intent(getContext(),UploadPhoto.class);
            intent.putExtra("Intent","Profile");
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else if(zaznamy.equals(v)){
            startActivity(new Intent(layout.getContext(),Records.class));
            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                            search = user.getDogNumber();
                            dogLayout.setVisibility(View.VISIBLE);
                            zaznamy.setVisibility(View.VISIBLE);
                            searchDog();
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

    public void searchDog(){
        try{
            /*URL url = new URL("https://egov.presov.sk/Default.aspx?NavigationState=803:0::plac2114:_272000_5_1");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));*/

            InputStream inputStream = getActivity().getAssets().open("zoznam.xml");
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputStream);

            NodeList nList = doc.getElementsByTagName("row");
            for(int i =0;i<nList.getLength();i++){
                HashMap<String,String> dog = new HashMap<>();
                Element elm = (Element)nList.item(i);
                if(nList.item(0).getNodeType() == Node.ELEMENT_NODE){
                    if (elm.getAttribute("col_1").contains(search)) {
                        dog.put("PlemenoPsa", elm.getAttribute("col_0"));
                        dog.put("CisloZnamky", elm.getAttribute("col_1"));
                        rasaPsa.setText(elm.getAttribute("col_0"));
                        cisloZnamky.setText(elm.getAttribute("col_1"));
                        nebezpecnyPes.setText("Nebezpečný pes: " + elm.getAttribute("col_3") );
                        registerOfPerson.setText(elm.getAttribute("col_4"));
                    }
                }
            }
        }
        catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void setImage(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Upload upload = postSnapshot.getValue(Upload.class);
                    if (mAuth.getCurrentUser().getUid().equals(upload.getUser())) {
                        Picasso.with(getContext()).load(Uri.parse(upload.getUrl())).fit().centerCrop().into(imageOfPerson);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toasty.error(getContext(),"Nepodarilo sa nahrať profilovú fotku", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

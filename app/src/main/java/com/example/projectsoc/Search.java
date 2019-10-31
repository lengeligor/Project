package com.example.projectsoc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Search extends Fragment {

    private ImageView searchBtn;
    private EditText searchText;
    private TextView resultCount;

    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.search_fragment,container,false);
        initialize();
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    final ArrayList<HashMap<String, String>> dogList = new ArrayList<>();
                    ListView lv = (ListView) mView.findViewById(R.id.searchedDogs);

            /*URL url = new URL("https://egov.presov.sk/Default.aspx?NavigationState=803:0::plac2114:_272000_5_1");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(url.openStream()));*/


                    InputStream inputStream = getActivity().getAssets().open("zoznam.xml");
                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
                    Document doc = docBuilder.parse(inputStream);

                    NodeList nList = doc.getElementsByTagName("row");
                    int c=0;
                    final String[] PlemenoPsa = new String[nList.getLength()];
                    final String[] dogIDs = new String[nList.getLength()];
                    final String[] nebezpecny = new String[nList.getLength()];
                    final String[] ulica = new String[nList.getLength()];
                    final String[] castMesta = new String[nList.getLength()];
                    String search = searchText.getText().toString().toLowerCase().trim();
                    for(int i =0;i<nList.getLength();i++){
                        HashMap<String,String> dog = new HashMap<>();
                        Element elm = (Element)nList.item(i);
                        if(nList.item(0).getNodeType() == Node.ELEMENT_NODE){
                            if (elm.getAttribute("col_1").contains(search)) {
                                dog.put("PlemenoPsa", elm.getAttribute("col_0"));
                                dog.put("CisloZnamky", elm.getAttribute("col_1"));
                                dogIDs[c] = elm.getAttribute("col_1");
                                PlemenoPsa[c] = elm.getAttribute("col_0");
                                nebezpecny[c] = elm.getAttribute("col_3");
                                castMesta[c] = elm.getAttribute("col_4");
                                ulica[c] = elm.getAttribute("col_5") + " " + elm.getAttribute("col_6").trim();
                                dogList.add(dog);
                                c++;
                            }
                        }
                    }
                    String rCount = Integer.toString(c);
                    resultCount.setText(rCount);
                    ListAdapter adapter = new SimpleAdapter(getContext(), dogList, R.layout.list_row,new String[]{"PlemenoPsa","CisloZnamky"}, new int[]{R.id.category, R.id.title});
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getContext(),DogActivity.class);
                            intent.putExtra("ZnamkaPsa",dogIDs[position]);
                            intent.putExtra("plemenoPsa",PlemenoPsa[position]);
                            intent.putExtra("nebezpecnyPes",nebezpecny[position]);
                            intent.putExtra("mestskaCast",castMesta[position]);
                            intent.putExtra("ulicaPsa", ulica[position]);
                            intent.putExtra("Intent","ListOfDogs");
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        }
                    });
                }
                catch (IOException | ParserConfigurationException | SAXException e) {
                    e.printStackTrace();
                }
            }
        });
        return mView;
    }

    private void initialize(){
        searchBtn = mView.findViewById(R.id.search_button);
        searchText = mView.findViewById(R.id.searchText);
        resultCount = mView.findViewById(R.id.resultCount);
    }


}

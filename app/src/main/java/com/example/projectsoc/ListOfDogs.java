package com.example.projectsoc;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ListOfDogs extends Fragment {

    private View layoutInflater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutInflater = inflater.inflate(R.layout.list_fragment,container,false);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        final ArrayList<HashMap<String, String>> dogList = new ArrayList<>();
        ListView lv = (ListView) layoutInflater.findViewById(R.id.dog_list);


        try {
            /*URL url = new URL("https://egov.presov.sk/Default.aspx?NavigationState=803:0::plac2114:_272000_5_1");
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            InputStream inputStream = httpsURLConnection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();
*/
            InputStream inputStream = getActivity().getAssets().open("zoznam.xml");
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputStream);


            NodeList nList = doc.getElementsByTagName("row");
            int c = 0;
            final String[] PlemenoPsa = new String[nList.getLength()];
            final String[] dogIDs = new String[nList.getLength()];
            final String[] nebezpecny = new String[nList.getLength()];
            final String[] ulica = new String[nList.getLength()];
            final String[] castMesta = new String[nList.getLength()];
            for (int i = 0; i < nList.getLength(); i++) {
                HashMap<String, String> dog = new HashMap<>();
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element elm = (Element) nNode;
                    dog.put("PlemenoPsa", elm.getAttribute("col_0")); //col_2 ulica col_0 neviem 4,5 prazdne
                    dog.put("CisloZnamky", elm.getAttribute("col_1")); //col 3 cislo ulice col_1 cast mesta
                    dogIDs[c] = elm.getAttribute("col_1");
                    PlemenoPsa[c] = elm.getAttribute("col_0");
                    nebezpecny[c] = elm.getAttribute("col_3");
                    castMesta[c] = elm.getAttribute("col_4");
                    ulica[c] = elm.getAttribute("col_5") + " " + elm.getAttribute("col_6").trim();
                    dogList.add(dog);
                    c++;
                }
            }
            ListAdapter adapter = new SimpleAdapter(getContext(), dogList, R.layout.list_row, new String[]{"PlemenoPsa", "CisloZnamky"}, new int[]{R.id.category, R.id.title});
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getContext(), DogActivity.class);
                    intent.putExtra("ZnamkaPsa", dogIDs[position]);
                    intent.putExtra("plemenoPsa", PlemenoPsa[position]);
                    intent.putExtra("nebezpecnyPes", nebezpecny[position]);
                    intent.putExtra("mestskaCast", castMesta[position]);
                    intent.putExtra("ulicaPsa", ulica[position]);
                    intent.putExtra("Intent", "ListOfDogs");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }catch(IOException | ParserConfigurationException | SAXException e){
            e.printStackTrace();
        }

        return layoutInflater;
    }
        /*
        Prešov                                  col_0
        Časť mesta	Nižná Šebastová             col_1
        Ulica (chovu psa)	Belianska           col_2
        Orient. č.	2                           col_3
        Plemeno	Zlatý retriever                 col_4
        Č. známky	14256                       col_5
        Farba	                                col_6
        Pohlavie	pes                         col_7
        Nebezpečný pes	                        col_8
        Dátum narodenia	16.06.2012              col_9
        Dátum odkedy sa pes drží	03.09.2012  col_10
        Dátum dokedy sa pes drží	            col_11
        Dátum evidencie	26.09.2012              col_12
        */

}

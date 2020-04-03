package com.doggocity.projectsoc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.rpc.Help;

import java.io.Console;

import es.dmoral.toasty.Toasty;

public class HelpOrganization extends Fragment {

    private View layoutInflater;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("helpOrganization");

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    private HelpAdapter adapter;

    private boolean exam;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutInflater = inflater.inflate(R.layout.help_organization_fragment,container,false);

        hodnota();

        Query query = notebookRef.orderBy("date",Query.Direction.DESCENDING);
        setUpRecyclerView(query);

        FloatingActionButton buttonAddRequest = layoutInflater.findViewById(R.id.btn_add_request);

        buttonAddRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HelpOrganization.this.exam) {
                    startActivity(new Intent(getContext(), AddHelpActivity.class));
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }   else{
                    Toasty.warning(getContext(),"Uverejňovať môže len organizácia", Toast.LENGTH_LONG).show();
                }
            }
        });

        return layoutInflater;
    }

    private void setUpRecyclerView(Query query){
        FirestoreRecyclerOptions<HelpRequest> options = new FirestoreRecyclerOptions.Builder<HelpRequest>()
                .setQuery(query,HelpRequest.class).build();
        adapter = new HelpAdapter(options);
        RecyclerView recyclerView = layoutInflater.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // VYMAZANIE OZNAMU
        if (firebaseAuth.getCurrentUser() != null) {
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    adapter.deleteItem(viewHolder.getAdapterPosition());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HelpOrganization()).commit();
                }
            }).attachToRecyclerView(recyclerView);
        }

        //ROZKLIKNUTIE OZNAMU
        adapter.setOnItemClickListener(new HelpAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                HelpRequest helpRequest = documentSnapshot.toObject(HelpRequest.class);
                Intent intent = new Intent(getContext(), NoticeActivity.class);
                intent.putExtra("Title",helpRequest.getTitle() );
                intent.putExtra("Description", helpRequest.getDescription() );
                intent.putExtra("PhoneNumber", helpRequest.getPhone());
                intent.putExtra("Url",helpRequest.getUrlImage() );
                intent.putExtra("Intent", "HelpOrganization");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    private void hodnota(){
        HelpOrganization.this.exam = false;
        if (firebaseAuth.getCurrentUser() != null) {
            mRootRef.addValueEventListener(new ValueEventListener() {
                User user = new User();
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String id = firebaseAuth.getCurrentUser().getEmail().replace("@", "f");
                        id = id.replace(".", "f");
                        id = id.replace("-", "f");
                        try {
                            user.setOrganization(ds.child(id).getValue(User.class).getOrganization());
                            break;
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                    HelpOrganization.this.exam = user.getOrganization();
                    System.out.println("TU TO JE HNED ZA LOOPOM" + HelpOrganization.this.exam);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

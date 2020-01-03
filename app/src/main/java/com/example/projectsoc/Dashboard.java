package com.example.projectsoc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import es.dmoral.toasty.Toasty;

public class Dashboard extends Fragment {

    View layoutInflater;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("dashboard");

    private NoteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutInflater = inflater.inflate(R.layout.dashboard_fragment,container,false);


        Query query = notebookRef.orderBy("date", Query.Direction.DESCENDING);

        if (getActivity().getIntent().getStringExtra("Query") != null &&
                !getActivity().getIntent().getStringExtra("Query").isEmpty()){
            if (getActivity().getIntent().getStringExtra("Query").equals("lostDogs")) {
                query = notebookRef.whereEqualTo("title", "Stratil sa mi psík");
                setUpRecyclerView(query);
            }
            else if (getActivity().getIntent().getStringExtra("Query").equals("findDogs")) {
                query = notebookRef.whereEqualTo("title", "Našiel som psíka");

                setUpRecyclerView(query);
            }else {
                setUpRecyclerView(query);
            }
        }else {
            setUpRecyclerView(query);
        }

        FloatingActionButton buttonAddNote = layoutInflater.findViewById(R.id.btn_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddNoteActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        FloatingActionButton buttonFilterNote = layoutInflater.findViewById(R.id.btn_filter_note);
        buttonFilterNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                bottomSheetDialog.show(getFragmentManager(),"bottomSheet");
            }
        });
        return layoutInflater;
    }

    private void  setUpRecyclerView(Query query){
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        adapter = new NoteAdapter(options);
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
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Dashboard()).commit();
                }
            }).attachToRecyclerView(recyclerView);
        }

        //ROZKLIKNUTIE OZNAMU
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Note note = documentSnapshot.toObject(Note.class);
                Intent intent = new Intent(getContext(), NoticeActivity.class);
                intent.putExtra("Title",note.getTitle() );
                intent.putExtra("Description", note.getDescription() );
                intent.putExtra("PhoneNumber", note.getPhoneNumber());
                intent.putExtra("Url",note.getUrlImage() );
                intent.putExtra("Intent", "Dashboard");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
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

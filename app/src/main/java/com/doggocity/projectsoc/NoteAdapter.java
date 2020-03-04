package com.doggocity.projectsoc;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doggocity.projectsoc.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder> {

    private View v;
    private OnItemClickListener listener;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder noteHolder, int i, @NonNull Note note) {
        noteHolder.textViewTitle.setText(note.getTitle());
        noteHolder.textViewDescription.setText(note.getDescription());
        if (note.getUrlImage().equals("@drawable/ic_pet")){
            noteHolder.imageViewDog.setImageResource(R.drawable.ic_pet);
        }else {
            Picasso.with(v.getContext()).load(Uri.parse(note.getUrlImage())).fit().centerCrop().into(noteHolder.imageViewDog);
        }
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new NoteHolder(v);
    }

    public void deleteItem(int position){
        Note note = getSnapshots().getSnapshot(position).toObject(Note.class);
        String uID = mAuth.getCurrentUser().getUid();
        if(uID.equals(note.getUserID())) {
            getSnapshots().getSnapshot(position).getReference().delete();
        }else{
            Toasty.error(v.getContext(),"Tento oznam si nepridal ty", Toast.LENGTH_SHORT).show();
        }
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewDescription;
        ImageView imageViewDog;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.title);
            textViewDescription = itemView.findViewById(R.id.description);
            imageViewDog = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}

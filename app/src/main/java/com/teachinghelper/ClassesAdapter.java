package com.teachinghelper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class ClassesAdapter extends RecyclerView.Adapter<ClassesAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ClassModel> classes = new ArrayList<>();
    private String day;

    public ClassesAdapter(Context context, ArrayList<ClassModel> classes, String day) {
        this.context = context;
        this.classes.addAll(classes);
        this.day = day;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

        ClassModel classModel = classes.get(position);

        viewHolder.name.setText(classModel.getName());
        viewHolder.note.setText(classModel.getNote());
        viewHolder.class_order.setText(String.valueOf(classModel.getOrder()));


        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context,R.style.AlertDialogCustom));
                alertDialogBuilder.setMessage("Da li ste sigurni za brisanje");
                alertDialogBuilder.setPositiveButton("DA",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deleteItem(position, classModel.getId());
                            }
                        });

                alertDialogBuilder.setNegativeButton("NE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return false;
            }
        });

    }

    private void deleteItem(int pos, String id) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("teachers").child(FirebaseAuth.getInstance().getUid()).child("classes").child(day).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return classes.size();
    }

    public void setItems(ArrayList<ClassModel> classes) {
        this.classes = new ArrayList<>();
        this.classes.addAll(classes);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView class_order;
        TextView note;


        private MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            class_order = itemView.findViewById(R.id.class_order);
            note = itemView.findViewById(R.id.note);


        }

    }


}
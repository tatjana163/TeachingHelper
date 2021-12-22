package com.teachinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teachinghelper.databinding.ActivityAddClassBinding;

public class AddClassActivity extends AppCompatActivity {

    private ActivityAddClassBinding binding;

    private FirebaseAuth mAuth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_class);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Dodaj čas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        binding.day.setText(getIntent().getStringExtra("DAY"));

        database = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

    }

    private void save() {
        AddClassModel addClass = new AddClassModel(binding.name.getText().toString(), Integer.valueOf(binding.classOrder.getText().toString()), binding.note.getText().toString());

        DatabaseReference newRequestRef = database.child("teachers").child(FirebaseAuth.getInstance().getUid()).child("classes").child(binding.day.getText().toString()).push();
        newRequestRef.setValue(addClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(AddClassActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
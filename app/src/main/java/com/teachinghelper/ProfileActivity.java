package com.teachinghelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teachinghelper.databinding.ActivityProfileBinding;
import com.teachinghelper.databinding.ActivityRegistrationBinding;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        database = FirebaseDatabase.getInstance().getReference();

        database.child("teachers").child(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    TeacherModel profileModel = task.getResult().getValue(TeacherModel.class);
                    binding.name.setText(profileModel.getName());
                    binding.email.setText(profileModel.getEmail());
                    binding.school.setText(profileModel.getSchool());
                }
            }
        });

    }


    private void update() {

        TeacherModel newRequest = new TeacherModel(binding.name.getText().toString(), binding.school.getText().toString(), binding.email.getText().toString());

        DatabaseReference newRequestRef = database.child("teachers").child(FirebaseAuth.getInstance().getUid());
        newRequestRef.setValue(newRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    finish();
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
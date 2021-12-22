package com.teachinghelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.teachinghelper.databinding.ActivityStartBinding;

public class StartActivity extends AppCompatActivity {

    private ActivityStartBinding binding;

    public FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);

        isLoggedIn();

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });

    }



    private void isLoggedIn() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            getCurrentTeacher();
        } else {
            binding.register.setVisibility(View.VISIBLE);
            binding.login.setVisibility(View.VISIBLE);
        }
    }

    private void getCurrentTeacher(){
        database = FirebaseDatabase.getInstance().getReference();

        database.child("teachers").child(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    TeacherModel teacher = task.getResult().getValue(TeacherModel.class);
                    Intent i = new Intent(StartActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra("TEACHER", teacher);
                    startActivity(i);
                }
            }
        });
    }
}
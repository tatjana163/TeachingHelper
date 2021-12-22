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
import com.teachinghelper.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;

    private FirebaseAuth mAuth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_registration);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Registracija");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create();
            }
        });

        database = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

    }


    private void create() {
        mAuth.createUserWithEmailAndPassword(binding.email.getText().toString(), binding.pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference newRequestRef = database.child("teachers").child(task.getResult().getUser().getUid());
                            newRequestRef.setValue(new TeacherModel(binding.name.getText().toString(), binding.school.getText().toString(), task.getResult().getUser().getEmail())).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        getCurrentTeacher();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegistrationActivity.this, "Pokušajte ponovo.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void getCurrentTeacher(){
        database = FirebaseDatabase.getInstance().getReference();

        database.child("teachers").child(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    TeacherModel teacher = task.getResult().getValue(TeacherModel.class);
                    Intent i = new Intent(RegistrationActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.putExtra("TEACHER", teacher);
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
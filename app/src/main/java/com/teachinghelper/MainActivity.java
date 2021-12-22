package com.teachinghelper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.teachinghelper.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private DatabaseReference database;

    private ClassesAdapter adapter;
    private ArrayList<ClassModel> classes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Pomoć u nastavi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.tabs.addTab(binding.tabs.newTab().setText("PON"));
        binding.tabs.addTab(binding.tabs.newTab().setText("UTO"));
        binding.tabs.addTab(binding.tabs.newTab().setText("SRE"));
        binding.tabs.addTab(binding.tabs.newTab().setText("ČET"));
        binding.tabs.addTab(binding.tabs.newTab().setText("PET"));
        binding.tabs.addTab(binding.tabs.newTab().setText("SUB"));
        binding.tabs.addTab(binding.tabs.newTab().setText("NED"));

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadClasses();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddClassActivity.class);
                i.putExtra("DAY", binding.tabs.getTabAt(binding.tabs.getSelectedTabPosition()).getText().toString());
                startActivity(i);
            }
        });

        database = FirebaseDatabase.getInstance().getReference();

        loadClasses();

    }

    private void loadClasses() {
        database.child("teachers").child(FirebaseAuth.getInstance().getUid()).child("classes").child(binding.tabs.getTabAt(binding.tabs.getSelectedTabPosition()).getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classes = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    ClassModel classModel = s.getValue(ClassModel.class);
                    classModel.setId(s.getKey());
                    classes.add(classModel);
                }
                setList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setList() {
        binding.recyclerView.setVisibility(View.INVISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setHasFixedSize(true);
        adapter = new ClassesAdapter(this, classes, binding.tabs.getTabAt(binding.tabs.getSelectedTabPosition()).getText().toString());
        binding.recyclerView.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recyclerView.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Context wrapper = new ContextThemeWrapper(this, R.style.YOURSTYLE_PopupMenu);
                PopupMenu popup = new PopupMenu(wrapper, binding.toolbar);
                popup.getMenuInflater().inflate(R.menu.profile_logout, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(MainActivity.this, StartActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        return true;
                    }
                });

                popup.show();
                return true;
            case R.id.profile:
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
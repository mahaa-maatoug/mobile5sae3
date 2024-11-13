package com.example.constat;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private AppDatabase database;
    private UsersAdapter usersAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        database = AppDatabase.getAppDatabase(this);
        recyclerView = findViewById(R.id.recycler_view);

        // Récupérer la liste des utilisateurs
        List<User> userList = database.userDao().getAll();
        usersAdapter = new UsersAdapter(this, userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(usersAdapter);
    }
}

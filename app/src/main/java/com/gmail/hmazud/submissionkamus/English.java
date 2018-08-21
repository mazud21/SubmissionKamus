package com.gmail.hmazud.submissionkamus;

import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gmail.hmazud.submissionkamus.adapter.SearchAdapter;
import com.gmail.hmazud.submissionkamus.db.KmsHelper;
import com.gmail.hmazud.submissionkamus.model.KmsModel;

import java.util.ArrayList;

public class English extends AppCompatActivity {

    private KmsHelper kmsHelper;
    private SearchAdapter adapter;
    private ArrayList<KmsModel> arrayList = new ArrayList<>();
    private boolean english = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_english);

        RecyclerView recyclerView = findViewById(R.id.rv_english);

        kmsHelper = new KmsHelper(this);
        adapter = new SearchAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        try {
            kmsHelper.open();
            arrayList = kmsHelper.getAllData(english);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            kmsHelper.close();
        }
        adapter.replace(arrayList);
    }
}

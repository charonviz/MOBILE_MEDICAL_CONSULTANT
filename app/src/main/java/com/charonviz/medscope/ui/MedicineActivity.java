/* Copyright (c) 2023-2025 Maksim Nikonov. MIT License. */
package com.charonviz.medscope.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.charonviz.medscope.R;
import com.charonviz.medscope.data.MedicineDatabase;
import com.charonviz.medscope.model.Medicine;
import java.util.List;

/** Searchable offline medicine reference with ATC codes. */
public class MedicineActivity extends AppCompatActivity {
    private MedicineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Medicine Reference");

        List<Medicine> all = new MedicineDatabase(this).getAll();
        RecyclerView rv = findViewById(R.id.recycler_medicine);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MedicineAdapter(all);
        rv.setAdapter(adapter);

        EditText search = findViewById(R.id.et_search);
        search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            public void onTextChanged(CharSequence s, int a, int b, int c) {}
            public void afterTextChanged(Editable s) { adapter.filter(s.toString()); }
        });
    }

    @Override public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}

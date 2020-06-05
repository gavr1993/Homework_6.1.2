package com.example.homework_411;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ListViewActivity extends AppCompatActivity {

    private static final String KEY_TEXT = "key_text";
    private static final String KEY_COUNT = "key_count";
    private static final String NOTE_TXT = "note_txt";
    private SwipeRefreshLayout swipeRefresh;
    private SharedPreferences textSharedPref;
    private static final String INDEXES_BUNDLE_KEY = "indexes of items to delete: ";
    private List<Map<String, String>> simpleAdapterContent = new ArrayList<>();
    private ArrayList<Integer> indexes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        final BaseAdapter listContentAdapter = createAdapter(simpleAdapterContent);
        final ListView list = findViewById(R.id.list);
        list.setAdapter(listContentAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                simpleAdapterContent.remove(position);
                listContentAdapter.notifyDataSetChanged();
                indexes.add(position);
            }
        });

        textSharedPref = getSharedPreferences("my_prefs", MODE_PRIVATE);
        saveDateToSharedPref();
        loadDateFromSharedPref();
        listContentAdapter.notifyDataSetChanged();
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                simpleAdapterContent.clear();
                loadDateFromSharedPref();
                listContentAdapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
            }
        });

        if (savedInstanceState != null) {
            indexes = savedInstanceState.getIntegerArrayList(INDEXES_BUNDLE_KEY);
            Objects.requireNonNull(indexes);
            for (Integer indexInt : indexes) {
                simpleAdapterContent.remove(indexInt.intValue());
            }
            listContentAdapter.notifyDataSetChanged();
        } else {
            indexes = new ArrayList<>();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putIntegerArrayList(INDEXES_BUNDLE_KEY, indexes);
    }

    private void loadDateFromSharedPref() {
        String text = textSharedPref.getString(NOTE_TXT, "");
        final String[] titles = text.split("\n\n");
        for (String title : titles) {
            Map<String, String> map = new HashMap<>();
            map.put(KEY_TEXT, title);
            map.put(KEY_COUNT, title.length() + "");
            simpleAdapterContent.add(map);
        }
    }

    private void saveDateToSharedPref() {
        if (textSharedPref.getString(NOTE_TXT, "").isEmpty()) {
            textSharedPref.edit().putString(NOTE_TXT, getString(R.string.large_text)).apply();
        }
    }

    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> text) {
        return new SimpleAdapter(this, text, R.layout.double_text, new String[]
                {KEY_TEXT, KEY_COUNT}, new int[]{R.id.textViewTop, R.id.textViewBottom});
    }
}
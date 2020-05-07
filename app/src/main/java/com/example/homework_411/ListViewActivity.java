package com.example.homework_411;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {

    private static final String KEY_TEXT = "key_text";
    private static final String KEY_COUNT = "key_count";
    private static final String NOTE_TXT = "note_txt";
    private SwipeRefreshLayout swipeRefresh;
    private SharedPreferences textSharedPref;
    List<Map<String, String>> simpleAdapterContent = new ArrayList<>();

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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        simpleAdapterContent.clear();
                        listContentAdapter.notifyDataSetChanged();
                        loadDateFromSharedPref();
                        listContentAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                }, 100);
            }
        });
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
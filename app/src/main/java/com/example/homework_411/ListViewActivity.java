package com.example.homework_411;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewActivity extends AppCompatActivity {

    private static final String KEY_TEXT = "key_text";
    private static final String KEY_COUNT = "key_count";
    private static final String NOTE_TXT = "note_txt";

    private SharedPreferences textSharedPref;
    List<Map<String, String>> simpleAdapterContent = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView list = findViewById(R.id.list);

        saveDateToSharedPref();

        String text = textSharedPref.getString(NOTE_TXT, "");
        String[] titles = text.split("\n\n");
        for (String title : titles) {
            Map<String, String> map = new HashMap<>();
            map.put(KEY_TEXT, title);
            map.put(KEY_COUNT, title.length() + "");
            simpleAdapterContent.add(map);
        }


        BaseAdapter listContentAdapter = createAdapter(simpleAdapterContent);

        list.setAdapter(listContentAdapter);

        textSharedPref = getSharedPreferences("my_prefs", MODE_PRIVATE);


    }

    private void saveDateToSharedPref() {
        SharedPreferences.Editor myEditor = textSharedPref.edit();
        if (textSharedPref.equals("")) {
            String text = getString(R.string.large_text);
            myEditor.putString(NOTE_TXT, text);
            myEditor.apply();
        }
    }

    @NonNull
    private BaseAdapter createAdapter(List<Map<String, String>> values) {
        return new SimpleAdapter(this, values, R.layout.double_text, new String[]
                {KEY_TEXT, KEY_COUNT}, new int[]{R.id.textViewTop, R.id.textViewBottom});
    }
}
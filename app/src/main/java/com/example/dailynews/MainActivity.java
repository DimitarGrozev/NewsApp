package com.example.dailynews;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.dailynews.Model.Articles;
import com.example.dailynews.Model.Headlines;
import com.example.dailynews.Model.Suggestion;
import com.example.dailynews.Model.Suggestions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String[] Suggestions = new String[]{
    };
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText etQuery;
    Button btnSearch, btnAboutUs;
    Dialog dialog;
    final String API_KEY = "fe7096fa41e84cd2b410230482fea758";
    Adapter adapter;
    List<Articles> articles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);

        etQuery = findViewById(R.id.etQuery);
        btnSearch = findViewById(R.id.btnSearch);
        dialog = new Dialog(MainActivity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final String country = getCountry();

        retrieveJson("", country, API_KEY);

        AutoCompleteTextView editText = findViewById(R.id.etQuery);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, Suggestions
        );
        editText.setAdapter(adapter);
        editText.setThreshold(1);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson("", country, API_KEY);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            private  List<String> GetSuggestions(List<Suggestion> suggestions) {
            List<String> words = new ArrayList<>();

                for (Suggestion s:
                     suggestions) {
                    words.add(s.getWord());
                }

                return words;
            }

            @Override
            public void afterTextChanged(Editable s) {
                Call<List<Suggestion>> call;

                if (s.toString() != "") {
                    call = AutocompleteClient.getInstance().getApi().getSuggestions(s.toString());

                    call.enqueue(new Callback<List<Suggestion>>() {
                        @Override
                        public void onResponse(Call<List<Suggestion>> call, Response<List<Suggestion>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                List<Suggestion> results = response.body();
                                Suggestions = GetSuggestions(results).toArray(new String[0]);

                                adapter.clear();
                                adapter.addAll(Suggestions);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Suggestion>> call, Throwable t) {
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etQuery.getText().toString().equals("")) {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson(etQuery.getText().toString(), country, API_KEY);
                        }
                    });
                    retrieveJson(etQuery.getText().toString(), country, API_KEY);
                } else {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson("", country, API_KEY);
                        }
                    });
                    retrieveJson("", country, API_KEY);
                }
            }
        });
    }

    public void retrieveJson(String query, String country, String apiKey) {
        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call;
        if (!etQuery.getText().toString().equals("")) {
            call = ApiClient.getInstance().getApi().getSpecificData(query, apiKey);
        } else {
            call = ApiClient.getInstance().getApi().getHeadlines(country, apiKey);
        }

        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(MainActivity.this, articles);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getCountry() {
        Locale locale = new Locale("Cyril", "bg");
        String country = locale.getCountry();
        return country;
    }
}


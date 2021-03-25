package com.example.dailynews;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.dailynews.Model.Articles;
import com.example.dailynews.Model.Headlines;
import com.example.dailynews.Model.Suggestion;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String[] Suggestions = new String[]{
    };
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView txtHeader;
    EditText etQuery;
    Button btnSearch, btnBusiness, btnSport, btnEntertainment, btnScience, btnTechnology, btnHealth;
    Dialog dialog;
    final String API_KEY = "fe7096fa41e84cd2b410230482fea758";
    Adapter adapter;
    List<Articles> articles = new ArrayList<>();
    int selectedCountry = 0;
    ArrayList<String> countryList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);

        txtHeader = (TextView)findViewById(R.id.txtHeader);
        etQuery = findViewById(R.id.etQuery);
        btnSearch = findViewById(R.id.btnSearch);
        btnBusiness = findViewById(R.id.btnBusiness);
        btnSport = findViewById(R.id.btnSport);
        btnHealth = findViewById(R.id.btnHealth);
        btnTechnology = findViewById(R.id.btnTechnology);
        btnEntertainment = findViewById(R.id.btnEntertainment);
        btnScience = findViewById(R.id.btnScience);
        dialog = new Dialog(MainActivity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        countryList.add("bg");
        countryList.add("gb");
        countryList.add("de");
        countryList.add("fr");
        countryList.add("ru");
        retrieveJson("", countryList.get(selectedCountry), API_KEY,"");

        AutoCompleteTextView editText = findViewById(R.id.etQuery);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, Suggestions
        );
        editText.setAdapter(adapter);
        editText.setThreshold(1);

        Spinner languageSpinner = (Spinner) findViewById(R.id.spinnerLanguage);
        ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.languages));
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(languageAdapter);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                String country = parent.getItemAtPosition(position).toString().toLowerCase();
                SelectLanguage(country);
                retrieveJson("", countryList.get(selectedCountry), API_KEY,"");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson("", countryList.get(selectedCountry), API_KEY,"");
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
                            retrieveJson(etQuery.getText().toString(),countryList.get(selectedCountry), API_KEY,"");
                        }
                    });
                    retrieveJson(etQuery.getText().toString(), countryList.get(selectedCountry), API_KEY,"");
                } else {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson("", countryList.get(selectedCountry), API_KEY,"");
                        }
                    });
                    retrieveJson("", countryList.get(selectedCountry), API_KEY,"");
                }
            }
        });

        btnBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etQuery.setText(btnBusiness.getText().toString());
                retrieveJson(etQuery.getText().toString(),countryList.get(selectedCountry), API_KEY, "business");
            }
        });

        btnSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etQuery.setText(btnSport.getText().toString());
                retrieveJson(etQuery.getText().toString(),countryList.get(selectedCountry), API_KEY,"sport");
            }
        });

        btnHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etQuery.setText(btnHealth.getText().toString());
                retrieveJson(etQuery.getText().toString(),countryList.get(selectedCountry), API_KEY,"health");
            }
        });

        btnTechnology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etQuery.setText(btnTechnology.getText().toString());
                retrieveJson(etQuery.getText().toString(),countryList.get(selectedCountry), API_KEY,"technology");
            }
        });

        btnEntertainment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etQuery.setText(btnEntertainment.getText().toString());
                retrieveJson(etQuery.getText().toString(),countryList.get(selectedCountry), API_KEY,"entertainment");
            }
        });

        btnScience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etQuery.setText(btnScience.getText().toString());
                retrieveJson(etQuery.getText().toString(),countryList.get(selectedCountry), API_KEY,"science");
            }
        });
    }



    public void retrieveJson(String query, String country, String apiKey, String category) {
        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call;
        if(!category.equals("")){
            call =  ApiClient.getInstance().getApi().getCategory(country, category, apiKey);
        }
        else if (!etQuery.getText().toString().equals("")) {
            call = ApiClient.getInstance().getApi().getSpecificData(query, country, apiKey);
        } else {
            call = ApiClient.getInstance().getApi().getHeadlines(country, apiKey, 50);
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

    public void SelectLanguage(String country){
        switch(country){
            case "bg":
                txtHeader.setText("НОВИНИ");
                etQuery.setHint("Търсене");
                etQuery.setText("");
                btnBusiness.setText("Бизнес");
                btnSport.setText("Спорт");
                btnHealth.setText("Здраве");
                btnTechnology.setText("Технологии");
                btnEntertainment.setText("Развлекателни");
                btnScience.setText("Наука");
                selectedCountry = 0;
                break;
            case "gb":
                txtHeader.setText("NEWS");
                etQuery.setHint("Search");
                etQuery.setText("");
                btnBusiness.setText("Business");
                btnSport.setText("Sport");
                btnHealth.setText("Health");
                btnTechnology.setText("Technology");
                btnEntertainment.setText("Entertainment");
                btnScience.setText("Science");
                selectedCountry = 1;
                break;
            case "de":
                txtHeader.setText("NACHRICHTEN");
                etQuery.setHint("Suche");
                etQuery.setText("");
                btnBusiness.setText("Geschäft");
                btnSport.setText("Sport");
                btnHealth.setText("Gesundheit");
                btnTechnology.setText("Technologie");
                btnEntertainment.setText("Unterhaltung");
                btnScience.setText("Wissenschaft");
                selectedCountry = 2;
                break;
            case "fr":
                txtHeader.setText("NOUVELLES");
                etQuery.setHint("Chercher");
                etQuery.setText("");
                btnBusiness.setText("Entreprise");
                btnSport.setText("Sport");
                btnHealth.setText("Santé");
                btnTechnology.setText("La technologie");
                btnEntertainment.setText("Divertissement");
                btnScience.setText("Science");
                selectedCountry = 3;
                break;
            case "ru":
                txtHeader.setText("НОВОСТИ");
                etQuery.setHint("Поиск");
                etQuery.setText("");
                btnBusiness.setText("Бизнес");
                btnSport.setText("Спорт");
                btnHealth.setText("Здоровье");
                btnTechnology.setText("Технологии");
                btnEntertainment.setText("Развлечения");
                btnScience.setText("Наука");
                selectedCountry = 4;
                break;
        }
    }
}


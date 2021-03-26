package com.example.dailynews;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
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
import java.util.Random;

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
    Button btnSearch;
    Dialog dialog;
    final String API_KEY = "fe7096fa41e84cd2b410230482fea758";
    Adapter adapter;
    public static boolean flag = true;
    List<Articles> articles = new ArrayList<>();
    static int selectedCountry = 0;
    ArrayList<String> countryList = new ArrayList<String>();
    NotificationGenerator generator = new NotificationGenerator();
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlarmHandler alarmHandler = new AlarmHandler(this);
        alarmHandler.cancelAlarmManager();
        alarmHandler.setAlarmManager();

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        recyclerView = findViewById(R.id.recyclerView);

        txtHeader = (TextView)findViewById(R.id.txtHeader);
        etQuery = findViewById(R.id.etQuery);
        btnSearch = findViewById(R.id.btnSearch);
        dialog = new Dialog(MainActivity.this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        countryList.add("bg");
        countryList.add("gb");
        countryList.add("de");
        countryList.add("fr");
        countryList.add("ru");
        retrieveJson("", countryList.get(selectedCountry), API_KEY);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

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
                String country = parent.getItemAtPosition(position).toString().toLowerCase();
                SelectLanguage(country);
                retrieveJson("", countryList.get(selectedCountry), API_KEY);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveJson("", countryList.get(selectedCountry), API_KEY);
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
                            retrieveJson(etQuery.getText().toString(),countryList.get(selectedCountry), API_KEY);
                        }
                    });
                    retrieveJson(etQuery.getText().toString(), countryList.get(selectedCountry), API_KEY);
                } else {
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            retrieveJson("", countryList.get(selectedCountry), API_KEY);
                        }
                    });
                    retrieveJson("", countryList.get(selectedCountry), API_KEY);
                }
            }
        });

          //  generator.StartThread();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null)
            {
                //Cry about not being clicked on
            } else {
                generator.StopThread();
            if (extras.getBoolean("00")) {
                    //  selectedCountry = 0;
                    languageSpinner.setSelection(0);
                    retrieveJson("", "bg", API_KEY);
                } else if (extras.getBoolean("01")) {
                    //  selectedCountry = 0;
                    languageSpinner.setSelection(0);
                    retrieveJson("", "bg", API_KEY);
                } else if (extras.getBoolean("02")) {
                    // selectedCountry = 0;
                    languageSpinner.setSelection(0);
                    retrieveJson("", "bg", API_KEY);
                } else if (extras.getBoolean("03")) {
                    // selectedCountry = 0;
                    languageSpinner.setSelection(0);
                    retrieveJson("", "bg", API_KEY);
                } else if (extras.getBoolean("04")) {
                    // selectedCountry = 0;
                    languageSpinner.setSelection(0);
                    retrieveJson("", "bg", API_KEY);
                } else if (extras.getBoolean("05")) {
                    // selectedCountry = 0;
                    languageSpinner.setSelection(0);
                    retrieveJson("", "bg", API_KEY);
                } else if (extras.getBoolean("10")) {
                    // selectedCountry = 1;
                    languageSpinner.setSelection(1);
                    retrieveJson("", "gb", API_KEY);
                } else if (extras.getBoolean("11")) {
                    //  selectedCountry = 1;
                    languageSpinner.setSelection(1);
                    retrieveJson("", "gb", API_KEY);
                } else if (extras.getBoolean("12")) {
                    //  selectedCountry = 1;
                    languageSpinner.setSelection(1);
                    retrieveJson("", "gb", API_KEY);
                } else if (extras.getBoolean("13")) {
                    // selectedCountry = 1;
                    languageSpinner.setSelection(1);
                    retrieveJson("", "gb", API_KEY);
                } else if (extras.getBoolean("14")) {
                    // selectedCountry = 1;
                    languageSpinner.setSelection(1);
                    retrieveJson("", "gb", API_KEY);
                } else if (extras.getBoolean("15")) {
                    // selectedCountry = 1;
                    languageSpinner.setSelection(1);
                    retrieveJson("", "gb", API_KEY);
                } else if (extras.getBoolean("20")) {
                    // selectedCountry = 2;
                    languageSpinner.setSelection(2);
                    retrieveJson("", "de", API_KEY);
                } else if (extras.getBoolean("21")) {
                    // selectedCountry = 2;
                    languageSpinner.setSelection(2);
                    retrieveJson("", "de", API_KEY);
                } else if (extras.getBoolean("22")) {
                    // selectedCountry = 2;
                    languageSpinner.setSelection(2);
                    retrieveJson("", "de", API_KEY);
                } else if (extras.getBoolean("23")) {
                    //  selectedCountry = 2;
                    languageSpinner.setSelection(2);
                    retrieveJson("", "de", API_KEY);
                } else if (extras.getBoolean("24")) {
                    //  selectedCountry = 2;
                    languageSpinner.setSelection(2);
                    retrieveJson("", "de", API_KEY);
                } else if (extras.getBoolean("25")) {
                    //  selectedCountry = 2;
                    languageSpinner.setSelection(2);
                    retrieveJson("", "de", API_KEY);
                } else if (extras.getBoolean("30")) {
                    // selectedCountry = 3;
                    languageSpinner.setSelection(3);
                    retrieveJson("", "fr", API_KEY);
                } else if (extras.getBoolean("31")) {
                    //  selectedCountry = 3;
                    languageSpinner.setSelection(3);
                    retrieveJson("", "fr", API_KEY);
                } else if (extras.getBoolean("32")) {
                    //  selectedCountry = 3;
                    languageSpinner.setSelection(3);
                    retrieveJson("", "fr", API_KEY);
                } else if (extras.getBoolean("33")) {
                    //  selectedCountry = 3;
                    languageSpinner.setSelection(3);
                    retrieveJson("", "fr", API_KEY);
                } else if (extras.getBoolean("34")) {
                    //  selectedCountry = 3;
                    languageSpinner.setSelection(3);
                    retrieveJson("", "fr", API_KEY);
                } else if (extras.getBoolean("35")) {
                    //  selectedCountry = 3;
                    languageSpinner.setSelection(3);
                    retrieveJson("", "fr", API_KEY);
                } else if (extras.getBoolean("40")) {
                    // selectedCountry = 4;
                    languageSpinner.setSelection(4);
                    retrieveJson("", "ru", API_KEY);
                } else if (extras.getBoolean("41")) {
                    //  selectedCountry = 4;
                    languageSpinner.setSelection(4);
                    retrieveJson("", "ru", API_KEY);
                } else if (extras.getBoolean("42")) {
                    //  selectedCountry =4;
                    languageSpinner.setSelection(4);
                    retrieveJson("", "ru", API_KEY);
                } else if (extras.getBoolean("43")) {
                    //  selectedCountry = 4;
                    languageSpinner.setSelection(4);
                    retrieveJson("", "ru", API_KEY);
                } else if (extras.getBoolean("44")) {
                    //  selectedCountry = 4;
                    languageSpinner.setSelection(4);
                    retrieveJson("", "ru", API_KEY);
                } else if (extras.getBoolean("45")) {
                    // selectedCountry = 4;
                    languageSpinner.setSelection(4);
                    retrieveJson("", "ru", API_KEY);
                }
                generator.StopThread();
            }

        }

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

    public void SelectLanguage(String country){
        switch(country){
            case "bg":
                txtHeader.setText("НОВИНИ");
                etQuery.setHint("Търсене");
                etQuery.setText("");
                selectedCountry = 0;

                break;
            case "gb":
                txtHeader.setText("NEWS");
                etQuery.setHint("Search");
                etQuery.setText("");
                selectedCountry = 1;

                break;
            case "de":
                txtHeader.setText("NACHRICHTEN");
                etQuery.setHint("Suche");
                etQuery.setText("");
                selectedCountry = 2;

                break;
            case "fr":
                txtHeader.setText("NOUVELLES");
                etQuery.setHint("Chercher");
                etQuery.setText("");
                selectedCountry = 3;

                break;
            case "ru":
                txtHeader.setText("НОВОСТИ");
                etQuery.setHint("Поиск");
                etQuery.setText("");
                selectedCountry = 4;

                break;
        }
    }
    class NotificationGenerator{
        private volatile boolean stopThread = false;
        NotificationRunnable runnable = new NotificationRunnable();
        Thread th1 = new Thread(runnable);
        public void StartThread(){

            th1.start();

        }
        public void StopThread(){
            th1.interrupt();
        }



        class NotificationRunnable implements Runnable {

            @Override
            public void run() {
              while(stopThread==false) {
                    try {
                        Thread.sleep(15000);
                        if (stopThread == false) {
                            CreateNotification();
                            MainActivity.flag = false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

               }

            }
        }
        private void CreateNotification(){







        }
    }

}


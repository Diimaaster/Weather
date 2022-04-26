package com.example.tutu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public EditText user;
    private Button btn;
    private Button history;
    private Button save;
    public TextView resultat;
    SharedPreferences sPref;
    SharedPreferences sPrefSecond;
    final String SAVED_TEXT = "saved_text";
    final String SAVED_TEXT_TEMP = "saved_text";
    Spinner spinner;
    String[] Citys = new String[] {
            "","Москва","Люберцы","Мариуполь"
    };
    ArrayList<String> CitySpiner = new ArrayList<>();
    String spin = "";
    String Msc = "Москва";
    String lub = "Люберцы";
    String Mar = "Мариуполь";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CitySpiner.add(spin);
        CitySpiner.add(Msc);
        CitySpiner.add(lub);
        CitySpiner.add(Mar);

        user = findViewById(R.id.user) ;
        btn = findViewById(R.id.main_btn) ;
        resultat = findViewById(R.id.result);
        history = findViewById(R.id.history_btn);
        save = findViewById(R.id.history_btn_save);
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, CitySpiner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selected = spinner.getSelectedItem().toString();
                user.setText(selected);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getText().toString().trim().equals(""))
                    Toast.makeText(MainActivity.this,R.string.no_user,Toast.LENGTH_LONG).show();
                else{
                    String city = user.getText().toString();
                    String key = "ad4210c46c67686e9d8722a8e404e010";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru";

                    new GetUrl().execute(url);
                }
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(MainActivity.this, History.class);
                startActivity(intent);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveText();
            }
        });


    }


    @SuppressLint("StaticFieldLeak")
    public class GetUrl extends AsyncTask<String,String,String>{

        protected void onPreExecute(){
            super.onPreExecute();
            resultat.setText("Ожидайте...");
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null){
                    buffer.append(line).append("\n");
                }
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(connection != null)
                    connection.disconnect();

                try {
                    if(reader != null)
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }

            return null;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                resultat.setText("Температура: " + jsonObject.getJSONObject("main").getDouble("temp") + " °"+ "\nОщущается как: "+ jsonObject.getJSONObject("main").getDouble("feels_like") + " °" + "\nСкорость ветра: "+ jsonObject.getJSONObject("wind").getDouble("speed") + " м/c");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

        public void saveText() {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        sPrefSecond = getSharedPreferences("MyPref2", MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        SharedPreferences.Editor ed1 = sPrefSecond.edit();
        ed.putString(SAVED_TEXT, user.getText().toString());
        ed1.putString(SAVED_TEXT_TEMP, resultat.getText().toString());
        ed.commit();
        ed1.commit();
        Toast.makeText(MainActivity.this, "Text saved", Toast.LENGTH_SHORT).show();
    }


}

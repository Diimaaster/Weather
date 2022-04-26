package com.example.tutu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class History extends MainActivity {

    private Button Clean;
    private TextView HistoryCity;
    private TextView History;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_history);

            Clean = findViewById(R.id.main_btn_act);
            History = findViewById(R.id.history);
            HistoryCity = findViewById(R.id.history_city);

            loadText();

            Clean.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    History.setText("");
                    HistoryCity.setText("");
                    SharedPreferences.Editor ed = sPref.edit();
                    SharedPreferences.Editor ed1 = sPrefSecond.edit();
                    ed.putString(SAVED_TEXT, History.getText().toString());
                    ed1.putString(SAVED_TEXT_TEMP, HistoryCity.getText().toString());
                    ed.commit();
                    ed1.commit();
                    Toast.makeText(History.this, "Text update", Toast.LENGTH_SHORT).show();

                }
            });

            HistoryCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    user.setText(HistoryCity.getText().toString());

                }
            });

        }

    private void loadText() {
        sPref = getSharedPreferences("MyPref", MODE_PRIVATE);
        sPrefSecond = getSharedPreferences("MyPref2", MODE_PRIVATE);
        String savedText = sPref.getString(SAVED_TEXT, "");
        CitySpiner.add(savedText);
        String savedTextTemp = sPrefSecond.getString(SAVED_TEXT_TEMP, "");
        HistoryCity.setText(" " + History.getText().toString());
        History.setText("\n" + savedText + "\n" + savedTextTemp);
        Toast.makeText(History.this, "Text loaded", Toast.LENGTH_SHORT).show();
    }
}

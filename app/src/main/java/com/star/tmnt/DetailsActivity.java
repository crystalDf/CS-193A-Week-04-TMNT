package com.star.tmnt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class DetailsActivity extends AppCompatActivity {

    private Set<String> mDictionary;

    private Button mSubmitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();

        String result = intent.getStringExtra(MainActivity.RESULT);

        setInfo(result);

        if (mDictionary == null) {
            loadDictionary();
        }

        mSubmitButton = (Button) findViewById(R.id.submit);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.word);

                String word = editText.getText().toString();

                if (mDictionary.contains(word)) {
                    Intent i = new Intent();
                    i.putExtra("word", word);
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    Toast.makeText(DetailsActivity.this, "Not in dictionary.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void setInfo(String result) {
        String[] turtles = getResources().getStringArray(R.array.turtles);
        String[] infos = getResources().getStringArray(R.array.infos);
        for (int i = 0; i < turtles.length; i++) {
            if (turtles[i].equals(result)) {
                TextView infoTextView = (TextView) findViewById(R.id.turtle_details);
                infoTextView.setText(getResources().getIdentifier(
                        infos[i], "string", getPackageName()));
                return;
            }
        }
    }

    private void loadDictionary() {
        mDictionary = new HashSet<>();

        Scanner scanner = new Scanner(getResources().openRawResource(R.raw.dict));

        while (scanner.hasNext()) {
            mDictionary.add(scanner.nextLine());
        }
    }
}

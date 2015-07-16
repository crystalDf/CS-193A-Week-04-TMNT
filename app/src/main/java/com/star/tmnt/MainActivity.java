package com.star.tmnt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public static final String RESULT = "RESULT";

    public static final String TURTLE_ID = "TURTLE_ID";

    public static final int GET_DETAILS = 0;
    public static final int TAKE_PHOTO = 1;

    private Button mTakePhotoButton;

    private Spinner mSpinner;
    private ImageView mImageView;

    private MediaPlayer mMediaPlayer;

    private String mResultString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mResultString = parent.getSelectedItem().toString();
                setResult(mResultString);
                setImageView(mResultString);
                setInfo(mResultString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mImageView = (ImageView) findViewById(R.id.imageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(RESULT, mResultString);
                startActivityForResult(intent, GET_DETAILS);
            }
        });

        mTakePhotoButton = (Button) findViewById(R.id.take_photo);
        mTakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        mMediaPlayer = MediaPlayer.create(this, R.raw.tmnt_theme);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mMediaPlayer != null) {
            mMediaPlayer.setLooping(true);
            mMediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        mMediaPlayer.pause();

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(TURTLE_ID, mSpinner.getSelectedItemPosition());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // without will lose power
        super.onRestoreInstanceState(savedInstanceState);
        // without will be also OK
        mSpinner.setSelection(savedInstanceState.getInt(TURTLE_ID));
    }

    private void setResult(String result) {
        TextView resultTextView = (TextView) findViewById(R.id.textView);
        resultTextView.setText("You chose " + result);
    }

    private void setImageView(String result) {
        String[] turtles = getResources().getStringArray(R.array.turtles);
        String[] images = getResources().getStringArray(R.array.images);
        for (int i = 0; i < turtles.length; i++) {
            if (turtles[i].equals(result)) {
                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageResource(getResources().getIdentifier(
                        images[i], "mipmap", getPackageName()));
                return;
            }
        }
    }

    private void setInfo(String result) {
        String[] turtles = getResources().getStringArray(R.array.turtles);
        String[] infos = getResources().getStringArray(R.array.infos);
        for (int i = 0; i < turtles.length; i++) {
            if (turtles[i].equals(result)) {
                TextView infoTextView = (TextView) findViewById(R.id.turtle_info);
                infoTextView.setText(getResources().getIdentifier(
                        infos[i], "string", getPackageName()));
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GET_DETAILS:
                if (resultCode == RESULT_OK) {
                    String word = data.getStringExtra("word");
                    Toast.makeText(this, "You typed " + word, Toast.LENGTH_LONG).show();
                }
                break;
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    mImageView.setImageBitmap(bitmap);
                }
        }
    }
}

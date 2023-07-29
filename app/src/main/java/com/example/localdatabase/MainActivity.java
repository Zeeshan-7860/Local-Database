package com.example.localdatabase;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText nameEditText,enrollmentEditText,universityEditText;
    private Button saveButton;
    private Button retrieveButton;
    private TextView nameTextView, enrollmentTextView, universityTextView;

    private SQLiteDatabase database;

    private static final String DATABASE_NAME = "my_database";
    private static final String TABLE_NAME = "my_table";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ENROLLMENT = "enrollment";
    private static final String COLUMN_UNIVERSITY = "university";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEditText = findViewById(R.id.nameEditText);
        enrollmentEditText = findViewById(R.id.EnrollEditText);
        universityEditText = findViewById(R.id.UniEditText);
        saveButton = findViewById(R.id.saveButton);
        retrieveButton = findViewById(R.id.retrieveButton);
        nameTextView = findViewById(R.id.nameTextView);
        enrollmentTextView = findViewById(R.id.enrollTextView);
        universityTextView = findViewById(R.id.uniTextView);

        // Create or open the database
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        createTable();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        retrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveData();
            }
        });
    }

    private void createTable() {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + COLUMN_NAME + " TEXT, "
                + COLUMN_ENROLLMENT + " TEXT,"
                + COLUMN_UNIVERSITY + " TEXT)";

        database.execSQL(createTableQuery);

    }

    private void saveData() {
        String name = nameEditText.getText().toString();
        String enroll = enrollmentEditText.getText().toString();
        String Uni = universityEditText.getText().toString();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_ENROLLMENT, enroll);
        values.put(COLUMN_UNIVERSITY, Uni);
        long result = database.insert(TABLE_NAME, null, values);
        if (result != -1) {
            nameEditText.setText("");
            enrollmentEditText.setText("");
            universityEditText.setText("");
        }
        Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
    }

    private void retrieveData() {
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = database.rawQuery(selectQuery, null);

        StringBuilder data = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") String enrol = cursor.getString(cursor.getColumnIndex(COLUMN_ENROLLMENT));
                @SuppressLint("Range") String Uni = cursor.getString(cursor.getColumnIndex(COLUMN_UNIVERSITY));

                nameTextView.append(name);
                enrollmentTextView.append(enrol);
                universityTextView.append(Uni);
                nameTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                enrollmentTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                universityTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                nameTextView.append("\n");
                enrollmentTextView.append("\n");
                universityTextView.append("\n");
            } while (cursor.moveToNext());
        }

        cursor.close();


        Toast.makeText(this, "Data retrieved successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
package com.example.firebasestudentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class AddStudentActivity extends AppCompatActivity {

    private static final String TAG = "AddStudentActivity";

    private EditText etName, etAge;
    private Button btnAdd;

    // TODO: Task 1 - Declare Firebase variables
    private FirebaseFirestore db;
    private CollectionReference colRef;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        etName = (EditText) findViewById(R.id.editTextName);
        etAge = (EditText) findViewById(R.id.editTextAge);
        btnAdd = (Button) findViewById(R.id.buttonAdd);


        // TODO: Task 2: Get FirebaseFirestore instance and collection reference to "students"
        db = FirebaseFirestore.getInstance();

        colRef = db.collection("students");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Task 3: Retrieve name and age from EditText and instantiate a new Student object
                if (!(etName.getText().toString().equalsIgnoreCase("") && etAge.getText().toString().equalsIgnoreCase(""))) {
                    Student student = new Student(etName.getText().toString(), Integer.parseInt(etAge.getText().toString()));

                    //TODO: Task 4: Add student to database and go back to main screen
                    // Add a new document with a generated id.

                    colRef
                        .add(student)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                Intent i = new Intent(AddStudentActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                    finish();
                }
            }
        });


    }
}

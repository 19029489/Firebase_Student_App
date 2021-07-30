package com.example.firebasestudentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class StudentDetailsActivity extends AppCompatActivity {

    private static final String TAG = "StudentDetailsActivity";

    private EditText etName, etAge;
    private Button btnUpdate, btnDelete;

    private Student student;

    // TODO: Task 1 - Declare Firebase variables
    private FirebaseFirestore db;
    private CollectionReference colRef;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);

        etName = (EditText) findViewById(R.id.editTextName);
        etAge = (EditText) findViewById(R.id.editTextAge);
        btnUpdate = (Button) findViewById(R.id.buttonUpdate);
        btnDelete = (Button) findViewById(R.id.buttonDelete);

        Intent intent = getIntent();
        String id = (String) intent.getStringExtra("StudentID");

        // TODO: Task 2: Get FirebaseFirestore instance
        db = FirebaseFirestore.getInstance();

        colRef = db.collection("students");

        //TODO: Task 3: Get document reference by the student's id and set the name and age to EditText
        Intent i = getIntent();
        String studentId = i.getStringExtra("studentId");

        docRef = colRef.document(studentId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + snapshot.getData());
                        student = snapshot.toObject(Student.class);
                        etName.setText(student.getName());
                        etAge.setText("" + student.getAge());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Task 4: Update Student record based on input given
                if (!(etName.getText().toString().equalsIgnoreCase("") && etAge.getText().toString().equalsIgnoreCase(""))) {
                    String name = etName.getText().toString();
                    int age = Integer.parseInt(etAge.getText().toString());
                    Student student = new Student(name, age);
                    docRef.set(student);
                }

                Toast.makeText(getApplicationContext(), "Student record updated successfully", Toast.LENGTH_SHORT).show();

                finish();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Task 5: Delete Student record based on student id
                docRef
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });

                Toast.makeText(getApplicationContext(), "Student record deleted successfully", Toast.LENGTH_SHORT).show();

                finish();
            }
        });


    }
}
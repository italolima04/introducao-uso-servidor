package com.example.atividade02;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SecondaryActivity extends AppCompatActivity {
    public static int RESULT_APPLY = 1;
    public static int RESULT_CANCEL = 2;
    EditText editId, editTitle, editIsbn, editAuthor, editBookMaker;
    int id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary);

        editId = findViewById(R.id.editId);
        editTitle = findViewById(R.id.editTitle);
        editIsbn = findViewById(R.id.editIsbn);
        editAuthor = findViewById(R.id.editAuthor);
        editBookMaker = findViewById(R.id.editBookMaker);

        if(getIntent().getExtras().getSerializable("id") == null) {
            id = -1;
        } else {
            id = (int)getIntent().getExtras().getSerializable("id");
            editId.setText(String.valueOf(id));
        }

        if(getIntent().getExtras().get("book") != null) {
            ModelBook book = (ModelBook) getIntent().getExtras().get("book");
            editId.setText(String.valueOf(book.getId()));
            editTitle.setText(book.getTitle());
            editIsbn.setText(book.getIsbn());
            editAuthor.setText(book.getAuthor());
            editBookMaker.setText(book.getBookMaker());
        }
    }

    public void apply(View view) {
        Intent intent = new Intent();
        String title = editTitle.getText().toString();
        String isbn = editIsbn.getText().toString();
        String author = editAuthor.getText().toString();
        String bookMaker = editBookMaker.getText().toString();

        ModelBook book = new ModelBook(Integer.parseInt(editId.getText().toString()), title, isbn, author, bookMaker);

        intent.putExtra("book", book);

        setResult(RESULT_APPLY, intent);

        finish();
    }


    public void cancel(View view) {
        setResult(RESULT_CANCEL);
        finish();
    }
}

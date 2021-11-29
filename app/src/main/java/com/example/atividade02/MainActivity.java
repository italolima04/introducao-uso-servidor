package com.example.atividade02;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static int REQUEST_ADD = 1;
    public static int REQUEST_EDIT = 2;
    List<ModelBook> books = new ArrayList<ModelBook>();
    ArrayAdapter<ModelBook> arrayAdapterBooks;
    ListView listView;
    EditText inputMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        inputMain = findViewById(R.id.inputMain);

        arrayAdapterBooks =  new ArrayAdapter<ModelBook>(this, android.R.layout.simple_list_item_1, books);
        listView.setAdapter(arrayAdapterBooks);

    }

    public void addClick(View view) {
        Intent intent = new Intent( this, SecondaryActivity.class);
        intent.putExtra("id",books.size());

        //startActivity(intent);
        startActivityForResult(intent, REQUEST_ADD);
    }

    public void editClick(View view) {
        Intent intent = new Intent( this, SecondaryActivity.class);
        //startActivity(intent);

        for (ModelBook book : books) {
            if(book.getId() == Integer.parseInt(inputMain.getText().toString())) {
                intent.putExtra("book", book);
                startActivityForResult(intent, REQUEST_EDIT);
            } else {
                Toast.makeText(this, "Erro", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ModelBook book = null;

        if(resultCode == SecondaryActivity.RESULT_ADD && requestCode == REQUEST_ADD) {
            if(data.getExtras() != null) {
                book = (ModelBook) data.getExtras().get("book");
                books.add(book);
                arrayAdapterBooks.notifyDataSetChanged();
            }
        }else if(resultCode == SecondaryActivity.RESULT_ADD && requestCode == REQUEST_EDIT) {
            if(data.getExtras() != null) {
                book = (ModelBook) data.getExtras().get("book");
                for (ModelBook bookSearch : books) {
                    if(bookSearch.getId() == book.getId()) {
                        bookSearch.setTitle(book.getTitle());
                        bookSearch.setIsbn(book.getIsbn());
                        bookSearch.setAuthor(book.getAuthor());
                        bookSearch.setBookMaker(book.getBookMaker());
                    }
                }
                arrayAdapterBooks.notifyDataSetChanged();
            }
        }
    }
}
package com.example.atividade02;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.Touch;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static int REQUEST_ADD = 1;
    public static int REQUEST_EDIT = 2;
    public static int REQUEST_DELETE = 3;

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

        showListBooks();
    }

    public void showListBooks() {
        books.clear();
        db.collection("books")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ModelBook mBook = new ModelBook((Long) document.get("id"), "", "", "", "");
                                mBook.setTitle((String) document.get("title"));
                                mBook.setIsbn((String) document.get("isbn"));
                                mBook.setAuthor((String) document.get("author"));
                                mBook.setBookMaker((String) document.get("bookMaker"));
                                books.add(mBook);
                            }
                            arrayAdapterBooks =  new ArrayAdapter<ModelBook>(MainActivity.this, android.R.layout.simple_list_item_1, books);
                            listView.setAdapter(arrayAdapterBooks);
                        } else {
                            System.out.println("Error to list books");
                        }
                    }
                });
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

    public void deleteClick(View view) {
        Intent intent = new Intent( this, SecondaryActivity.class);

        for (ModelBook book : books) {
            if(book.getId() == Integer.parseInt(inputMain.getText().toString())) {
                intent.putExtra("book", book);
                startActivityForResult(intent, REQUEST_DELETE);
            } else {
                Toast.makeText(this, "Erro", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ModelBook book = null;

        if(resultCode == SecondaryActivity.RESULT_APPLY && requestCode == REQUEST_ADD) {
            if(data.getExtras() != null) {
                book = (ModelBook) data.getExtras().get("book");
                db.collection("books")
                        .add(book)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                arrayAdapterBooks.notifyDataSetChanged();
                                System.out.println("Documento adicionado com sucesso");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                System.out.println("Erro ao adicionar documento" + e);
                            }
                        });
            }
            showListBooks();
            arrayAdapterBooks.notifyDataSetChanged();
        }else if(resultCode == SecondaryActivity.RESULT_APPLY && requestCode == REQUEST_EDIT) {
            if(data.getExtras() != null) {
                book = (ModelBook) data.getExtras().get("book");
                for (ModelBook bookSearch : books) {
                    if(bookSearch.getId() == book.getId()) {
                        ModelBook finalBook = book;
                        db.collection("books")
                                .whereEqualTo("title", bookSearch.getTitle())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                db.collection("books").document(document.getId())
                                                        .update(
                                                                "title", finalBook.getTitle(),
                                                                "author", finalBook.getAuthor(),
                                                                "bookMaker", finalBook.getBookMaker(),
                                                                "isbn", finalBook.getIsbn()
                                                        )
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                System.out.println("Documento atualizado com sucesso");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                System.out.println("Erro ao atualizar documento" + e);
                                                            }
                                                        });

                                            }
                                        } else {
                                            System.out.println("Error: " +  task.getException());
                                        }
                                    }
                                });
                    }
                }
            }
            showListBooks();
            arrayAdapterBooks.notifyDataSetChanged();
        }else if(resultCode == SecondaryActivity.RESULT_APPLY && requestCode == REQUEST_DELETE) {
            if(data.getExtras() != null) {
                book = (ModelBook) data.getExtras().get("book");
                db.collection("books")
                        .whereEqualTo("title", book.getTitle())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        db.collection("books").document(document.getId())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        System.out.println("Documento excluído com sucesso");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        System.out.println("Erro ao deletar documento" + e);
                                                    }
                                                });
                                    }
                                } else {
                                    System.out.println("Error: " +  task.getException());
                                }
                            }
                        });
            }
            showListBooks();
            arrayAdapterBooks.notifyDataSetChanged();
        }
    }
}
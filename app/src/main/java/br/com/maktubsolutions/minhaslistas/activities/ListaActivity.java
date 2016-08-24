package br.com.maktubsolutions.minhaslistas.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import br.com.maktubsolutions.minhaslistas.R;
import br.com.maktubsolutions.minhaslistas.adapter.RecyclerAdapter;
import br.com.maktubsolutions.minhaslistas.util.LibraryClass;

public class ListaActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private ArrayList itens = new ArrayList<>();
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private EditText edt_item;
    private FloatingActionButton fab;
    private String nomeLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Itens da lista");

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(ListaActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(authStateListener);
        databaseReference = LibraryClass.getFirebase();

        edt_item = (EditText) findViewById(R.id.edt_addItem);

        fab = (FloatingActionButton) findViewById(R.id.fab_addItem);
        fab.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.rv_itens);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(this, itens, false);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        String nLista = getIntent().getStringExtra("nomeLista");
        nomeLista = nLista;
    }

    @Override
    public void onClick(View v) {
        if (!edt_item.getText().toString().isEmpty()) {
            String uid = firebaseAuth.getCurrentUser().getUid();
            String item = edt_item.getText().toString();

            adapter.addItem(item);
            if (nomeLista != null) {
                databaseReference.child("users").child(uid).child("listas").child(nomeLista).push().setValue(item);
            }
            edt_item.setText("");
        } else {
            itens.set(0, edt_item.getText().toString());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
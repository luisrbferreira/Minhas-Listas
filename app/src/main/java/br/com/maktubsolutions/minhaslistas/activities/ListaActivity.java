package br.com.maktubsolutions.minhaslistas.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

import br.com.maktubsolutions.minhaslistas.R;
import br.com.maktubsolutions.minhaslistas.adapter.RecyclerAdapter;

public class ListaActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList itens = new ArrayList<>();
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private EditText edt_item;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Itens da lista");

        edt_item = (EditText)findViewById(R.id.edt_addItem);

        fab = (FloatingActionButton) findViewById(R.id.fab_addItem);
        fab.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.rv_users1);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(this, itens, false);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if(!edt_item.getText().toString().isEmpty()){
            adapter.addItem(edt_item.getText().toString());
            edt_item.setText("");
        }
        else {
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
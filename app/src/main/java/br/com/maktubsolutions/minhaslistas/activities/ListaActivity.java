package br.com.maktubsolutions.minhaslistas.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

import br.com.maktubsolutions.minhaslistas.R;
import br.com.maktubsolutions.minhaslistas.adapter.RecyclerAdapter;

public class ListaActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList listas = new ArrayList<>();
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private EditText edt_itens;
    private int edit_position;
    private View view;
    private Paint p = new Paint();
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Itens da lista");

        initViews();
    }

    protected void initViews() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addItem);
        fab.setOnClickListener(this);

        edt_itens = (EditText) findViewById(R.id.edt_addItem);

        recyclerView = (RecyclerView) findViewById(R.id.rv_users1);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(this, listas);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_addItem:
                if (!edt_itens.getText().toString().isEmpty()) {
                    adapter.addItem(edt_itens.getText().toString());
                } else {
                    listas.set(edit_position, edt_itens.getText().toString());
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void removeView() {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }
}
package br.com.maktubsolutions.minhaslistas.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.maktubsolutions.minhaslistas.R;
import br.com.maktubsolutions.minhaslistas.activities.ListaActivity;

/**
 * Created by luis.ferreira on 05/07/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<String> listas;
    private Context context;

    public RecyclerAdapter(Context context, List<String> listas) {
        this.context = context;
        this.listas = listas;
    }

    @Override
    public int getItemCount() {
        return listas.size();
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_layout, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder viewHolder, int i) {

        viewHolder.name.setText(listas.get(i));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListaActivity.class);
                viewHolder.itemView.getContext().startActivity(intent);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private Context context;
        private List<String> listas = new ArrayList<>();

        public ViewHolder(View view, Context context) {
            super(view);

            view.setOnClickListener(this);
            this.listas = listas;
            name = (TextView) view.findViewById(R.id.textViewName);
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            listas.get(position);
            Intent intent = new Intent(this.context, ListaActivity.class);
            this.context.startActivity(intent);
        }
    }

    public void addItem(String item) {
        listas.add(item);
        notifyItemInserted(listas.size());
    }

    public void removeItem(int position) {
        listas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listas.size());
    }
}
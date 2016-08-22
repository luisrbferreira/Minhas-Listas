package br.com.maktubsolutions.minhaslistas.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.maktubsolutions.minhaslistas.Interface.ItemClickListener;
import br.com.maktubsolutions.minhaslistas.R;
import br.com.maktubsolutions.minhaslistas.activities.ListaActivity;

/**
 * Created by luis.ferreira on 05/07/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<String> listas;
    private Context context;
    private boolean deriva;

    public RecyclerAdapter(Context context, List<String> listas, boolean deriva) {
        this.context = context;
        this.listas = listas;
        this.deriva = deriva;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.ViewHolder viewHolder, int i) {

        viewHolder.name.setText(listas.get(i));

        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(context, "#" + position + " - " + listas.get(position) + " (Long click)", Toast.LENGTH_SHORT).show();
                } else {
                    if (deriva) {
                        Intent intent = new Intent(view.getContext(), ListaActivity.class);
                        context.startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView name;
        private ItemClickListener clickListener;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewName);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener){
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(v, getAdapterPosition(), true);
            return true;
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
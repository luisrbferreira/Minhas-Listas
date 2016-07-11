package br.com.maktubsolutions.minhaslistas.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.maktubsolutions.minhaslistas.R;

/**
 * Created by luis.ferreira on 05/07/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<String> listas;

    public RecyclerAdapter(List<String> listas) {
        this.listas = listas;
    }

    @Override
    public int getItemCount() {
        return listas.size();
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder viewHolder, int i) {

        viewHolder.name.setText(listas.get(i));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        public ViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.textViewName);
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
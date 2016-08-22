package br.com.maktubsolutions.minhaslistas.Interface;

import android.view.View;

/**
 * Created by luis.ferreira on 22/08/2016.
 */
public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
}
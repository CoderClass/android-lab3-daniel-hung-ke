package net.fitken.simplechat;

import android.view.View;

/**
 * Created by Ken on 2/2/2017.
 */

public interface RecyclerViewClickListener {
    void onItemClick(View v, int position);

    void onItemLongClick(View v, int position);
}

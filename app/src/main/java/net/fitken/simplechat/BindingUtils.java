package net.fitken.simplechat;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


/**
 * Created by Ken on 2/17/2017.
 */

public class BindingUtils {


    @BindingAdapter("imgUrl")
    public static void loadImageUrl(ImageView imageView, String imgUrl) {
        if (imgUrl == null) {
            return;
        }
        if (imgUrl.isEmpty()) {
            return;
        }
        Picasso.with(imageView.getContext()).load(imgUrl)
                .into(imageView);
    }
}


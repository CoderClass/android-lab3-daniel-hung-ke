package net.fitken.simplechat;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ken on 2/2/2017.
 */

public abstract class AbsBindingAdapter<T extends ViewDataBinding> extends RecyclerView.Adapter<AbsBindingHolder<? extends ViewDataBinding>> {
    private static RecyclerViewClickListener itemListener;

    public AbsBindingAdapter(RecyclerViewClickListener itemListener) {
        this.itemListener = itemListener;
    }

    @Override
    public AbsBindingHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        T binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getLayoutResourceId(viewType), parent, false);
//        T binding = getBindingView(parent,viewType);
        return new AbsBindingHolder<>(binding, itemListener);
    }

    @Override
    public void onBindViewHolder(AbsBindingHolder holder, int position) {
        updateBinding(holder.getBinding(), position);
    }

    @LayoutRes
    protected abstract int getLayoutResourceId(int viewType);


    public abstract void updateBinding(ViewDataBinding binding, int position);
}

class AbsBindingHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    final T binding;
    RecyclerViewClickListener itemListener;

    public AbsBindingHolder(T binding, RecyclerViewClickListener itemListener) {
        super(binding.getRoot());
        this.binding = binding;
        this.itemListener = itemListener;
        binding.getRoot().setOnClickListener(this);
        binding.getRoot().setOnLongClickListener(this);
    }

    public T getBinding() {
        return binding;
    }

    @Override
    public void onClick(View v) {
        itemListener.onItemClick(v, this.getLayoutPosition());

    }

    @Override
    public boolean onLongClick(View view) {
        itemListener.onItemLongClick(view, this.getLayoutPosition());
        return false;
    }
}

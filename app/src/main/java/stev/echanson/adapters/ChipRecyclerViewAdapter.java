package stev.echanson.adapters;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import stev.echanson.R;
import stev.echanson.classes.Model;

public class ChipRecyclerViewAdapter extends RecyclerView.Adapter<ChipRecyclerViewAdapter.ViewHolder> {

    private List<Map.Entry> mModelList;

    public ChipRecyclerViewAdapter(List<Map.Entry> modelList) {
        mModelList = modelList;
    }

    @Override
    public ChipRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chip_with_image, parent, false);
        return new ChipRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Map.Entry model = mModelList.get(position);
        Drawable imageViewDrawable = (Drawable) mModelList.get(position).getValue();
        String textViewText = (String) mModelList.get(position).getKey();

        holder.textView.setText(textViewText);
        holder.imageView.setImageDrawable(imageViewDrawable);
    }

    @Override
    public int getItemCount() {
        return mModelList == null ? 0 : mModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private ImageView imageView;
        private TextView textView;

        private ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = itemView.findViewById(R.id.chip_image);
            textView = itemView.findViewById(R.id.chip_text);
        }
    }
}

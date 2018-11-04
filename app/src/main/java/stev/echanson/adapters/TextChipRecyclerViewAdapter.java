package stev.echanson.adapters;

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

public class TextChipRecyclerViewAdapter extends RecyclerView.Adapter<TextChipRecyclerViewAdapter.ViewHolder> {

    private List<String> mModelList;

    public TextChipRecyclerViewAdapter(List<String> modelList) {
        mModelList = modelList;
    }

    @Override
    public TextChipRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chip, parent, false);
        return new TextChipRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TextChipRecyclerViewAdapter.ViewHolder holder, int position) {
        final String model = mModelList.get(position);
        String textViewText = (String) mModelList.get(position);

        holder.textView.setText(textViewText);
    }

    @Override
    public int getItemCount() {
        return mModelList == null ? 0 : mModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView textView;

        private ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textView = itemView.findViewById(R.id.chip_text);
        }
    }
}
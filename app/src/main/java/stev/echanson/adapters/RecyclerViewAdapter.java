package stev.echanson.adapters;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import stev.echanson.R;
import stev.echanson.classes.Model;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Model> mModelList;
    private int mSelectedColor;
    private int mNotSelectedColor;

    public RecyclerViewAdapter(List<Model> modelList, int selectedColor) {
        mModelList = modelList;
        mSelectedColor = selectedColor;
        mNotSelectedColor = Color.WHITE;
    }

    public RecyclerViewAdapter(List<Model> modelList, int selectedColor, int notSelectedColor) {
        mModelList = modelList;
        mSelectedColor = selectedColor;
        mNotSelectedColor = notSelectedColor;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Model model = mModelList.get(position);
        holder.textView.setText(model.getText());
        holder.view.setBackgroundColor(model.isSelected() ? mSelectedColor : mNotSelectedColor);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                model.setSelected(!model.isSelected());
                holder.view.setBackgroundColor(model.isSelected() ? mSelectedColor : mNotSelectedColor);
                holder.textView.setTextColor(model.isSelected() ? Color.WHITE : Color.DKGRAY);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mModelList == null ? 0 : mModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView textView;

        private MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}

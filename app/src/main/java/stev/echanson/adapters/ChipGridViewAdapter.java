package stev.echanson.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import stev.echanson.R;

public class ChipGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map.Entry> mModelList;

    // Gets the context so it can be used later
    public ChipGridViewAdapter(Context c, List<Map.Entry> modelList) {
        mContext = c;
        mModelList = modelList;
    }

    @Override
    public int getCount() {
        return mModelList == null ? 0 : mModelList.size();
    }

    @Override
    public Object getItem(int i) {
        return mModelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Drawable imageViewDrawable = (Drawable) mModelList.get(position).getValue();
        String textViewText = (String) mModelList.get(position).getKey();

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(mContext);

            // get layout
            gridView = inflater.inflate(R.layout.chip_with_image, null);

            // set image
            ImageView imageView = gridView
                    .findViewById(R.id.chip_image);

            imageView.setImageDrawable(imageViewDrawable);

            TextView tv = gridView
                    .findViewById(R.id.chip_text);

            tv.setText(textViewText);


        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }
}


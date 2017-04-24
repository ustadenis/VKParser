package com.app.vkparser.vkparser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ustad on 22.04.2017.
 */

public class GropesAdapter extends ArrayAdapter {

    private List<HashMap<String, Object>> mGropes;

    public GropesAdapter(Context context, List<HashMap<String, Object>> gropes) {
        super(context, R.layout.gropes_item);
        mGropes = gropes;
    }

    @Override
    public int getCount() {
        return mGropes.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.gropes_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();

        String imageUrl = (String)mGropes.get(position).get(Variables.TAG_IMAGE);

        viewHolder.mNameTV.setText((String)mGropes.get(position).get(Variables.TAG_NAME));
        viewHolder.mIdTV.setText((String)mGropes.get(position).get(Variables.TAG_ID));
        Picasso.with(getContext())
                .load(imageUrl != null && !imageUrl.isEmpty() ? imageUrl : "http://mock.ru")
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(viewHolder.mGroupIm);

        return convertView;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return mGropes.get(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mIdTV;
        TextView mNameTV;
        ImageView mGroupIm;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdTV = (TextView) mView.findViewById(R.id.id);
            mNameTV = (TextView) mView.findViewById(R.id.name);
            mGroupIm = (ImageView) mView.findViewById(R.id.group_image);
        }
    }
}

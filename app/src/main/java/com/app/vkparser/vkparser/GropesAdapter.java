package com.app.vkparser.vkparser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by ustad on 22.04.2017.
 */

public class GropesAdapter extends ArrayAdapter {

    private static final int VIEW_HOLDER_TAG = 100;
    private List<HashMap<String, Object>> mGropes;

    public GropesAdapter(Context context, List<HashMap<String, Object>> gropes) {
        super(context, R.layout.gropes_item);
        mGropes = gropes;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.gropes_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(VIEW_HOLDER_TAG, viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.mNameTV.setText((String)mGropes.get(position).get(Variables.TAG_NAME));
        viewHolder.mIdTV.setText((String)mGropes.get(position).get(Variables.TAG_ID));

        return convertView;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Unbinder unbinder;

        @BindView(R.id.id) TextView mIdTV;
        @BindView(R.id.name) TextView mNameTV;

        public ViewHolder(View view) {
            super(view);
            unbinder = ButterKnife.bind(view);
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            unbinder.unbind();
        }
    }
}

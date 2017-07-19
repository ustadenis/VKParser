package com.app.vkparser.vkparser.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.vkparser.vkparser.R;
import com.app.vkparser.vkparser.Variables;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ustad on 22.04.2017.
 */

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {

    private List<HashMap<String, Object>> mGropes;
    private Context mContext;

    public GroupsAdapter(Context context, List<HashMap<String, Object>> gropes) {
        mGropes = gropes;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gropes_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String imageUrl = (String)mGropes.get(position).get(Variables.TAG_IMAGE);

        holder.mNameTV.setText((String)mGropes.get(position).get(Variables.TAG_NAME));
        holder.mIdTV.setText((String)mGropes.get(position).get(Variables.TAG_ID));
        Picasso.with(mContext)
               .load(imageUrl != null && !imageUrl.isEmpty() ? imageUrl : "http://mock.ru")
               .placeholder(R.drawable.placeholder)
               .error(R.drawable.placeholder)
               .into(holder.mGroupIm);
    }

    @Override
    public int getItemCount() {
        return mGropes.size();
    }

    public void updateGroups(List<HashMap<String, Object>> groups) {
        mGropes.clear();
        mGropes.addAll(groups);
        notifyDataSetChanged();
    }

    public void addGroups(List<HashMap<String, Object>> groups) {
        mGropes.addAll(groups);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mIdTV;
        TextView mNameTV;
        ImageView mGroupIm;

        public ViewHolder(View view) {
            super(view);
            mIdTV = (TextView) itemView.findViewById(R.id.id);
            mNameTV = (TextView) itemView.findViewById(R.id.name);
            mGroupIm = (ImageView) itemView.findViewById(R.id.group_image);
        }
    }
}

package com.app.vkparser.vkparser;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.List;

/**
 * Created by denustav on 24/04/2017.
 */

public class MemberAdapter extends ArrayAdapter {

    private Handler mUiHandler;

    interface OnUserReady {
        void onUser(VKApiUserFull user);
    }

    public MemberAdapter(Context context, List<VKApiUserFull> members) {
        super(context, R.layout.member_item, members);
        mUiHandler = new Handler(context.getMainLooper());
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MemberAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.member_item, null);
            viewHolder = new MemberAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        viewHolder = (MemberAdapter.ViewHolder) convertView.getTag();

        VKApiUserFull user = (VKApiUserFull)getItem(position);

        String imageUrl = user.photo_50;

        viewHolder.mNameTV.setText(user.first_name + " " + user.last_name);
        viewHolder.mIdTV.setText(Integer.toString(user.id));
        viewHolder.mCity.setText(user.city != null ? user.city.title : "Unknown");
        Picasso.with(getContext())
                .load(imageUrl != null && !imageUrl.isEmpty() ? imageUrl : "http://mock.ru")
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(viewHolder.mGroupIm);

        return convertView;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mIdTV;
        TextView mNameTV;
        ImageView mGroupIm;
        TextView mCity;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdTV = (TextView) mView.findViewById(R.id.id);
            mNameTV = (TextView) mView.findViewById(R.id.name);
            mGroupIm = (ImageView) mView.findViewById(R.id.group_image);
            mCity = (TextView) mView.findViewById(R.id.city);
        }
    }
}

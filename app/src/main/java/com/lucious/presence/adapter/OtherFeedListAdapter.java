package com.lucious.presence.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lucious.presence.BaseApplication;
import com.lucious.presence.BaseObjectListAdapter;
import com.lucious.presence.R;
import com.lucious.presence.entity.Feed;
import com.lucious.presence.entity.PeopleProfile;
import com.lucious.presence.view.EmoticonsTextView;
import com.lucious.presence.view.HandyTextView;

import java.util.List;

/**
 * Created by ztx on 2017/2/8.
 */

public class OtherFeedListAdapter extends BaseObjectListAdapter {
    private PeopleProfile mProfile;
    private int mPosition;

    public OtherFeedListAdapter(PeopleProfile profile,BaseApplication application, Context context, List<?> datas) {
        super(application, context, datas);
        mProfile = profile;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.listitem_feed,null);
            holder = new ViewHolder();
            holder.root = (RelativeLayout) convertView.findViewById(R.id.feed_item_layout_root);
            holder.avatar = (ImageView) convertView.findViewById(R.id.feed_item_iv_avatar);
            holder.time = (HandyTextView) convertView.findViewById(R.id.feed_item_htv_time);
            holder.name = (HandyTextView) convertView.findViewById(R.id.feed_item_htv_name);
            holder.content = (EmoticonsTextView) convertView.findViewById(R.id.feed_item_etv_content);
            holder.contentImage = (ImageView) convertView.findViewById(R.id.feed_item_iv_content);
            holder.more = (ImageButton) convertView.findViewById(R.id.feed_item_ib_more);
            holder.comment = (LinearLayout) convertView.findViewById(R.id.feed_item_layout_comment);
            holder.commentCount = (HandyTextView) convertView.findViewById(R.id.feed_item_htv_commentcount);
            holder.site = (HandyTextView) convertView.findViewById(R.id.feed_item_htv_site);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Feed feed = (Feed) getItem(position);
        holder.avatar.setImageBitmap(mApplication.getAvat);
        return super.getView(position, convertView, parent);
    }

    class ViewHolder {
        RelativeLayout root;
        ImageView avatar;
        HandyTextView time;
        HandyTextView name;
        EmoticonsTextView content;
        ImageView contentImage;
        ImageButton more;
        LinearLayout comment;
        HandyTextView commentCount;
        HandyTextView site;
    }













}

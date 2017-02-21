package com.lucious.presence.fragment.maintabs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lucious.presence.BaseApplication;
import com.lucious.presence.BaseFragment;
import com.lucious.presence.R;
import com.lucious.presence.adapter.OtherFeedListAdapter;
import com.lucious.presence.config.Config;
import com.lucious.presence.entity.Feed;
import com.lucious.presence.entity.PeopleProfile;
import com.lucious.presence.util.DatabaseUtils;
import com.lucious.presence.util.LogUtils;
import com.lucious.presence.util.NetworkUtils.NetworkUtils;
import com.lucious.presence.util.ToastUtils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztx on 2017/2/5.
 */

public class PlaygroundFragment extends BaseFragment {
    //API
    private static final String playgroundApi = Config.BASE_URL + "/u/api/playground";
    private static final String TABLE_NAME_PLAYGROUND = "PlaygroundFragment";
    private PullRefreshLayout layout;
    private ListView mListView;
    private OtherFeedListAdapter mAdapter;
    private PeopleProfile mPeople;

    private List<Feed> mFeeds;
    private ArrayList<String> list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.i("PlaygroundFragment生命周期","onCreateView");
        mView = inflater.inflate(R.layout.fragment_playground,container,false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initViews() {
        layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout_playground);
        mListView = (ListView) findViewById(R.id.fragment_playground_listview);
    }

    @Override
    protected void initEvents() {
//        layout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                layout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        layout.setRefreshing(false);
//                    }
//                },3000);
//                list = new ArrayList<String>();
//                for(int x = 0;x < 30;x++) {
//                    list.add("Item " + x);
//                }
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
//                mListView.setAdapter(adapter);
//                setListViewHeightBasedOnChildren(mListView);
//                layout.setRefreshing(false);
                httpsGetPlaygroundDatas();
            }
        });
    }

    private void httpsGetPlaygroundDatas() {
        resolvePlaygroundDatas(DatabaseUtils.getInstance(getActivity()).getJsonFromDatabase(TABLE_NAME_PLAYGROUND),mFeeds);
        AsyncHttpClient client = NetworkUtils.getAsyncHttpClientInstance();
        client.addHeader("Authorization","Bearer " + BaseApplication.token);
        RequestParams params = new RequestParams();
        params.put("rs",Config.RS);

        client.post(playgroundApi, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ToastUtils.showToast("Error");
                LogUtils.i("APITEST","主界面: " + "请求失败" + "--statusCode:" + statusCode + "--responseString:" + responseString);
                layout.setRefreshing(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                LogUtils.i("APITEST","主界面请求成功: " + responseString);
                DatabaseUtils.getInstance(getActivity()).saveJsonToDatabase(TABLE_NAME_PLAYGROUND,responseString);
                boolean result = resolvePlaygroundDatas(responseString,mFeeds);
                if(!result) {
                    ToastUtils.showToast("Error");
                    layout.setRefreshing(false);
                } else {
                    mAdapter = new OtherFeedListAdapter(mPeople,mApplication,getActivity(),mFeeds);
                    mListView.setAdapter(mAdapter);
                }
                layout.setRefreshing(false);
            }
        });
    }

    private boolean resolvePlaygroundDatas(String responseString,List<Feed> feeds) {
        if(responseString.equals("")) {
            return false;
        }
        try {
            JSONArray array = new JSONArray(responseString);
            Feed feed = null;
            for(int i = 0;i < array.length();i++) {
                JSONObject object = array.getJSONObject(i);
                String time = object.getString(Feed.TIME);
                String content = object.getString(Feed.CONTENT);
                String contentImage = null;
                if(object.has(Feed.CONTENT_IMAGE)) {
                    contentImage = object.getString(Feed.CONTENT_IMAGE);
                }
                String site = object.getString(Feed.SITE);
                int commentCount = object.getInt(Feed.COMMENT_COUNT);
                feeds.add(feed);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            feeds = null;
            return false;
        }
        return true;
    }

    private static void setListViewHeightBasedOnChildren(ListView mListView) {
        ListAdapter listAdapter = mListView.getAdapter();
        if(listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for(int i = 0;i < listAdapter.getCount();i++) {
            View listItem = listAdapter.getView(i,null,mListView);
            listItem.measure(0,0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = mListView.getLayoutParams();
        params.height = totalHeight + (mListView.getDividerHeight() * (listAdapter.getCount() - 1));
        mListView.setLayoutParams(params);
    }

    @Override
    protected void init() {
        httpsGetPlaygroundDatas();
    }
}

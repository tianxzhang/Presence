package com.lucious.presence.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lucious.presence.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by ztx on 2017/2/7.
 */

public class SlideShowView extends FrameLayout {
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private final static int IMAGE_COUNT = 5;
    private final static int TIME_INTERVAL = 5;
    private final static boolean isAutoPlay = false;

    private String[] imageUrls;
    private List<ImageView> imageViewList;
    private List<View> dotViewsList;

    private ViewPager viewPager;
    private int currentItem = 0;
    private ScheduledExecutorService scheduledExecutorService;

    private Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }
    };

    public SlideShowView(Context context) {
        this(context,null);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initImageLoader(context);
        initData();
        if(isAutoPlay) {
            startPlay();
        }
    }

    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(),1,4, TimeUnit.SECONDS);
    }

    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViewList.size();
                handler.obtainMessage().sendToTarget();
            }
        }
    }

    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    private void initData() {
        imageViewList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();
        new GetListTask().execute("");
    }

    class GetListTask extends AsyncTask<String,Integer,Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            imageUrls = new String[] {
                    "drawable://"+R.drawable.wodegeshengli,
                    "drawable://"+R.drawable.wodegeshengli,
                    "drawable://"+R.drawable.wodegeshengli,
                    "drawable://"+R.drawable.wodegeshengli
            };
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result) {
                initUI(context);
            }
        }
    }

    private void initUI(Context context) {
        if(imageUrls == null || imageUrls.length == 0)
            return;
        LayoutInflater.from(context).inflate(R.layout.layout_slideshow,this,true);
        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.fragment_playground_viewpager_dot);
        dotLayout.removeAllViews();

        for(int i = 0;i < imageUrls.length;i++) {
            ImageView view = new ImageView(context);
            view.setTag(imageUrls[i]);
            if(i == 0)
                view.setBackgroundResource(R.drawable.wodegeshengli);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewList.add(view);
            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;
            dotLayout.addView(dotView,params);
            dotViewsList.add(dotView);
        }
        viewPager = (ViewPager) findViewById(R.id.fragment_playground_viewpager);
        viewPager.setFocusable(true);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        boolean isAutoPlay = false;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            currentItem = position;
            for(int i = 0;i < dotViewsList.size();i++) {
                if(i == position) {
                    dotViewsList.get(position).setBackgroundResource(R.drawable.dot_focus);
                } else {
                    dotViewsList.get(i).setBackgroundResource(R.drawable.dot_blur);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case 1:
                    isAutoPlay = false;
                    break;
                case 2:
                    isAutoPlay = true;
                    break;
                case 0:
                    if(viewPager.getCurrentItem() == viewPager.getAdapter().getCount()-1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    } else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }
    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViewList.get(position);
            imageLoader.displayImage(imageView.getTag() + "",imageView);
            container.addView(imageViewList.get(position));
            return imageViewList.get(position);
        }

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }
    }

    private void destroyBitmaps() {
        for(int i = 0;i < IMAGE_COUNT;i++) {
            ImageView imageView = imageViewList.get(i);
            Drawable drawable = imageView.getDrawable();
            if(drawable != null) {
                drawable.setCallback(null);
            }
        }
    }
    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(
                Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }
}

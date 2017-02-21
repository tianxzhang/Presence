package com.lucious.presence.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lucious.presence.R;
import com.lucious.presence.util.LogUtils;

/**
 * Created by ztx on 2017/2/6.
 */

public class HeaderLayout extends RelativeLayout {
    private RelativeLayout mRlHeader;
    private RelativeLayout mRlOriginHeader;
    private ImageView mIvLeftIcon;
    private TextView mTvMiddleText;
    private ImageView mIvRightIcon;
    private TextView mTvRightText;
    private ProgressBar mPgLoading;

    private Drawable leftIcon;
    private String middleText;
    private Drawable rightIcon;
    private String rightText;

    private TypedArray typedArray;

    private HeaderMiddleClickListener headerMiddleClickListener = null;
    private HeaderButtonClickListener headerButtonClickListener = null;
    private Context mContext;

    public interface HeaderButtonClickListener {
        void onLeftButtonClick();
        void onRightButtonClick();
    }
    public interface HeaderMiddleClickListener {
        void onMiddleClick();
    }

    public void addHeaderButtonClickListener(HeaderButtonClickListener listener) {
        this.headerButtonClickListener = listener;
    }

    public void addHeaderMiddleClickListener(HeaderMiddleClickListener listener) {
        this.headerMiddleClickListener = listener;
    }

    public HeaderLayout(Context context) {
        super(context);
    }

    public HeaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRlHeader = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_self_header,this,true);
        typedArray = context.obtainStyledAttributes(attrs,R.styleable.HeaderLayout);
        mContext = context;
        initViews();
        initAttrs();
        initEvents();
    }

    private void initEvents() {
        OnClickListener onClickListenerMiddle = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(headerMiddleClickListener != null) {
                    headerMiddleClickListener.onMiddleClick();
                }
            }
        };
        OnClickListener onClickListenerLeft = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(headerButtonClickListener != null) {
                    headerButtonClickListener.onLeftButtonClick();
                }
            }
        };
        OnClickListener onClickListenerRight = new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i("HeaderLayoutLog","进入1");
                if(headerButtonClickListener != null) {
                    headerButtonClickListener.onRightButtonClick();
                    LogUtils.i("HeaderLayoutLog","进入2");
                }
            }
        };
        mIvLeftIcon.setOnClickListener(onClickListenerLeft);
        mIvRightIcon.setOnClickListener(onClickListenerRight);
        mTvRightText.setOnClickListener(onClickListenerRight);
        mRlOriginHeader.setOnClickListener(onClickListenerMiddle);
    }

    private void initAttrs() {
        int N = typedArray.getIndexCount();
        int attr;
        for(int i = 0;i < N;i++) {
            attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.HeaderLayout_left_ico:
                    leftIcon = typedArray.getDrawable(attr);
                    break;
                case R.styleable.HeaderLayout_title_text:
                    middleText = typedArray.getString(attr);
                    break;
                case R.styleable.HeaderLayout_right_ico:
                    rightIcon = typedArray.getDrawable(attr);
                    break;
                case R.styleable.HeaderLayout_right_text:
                    rightText = typedArray.getString(attr);
                    break;
            }
        }
        typedArray.recycle();
        if(leftIcon == null) {
            mIvLeftIcon.setVisibility(GONE);
        } else {
            mIvLeftIcon.setVisibility(VISIBLE);
            mIvLeftIcon.setImageDrawable(leftIcon);
        }
        if(rightIcon == null) {
            mIvRightIcon.setVisibility(GONE);
            mTvRightText.setText(rightText);
            mTvRightText.setVisibility(VISIBLE);
        } else {
            mIvRightIcon.setVisibility(VISIBLE);
            mTvRightText.setVisibility(GONE);
            mIvRightIcon.setImageDrawable(rightIcon);
        }
    }

    private void initViews() {
        mRlOriginHeader = (RelativeLayout) findViewById(R.id.origin_header);
        mIvLeftIcon = (ImageView) findViewById(R.id.iv_header_left);
        mTvMiddleText = (TextView) findViewById(R.id.tv_header_middle);
        mIvRightIcon = (ImageView) findViewById(R.id.iv_header_right);
        mTvRightText = (TextView) findViewById(R.id.tv_header_right);
        mPgLoading = (ProgressBar) findViewById(R.id.pb_loading);
    }

    public HeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRightText(String rightText) {
        rightText = rightText;
    }
    public void setmIvRightIcon(Drawable rightIcon) {
        rightIcon = rightIcon;
    }
    public void setMiddleText(String middleText) {
        mTvMiddleText.setText(middleText);
    }
    public void setLeftIcon(Drawable leftIcon) {
        leftIcon = leftIcon;
    }
    public Drawable getLeftIcon() {
        return leftIcon;
    }
    public String getMiddleText() {
        return middleText;
    }
    public Drawable getRightIcon() {
        return rightIcon;
    }
    public String getRightText() {
        return rightText;
    }

    public void setLeftVisibility(int visibility) {
        switch (visibility) {
            case VISIBLE:
                mIvLeftIcon.setVisibility(VISIBLE);
                break;
            case INVISIBLE:
                mIvLeftIcon.setVisibility(INVISIBLE);
                break;
            case GONE:
                mIvLeftIcon.setVisibility(GONE);
                break;
        }
    }
    public void setRightIconVisibility(int visibility) {
        switch (visibility) {
            case VISIBLE:
                mIvRightIcon.setVisibility(VISIBLE);
                break;
            case INVISIBLE:
                mIvRightIcon.setVisibility(INVISIBLE);
                break;
            case GONE:
                mIvRightIcon.setVisibility(GONE);
                break;
        }
    }
    public void setRightTextVisibility(int visibility) {
        switch (visibility) {
            case VISIBLE:
                mTvRightText.setVisibility(VISIBLE);
                break;
            case INVISIBLE:
                mTvRightText.setVisibility(INVISIBLE);
                break;
            case GONE:
                mTvRightText.setVisibility(GONE);
                break;
        }
    }
    public void setTitle(String title) {
        mTvMiddleText.setText(title);
    }
    public String getTitle() {
        return mTvMiddleText.getText().toString().trim();
    }
    public void showLoadingBar() {
        Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.set_animation_process_bar_in);
        mPgLoading.startAnimation(animation);
        mPgLoading.setVisibility(VISIBLE);
    }
    public void dismissLoadingBar() {
        Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.set_animation_process_bar_out);
        mPgLoading.startAnimation(animation);
        mPgLoading.setVisibility(INVISIBLE);
    }
}

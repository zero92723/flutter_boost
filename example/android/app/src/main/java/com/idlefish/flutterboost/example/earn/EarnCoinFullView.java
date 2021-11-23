package com.idlefish.flutterboost.example.earn;

import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.idlefish.flutterboost.example.R;

/**
 * @author zhaozhuang
 * @Date on 2021/10/8
 * @Desc: 积分外部View
 */
public class EarnCoinFullView extends FrameLayout {

    /**
     * 兜底图
     */
    private final String defaultImage
        = "https://gw.alicdn.com/imgextra/i4/O1CN01vnndNN1Lj14Wu6nJ2_!!6000000001334-2-tps-120-120.png";

    /**
     * 兜底Lottie
     */
    private final String defaultLottie
        = "https://gw.alipayobjects.com/os/finxbff/lolita/707cdab1-d679-49f9-99f0-6cbf8baabb84/lottie.json";

    /**
     * 兜底
     */
    private final ImageView mHongBaoImage;

    /**
     * 发金币动画
     */
    private final FrameLayout mFLayoutDefault;
    private final TextView mTvCoin;

    public EarnCoinFullView(Context context, Map<String, Object> extraParams) {
        super(context);

        ViewGroup viewRoot = (ViewGroup)LayoutInflater.from(context).inflate(R.layout.hd_earn_coin_layout, this);
        mHongBaoImage = viewRoot.findViewById(R.id.iv_hongbao);
        mFLayoutDefault = viewRoot.findViewById(R.id.fl_default);
        mTvCoin = viewRoot.findViewById(R.id.tv_coin);

        try {
            int earnCoinViewHeight = getDefaultEarnCoinSize();
            int earnCoinViewWidth = getDefaultEarnCoinSize();

            //父布局
            LayoutParams layoutParams = new LayoutParams(earnCoinViewWidth, earnCoinViewHeight);
            layoutParams.gravity = Gravity.CENTER;
            viewRoot.setLayoutParams(layoutParams);

            // 金币动画
            int coinAnimaWidth = earnCoinViewWidth;
            int coinAnimaHeight = 17;

            //兜底图
            int defaultPicSize = earnCoinViewWidth;

            //兜底图
            LayoutParams layoutParamsImage = new LayoutParams(defaultPicSize, defaultPicSize);
            layoutParamsImage.gravity = Gravity.CENTER;
            mFLayoutDefault.setLayoutParams(layoutParamsImage);

            // 获取金币View的位置 金币动画
            LayoutParams layoutParamsCoin = new LayoutParams(coinAnimaWidth, coinAnimaHeight);
            double rate = (double)earnCoinViewHeight / getDefaultEarnCoinSize();
            //layoutParamsCoin.topMargin = (int)(rate * defaultSize);
            layoutParamsCoin.gravity = Gravity.CENTER;
            layoutParamsCoin.gravity = Gravity.CENTER_HORIZONTAL;
            mTvCoin.setLayoutParams(layoutParamsCoin);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 打底图
        String defaultPic = defaultImage;
        //mHongBaoImage.setImageBitmap(defaultPic);
    }

    /**
     * 获取默认大小
     *
     * @return
     */
    public int getDefaultEarnCoinSize() {
        int earnCoinSize = 150;
        return earnCoinSize;
    }
}
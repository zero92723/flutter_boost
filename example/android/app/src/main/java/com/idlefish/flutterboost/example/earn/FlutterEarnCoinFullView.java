package com.idlefish.flutterboost.example.earn;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.idlefish.flutterboost.example.R;
import io.flutter.plugin.platform.PlatformView;

/**
 * @author Bill
 * @Date on 2021/11/10
 * @Desc: 桥接的flutter View
 */
public class FlutterEarnCoinFullView implements PlatformView {

    private Map<String, Object> creationParams;
    private Context context;
    private final TextView mTvText;
    ImageView imageView;

    public FlutterEarnCoinFullView(Context context, Map<String, Object> creationParams) {
        this.creationParams = creationParams;
        this.context = context;
        mTvText = new TextView(this.context);
        imageView = new ImageView(context);
    }

    @Override
    public View getView() {
        //String bizCode = (String)creationParams.get("bizCode");
        //String tag = (String)creationParams.get("tag");
        //Map<String, Object> extraParams = (Map<String, Object>)creationParams.get("extraParams");
        //EarnCoinFullView earnCoinFullView = EarnCoinManager.instance().getEarnCoinFullView(context, bizCode, tag,
        //    extraParams);
        //return earnCoinFullView;

        //FrameLayout frameLayout = new FrameLayout(context);
        //frameLayout.setLayoutParams(new LayoutParams(200, 200));

        //mTvText.setTextSize(14);
        //mTvText.setBackgroundColor(Color.rgb(100, 200, 255));
        //mTvText.setText("22222222");

        imageView.setLayoutParams(new LayoutParams(200, 200));
        imageView.setBackgroundColor(Color.GREEN);
        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.rarn_hongbao));

        EarnCoinFullView earnCoinFullView = new EarnCoinFullView(context, new HashMap<>());
        return earnCoinFullView;
    }

    @Override
    public void dispose() {
    }
}
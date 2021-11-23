package com.idlefish.flutterboost.example.earn;

import java.util.Map;

import android.content.Context;
import android.view.View;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

/**
 * @author zhaozhuang
 */
public class EarnFullViewFactory extends PlatformViewFactory {

    public EarnFullViewFactory() {
        super(StandardMessageCodec.INSTANCE);
    }

    @Override
    public PlatformView create(Context context, int id, Object args) {
        if (args == null || context == null) {
            return null;
        }

        final Map<String, Object> creationParams = (Map<String, Object>)args;
        if (creationParams == null) {
            return null;
        }

        FlutterEarnCoinFullView flutterEarnCoinFullView = new FlutterEarnCoinFullView(context, creationParams);
        return flutterEarnCoinFullView;
    }
}

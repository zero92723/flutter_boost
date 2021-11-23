package com.idlefish.flutterboost.example.earn;

import com.idlefish.flutterboost.example.earn.EarnFullViewFactory;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;

/**
 * LtaoEarncoinFlutterPlugin
 *
 * @author zhaozhuang
 */
public class LtaoEarncoinFlutterPlugin implements FlutterPlugin {

    public LtaoEarncoinFlutterPlugin() {}

    public static void registerWith(io.flutter.plugin.common.PluginRegistry.Registrar registrar) {
        registrar
            .platformViewRegistry()
            .registerViewFactory(
                "plugins.flutter.io/earnCoinView",
                new EarnFullViewFactory());
    }

    @Override
    public void onAttachedToEngine(FlutterPluginBinding binding) {
        BinaryMessenger messenger = binding.getBinaryMessenger();
        binding
            .getFlutterEngine()
            .getPlatformViewsController()
            .getRegistry()
            .registerViewFactory(
                "plugins.flutter.io/earnCoinView", new EarnFullViewFactory());
    }

    @Override
    public void onDetachedFromEngine(FlutterPluginBinding binding) {
    }

}

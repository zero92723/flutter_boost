import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:flutter/rendering.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/semantics.dart';

class EarnView extends StatefulWidget {
  Map earnMoneyData;

  EarnView(this.earnMoneyData);

  @override
  State<StatefulWidget> createState() {
    // TODO: implement createState
    return EarnState();
  }
}

class EarnState extends State<EarnView> {
  double opacity = 0.0;
  double realOpacity = 0.0;

  @override
  Widget build(BuildContext context) {
    const String viewType = 'plugins.flutter.io/earnCoinView';
    // Pass parameters to the platform side.
    final Map<String, dynamic> creationParams = <String, dynamic>{
      'msg': 'Hi there!'
    };
    return Platform.isIOS
        ? UiKitView(
            viewType: viewType,
            layoutDirection: TextDirection.ltr,
            creationParams: widget.earnMoneyData,
            creationParamsCodec: const StandardMessageCodec(),
          )
        : Stack(
            children: [
              // AndroidView(
              //   viewType: viewType,
              //   onPlatformViewCreated: (int id) {
              //     // invokeOpacity();
              //   },
              //   layoutDirection: TextDirection.rtl,
              //   creationParams: widget.earnMoneyData,
              //   creationParamsCodec: const StandardMessageCodec(),
              // ),
              PlatformViewLink(
                viewType: viewType,
                surfaceFactory:
                    (BuildContext context, PlatformViewController controller) {
                  return AndroidViewSurface(
                    controller: controller as AndroidViewController,
                    gestureRecognizers: const <
                        Factory<OneSequenceGestureRecognizer>>{},
                    hitTestBehavior: PlatformViewHitTestBehavior.opaque,
                  );
                },
                onCreatePlatformView: (PlatformViewCreationParams params) {
                  return PlatformViewsService.initSurfaceAndroidView(
                    id: params.id,
                    viewType: viewType,
                    layoutDirection: TextDirection.ltr,
                    creationParams: creationParams,
                    creationParamsCodec: StandardMessageCodec(),
                    onFocus: () {
                      params.onFocusChanged(true);
                    },
                  )
                    ..addOnPlatformViewCreatedListener(
                        params.onPlatformViewCreated)
                    ..create();
                },
              ),
            ],
          );
  }
}

package com.barnatwo.sharewithflutter;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class ShareWithFlutterPlugin implements MethodCallHandler {
  private static final String SHARE_ON_TWITTER_METHOD = "shareOnTwitter";
  private static final String SHARE_ON_FACEBOOK_METHOD = "shareOnFacebook";

  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "share_with_flutter");
    channel.setMethodCallHandler(new ShareWithFlutterPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    switch (call.method) {

      case SHARE_ON_TWITTER_METHOD: {
        shareOnTwitter(result);
        break;
      }
      case SHARE_ON_FACEBOOK_METHOD: {
        shareOnFacebook(result);
        break;
      }
      default:
        result.notImplemented();
        break;
    }
  }

  private void shareOnTwitter(Result result) {
    result.success(true);
  }

  private void shareOnFacebook(Result result) {
    result.success(true);
  }
}

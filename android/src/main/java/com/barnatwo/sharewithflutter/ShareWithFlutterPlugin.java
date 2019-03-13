package com.barnatwo.sharewithflutter;

import android.content.Intent;
import android.net.Uri;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.util.HashMap;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class ShareWithFlutterPlugin implements MethodCallHandler, PluginRegistry.ActivityResultListener {
    private static final String SHARE_ON_TWITTER_METHOD = "shareOnTwitter";
    private static final String SHARE_ON_FACEBOOK_METHOD = "shareOnFacebook";

    private final CallbackManager callbackManager = CallbackManager.Factory.create();

    Registrar registrar;
    Result pendingResult;

    private ShareWithFlutterPlugin(Registrar registrar) {
        this.registrar = registrar;
        this.registrar.addActivityResultListener(this);
    }

    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "share_with_flutter");
        channel.setMethodCallHandler(new ShareWithFlutterPlugin(registrar));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        switch (call.method) {
            case SHARE_ON_TWITTER_METHOD:
                shareOnTwitter(result);
                break;
            case SHARE_ON_FACEBOOK_METHOD:
                shareOnFacebook(
                    (String) call.argument("link"),
                    (String) call.argument("imagePath"),
                    result);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    private void shareOnTwitter(Result result) {
      result.success(true);
  }

    private void shareOnFacebook(String link, String imagePath, Result result) {
        if((imagePath == null) || (imagePath.isEmpty())) {
            shareLinkOnFacebook(link, result);
            return;
        }

        sharePictureOnFacebook(link, imagePath, result);
    }

    private void shareLinkOnFacebook(String link, Result result) {
        ShareContent content = new ShareLinkContent.Builder()
              .setContentUrl(Uri.parse(link))
              .build();
        shareContentOnFacebook(content);

        pendingResult = result;
    }

    private void sharePictureOnFacebook(String link, String imagePath, Result result) {
        SharePhotoContent.Builder contentBuilder = new SharePhotoContent.Builder();

        if((imagePath != null) && (!imagePath.isEmpty())) {
            Uri contentUri = Uri.fromFile(new File(imagePath));
            SharePhoto photo = new SharePhoto.Builder()
                  .setImageUrl(contentUri)
                  .build();
            contentBuilder.addPhoto(photo);
        }

        ShareContent content = contentBuilder.build();
        shareContentOnFacebook(content);

        pendingResult = result;
  }

    private void shareContentOnFacebook(ShareContent content) {
        ShareDialog dialog = new ShareDialog(registrar.activity());
        dialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                finishWithResult(
                    new HashMap<String, Object>() {{
                        put("status", "SUCCESS");
                    }}
                );
            }

            @Override
            public void onCancel() {
                finishWithResult(
                    new HashMap<String, Object>() {{
                        put("status", "CANCEL");
                    }}
                );
            }

            @Override
            public void onError(final FacebookException error) {
                finishWithResult(
                    new HashMap<String, Object>() {{
                        put("status", "ERROR");
                        put("errorMessage", error.getMessage());
                    }}
                );
            }
        });
        dialog.show(content);
    }

    private void finishWithResult(Object result) {
        if (pendingResult != null) {
            pendingResult.success(result);
            pendingResult = null;
        }
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}

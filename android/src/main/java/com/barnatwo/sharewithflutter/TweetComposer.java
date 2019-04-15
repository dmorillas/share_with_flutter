package com.barnatwo.sharewithflutter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class TweetComposer {
    @SuppressLint("StaticFieldLeak")
    static volatile TweetComposer instance;
    private static final String MIME_TYPE_PLAIN_TEXT = "text/plain";
    private static final String MIME_TYPE_JPEG = "image/jpeg";
    private static final String TWITTER_PACKAGE_NAME = "com.twitter.android";
    private static final String WEB_INTENT = "https://twitter.com/intent/tweet?text=%s&url=%s";

    public static TweetComposer getInstance() {
        if (instance == null) {
            synchronized (TweetComposer.class) {
                if (instance == null) {
                    instance = new TweetComposer();
                }
            }
        }
        return instance;
    }

    TweetComposer() {
    }

    public static class Builder {
        private final Context context;
        private String text;
        private URL url;
        private Uri imageUri;

        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context;
        }

        public Builder text(String text) {
            if (text == null) {
                throw new IllegalArgumentException("text must not be null.");
            }

            if (this.text != null) {
                throw new IllegalStateException("text already set.");
            }
            this.text = text;

            return this;
        }

        public Builder url(URL url) {
            if (url == null) {
                throw new IllegalArgumentException("url must not be null.");
            }

            if (this.url != null) {
                throw new IllegalStateException("url already set.");
            }
            this.url = url;

            return this;
        }

        public Builder image(Uri imageUri) {
            if (imageUri == null) {
                throw new IllegalArgumentException("imageUri must not be null.");
            }

            if (this.imageUri != null) {
                throw new IllegalStateException("imageUri already set.");
            }
            this.imageUri = imageUri;

            return this;
        }

        public Intent createIntent() {
            Intent intent = createTwitterIntent();

            if (intent == null) {
                intent = createWebIntent();
            }

            return intent;
        }

        private Intent createTwitterIntent() {
            final Intent intent = new Intent(Intent.ACTION_SEND);

            final StringBuilder builder = new StringBuilder();

            if (!TextUtils.isEmpty(text)) {
                builder.append(text);
            }

            if (url != null) {
                if (builder.length() > 0) {
                    builder.append(' ');
                }
                builder.append(url.toString());
            }

            intent.putExtra(Intent.EXTRA_TEXT, builder.toString());
            intent.setType(MIME_TYPE_PLAIN_TEXT);

            if (imageUri != null) {
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                intent.setType(MIME_TYPE_JPEG);
            }

            final PackageManager packManager = context.getPackageManager();
            final List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveInfo: resolvedInfoList){
                if (resolveInfo.activityInfo.packageName.startsWith(TWITTER_PACKAGE_NAME)){
                    intent.setClassName(resolveInfo.activityInfo.packageName,
                            resolveInfo.activityInfo.name);
                    return intent;
                }
            }

            return null;
        }

        private Intent createWebIntent() {
            final String url = (this.url == null ? "" : this.url.toString());

            final String tweetUrl =
                    String.format(WEB_INTENT, urlEncode(text), urlEncode(url));
            return new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl));
        }

        public void show() {
            final Intent intent = createIntent();
            context.startActivity(intent);
        }

        private static String urlEncode(String s) {
            if (s == null) {
                return "";
            }

            try {
                return URLEncoder.encode(s, "UTF8");
            } catch (UnsupportedEncodingException exception) {
                throw new RuntimeException(exception.getMessage(), exception);
            }
        }
    }
}

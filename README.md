# share_with_flutter

[Flutter](https://flutter.io) plugin to share content in Twitter and Facebook and get a callback when the action has taken place.

### Android integration

- Create your [Facebook App](https://developers.facebook.com/apps) and get your _Facebook App ID_. You can find it in your Facebook App's dashboard.  

- Open your **/android/app/src/main/res/values/strings.xml** file and add the following line:
```xml
    <string name="facebook_app_id">{FB_APP_ID}</string>
```
- Add the following permission to your **AndroidManifest.xml** file:
```xml
    <uses-permission android:name="android.permission.INTERNET"/>
```

- Add the following meta-data element to the application element:
```xml
    <meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/facebook_app_id"/>
```

- Finally, you'll also need to add the following provider to the application element in your **AndroidManifest.xml**. Remember to substitute _{APP_ID}_ by your actual AppId
```xml
<provider android:authorities="com.facebook.app.FacebookContentProvider{FB_APP_ID}"
          android:name="com.facebook.FacebookContentProvider"
          android:exported="true"/>
```

You can find an example of an AndroidManifest [here](https://github.com/dmorillas/share_with_flutter/blob/master/example/android/app/src/main/AndroidManifest.xml).

Done!

### iOS integration

Done!

### Usage

#### Add the following import to your code:

```
import 'package:share_with_flutter/share_with_flutter.dart';
```

#### Use the methods:
```
shareOnTwitter({String message = '', String link = '', String imagePath = ''})
```
Shares a tweet with the information passed. Returns when the share has been completed successfully with the result of it.
Accepts the following parameters:

| Parameter        | Description                |
|------------------|----------------------------|
| String message   | Text message               |
| String link      | The URL                    |
| String imagePath | The local path of the file |

```
shareOnFacebook({String link = '', String imagePath = ''})
```
shares a _link_ or an _image_ on Facebook. Returns when the share has been completed successfully with the result of it.
Accepts the following arguments:

| Parameter        | Description                |
|------------------|----------------------------|
| String link      | The URL                    |
| String imagePath | The local path of the file |

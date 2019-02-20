import 'dart:async';

import 'package:flutter/services.dart';

class ShareWithFlutter {
  static const MethodChannel _channel =
      const MethodChannel('share_with_flutter');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  Future<String> shareOnTwitter({String message = ''}) async {
    return await _channel.invokeMethod('shareOnTwitter', <String, dynamic>{
      'message': message,
    });
  }

  Future<String> shareOnFacebook({String message = ''}) async {
    return await _channel.invokeMethod('shareOnFacebook', <String, dynamic>{
      'message': message,
    });
  }
}

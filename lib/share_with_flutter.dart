import 'dart:async';

import 'package:flutter/services.dart';

class ShareWithFlutter {
  static const MethodChannel _channel =
      const MethodChannel('share_with_flutter');

  static Future<String> shareOnTwitter({String message = '', String link = '', String imagePath = ''}) async {
    return await _channel.invokeMethod('shareOnTwitter', <String, dynamic>{
      'message': message,
    });
  }

  static Future<void> shareOnFacebook(Function(String status, String message) onResult, {String link = '', String imagePath = ''}) async {
    if(link.isEmpty && imagePath.isEmpty) {
      print("shareOnFacebook: link and imagePath can not be both empty");
      return "ERROR";
    }

    dynamic result =  await _channel.invokeMethod('shareOnFacebook', <String, dynamic> {
      'link': link,
      'imagePath': imagePath,
    });

    processResult(result, onResult);
  }

  static void processResult(dynamic result, Function onResult) {
    String status = result["status"];
    String message = result["errorMessage"];
    switch(status) {
      case "SUCCESS":
        print("Post shared successfully");
        break;
      case "CANCEL":
        print("Shared cancelled");
        break;
      case "ERROR":
        message = result["errorMessage"];
        print("Some error ocurred: $message");
        break;
    }

    onResult(status, message);
  }
}

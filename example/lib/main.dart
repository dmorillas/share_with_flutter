import 'dart:io';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:share_with_flutter/share_with_flutter.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Container(
          alignment: AlignmentDirectional.center,
          padding: const EdgeInsets.all(50.0),
          child: Column(
            children: <Widget>[
              RaisedButton(
                onPressed: onShareLinkOnFacebook,
                child: Text(
                  "Share Link on Facebook",
                  style: TextStyle(
                      fontSize: 16.0,
                  ),
                ),
              ),
              RaisedButton(
                onPressed: onSharePictureOnFacebook,
                child: Text(
                  "Share Picture on Facebook",
                  style: TextStyle(
                    fontSize: 16.0,
                  ),
                ),
              ),
              RaisedButton(
                onPressed: onShareLinkOnFacebook,
                child: Text(
                  "Share on Twitter",
                  style: TextStyle(
                    fontSize: 16.0,
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void onShareLinkOnFacebook() async {
    await ShareWithFlutter.shareOnFacebook(
        (String status, String message) async {
          print("Result when sharing link on Facebook: $status");
        },
        link: "flutter.io");
  }

  void onSharePictureOnFacebook() async {
    String imagePath = await getTempImagePath();

    await ShareWithFlutter.shareOnFacebook(
        (String status, String message) async {
            print("Result when sharing image on Facebook: $status");
        },
        imagePath: imagePath);
  }

  Future<String> getTempImagePath() async {
    ByteData byteData = await DefaultAssetBundle.of(context).load("assets/flutter.png");

    final Uint8List list = byteData.buffer.asUint8List();
    final tempDir = Directory.systemTemp;
    final imagePath = '${tempDir.path}/temp.png';
    final file = await new File(imagePath).create();
    await file.writeAsBytes(list);

    return imagePath;
  }
}

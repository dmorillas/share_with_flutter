#import "ShareWithFlutterPlugin.h"
#import <FBSDKCoreKit/FBSDKCoreKit.h>
#import <FBSDKShareKit/FBSDKShareDialog.h>
#import <FBSDKShareKit/FBSDKShareLinkContent.h>
#import <FBSDKShareKit/FBSDKShareMediaContent.h>
#import <FBSDKShareKit/FBSDKSharePhoto.h>

@implementation ShareWithFlutterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"share_with_flutter"
            binaryMessenger:[registrar messenger]];
  ShareWithFlutterPlugin* instance = [[ShareWithFlutterPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    NSDictionary *arguments = [call arguments];
    
    if ([@"shareOnFacebook" isEqualToString:call.method]) {
        [self shareOnFacebookLink:arguments[@"link"] withImagePath:arguments[@"imagePath"] AndResult:result];
    } else if ([@"shareOnTwitter" isEqualToString:call.method]) {
        NSLog(@">>>>>>>>>> shareOnTwitter");
    } else {
        result(FlutterMethodNotImplemented);
    }
}

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {

  // You can skip this line if you have the latest version of the SDK installed
  [[FBSDKApplicationDelegate sharedInstance] application:application
    didFinishLaunchingWithOptions:launchOptions];
  // Add any custom logic here.
  return YES;
}

- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
            options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {

  BOOL handled = [[FBSDKApplicationDelegate sharedInstance] application:application
    openURL:url
    sourceApplication:options[UIApplicationOpenURLOptionsSourceApplicationKey]
    annotation:options[UIApplicationOpenURLOptionsAnnotationKey]
  ];
  // Add any custom logic here.
  return handled;
}

- (void)addX:(NSString*)x toY:(int)y {
}

- (void) shareOnFacebookLink:(NSString*)link withImagePath:(NSString*)imagePath AndResult:(FlutterResult)result {
    if (imagePath == nil || [imagePath length] == 0) {
        [self shareLinkOnFacebook:link AndResult:result];
        return;
    }
    
    [self sharePictureOnFacebook:imagePath AndResult:result];
}

- (void) shareLinkOnFacebook:(NSString*)link AndResult:(FlutterResult)result {
    FBSDKShareLinkContent *content = [[FBSDKShareLinkContent alloc] init];
    content.contentURL = [NSURL URLWithString:link];
    
    UIViewController *controller =[UIApplication sharedApplication].keyWindow.rootViewController;
    [FBSDKShareDialog showFromViewController:controller
                                 withContent:content
                                    delegate:self];
    
//    pendingResult = result;
}

- (void) sharePictureOnFacebook:(NSString*)imagePath AndResult:(FlutterResult)result {
    UIImage *image = [UIImage imageWithContentsOfFile:imagePath];
    FBSDKSharePhoto *photo = [FBSDKSharePhoto photoWithImage:image userGenerated:YES];
    FBSDKShareMediaContent *content = [FBSDKShareMediaContent new];
    content.media = @[photo];
    
    UIViewController *controller =[UIApplication sharedApplication].keyWindow.rootViewController;
    
    [FBSDKShareDialog showFromViewController:controller
                                 withContent:content
                                    delegate:self];
                              
//    pendingResult = result;
}

- (void)sharer:(id<FBSDKSharing>)sharer didCompleteWithResults:(NSDictionary *)results {
//    finishWithResult(
//                     new HashMap<String, Object>() {{
//        put("status", "SUCCESS");
//    }}
//                     );
}

- (void)sharer:(id<FBSDKSharing>)sharer didFailWithError:(NSError *)error {
//    finishWithResult(
//                     new HashMap<String, Object>() {{
//        put("status", "ERROR");
//        put("errorMessage", error.getMessage());
//    }}
//                     );
}

- (void)sharerDidCancel:(id<FBSDKSharing>)sharer {
//    finishWithResult(
//                     new HashMap<String, Object>() {{
//        put("status", "CANCEL");
//    }}
//                     );
}

//private void finishWithResult(Object result) {
//    if (pendingResult != null) {
//        pendingResult.success(result);
//        pendingResult = null;
//    }
//}

@end

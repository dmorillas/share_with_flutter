#import "ShareWithFlutterPlugin.h"
#import <share_with_flutter/share_with_flutter-Swift.h>

@implementation ShareWithFlutterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftShareWithFlutterPlugin registerWithRegistrar:registrar];
}
@end

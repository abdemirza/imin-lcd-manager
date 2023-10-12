
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNLcdManagerSpec.h"

@interface LcdManager : NSObject <NativeLcdManagerSpec>
#else
#import <React/RCTBridgeModule.h>

@interface LcdManager : NSObject <RCTBridgeModule>
#endif

@end

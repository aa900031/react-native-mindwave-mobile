#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#import "RCTEventEmitter.h"
#else
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#endif

#import "MWMDevice.h"
#import "MWMDelegate.h"

@interface RNMindWaveMobile : RCTEventEmitter <MWMDelegate>
{
    MWMDevice *mwDevice;
}
@end

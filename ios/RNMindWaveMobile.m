#import "RNMindWaveMobile.h"
#import <React/RCTConvert.h>

@implementation RNMindWaveMobile
{
    RCTResponseSenderBlock _connectDeviceCallback;
    RCTResponseSenderBlock _disconnectDeviceCallback;
}

RCT_EXPORT_MODULE();

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

- (NSArray<NSString *> *)supportedEvents
{
    return @[@"deviceFound", @"eegPowerLowBeta", @"eegPowerDelta", @"eSense", @"eegBlink", @"mwmBaudRate", @"didConnect", @"didDisconnect"];
}

RCT_EXPORT_METHOD(instance)
{
    mwDevice = [MWMDevice sharedInstance];
    [mwDevice setDelegate:self];
    [mwDevice enableConsoleLog:YES];
    NSLog(@"call instance");
}

RCT_EXPORT_METHOD(scan)
{
    NSLog(@"call scan");
    [mwDevice scanDevice];
}

RCT_EXPORT_METHOD(connect:(NSString *)deviceID)
{
    NSLog(@"call connect");
    [mwDevice connectDevice:deviceID];
}

RCT_EXPORT_METHOD(disconnect)
{
    NSLog(@"call disconnect");
    [mwDevice disconnectDevice];
}

#pragma mark MWMDeviceDelegate

-(void)deviceFound:(NSString *)devName MfgID:(NSString *)mfgID DeviceID:(NSString *)deviceID
{
    NSLog(@"deviceFound");
    if ([mfgID isEqualToString:@""] || nil == mfgID || NULL == mfgID) {
        return;
    }
    NSDictionary *device = @{@"id": mfgID, @"name": devName, @"mfgId": deviceID};
    
    [self sendEventWithName:@"deviceFound" body:device];
}

-(void)didConnect
{
    NSLog(@"%s", __func__);
    [[MWMDevice sharedInstance] enableLoggingWithOptions:LoggingOptions_Processed | LoggingOptions_Raw];
    
    NSDictionary *data = @{@"success": @(TRUE)};

    [self sendEventWithName:@"didConnect" body:data];
}

-(void)didDisconnect
{
    NSLog(@"%s", __func__);
    
    NSDictionary *data = @{@"success": @(TRUE)};
    
    [self sendEventWithName:@"didDisconnect" body:data];
}

-(void)eegPowerLowBeta:(int)lowBeta HighBeta:(int)highBeta LowGamma:(int)lowGamma MidGamma:(int)midGamma
{
    NSLog(@"%s >>>>>>>-----eegPower: lowBeta:%d highBeta:%d lowGamma:%d midGamma:%d", __func__, lowBeta, highBeta, lowGamma, midGamma);
    NSDictionary *data = @{
                           @"lowBeta": @(lowBeta),
                           @"highBeta": @(highBeta),
                           @"lowGamma": @(lowGamma),
                           @"midGamma": @(midGamma),
                           };
    [self sendEventWithName:@"eegPowerLowBeta" body:data];
}

-(void)eegPowerDelta:(int)delta Theta:(int)theta LowAlpha:(int)lowAplpha HighAlpha:(int)highAlpha
{
    NSLog(@"%s >>>>>>>-----eegPower: delta:%d theta:%d lowAplpha:%d hightAlpha:%d", __func__, delta, theta, lowAplpha, highAlpha);
    NSDictionary *data = @{
                           @"delta": @(delta),
                           @"theta": @(theta),
                           @"lowAplpha": @(lowAplpha),
                           @"highAlpha": @(highAlpha),
                           };
    [self sendEventWithName:@"eegPowerDelta" body:data];
}

-(void)eSense:(int)poorSignal Attention:(int)attention Meditation:(int)meditation
{
    NSLog(@"%s >>>>>>>-----eSense:%d Attention:%d Meditation:%d", __func__, poorSignal, attention, meditation);
    NSDictionary *data = @{
                           @"poorSignal": @(poorSignal),
                           @"attention": @(attention),
                           @"meditation": @(meditation),
                           };
    [self sendEventWithName:@"eSense" body:data];
}

-(void)eegBlink:(int)blinkValue
{
    NSLog(@"%s >>>>>>>-----eegBlink: blinkValue:%d ", __func__, blinkValue);
    NSDictionary *data = @{@"blinkValue": @(blinkValue)};
    [self sendEventWithName:@"eegBlink" body:data];
}

-(void)mwmBaudRate:(int)baudRate NotchFilter:(int)notchFilter
{
    NSLog(@"%s >>>>>>>-----mwmBaudRate:%d NotchFilter:%d ", __func__, baudRate, notchFilter);
    NSDictionary *data = @{
                           @"baudRate": @(baudRate),
                           @"notchFilter": @(notchFilter)
                           };
    [self sendEventWithName:@"mwmBaudRate" body:data];
}
@end
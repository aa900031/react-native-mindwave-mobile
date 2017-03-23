
# react-native-mindwave-mobile

## Getting started

`$ npm install react-native-mindwave-mobile --save`

### Mostly automatic installation

`$ react-native link react-native-mindwave-mobile`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-mindwave-mobile` and add `RNMindWaveMobile.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNMindWaveMobile.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android  \*comming soon

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNMindWaveMobilePackage;` to the imports at the top of the file
  - Add `new RNMindWaveMobilePackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-mindwave-mobile'
  	project(':react-native-mindwave-mobile').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-mindwave-mobile/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-mindwave-mobile')
  	```

## Usage
```javascript
import MindWaveMobile from 'react-native-mindwave-mobile';

const mwm = new MindWaveMobile()

mwm.scan()
mwm.onFoundDevice(device => {
  console.log(device)
})
```

## Method
### `scan()`

### `connect(deviceId) :Promise`

### `disconnect() :Promise`

### `removeAllListeners()`

## Event

### `onFoundDevice(callback)`
- callback `function(device)`
```
  device = {
    id, name, mfgId
  }
```
### `onEEGPowerLowBeta(callback)`
- callback `function(data)`
```
  data = {
    lowBeta, highBeta, lowGamma, midGamma
  }
```
### `onEEGPowerDelta(callback)`
- callback `function(data)`
```
  data = {
    delta, theta, lowAplpha, highAlpha
  }
```
### `onESense(callback)`
- callback `function(data)`
```
  data = {
    poorSignal, attention, meditation
  }
```
### `onEEGBlink(callback)`
- callback `function(data)`
```
  data = {
    blinkValue
  }
```
### `onMWMBaudRate(callback)`
- callback `function(data)`
```
  data = {
    baudRate, notchFilter
  }
```
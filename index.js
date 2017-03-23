
import { NativeModules, NativeEventEmitter } from 'react-native';

const { RNMindWaveMobile } = NativeModules;
const RNMindWaveMobileEventEmitter = new NativeEventEmitter(RNMindWaveMobile);

export default class {

  constructor() {
    RNMindWaveMobile.inital()
  }

  scan() {
    RNMindWaveMobile.scan()
  }

  connect(deviceId) {
    return new Promise((resolve, reject) => {
      RNMindWaveMobile.connect(deviceId, err => {
        if (err) reject()
        resolve()
      })
    })
  }

  disconnect() {
    return new Promise((resolve, reject) => {
      RNMindWaveMobile.disconnect(err => {
        if (err) reject()
        resolve()
      })
    });
  }

  onFoundDevice(callback) {
    RNMindWaveMobileEventEmitter.addListener('deviceFound', callback)
  }

  onEEGPowerLowBeta(callback) {
    RNMindWaveMobileEventEmitter.addListener('eegPowerLowBeta', callback)
  }

  onEEGPowerDelta(callback) {
    RNMindWaveMobileEventEmitter.addListener('eegPowerDelta', callback)
  }

  onESense(callback) {
    RNMindWaveMobileEventEmitter.addListener('eSense', callback)
  }

  onEEGBlink(callback) {
    RNMindWaveMobileEventEmitter.addListener('eegBlink', callback)
  }

  onMWMBaudRate(callback) {
    RNMindWaveMobileEventEmitter.addListener('mwmBaudRate', callback)
  }

  removeAllListeners() {
    RNMindWaveMobileEventEmitter.removeAllListeners()
  }

};


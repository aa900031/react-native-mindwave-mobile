
import { NativeModules, NativeEventEmitter } from 'react-native';

const { RNMindWaveMobile } = NativeModules;
const RNMindWaveMobileEventEmitter = new NativeEventEmitter(RNMindWaveMobile);

export default class {

  constructor() {
    RNMindWaveMobile.instance()
  }

  scan() {
    RNMindWaveMobile.scan()
  }

  connect(deviceId) {
    RNMindWaveMobile.connect(deviceId)
  }

  disconnect() {
    RNMindWaveMobile.disconnect()
  }

  onConnect(callback) {
    RNMindWaveMobileEventEmitter.addListener('didConnect', callback)
  }

  onDisconnect(callback) {
    RNMindWaveMobileEventEmitter.addListener('didDisconnect', callback)
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


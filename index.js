
import { NativeModules, NativeEventEmitter } from 'react-native';

const { RNMindWaveMobile } = NativeModules;
const RNMindWaveMobileEventEmitter = new NativeEventEmitter(RNMindWaveMobile);

export default class {
  events = {
    didConnect: 'didConnect',
    didDisconnect: 'didDisconnect',
    deviceFound: 'deviceFound',
    eegPowerLowBeta: 'eegPowerLowBeta',
    eegPowerDelta: 'eegPowerDelta',
    eSense: 'eSense',
    eegBlink: 'eegBlink',
    mwmBaudRate: 'mwmBaudRate',
  }

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
    RNMindWaveMobileEventEmitter.addListener(this.events.didConnect, callback)
  }

  onDisconnect(callback) {
    RNMindWaveMobileEventEmitter.addListener(this.events.didDisconnect, callback)
  }

  onFoundDevice(callback) {
    RNMindWaveMobileEventEmitter.addListener(this.events.deviceFound, callback)
  }

  onEEGPowerLowBeta(callback) {
    RNMindWaveMobileEventEmitter.addListener(this.events.eegPowerLowBeta, callback)
  }

  onEEGPowerDelta(callback) {
    RNMindWaveMobileEventEmitter.addListener(this.events.eegPowerDelta, callback)
  }

  onESense(callback) {
    RNMindWaveMobileEventEmitter.addListener(this.events.eSense, callback)
  }

  onEEGBlink(callback) {
    RNMindWaveMobileEventEmitter.addListener(this.events.eegBlink, callback)
  }

  onMWMBaudRate(callback) {
    RNMindWaveMobileEventEmitter.addListener(this.events.mwmBaudRate, callback)
  }

  removeAllListeners() {
    Object.keys(this.events).map(key => {
      const event = this.events[key]
      RNMindWaveMobileEventEmitter.removeAllListeners(event)
    })
  }

};


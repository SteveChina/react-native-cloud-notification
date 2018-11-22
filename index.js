
import { NativeModules, DeviceEventEmitter } from 'react-native';

const { RNCloudNotification } = NativeModules;

RNCloudNotification.on = (eventName, callback) => {
    DeviceEventEmitter.addListener(eventName, callback);
};

export default RNCloudNotification;

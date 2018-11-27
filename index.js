
import { NativeModules, DeviceEventEmitter } from 'react-native';

const { RNCloudNotification } = NativeModules;

RNCloudNotification.on = (eventName, callback) => {
    DeviceEventEmitter.addListener(eventName, callback);
};

export const EVENT_CHANGE_ROUTE = 'FCMChangeRoute';
export const EVENT_FCM_NOTIFICATION = 'FCMIncomingMessage';
export const EVENT_FCM_TOKEN_UPDATE = 'FCMTokenUpdate';

export const PRIORITY_URGENT = 'urgent';
export const PRIORITY_HIGH = 'high';
export const PRIORITY_MEDIUM = 'default';

export default RNCloudNotification;

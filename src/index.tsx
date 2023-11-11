import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'imin-lcd-manager' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const LcdManager = NativeModules.LcdManager
  ? NativeModules.LcdManager
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function multiply(a: number, b: number): Promise<number> {
  return LcdManager.multiply(a, b);
}

export function sendLCDCommand(flag: number): Promise<any> {
  return LcdManager.sendLCDCommand(flag);
}

export function sendLCDString(text: string): Promise<any> {
  return LcdManager.sendLCDString(text);
}

export function sendLCDMultiString(
  text: string[],
  align: number[]
): Promise<any> {
  return LcdManager.sendLCDMultiString(text, align);
}

export function sendLCDDoubleString(
  topText: string,
  bottomText: string
): Promise<any> {
  return LcdManager.sendLCDDoubleString(topText, bottomText);
}

export function sendLCDBitmapFromFile(filePath: string): Promise<any> {
  return LcdManager.sendLCDBitmapFromFile(filePath);
}
export function base64ToBitmap(base64String: string): Promise<any> {
  return LcdManager.base64ToBitmap(base64String);
}
export function sendLCDBitmapFromURL(url: string): Promise<any> {
  return LcdManager.sendLCDBitmapFromURL(url);
}

export function generateQRCodeBitmap(qrCode: string): Promise<any> {
  return LcdManager.generateQRCodeBitmap(qrCode);
}

export function setTextSize(size: number): Promise<any> {
  return LcdManager.setTextSize(size);
}

export function getTextBitmap(text: string): Promise<any> {
  return LcdManager.getTextBitmap(text);
}

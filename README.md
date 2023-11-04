# LcdManagerModule

![LcdManagerModule Logo](link-to-logo)

LcdManagerModule is a powerful React Native module for Android that provides a variety of methods to interact with an LCD manager.

## 📦 Installation

To install the package, use npm:

```bash
npm install @iminlcdmanager/lcdmanager
```

## 📚 Usage

First, import the module in your React Native file:

```javascript
import { LcdManager } from '@iminlcdmanager/lcdmanager';
```

Then, you can use the methods provided by the module:

```javascript
// Send an LCD command
LcdManager.sendLCDCommand(flag);

// Send a string to the LCD
LcdManager.sendLCDString(string);

// Send multiple strings to the LCD
LcdManager.sendLCDMultiString(text, align);

// Send a double string to the LCD
LcdManager.sendLCDDoubleString(topText, bottomText);

// Send a bitmap from a file to the LCD
LcdManager.sendLCDBitmapFromFile(filePath);

// Send a bitmap from a URL to the LCD
LcdManager.sendLCDBitmapFromURL(url);

// Set the text size
LcdManager.setTextSize(size);
```

## 📝 Methods

Here are the methods provided by the LcdManagerModule:

```javascript
sendLCDCommand(flag): Sends an LCD command.
sendLCDString(string): Sends a string to the LCD.
sendLCDMultiString(text, align): Sends multiple strings to the LCD.
sendLCDDoubleString(topText, bottomText): Sends a double string to the LCD.
sendLCDBitmapFromFile(filePath): Sends a bitmap from a file to the LCD.
sendLCDBitmapFromURL(url): Sends a bitmap from a URL to the LCD.
setTextSize(size): Sets the text size.
```

## 📄 License

This project is licensed under the MIT License.

## 💡 Contribution

We're open to contributions!

---

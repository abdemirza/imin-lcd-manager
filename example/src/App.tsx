import * as React from 'react';

import {
  StyleSheet,
  View,
  Text,
  NativeModules,
  TouchableOpacity,
  ScrollView,
} from 'react-native';
import {
  multiply,
  sendLCDCommand,
  sendLCDDoubleString,
  sendLCDString,
  setTextSize,
} from 'imin-lcd-manager';

export default function App() {
  const [result, setResult] = React.useState<number | undefined>();
  const INIT = 1,
    WAKE = 2,
    HIBERNATE = 3,
    CLEAR = 4;
  React.useEffect(() => {
    multiply(3, 7).then(setResult);
  }, []);
  console.log();
  const delay = (ms: number) =>
    new Promise((resolve) => setTimeout(resolve, ms));

  const onPress = async () => {
    await delay(1000);
    sendLCDCommand(HIBERNATE);
    await delay(1000);
    sendLCDCommand(WAKE);
    await delay(1000);
    sendLCDCommand(INIT);
    await delay(1000);
    sendLCDCommand(CLEAR);
    await delay(2000);
    setTextSize(48);
    await delay(3000);
    sendLCDString('hello from LCD string');
    await delay(4000);
    sendLCDDoubleString('hello', 'world');
  };
  const sendCommand = (command: number) => {
    sendLCDCommand(command);
  };
  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
      <ScrollView>
        <TouchableOpacity onPress={onPress}>
          <View style={styles.box}>
            <Text>getTextBitmapasd</Text>
          </View>
        </TouchableOpacity>
        <TouchableOpacity
          onPress={() => {
            setTextSize(48);
          }}
        >
          <View style={styles.box}>
            <Text>sendLCDString</Text>
          </View>
        </TouchableOpacity>
        <TouchableOpacity
          onPress={() =>
            NativeModules?.LcdManager?.sendLCDMultiString(
              ['hello', 'world'],
              [0, 1]
            )
          }
        >
          <View style={styles.box}>
            <Text>sendLCDMultiString</Text>
          </View>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => sendCommand(1)}>
          <View style={styles.box}>
            <Text>1</Text>
          </View>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => sendCommand(2)}>
          <View style={styles.box}>
            <Text>2</Text>
          </View>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => sendCommand(3)}>
          <View style={styles.box}>
            <Text>3</Text>
          </View>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => sendCommand(4)}>
          <View style={styles.box}>
            <Text>4</Text>
          </View>
        </TouchableOpacity>
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 100,
    height: 100,
    backgroundColor: 'red',
    justifyContent: 'center',
    alignItems: 'center',
  },
});

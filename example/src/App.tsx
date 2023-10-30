import * as React from 'react';
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  TextInput,
  ScrollView,
} from 'react-native';
import {
  sendLCDCommand,
  sendLCDDoubleString,
  sendLCDString,
} from 'imin-lcd-manager';

export default function App() {
  const [lcdText, setLcdText] = React.useState('');
  const [lcdSecondText, setLcdSecondText] = React.useState('');

  React.useEffect(() => {
    // Replace this with your actual initialization code
    // multiply(3, 7).then(setResult);
  }, []);

  const sendFlag = (flagValue: number) => {
    if (!isNaN(flagValue)) {
      sendLCDCommand(parseInt(flagValue));
    }
  };

  const sendLCDText = () => {
    sendLCDString(lcdText);
  };

  const sendDoubleString = () => {
    sendLCDDoubleString(lcdText, lcdSecondText);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.title}>LCD Manager App</Text>
      <TextInput
        style={styles.input}
        placeholder="Enter LCD String"
        value={lcdText}
        onChangeText={(text) => setLcdText(text)}
      />
      <TextInput
        style={styles.input}
        placeholder="Enter LCD String(for Double String)"
        value={lcdSecondText}
        onChangeText={(text) => setLcdSecondText(text)}
      />
      <ScrollView contentContainerStyle={styles.buttonContainer}>
        <TouchableOpacity onPress={() => sendFlag(lcdText)}>
          <View style={styles.button}>
            <Text style={styles.buttonText}>Flag</Text>
          </View>
        </TouchableOpacity>
        <TouchableOpacity onPress={sendLCDText}>
          <View style={styles.button}>
            <Text style={styles.buttonText}>Send LCD String</Text>
          </View>
        </TouchableOpacity>
        <TouchableOpacity onPress={sendDoubleString}>
          <View style={styles.button}>
            <Text style={styles.buttonText}>Send Double String</Text>
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
    backgroundColor: '#f2f2f2',
  },
  title: {
    fontSize: 24,
    marginBottom: 20,
    fontWeight: 'bold',
  },
  input: {
    width: '80%',
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    marginBottom: 20,
    paddingLeft: 10,
    backgroundColor: 'white',
    borderRadius: 10,
  },
  buttonContainer: {
    width: '100%',
    alignItems: 'center',
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingHorizontal: '10%',
  },
  button: {
    width: 300,
    height: 60,
    backgroundColor: 'blue',
    borderRadius: 30,
    justifyContent: 'center',
    alignItems: 'center',
    marginVertical: 10,
  },
  buttonText: {
    color: 'white',
    fontSize: 18,
    fontWeight: 'bold',
  },
});

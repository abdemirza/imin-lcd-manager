import * as React from 'react';
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  TextInput,
  ScrollView,
  Dimensions,
  type Permission,
} from 'react-native';
import {
  sendLCDBitmapFromFile,
  sendLCDBitmapFromURL,
  sendLCDCommand,
  sendLCDDoubleString,
  sendLCDString,
} from 'imin-lcd-manager';
import { PermissionsAndroid } from 'react-native';
import {
  launchImageLibrary,
  type ImagePickerResponse,
} from 'react-native-image-picker';

export default function App() {
  const [lcdText, setLcdText] = React.useState('');
  const [lcdSecondText, setLcdSecondText] = React.useState('');

  const INIT = 1;
  const WAKE = 2;
  const HIBERNATE = 3;
  const CLEAR = 4;

  const selectImageFromFile = () => {
    launchImageLibrary(
      {
        mediaType: 'photo',
      },
      (response: ImagePickerResponse) => {
        if (response.didCancel) {
          console.log('User cancelled image picker');
        } else if (response.errorMessage) {
          console.log('ImagePicker Error: ', response.errorMessage);
        } else if (
          response.assets?.length &&
          response.assets[0]?.originalPath
        ) {
          const source = { uri: response.assets[0]?.originalPath };
          sendLCDBitmapFromFile(source.uri);
        }
      }
    );
  };

  const selectImageFromURL = () => {
    try {
      sendLCDBitmapFromURL(lcdText);
    } catch (error) {
      console.error('Invalid URL');
    }
  };

  const sendFlag = (flagValue: number) => {
    if (!isNaN(flagValue)) {
      sendLCDCommand(flagValue);
    }
  };

  const sendLCDText = () => {
    sendLCDString(lcdText);
  };

  const sendDoubleString = () => {
    sendLCDDoubleString(lcdText, lcdSecondText);
  };

  React.useEffect(() => {
    const PERMISSIONS = [
      PermissionsAndroid.PERMISSIONS.CAMERA,
      PermissionsAndroid.PERMISSIONS.READ_CONTACTS,
      PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
    ];
    const checkAllPermissions = async () => {
      try {
        const permissionsToRequest: Permission[] = PERMISSIONS.filter(
          (permission): permission is Permission => permission !== undefined
        );
        const result = await PermissionsAndroid.requestMultiple(
          permissionsToRequest
        );

        if (
          permissionsToRequest.every(
            (permission: Permission) => result[permission] === 'granted'
          )
        ) {
          console.log('All permissions granted!');
        } else {
          console.log('Permissions denied!');
        }
      } catch (error) {
        console.log(error);
      }
    };
    checkAllPermissions();
  }, []);

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
        placeholder="Enter LCD String (for Double String)"
        value={lcdSecondText}
        onChangeText={(text) => setLcdSecondText(text)}
      />
      <ScrollView contentContainerStyle={styles.buttonContainer}>
        {[
          { text: 'Send Lcd String', onPress: sendLCDText },
          { text: 'Send Double String', onPress: sendDoubleString },
          { text: 'Select Local Image', onPress: selectImageFromFile },
          { text: 'Select URL Image', onPress: selectImageFromURL },
          { text: 'WAKE SCREEN', onPress: () => sendFlag(WAKE) },
          { text: 'INIT SCREEN', onPress: () => sendFlag(INIT) },
          { text: 'HIBERNATE', onPress: () => sendFlag(HIBERNATE) },
          { text: 'CLEAR SCREEN', onPress: () => sendFlag(CLEAR) },
        ].map((button, index) => (
          <TouchableOpacity key={index} onPress={button.onPress}>
            <View
              style={[
                styles.button,
                button.text === 'CLEAR SCREEN' && {
                  width: Dimensions.get('window').width * 0.8,
                },
              ]}
            >
              <Text style={styles.buttonText}>{button.text}</Text>
            </View>
          </TouchableOpacity>
        ))}
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
    flexWrap: 'wrap-reverse',
    justifyContent: 'space-between',
    paddingHorizontal: '10%',
  },
  button: {
    width: 300,
    height: 60,
    backgroundColor: '#007BFF',
    borderRadius: 10,
    justifyContent: 'center',
    alignItems: 'center',
    marginVertical: 10,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 5,
    },
    shadowOpacity: 0.29,
    shadowRadius: 4.65,
    elevation: 7,
  },
  buttonText: {
    color: 'white',
    fontSize: 18,
    fontWeight: 'bold',
  },
});

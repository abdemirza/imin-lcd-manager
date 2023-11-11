package com.iminlcdmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout.Alignment;
import android.text.TextPaint;
import android.util.Base64;
import android.util.Log;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.htt.image.FreeImageUtil;
import com.imin.image.ILcdManager;
import com.imin.image.ThreadUtils.SimpleTask;
import java.io.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ReactModule(name = LcdManagerModule.NAME)
public class LcdManagerModule extends ReactContextBaseJavaModule {

  public static final String NAME = "LcdManager";

  // Context context;
  // static ILcdManager iLcdManager;
  // TextPaint textPaint;
  // private Typeface textTypeface;
  // private boolean haveBold;
  // private int textSize;
  // private boolean haveUnderline;
  // private static int textWidth = 240;
  // private int fontSizeStyle;
  // private Alignment textAlignment;
  // private float textLineSpacing;
  // private List<Alignment> mAlignments;
  // private final byte[] bmp;
  // int sizeForFrame;
  // long startTime;
  // long endTime;

  public LcdManagerModule(ReactApplicationContext reactContext) {
    super(reactContext);
    //   this.textTypeface = Typeface.DEFAULT;
    //   this.textSize = 55;
    //   this.fontSizeStyle = 0;
    //   this.textAlignment = Alignment.ALIGN_CENTER;
    //   this.textLineSpacing = 1.0F;
    //   this.mAlignments = new ArrayList();
    //   this.bmp = new byte[] { 66, 77, 56, 88, 2 }; // Initialize bmp here
    //   this.sizeForFrame = 153600;
    //   this.startTime = 0L;
    //   this.endTime = 0L;
    //   this.context = context;
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  public Bitmap urlToBitmap(String urlString)
    throws MalformedURLException, IOException {
    try {
      URL url = new URL(urlString);
      InputStream inputStream = url.openStream();
      Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
      if (bitmap == null) {
        throw new IOException("Failed to decode image from the provided URL.");
      }
      return bitmap;
    } catch (MalformedURLException e) {
      // Handle MalformedURLException
      throw e; // or rethrow it if necessary
    } catch (IOException e) {
      // Handle IOException
      throw e; // or rethrow it if necessary
    }
  }

  private Bitmap generateQRCodeBitmap(String qrCodeData)
    throws WriterException {
    // You may need to adjust the size and other properties based on your requirements
    int qrCodeSize = 300;
    BitMatrix bitMatrix = new MultiFormatWriter()
      .encode(qrCodeData, BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, null);

    int width = bitMatrix.getWidth();
    int height = bitMatrix.getHeight();
    int[] pixels = new int[width * height];

    // Generate the QR Code bitmap
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        pixels[y * width + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
      }
    }

    Bitmap qrCodeBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
    qrCodeBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

    // Rotate the QR Code bitmap if needed
    Matrix matrix = new Matrix();
    matrix.postRotate(90);
    return Bitmap.createBitmap(
      qrCodeBitmap,
      0,
      0,
      qrCodeBitmap.getWidth(),
      qrCodeBitmap.getHeight(),
      matrix,
      true
    );
  }

  @ReactMethod
  public void generateQRCodeBitmap(String qrCodeData, Promise promise) {
    ILcdManager lcdManager = ILcdManager.getInstance(
      getReactApplicationContext()
    );

    try {
      // Create a QR Code bitmap from the provided data
      Bitmap qrCodeBitmap = generateQRCodeBitmap(qrCodeData);

      // Send the QR Code bitmap to the lcdManager
      lcdManager.sendLCDBitmap(qrCodeBitmap);

      // Resolve the promise with success
      promise.resolve("QR Code bitmap sent successfully");
    } catch (Exception e) {
      // Reject the promise with an error message
      promise.reject(
        "QR_CODE_ERROR",
        "Failed to generate or send QR Code bitmap"
      );
    }
  }

  public Bitmap filePathToBitmap(String filePath) throws IOException {
    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
    if (bitmap == null) {
      throw new IOException(
        "Failed to decode image from the provided file path."
      );
    }
    return bitmap;
  }

  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b * b);
  }

  @ReactMethod
  public void sendLCDCommand(int flag) {
    ILcdManager lcdManager = ILcdManager.getInstance(
      getReactApplicationContext()
    );
    lcdManager.sendLCDCommand(flag);
  }

  @ReactMethod
  public void sendLCDString(String string) {
    ILcdManager lcdManager = ILcdManager.getInstance(
      getReactApplicationContext()
    );
    lcdManager.setTextSize(48);
    lcdManager.sendLCDString(string);
  }

  @ReactMethod
  public void getTextBitmap(String text) {
    ILcdManager lcdManager = ILcdManager.getInstance(
      getReactApplicationContext()
    );
    lcdManager.getTextBitmap(text);
  }

  @ReactMethod
  public void sendLCDMultiString(String[] text, int[] align) {
    ILcdManager lcdManager = ILcdManager.getInstance(
      getReactApplicationContext()
    );
    lcdManager.setTextSize(48);
    lcdManager.sendLCDMultiString(text, align);
  }

  @ReactMethod
  public void sendLCDDoubleString(String topText, String bottomText) {
    ILcdManager lcdManager = ILcdManager.getInstance(
      getReactApplicationContext()
    );
    lcdManager.sendLCDDoubleString(topText, bottomText);
  }

  @ReactMethod
  public void sendLCDBitmapFromFile(String filePath) throws IOException {
    ILcdManager lcdManager = ILcdManager.getInstance(
      getReactApplicationContext()
    );
    try {
      Bitmap bitmap = filePathToBitmap(filePath);
      lcdManager.sendLCDBitmap(bitmap);
    } catch (IOException e) {
      throw new IOException("Incorrect File Path");
    }
  }

  @ReactMethod
  public void sendLCDBitmapFromURL(String url) throws IOException {
    ILcdManager lcdManager = ILcdManager.getInstance(
      getReactApplicationContext()
    );
    try {
      Bitmap bitmap = urlToBitmap(url);
      lcdManager.sendLCDBitmap(bitmap);
    } catch (IOException e) {
      throw new IOException("Incorrect Url");
    }
  }

  @ReactMethod
  public void base64ToBitmap(String base64String) {
    ILcdManager lcdManager = ILcdManager.getInstance(
      getReactApplicationContext()
    );

    try {
      byte[] decodedImageBytes = Base64.decode(base64String, Base64.DEFAULT);
      Bitmap decodedBitmap = BitmapFactory.decodeByteArray(
        decodedImageBytes,
        0,
        decodedImageBytes.length
      );

      if (decodedBitmap != null) {
        // If decoding is successful, send the Bitmap to the lcdManager.
        lcdManager.sendLCDBitmap(decodedBitmap);
      } else {
        // Handle the case where decoding fails (e.g., invalid base64 string).
        // You can log an error or take appropriate action.
      }
    } catch (Exception e) {
      // Handle any exceptions that may occur during decoding.
      e.printStackTrace();
    }
  }

  @ReactMethod
  public void setTextSize(int size) {
    ILcdManager lcdManager = ILcdManager.getInstance(
      getReactApplicationContext()
    );
    lcdManager.setTextSize(size);
  }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.imin.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.os.Build;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.util.Log;
import com.htt.image.FreeImageUtil;
import com.imin.image.ThreadUtils.SimpleTask;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ILcdManager {
    Context context;
    static ILcdManager iLcdManager;
    TextPaint textPaint;
    private Typeface textTypeface;
    private boolean haveBold;
    private int textSize;
    private boolean haveUnderline;
    private static int textWidth = 240;
    private int fontSizeStyle;
    private Alignment textAlignment;
    private float textLineSpacing;
    private List<Alignment> mAlignments;
    private final byte[] bmp;
    int sizeForFrame;
    long startTime;
    long endTime;

    private ILcdManager(Context context) {
        this.textTypeface = Typeface.DEFAULT;
        this.textSize = 55;
        this.fontSizeStyle = 0;
        this.textAlignment = Alignment.ALIGN_CENTER;
        this.textLineSpacing = 1.0F;
        this.mAlignments = new ArrayList();
        this.bmp = new byte[]{66, 77, 56, 88, 2};
        this.sizeForFrame = 153600;
        this.startTime = 0L;
        this.endTime = 0L;
        this.context = context;
    }

    public static ILcdManager getInstance(Context context) {
        Class var1 = ILcdManager.class;
        synchronized(ILcdManager.class) {
            if (iLcdManager == null) {
                iLcdManager = new ILcdManager(context);
            }
        }

        return iLcdManager;
    }

    public void sendLCDCommand(int flag) {
        Log.d("imin_asd", "sendLCDCommand flag  " + flag);

        try {
            BufferedWriter bw;
            if (flag != 4) {
                bw = new BufferedWriter(new FileWriter("sys/spi_lcm/spi_lcm_power"));
                bw.write(String.valueOf(flag));
                bw.close();
            } else {
                bw = new BufferedWriter(new FileWriter("sys/spi_lcm/spi_lcm_power"));
                bw.write("5");
                bw.close();
            }
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public void sendLCDString(String string) {
        Log.d("imin_asd", "sendLCDString string  " + string);
        if (!StringUtils.isEmpty(string)) {
            Bitmap bitmap = this.getTextBitmap(string);
            this.imageToBmp(bitmap);
        }
    }

    public void sendLCDMultiString(String[] text, int[] align) {
        Log.d("imin_asd", "sendLCDMultiString string  " + text);
        if (text != null && text.length > 0) {
            if (text.length != align.length) {
                Log.d("imin_asd", "text.length != align.length");
                return;
            }

            Bitmap bitmap = this.getTableBitMap(text, align);
            this.imageToBmp(bitmap);
        }

    }

    public void sendLCDFillString(String string, int size, boolean fill) {
        Log.d("imin_asd", "sendLCDFillString string  " + string);
        if (!StringUtils.isEmpty(string)) {
            this.setTextSize(size);
            Bitmap bitmap = this.getTextBitmap(string);
            this.imageToBmp(bitmap);
        }
    }

    public void sendLCDFillStringWithSize(String string, int size) {
        Log.d("imin_asd", "sendLCDFillString string  " + string);
        if (!StringUtils.isEmpty(string)) {
            this.setTextSize(size);
            Bitmap bitmap = this.getTextBitmap(string);
            this.imageToBmp(bitmap);
        }
    }

    public void sendLCDDoubleString(String topText, String bottomText) {
        if (!StringUtils.isEmpty(topText) && !StringUtils.isEmpty(bottomText)) {
            Bitmap bitmap = this.getDoubleTextBitmap(topText, bottomText);
            this.imageToBmp(bitmap);
        }
    }

    public void sendLCDBitmap(Bitmap bitmap) {
        Log.d("imin_asd", "sendLCDBitmap s  ");
        if (bitmap != null) {
            this.imageToBmp(bitmap);
        }

    }

    public void sendLCDBitmap(InputStream in, InputStream is) {
        Log.d("imin_asd", "sendLCDBitmap s  ");
        if (is != null) {
            Log.d("imin_asd", "sendLCDBitmap  ");
            this.sendResToAuxiliaryScreen(in, is);
        }

    }

    public ILcdManager setTextSize(int size) {
        this.textSize = size;
        return this;
    }

    public ILcdManager setTextSizeNormal() {
        this.textSize = 55;
        return this;
    }

    public Bitmap getTextBitmap(String text) {
        Bitmap newBitmap = Bitmap.createBitmap(240, 320, Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(-1, Mode.CLEAR);
        Paint paint = new Paint();
        String familyName = "Arial";
        this.textTypeface = Typeface.create(familyName, 1);
        paint.setColor(-1);
        paint.setTypeface(this.textTypeface);
        paint.setTextSize((float)this.textSize);
        canvas.drawText(text, 10.0F, 160.0F, paint);
        return newBitmap;
    }

    public Bitmap getDoubleTextBitmap(String text, String bottom) {
        Bitmap newBitmap = Bitmap.createBitmap(240, 320, Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(-1, Mode.CLEAR);
        Paint paint = new Paint();
        String familyName = "Arial";
        this.textTypeface = Typeface.create(familyName, 1);
        paint.setColor(-1);
        paint.setTypeface(this.textTypeface);
        paint.setTextSize((float)this.textSize);
        int texth = this.getFontHeight((float)this.textSize);
        canvas.drawText(text, 10.0F, (float)(160 - texth / 2), paint);
        canvas.drawText(bottom, 10.0F, (float)(160 - texth / 2 + texth + 2), paint);
        return newBitmap;
    }

    public Bitmap getTableBitMap(String[] text, int[] align) {
        Bitmap newBitmap = Bitmap.createBitmap(240, 320, Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(-1, Mode.CLEAR);
        Paint paint = new Paint();
        String familyName = "Arial";
        this.textTypeface = Typeface.create(familyName, 1);
        paint.setColor(-1);
        paint.setTypeface(this.textTypeface);
        paint.setTextSize((float)this.textSize);
        int x = true;
        int textH = this.getFontHeight((float)this.textSize);
        int totalH = 0;

        int startH;
        for(startH = 0; startH < text.length; ++startH) {
            totalH += this.getFontHeight((float)this.textSize) + 2;
        }

        startH = 160 - (320 - totalH) / 2;
        int h = startH;

        for(int i = 0; i < text.length; ++i) {
            if (text[i].length() != 0) {
                int x;
                if (align[i] == 0) {
                    x = 10;
                } else if (align[i] == 1) {
                    int w = 120;
                    int textLength = text[i].length() + 20;
                    x = w - textLength;
                } else {
                    x = 140 - text[i].length();
                }

                canvas.drawText(text[i], (float)x, (float)h, paint);
                h += textH + 2;
            }
        }

        return newBitmap;
    }

    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        FontMetrics fm = paint.getFontMetrics();
        return (int)Math.ceil((double)(fm.descent - fm.top)) + 2;
    }

    private boolean checkFile(InputStream bis) {
        try {
            byte[] b0 = new byte[5];
            bis.read(b0);
            Log.d("imin_asdasd", " bmp : " + Arrays.toString(this.bmp) + " b0 : " + Arrays.toString(b0));
            if (!Arrays.equals(b0, this.bmp)) {
                Log.d("imin_asdasd", "drawable type not match bmp");
                return false;
            } else {
                byte[] b1 = new byte[13];
                Log.d("imin_asdasd", "b1 : " + Arrays.toString(b1));
                bis.read(b1);
                byte[] b2 = new byte[4];
                bis.read(b2);
                Log.d("imin_asdasd", "b2 : " + Arrays.toString(b2));
                byte[] b3 = new byte[4];
                bis.read(b3);
                Log.d("imin_asdasd", "b3 : " + Arrays.toString(b3));
                int width = this.byte2Int(b2);
                if (width != 240) {
                    Log.d("imin_asd", "drawable width not match 240");
                    return false;
                } else {
                    int heigth = this.byte2Int(b3);
                    if (heigth != 320) {
                        Log.d("imin_asdasd", "drawable height not match 320");
                        return false;
                    } else {
                        Log.d("imin_asdasd", "width : " + width + "heigth: " + heigth);
                        byte[] b4 = new byte[2];
                        bis.read(b4);
                        byte[] b5 = new byte[1];
                        bis.read(b5);
                        Log.d("imin_asdasd", "db5 : " + Arrays.toString(b5));
                        byte[] b6 = new byte[1];
                        bis.read(b6);
                        Log.d("imin_asdasd", "db6 : " + Arrays.toString(b6));
                        int bitsPerPixel = b5[0] & 255 | (b6[0] & 255) << 8;
                        Log.d("imin_asdasd", "bits per pixel   " + bitsPerPixel);
                        if (bitsPerPixel != bitsPerPixel) {
                            Log.d("imin_asd", "bits per pixel not match  " + bitsPerPixel);
                            return false;
                        } else {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception var12) {
            var12.printStackTrace();
            return false;
        }
    }

    public int byte2Int(byte[] by) {
        int t1 = by[3] & 255;
        int t2 = by[2] & 255;
        int t3 = by[1] & 255;
        int t4 = by[0] & 255;
        int num = t1 << 24 | t2 << 16 | t3 << 8 | t4;
        return num;
    }

    private void sendResToAuxiliaryScreen(InputStream in, InputStream is) {
        if (in == null) {
            throw new NullPointerException("bitmap cannot be empty");
        } else if (!this.checkFile(in)) {
            Log.d("imin_asdasd", "error    bitmap ssize : ");
        } else {
            FileOutputStream fos = null;

            try {
                String path = "/dev/spidev1.0";
                fos = new FileOutputStream(new File(path));
                int ssize = is.available();
                Log.d("imin_asdasd", "ssize : " + ssize);
                byte[] buffer;
                if (ssize > this.sizeForFrame) {
                    buffer = new byte[this.sizeForFrame];
                } else {
                    buffer = new byte[ssize];
                }

                while(is.read(buffer) > 0) {
                    Log.d("imin_asdasd", "buffer==>eeeeee 2: " + Arrays.toString(buffer));
                    fos.write(buffer, 0, buffer.length);
                    int len = is.available();
                    Log.d("imin_asdasd", "len : " + len);
                    if (len > this.sizeForFrame) {
                        buffer = new byte[this.sizeForFrame];
                    } else {
                        buffer = new byte[len];
                    }
                }

                this.endTime = System.currentTimeMillis();
                Log.d("imin_asdasd", "时间差==>：" + (this.endTime - this.startTime));
            } catch (Exception var16) {
                var16.printStackTrace();
                Log.d("imin_asdasd", "ssize  Exception:1 " + var16.getMessage());
            } finally {
                try {
                    fos.flush();
                    fos.close();
                    in.close();
                    is.close();
                } catch (IOException var15) {
                    var15.printStackTrace();
                    Log.d("imin_asdasd", "ssize  Exception:2 " + var15.getMessage());
                }

            }

        }
    }

    private void imageToBmp(Bitmap bitmap) {
        if (bitmap == null) {
            Log.d("imin_asdasd", "bitmap is null !  " + Build.MODEL);
        } else {
            Log.d("imin_asdasd", "Build.MODEL== !  " + Build.MODEL);
            this.startTime = System.currentTimeMillis();
            Matrix m = new Matrix();
            m.postRotate(-90.0F);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            final String filePath = BitmapUtils.saveBitmap(bitmap);
            File file = new File(filePath);
            if (!file.exists()) {
                Log.d("imin_asdasd", "bitmap is null!");
            } else {
                ThreadUtils.executeByCached(new SimpleTask<Boolean>() {
                    public Boolean doInBackground() throws Throwable {
                        return FreeImageUtil.INSTANCE.imageToBmp(filePath, BitmapUtils.outFilePath, 240, 320);
                    }

                    public void onSuccess(Boolean result) {
                        Log.d("imin_asdasd", "onSuccess ==>" + result);
                        if (result) {
                            InputStream in = BitmapUtils.bitmapPathToIn(BitmapUtils.outFilePath);
                            InputStream is = BitmapUtils.bitmapPathToIn(BitmapUtils.outFilePath);
                            ILcdManager.this.sendResToAuxiliaryScreen(in, is);
                        }

                    }

                    public void onFail(Throwable t) {
                        super.onFail(t);
                        if (t != null) {
                            Log.d("imin_asdasd", t.getMessage());
                        }

                    }
                });
            }
        }
    }

    public long getTimeDifference() {
        return this.endTime - this.startTime;
    }
}


// #####//#endregion
package com.iminlcdmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout.Alignment;
import android.text.TextPaint;
import android.util.Log;
import androidx.annotation.NonNull;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;
import com.htt.image.FreeImageUtil;
import com.imin.image.ILcdManager;
import com.imin.image.ThreadUtils.SimpleTask;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ReactModule(name = LcdManagerModule.NAME)
public class LcdManagerModule extends ReactContextBaseJavaModule {

  public static final String NAME = "LcdManager";
  Context context;
  static ILcdManager iLcdManager;
  TextPaint textPaint;
  private Typeface textTypeface;
  private boolean haveBold;
  private int textSize;
  private boolean haveUnderline;
  private static int textWidth = 240;
  private int fontSizeStyle;
  private Alignment textAlignment;
  private float textLineSpacing;
  private List<Alignment> mAlignments;
  private final byte[] bmp;
  int sizeForFrame;
  long startTime;
  long endTime;

  public LcdManagerModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.textTypeface = Typeface.DEFAULT;
    this.textSize = 55;
    this.fontSizeStyle = 0;
    this.textAlignment = Alignment.ALIGN_CENTER;
    this.textLineSpacing = 1.0F;
    this.mAlignments = new ArrayList();
    this.bmp = new byte[] { 66, 77, 56, 88, 2 };
    this.sizeForFrame = 153600;
    this.startTime = 0L;
    this.endTime = 0L;
    this.context = context;
  }

  public static ILcdManager getInstance(Context context) {
    Class var1 = ILcdManager.class;
    synchronized (ILcdManager.class) {
      if (iLcdManager == null) {
        iLcdManager = new ILcdManager(context);
      }
    }

    return iLcdManager;
  }

  public Bitmap getTextBitmap(String text) {
    Bitmap newBitmap = Bitmap.createBitmap(240, 320, Config.ARGB_8888);
    Canvas canvas = new Canvas(newBitmap);
    canvas.drawColor(-1, Mode.CLEAR);
    Paint paint = new Paint();
    String familyName = "Arial";
    this.textTypeface = Typeface.create(familyName, 1);
    paint.setColor(-1);
    paint.setTypeface(this.textTypeface);
    paint.setTextSize((float) this.textSize);
    canvas.drawText(text, 10.0F, 160.0F, paint);
    return newBitmap;
  }

  public void sendLCDCommand(int flag) {
    Log.d("imin_asd", "sendLCDCommand flag  " + flag);

    try {
      BufferedWriter bw;
      if (flag != 4) {
        bw = new BufferedWriter(new FileWriter("sys/spi_lcm/spi_lcm_power"));
        bw.write(String.valueOf(flag));
        bw.close();
      } else {
        bw = new BufferedWriter(new FileWriter("sys/spi_lcm/spi_lcm_power"));
        bw.write("5");
        bw.close();
      }
    } catch (IOException var3) {
      var3.printStackTrace();
    }
  }

  public void sendLCDString(String string) {
    Log.d("imin_asd", "sendLCDString string  " + string);
    if (!StringUtils.isEmpty(string)) {
      Bitmap bitmap = this.getTextBitmap(string);
      this.imageToBmp(bitmap);
    }
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b * b);
  }
}

package com.upv.adm.adm_personal_shapes.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

public class QRCodeDecoder {

    public static String decode(byte[] imageData) {
        String result = "";
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
            RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
            BinaryBitmap binBitmap = new BinaryBitmap(new HybridBinarizer(source));
            QRCodeReader reader = new QRCodeReader();
            result = reader.decode(binBitmap).getText();
        }
        catch (Exception e) {}
        System.gc();
        return result;
    }
}

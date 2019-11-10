package org.bochman.upapp.data.enteties;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import org.bochman.upapp.utils.Debug;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Date;

import androidx.room.TypeConverter;

public class BitmapConverter {

//    @TypeConverter
//    public static Bitmap toBitmap(byte[] byteArray) {
//        if (byteArray == null) {
//            return null;
//        } else {
//            Log.i(Debug.getTag(), String.format("toBitmap: size %d ", bytes.length));
//
//            Bitmap.Config configBmp = Bitmap.Config.valueOf(bitmap.getConfig().name());
//            Bitmap bitmap_tmp = Bitmap.createBitmap(width, height, configBmp);
//            ByteBuffer buffer = ByteBuffer.wrap(byteArray);
//            bitmap_tmp.copyPixelsFromBuffer(buffer);
//
//           // Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//            return bmp;
//        }
//    }
//
//    @TypeConverter
//    public static byte[] toByteArray(Bitmap bitmap) {
//        if( bitmap==null) {
//            return null;
//        } else{
//            int size = bitmap.getRowBytes() * bitmap.getHeight();
//            Log.i(Debug.getTag(), String.format("toByteArray: size %d ", size));
//            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
//            bitmap.copyPixelsToBuffer(byteBuffer);
//            return byteBuffer.array();
//        }
//    }

    /**
     * @param bitmap
     * @return converting bitmap and return a string
     */
    @TypeConverter
    public static String toByteArray(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    @TypeConverter
    public static Bitmap toBitmap(String encodedString){
        try{
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
}

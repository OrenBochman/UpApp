package org.bochman.upapp.data.enteties;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.nio.ByteBuffer;
import java.util.Date;

import androidx.room.TypeConverter;

public class BitmapConverter {

    @TypeConverter
    public static Bitmap toBitmap(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bmp;
        }
    }

    @TypeConverter
    public static byte[] toByteArray(Bitmap bitmap) {
        if( bitmap==null) {
            return null;
        } else{
            int size = bitmap.getRowBytes() * bitmap.getHeight();
            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            bitmap.copyPixelsToBuffer(byteBuffer);
            return byteBuffer.array();
        }
    }
}

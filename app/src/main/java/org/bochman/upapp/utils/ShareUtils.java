package org.bochman.upapp.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.gson.Gson;

import org.bochman.upapp.data.enteties.Poi;

public class ShareUtils {

    /**
     * Share intent to share an object.
     *
     * @param context - activity
     * @param subject - subject massage
     * @param body    - body of the message
     * @param message - message show to user
     */
    public static void shareByMail(Context context, String subject, String body, String message, Poi poi) {
        //create a share intent
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        // Add data to the intent, the receiving app will decide what to do with it.
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body + poi.name );
        Gson gson  = new Gson();
        gson.toJson(poi);
        intent.putExtra(Intent.EXTRA_STREAM,gson.toJson(poi));

        //start a chooser
        context.startActivity(Intent.createChooser(intent, message));
    }

    /**
     * Share an image in shared storage.
     *
     * @param context     - calling activity
     * @param pathToImage -location of image
     * @return
     */
    private static Intent shareImage(Context context, String pathToImage) {

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("image/*"); //filter apps by mime type

        // For a file in shared storage.
        // For data in private storage, use a ContentProvider.
        Uri uri;
        uri = Uri.fromFile(context.getFileStreamPath(pathToImage));
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        return shareIntent;

    }



}

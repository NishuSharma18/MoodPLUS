package com.example.android.moodplus.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import com.cottacush.android.hiddencam.HiddenCam;
import com.cottacush.android.hiddencam.OnImageCapturedListener;
import com.example.android.moodplus.helper.DBHelper;
import com.example.android.moodplus.helper.DBHelper1;
import com.example.android.moodplus.helper.EmotionClasses;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.MemoryFormat;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

public class UnlockReceiver extends BroadcastReceiver {

    static boolean done = false;
    private static HiddenCam hiddenCam;
    DBHelper1 db1;

    public static final String SHARED_PREFS = "sharedPrefs";
    private final String TEXT = "text";

    @Override
    public void onReceive(final Context context, Intent intent) {

        db1 = new DBHelper1(context);

        hiddenCam = new HiddenCam(context, context.getCacheDir(), new OnImageCapturedListener() {
            @Override
            public void onImageCaptured(File file) {
                Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                done = true;
                hiddenCam.stop();
                hiddenCam = null;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                Matrix matrix = new Matrix();
                matrix.postRotate(-90);
                Bitmap scaledBitMap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                bitmap = Bitmap.createBitmap(scaledBitMap, 0, 0, scaledBitMap.getWidth(), scaledBitMap.getHeight(), matrix, true);

                DetectSmile(bitmap,context);
                file.delete();
            }

            @Override
            public void onImageCaptureError(Throwable throwable) {
                Toast.makeText(context, "Failed, " + (throwable != null ? throwable.getMessage() : "Throwable null :("), Toast.LENGTH_LONG).show();
                done = true;
                hiddenCam.stop();
                hiddenCam = null;
            }
        });

        hiddenCam.start();
        hiddenCam.captureImage();

    }

    private void DetectSmile(Bitmap bitmap,Context context){
        FaceDetectorOptions highAccuracyOpts =
                new FaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();

        InputImage image = InputImage.fromBitmap(bitmap, 0);

        FaceDetector detector = FaceDetection.getClient(highAccuracyOpts);
        Log.e("UnlockReceiver","Face Detection started");

        Task<List<Face>> result =
                detector.process(image)
                        .addOnSuccessListener(
                                new OnSuccessListener<List<Face>>() {
                                    @Override
                                    public void onSuccess(List<Face> faces) {
                                        // Task completed successfully
                                        // ...
                                        for (Face face : faces){
                                            // If classification was enabled:
                                            if (face.getSmilingProbability() != null) {
                                                float smileProb = face.getSmilingProbability();
                                                Log.e("UnlockReceiver","Emotion Detection started");
                                                DetectEmotion(bitmap,context,String.valueOf(smileProb));
                                            }
                                        }
                                    }
                                })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(context,"failed",Toast.LENGTH_SHORT).show();
                                        // Task failed with an exception
                                        // ...
                                    }
                                });
    }
    private void DetectEmotion(Bitmap bitmap,Context context,String smileProb){
        float f = Float.parseFloat(smileProb);
        if(f<0.1){
            Module module = null;
            try {
                module = LiteModuleLoader.load(assetFilePath(context, "EmotionRecognition_scripted.pt"));
            } catch (IOException e) {
                Log.e("UnlockReceiver", "Error reading assets", e);
            }
            // preparing input tensor
            final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(bitmap,
                    TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB, MemoryFormat.CHANNELS_LAST);

            // running the model
            final Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();

            // getting tensor content as java array of floats
            final float[] scores = outputTensor.getDataAsFloatArray();

            String className = "";
            if ( scores.length == 1) {
                className = "-1";
            } else {
                // searching for the index with maximum score
                float maxScore = -Float.MAX_VALUE;
                int maxScoreIdx = -1;
                for (int i = 0; i < scores.length; i++) {
                    if (scores[i] > maxScore) {
                        maxScore = scores[i];
                        maxScoreIdx = i;
                    }
                }
                className = EmotionClasses.EMOTION_CLASSES[maxScoreIdx];
            }

            if(!className.equals("-1")){
                if(className.equals("Happy")){
                    className = "Neutral";
                }
                check(context,className,smileProb);
                Toast.makeText(context,className,Toast.LENGTH_SHORT).show();
                Toast.makeText(context,smileProb,Toast.LENGTH_SHORT).show();
            }
        }
        else{
            check(context,"Happy",smileProb);
        }

    }
    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer,0,read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }

    public void newInsert(String emotionName,String smileValue){
        Boolean checkInsertData = db1.insertUserData(emotionName,smileValue);
        if(checkInsertData){
            Log.e("UnlockReceiver","New Entry inserted");
        } else {
            Log.e("UnlockReceiver","No New Entry inserted");
        }
    }

    private float calculateAvg(float currentMoodAvg,long currentNo,float currentMood){
        return (currentMoodAvg*currentNo + currentMood)/(currentNo + 1);
    }


    public void check(Context context,String emotion,String smileProb){
        Calendar currentDate = Calendar.getInstance();
        int currentDayOfYear = currentDate.get(Calendar.DAY_OF_YEAR);
        int currentYear = currentDate.get(Calendar.YEAR);
        float emotionValue = Float.parseFloat(smileProb);
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, context.MODE_PRIVATE);
        int lastCompletedDayOfYear = sharedPreferences.getInt("lastCompletedDayOfYear", -1);
        int lastCompletedYear = sharedPreferences.getInt("lastCompletedYear", -1);
        setEmotion(context,emotion);

        if (lastCompletedDayOfYear != -1 && lastCompletedYear != -1){
            if (lastCompletedYear < currentYear || lastCompletedDayOfYear < currentDayOfYear) {
                // Day has been completed
                // Save the current date as the last completed date
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("lastCompletedDayOfYear", currentDayOfYear);
                editor.putInt("lastCompletedYear", currentYear);
                // inserting db
                Float completeDayAvg =  sharedPreferences.getFloat("newMoodAvg",0);
                newInsert(emotion,String.valueOf(completeDayAvg));
                editor.putFloat("newMoodAvg",emotionValue);
                editor.putLong("noOfData",1);
                editor.apply();
                Log.e("TAG",String.valueOf(emotionValue));
            } else {
                // Day has not been completed
                float currentMoodAvg = sharedPreferences.getFloat("newMoodAvg", 0);
                long no = sharedPreferences.getLong("noOfData",0);
                currentMoodAvg = calculateAvg(currentMoodAvg, no, emotionValue);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat("newMoodAvg", currentMoodAvg);
                editor.putLong("noOfData",no+1);
                editor.apply();
                Log.e("TAG",String.valueOf(currentMoodAvg));
            }
        } else {
            // This is the first time the app is being used
            // Save the current date as the last completed date
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("lastCompletedDayOfYear", currentDayOfYear);
            editor.putInt("lastCompletedYear", currentYear);
            // checks if value is present or not
            loadAppStartTimeData(context,"newMoodAvg",emotionValue);
            editor.putFloat("newMoodAvg",emotionValue);
            editor.putLong("noOfData",1);
            editor.apply();
            Log.e("TAG","First val");
        }
    }

    public void saveAppStartTimeData(Context context,String key,Float value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key,0);
        editor.apply();
    }

    public void loadAppStartTimeData(Context context,String key,Float value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS,context.MODE_PRIVATE);
        float data = sharedPreferences.getFloat(key,0);
        if(data==0.0000000){
            saveAppStartTimeData(context,key,value);
        }
    }

    private void setEmotion(Context context,String emotion){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS,context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // String keys for shared preferences

        String angry = "Angry";
        String disgust = "Disgust";
        String fear = "Fear";
        String happy = "Happy";
        String neutral = "Neutral";
        String sad = "Sad";
        String surprise = "Surprise";

        if(emotion.equals(angry)){
            int value = sharedPreferences.getInt(angry,0);
            editor.putInt(angry,value + 1);
        }
        else if(emotion.equals(disgust)){
            int value = 0;
            value = sharedPreferences.getInt(disgust,0);
            editor.putInt(disgust,value + 1);
        }
        else if(emotion.equals(fear)){
            int value = 0;
            value = sharedPreferences.getInt(fear,0);
            editor.putInt(fear,value + 1);
        }
        else if(emotion.equals(happy)){
            int value = 0;
            value = sharedPreferences.getInt(happy,0);
            editor.putInt(happy,value + 1);
        }
        else if(emotion.equals(neutral)){
            int value = sharedPreferences.getInt(neutral,0);
            editor.putInt(neutral,value + 1);
        }
        else if(emotion.equals(surprise)){
            int value = sharedPreferences.getInt(surprise,0);
            editor.putInt(surprise,value + 1);
        }
        else if(emotion.equals(sad)){
            int value = sharedPreferences.getInt(sad,0);
            editor.putInt(sad,value + 1);
        }
        editor.apply();
        Log.e("TAG",emotion);
    }

}

package com.example.taskone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class GameSignal {


    @SuppressLint("StaticFieldLeak")
    private static GameSignal signal;
    private Context context;

    private GameSignal(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        if (signal == null) {
            signal = new GameSignal(context.getApplicationContext());
        }
    }

    public static GameSignal getInstance() {
        return signal;
    }

    public void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void vibrate() {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }

    public void openDialog(String title, String message, String button) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(button, (dialog, which) -> {})
                .show();
    }

}

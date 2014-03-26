package com.beerchallenge.sensor;

import android.media.MediaRecorder;

import java.io.IOException;

public class SoundMeter {

    private MediaRecorder mediaRecorder;

    public void start() {
        if(mediaRecorder == null){
            createRecorder();
            try {
                mediaRecorder.prepare();
            } catch (IOException | IllegalStateException e) {
                // TODO
                e.printStackTrace();
            }
            mediaRecorder.start();
        }
    }

    public void stop() {

        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public double getAmplitude() {

        if (mediaRecorder != null) {
            return (mediaRecorder.getMaxAmplitude() / 2700.0);
        }
        return 0;
    }

    private void createRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile("/dev/null");
    }

}

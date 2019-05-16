package ptwdd.com.conferencing;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;


public class Chat extends AppCompatActivity {


    private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        getSupportActionBar().hide();
        media();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
    }

    private void media() {

            ImageButton volume_up = findViewById(R.id.volume_up);
            ImageButton volume_down = findViewById(R.id.volume_down);


            volume_up.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int AUDIO_ADJUST_FLAGS = AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI;
                            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AUDIO_ADJUST_FLAGS);
                        }
                    });

            volume_down.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int AUDIO_ADJUST_FLAGS = AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI;
                            mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AUDIO_ADJUST_FLAGS);
                        }
                    });

    }
}

package ptwdd.com.conferencing;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;


public class Chat extends AppCompatActivity implements ServiceConnection {

    private static final String TAG = "Chat";
    private AudioManager mAudioManager;

    SinchClient sinchClient;
    int PERMISSION_ALL = 1;
    private static boolean permissionAccepted = false;
    private String [] permissions = {
                                        Manifest.permission.RECORD_AUDIO,
                                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                                        Manifest.permission.READ_PHONE_STATE};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        getSupportActionBar().hide();
        media();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        if(!hasPermissions(this, permissions)){
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_ALL);
        }
        if(permissionAccepted == true){
            Log.d(TAG, "permissionAccepted: "+permissionAccepted);
            initSinchClient();
        }


    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        permissionAccepted = true;
        return true;
    }







    private void initSinchClient() {

        ImageButton start_conf = findViewById(R.id.talk_button);
        start_conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startConference();
            }
        });

        String deviceId = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.d(TAG, "initSinchClient: "+deviceId);

        // Instantiate a SinchClient using the SinchClientBuilder.
        android.content.Context context = this.getApplicationContext();
        sinchClient = Sinch.getSinchClientBuilder().context(context)
                .applicationKey("dda6939c-f8f2-49df-94f0-04bdc6bef99b")
                .applicationSecret("zVSlUwYDk0Kp7FtTfDK19g==")
                .environmentHost("sandbox.sinch.com")
                .userId(deviceId)
                .build();
        sinchClient.setSupportCalling(true);

        sinchClient.addSinchClientListener(new SinchClientListener() {

            public void onClientStarted(SinchClient client) {
                Log.d(TAG, "onClientStarted: ");
            }

            public void onClientStopped(SinchClient client) {
                Log.d(TAG, "onClientStopped: ");
            }

            public void onClientFailed(SinchClient client, SinchError error) {
                Log.d(TAG, "onClientFailed: "+error.getMessage());
            }

            public void onRegistrationCredentialsRequired(SinchClient client, ClientRegistration registrationCallback) {
                Log.d(TAG, "onRegistrationCredentialsRequired: ");
            }

            public void onLogMessage(int level, String area, String message) {
                Log.d(TAG, "onLogMessage: "+message);
            }
        });

        sinchClient.start();
        sinchClient.checkManifest();
        Log.d(TAG, "initSinchClient: checkManifest ");



    }

    private void startConference() {

        CallClient callClient = sinchClient.getCallClient();
        Call call = callClient.callConference("airclickconferenceId");
        call.addCallListener(new CallListener() {
            @Override
            public void onCallProgressing(Call call) {
                Log.d(TAG, "onCallProgressing: "+call);
            }

            @Override
            public void onCallEstablished(Call call) {
                Log.d(TAG, "onCallEstablished: "+call);
            }

            @Override
            public void onCallEnded(Call call) {
                Log.d(TAG, "onCallEnded: "+call);
            }

            @Override
            public void onShouldSendPushNotification(Call call, List<PushPair> list) {

            }
        });
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        sinchClient.stopListeningOnActiveConnection();
        sinchClient.terminate();
    }
}

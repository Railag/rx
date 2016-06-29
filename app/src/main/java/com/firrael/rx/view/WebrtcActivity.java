package com.firrael.rx.view;

import android.content.Intent;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.firrael.rx.R;
import com.firrael.rx.model.InviteToCallResult;
import com.firrael.rx.model.User;
import com.firrael.rx.presenter.WebrtcPresenter;

import org.json.JSONException;
import org.webrtc.MediaStream;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoRendererGui;
import org.webrtc.VideoTrack;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.pchab.webrtcclient.PeerConnectionParameters;
import fr.pchab.webrtcclient.WebRtcClient;
import nucleus.factory.RequiresPresenter;
import nucleus.view.NucleusAppCompatActivity;

/**
 * Created by Railag on 15.06.2016.
 */
@RequiresPresenter(WebrtcPresenter.class)
public class WebrtcActivity extends NucleusAppCompatActivity<WebrtcPresenter> implements WebRtcClient.RtcListener {

    private static final String TAG = WebrtcActivity.class.getSimpleName();

    public final static String HOST = "host";

    private final static int REMOTE_PEERS = 4;
    private boolean[] presentPeers = new boolean[REMOTE_PEERS];

    private final static int VIDEO_CALL_SENT = 666;
    private static final String VIDEO_CODEC_VP9 = "VP9";
    private static final String AUDIO_CODEC_OPUS = "opus";
    // Local preview screen position before call is connected.
    private static final int LOCAL_X_CONNECTING = 0;
    private static final int LOCAL_Y_CONNECTING = 0;
    private static final int LOCAL_WIDTH_CONNECTING = 100;
    private static final int LOCAL_HEIGHT_CONNECTING = 100;
    // Local preview screen position after call is connected.
    private static final int LOCAL_X_CONNECTED = 25;
    private static final int LOCAL_Y_CONNECTED = 25;
    private static final int LOCAL_WIDTH_CONNECTED = 25;
    private static final int LOCAL_HEIGHT_CONNECTED = 25;
    // Remote video screen position
    private static final int REMOTE_WIDTH_MODIFIER = 25;
    private static final int REMOTE_HEIGHT_MODIFIER = 25;
    private VideoRendererGui.ScalingType scalingType = VideoRendererGui.ScalingType.SCALE_ASPECT_FILL;
    private VideoRenderer.Callbacks localRender;
    private VideoRenderer.Callbacks remoteRenders[];
    private WebRtcClient client;
    private String socketAddress;
    private String callerId;

    private int endpoints;

    @BindView(R.id.callView)
    GLSurfaceView vsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_webrtc);
        ButterKnife.bind(this);

        final Intent intent = getIntent();
        if (intent != null && intent.hasExtra(HOST)) {
            String host = intent.getStringExtra(HOST);
            int dividerPos = host.indexOf('#');
            if (dividerPos != -1) {
                socketAddress = host.substring(0, dividerPos + 1);
                callerId = host.substring(dividerPos + 1);
            }
            //getString(R.string.webrtchost);
        }

        vsv.setPreserveEGLContextOnPause(true);
        vsv.setKeepScreenOn(true);
        VideoRendererGui.setView(vsv, this::init);

        // local and remote render
        remoteRenders = new VideoRenderer.Callbacks[REMOTE_PEERS];
        initRemoteRenders();
        localRender = VideoRendererGui.create(
                LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING, scalingType, true);

        //    final Intent intent = getIntent();
        final String action = intent.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            final List<String> segments = intent.getData().getPathSegments();
            callerId = segments.get(0);
        }
    }

    private void initRemoteRenders() {
        for (int render = 0; render < REMOTE_PEERS; render++) {
            remoteRenders[render] = VideoRendererGui.create(
                    0, 0,
                    0, 0, scalingType, false);
        }
    }

    private void init() {
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        PeerConnectionParameters params = new PeerConnectionParameters(
                true, false, displaySize.x, displaySize.y, 30, 1, VIDEO_CODEC_VP9, true, 1, AUDIO_CODEC_OPUS, true);

        client = new WebRtcClient(this, socketAddress, params, VideoRendererGui.getEGLContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        if (vsv != null) {
            vsv.onPause();
            if (client != null) {
                client.onPause();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (vsv != null) {
            vsv.onResume();
            if (client != null) {
                client.onResume();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (client != null) {
            client.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onCallReady(String callId) {
        Log.i(TAG, "#onCallReady");

        if (callerId != null) {
            try {
                answer(callerId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            call(callId);
        }
    }

    public void answer(String callerId) throws JSONException {
        Log.i(TAG, "#Answer");
        client.sendMessage(callerId, "init", null);
        startCam();
    }

    public void call(String callId) {
        Log.i(TAG, "#Call");

        Log.i(TAG, socketAddress.replace("10.0.3.2", "localhost") + callId);
        //    startCam();

        User user = User.get(this);
        getPresenter().inviteToCall(user.getId(), socketAddress, callId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "#onActivityResult");

        if (requestCode == VIDEO_CALL_SENT) {
            startCam();
        }
    }

    public void startCam() {
        Log.i(TAG, "#startCam");

        // Camera settings
        client.start("android_test");
    }


    @Override
    public void onStatusChanged(final String newStatus) {
        Log.i(TAG, "#onStatusChanged");

        runOnUiThread(() -> Toast.makeText(this, newStatus, Toast.LENGTH_SHORT).show());
        Log.i(TAG, newStatus);
    }

    @Override
    public void onLocalStream(MediaStream localStream) {
        Log.i(TAG, "#onLocalStream");

        List<VideoTrack> videoTracks = localStream.videoTracks;
        if (videoTracks != null && videoTracks.size() > 0) {
            videoTracks.get(0).addRenderer(new VideoRenderer(localRender));
            VideoRendererGui.update(localRender,
                    LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                    LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING,
                    scalingType);
        }
    }

    @Override
    public void onAddRemoteStream(MediaStream remoteStream, int endPoint) {
        Log.i(TAG, "#onAddRemoteStream");

        endpoints++;
        presentPeers[endPoint - 1] = true;

        List<VideoTrack> videoTracks = remoteStream.videoTracks;
        if (videoTracks != null && videoTracks.size() > 0) {
            videoTracks.get(0).addRenderer(new VideoRenderer(remoteRenders[endPoint - 1]));

            VideoRendererGui.update(localRender,
                    LOCAL_X_CONNECTED, LOCAL_Y_CONNECTED,
                    LOCAL_WIDTH_CONNECTED, LOCAL_HEIGHT_CONNECTED,
                    scalingType);

            refreshRemotes();
        }
    }

    private void refreshRemotes() {
        int sizeX = getRemoteXSize();
        int sizeY = getRemoteYSize(sizeX);
        for (int endPoint = 1; endPoint < REMOTE_PEERS + 1; endPoint++) {
            if (!presentPeers[endPoint - 1])
                continue;

            int remoteX = getRemoteXForEndpoint(endPoint);
            int remoteY = getRemoteYForEndpoint(endPoint);

            VideoRendererGui.update(remoteRenders[endPoint - 1],
                    remoteX, remoteY,
                    sizeX, sizeY, scalingType);
        }
    }

    private int getRemoteXSize() {
        switch (endpoints) {
            case 1:
                return 100;
            case 2:
            case 3:
            case 4:
                return 50;
            default:
                return 0;
        }
    }

    private int getRemoteYSize(int size) {
        switch (endpoints) {
            case 1:
            case 3:
            case 4:
                return size;
            case 2:
                return size * 2;
            default:
                return size;
        }
    }

    @Override
    public void onRemoveRemoteStream(int remoteEndpoint) { // remote endpoints (0-3)
        Log.i(TAG, "#onRemoveRemoteStream");

        endpoints--;

        presentPeers[remoteEndpoint] = false;

        VideoRendererGui.update(remoteRenders[remoteEndpoint],
                0, 0,
                0, 0, scalingType);

        if (endpoints == 0) {
            /*VideoRendererGui.update(localRender,
                    LOCAL_X_CONNECTING, LOCAL_Y_CONNECTING,
                    LOCAL_WIDTH_CONNECTING, LOCAL_HEIGHT_CONNECTING,
                    scalingType);*/
            finish();
        } else if (endpoints == 3) {
            VideoRendererGui.update(localRender,
                    getRemoteXForEndpoint(remoteEndpoint + 1), getRemoteYForEndpoint(remoteEndpoint + 1),
                    50, 50,
                    scalingType);
        }

        refreshRemotes();
    }

    private int getRemoteXForEndpoint(int endpoint) {
        switch (endpoint) {
            case 0: // local peer, ignore
                return -1;
            case 1:
                return 0;
            case 2:
                return REMOTE_WIDTH_MODIFIER * 2;
            case 3:
                return 0;
            case 4:
                return REMOTE_WIDTH_MODIFIER * 2;
            default:
                return -1;
        }
    }

    private int getRemoteYForEndpoint(int endpoint) {
        switch (endpoint) {
            case 0: // local peer, ignore
                return -1;
            case 1:
                return 0;
            case 2:
                return 0;
            case 3:
                return REMOTE_HEIGHT_MODIFIER * 2;
            case 4:
                return REMOTE_HEIGHT_MODIFIER * 2;
            default:
                return -1;
        }
    }

    public void onSuccessInviteCall(InviteToCallResult result) {
        if (result.invalid()) {
            Toast.makeText(this, result.error, Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, result.result, Toast.LENGTH_LONG).show();
        startCam();
    }

    public void onErrorInviteCall(Throwable throwable) {
        throwable.printStackTrace();
    }
}
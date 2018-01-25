package org.pjsip.mypj;

import android.util.Log;

import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallSdpCreatedParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.VideoPreview;
import org.pjsip.pjsua2.VideoWindow;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsua2;
import org.pjsip.pjsua2.pjsua_call_media_status;

public class MyCall extends Call {
    private final String TAG = getClass().getSimpleName();
    public VideoWindow vidWin;
    public VideoPreview vidPrev;

    MyCall(MyAccount acc, int call_id) {
        super(acc, call_id);
        vidWin = null;
    }

    /**
     *
     * @param prm
     * pjsip_inv_state.PJSIP_INV_STATE_CALLING
     * 接听是下面四个，拨打是总共5个，就那个PJSIP_INV_STATE_INCOMING不输出
     * pjsip_inv_state.PJSIP_INV_STATE_EARLY
     * pjsip_inv_state.PJSIP_INV_STATE_CONNECTING
     * pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED
     * pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED
     */
    @Override
    public void onCallState(OnCallStateParam prm) {
        MyApp.observer.notifyCallState(this);
        try {
            CallInfo ci = getInfo();
            if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                Log.d(TAG, "nova======onCallState: pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED");
                MyApp.ep.utilLogWrite(3, "MyCall", this.dump(true, ""));
                this.delete();
            }
            if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                //此处还需要判断
                Log.d(TAG, "nova======onCallState: pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED");
//                setKeyByAndroid(KeysEntity.getInstance().getKeyString(), KeysEntity.getInstance().getKeyString().length());
            }
            if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_INCOMING) {
                //就这个没输出..
                Log.d(TAG, "nova======onCallState: pjsip_inv_state.PJSIP_INV_STATE_INCOMING");
            }
            if(ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_EARLY){
                Log.d(TAG, "nova======onCallState: pjsip_inv_state.PJSIP_INV_STATE_EARLY");
            }
            if(ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CALLING){
                Log.d(TAG, "nova======onCallState: pjsip_inv_state.PJSIP_INV_STATE_CALLING");
            }
            if(ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONNECTING){
                Log.d(TAG, "nova======onCallState: pjsip_inv_state.PJSIP_INV_STATE_CONNECTING");
            }
            if(ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_NULL) {
                Log.d(TAG, "nova======onCallState: pjsip_inv_state.PJSIP_INV_STATE_NULL");
            }
        } catch (Exception e) {
            return;
        }
    }

    @Override
    public void onCallSdpCreated(OnCallSdpCreatedParam prm) {
        super.onCallSdpCreated(prm);
        this.getId();
        Log.d(TAG, "nova====onCallSdpCreated: callid=" + this.getId());
        //打电话直接把keyid设置下去，接电话需要获取keyid然后在answer里面处理
//        Log.d(TAG, "nova====onCallSdpCreated: "+prm.getRemSdp().getWholeSdp());
//        if (prm.getRemSdp().getWholeSdp().equals("")) {
//            if (KeysEntity.getInstance().isEncrypt()) {
//                Log.d(TAG, "nova========onCallSdpCreated: 打加密电话...id = " + KeysEntity.getInstance().getBookid());
//                setKeyId(prm, KeysEntity.getInstance().getBookid());
//
//            } else
//            {
//                Log.d(TAG, "nova========onCallSdpCreated: 非加密打电话");
//            }
//        } else {
//            KeysEntity.getInstance().setBookid(getKeyId(prm));
//            Log.d(TAG, "nova========onCallSdpCreated: 接加密电话...id = " + KeysEntity.getInstance().getBookid());
//        }

    }

    @Override
    public void onCallMediaState(OnCallMediaStateParam prm) {
        CallInfo ci;
        try {
            ci = getInfo();
        } catch (Exception e) {
            return;
        }

        CallMediaInfoVector cmiv = ci.getMedia();

        for (int i = 0; i < cmiv.size(); i++) {
            CallMediaInfo cmi = cmiv.get(i);
            if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO &&
                    (cmi.getStatus() ==
                            pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE ||
                            cmi.getStatus() ==
                                    pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD)) {
                // unfortunately, on Java too, the returned Media cannot be
                // downcasted to AudioMedia
                Media m = getMedia(i);
                AudioMedia am = AudioMedia.typecastFromMedia(m);

                // connect ports
                try {
                    MyApp.ep.audDevManager().getCaptureDevMedia().
                            startTransmit(am);
                    am.startTransmit(MyApp.ep.audDevManager().
                            getPlaybackDevMedia());
                } catch (Exception e) {
                    continue;
                }
            } else if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_VIDEO &&
                    cmi.getStatus() ==
                            pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE &&
                    cmi.getVideoIncomingWindowId() != pjsua2.INVALID_ID) {
                vidWin = new VideoWindow(cmi.getVideoIncomingWindowId());
                vidPrev = new VideoPreview(cmi.getVideoCapDev());
            }
        }

        MyApp.observer.notifyCallMediaState(this);
    }
}
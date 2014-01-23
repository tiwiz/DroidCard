package net.orgiu.rokard;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

public class RokardActivity extends Activity implements NfcAdapter.CreateNdefMessageCallback, NfcAdapter.OnNdefPushCompleteCallback {

    private Animation glow;
    public static final int REQUEST_CODE = 87;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if(isBeamAvailable()){
            //gets NFC Adapter
            NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            // Register callback to set NDEF message
            nfcAdapter.setNdefPushMessageCallback(this, this);
            // Register callback to listen for message-sent success
            nfcAdapter.setOnNdefPushCompleteCallback(this, this);
        }

        final ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        glow = AnimationUtils.loadAnimation(this, R.anim.glow);
        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final LinearLayout cardLayout = (LinearLayout) findViewById(R.id.photolayout);
        final Animation drawCardAnimation = AnimationUtils.loadAnimation(this, R.anim.draw_card);

        glow.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                imageButton.startAnimation(glow);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                imageView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        imageButton.startAnimation(glow);
        imageView.startAnimation(fadeIn);
        cardLayout.startAnimation(drawCardAnimation);

    }

    private boolean isBeamAvailable(){

        PackageManager packageManager = getPackageManager();

        return packageManager.hasSystemFeature(PackageManager.FEATURE_NFC);
    }

    public void shareLink(View w){

        final StartChooserRunnable runnable = new StartChooserRunnable(this);
        final View parameterView = w;

        final Animation click = AnimationUtils.loadAnimation(this, R.anim.click);
        click.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                parameterView.startAnimation(glow);
                runnable.execute();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        w.startAnimation(click);
    }

    public void biggerQRCode(View w){

        final Animation openActivityAnimation = AnimationUtils.loadAnimation(this, R.anim.open_activity);
        final StartActivityRunnable runnable = new StartActivityRunnable(this);

        openActivityAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {runnable.execute();}
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        w.startAnimation(openActivityAnimation);
    }

    public void clickNoAction(View z){

        final Animation clickAnimation = AnimationUtils.loadAnimation(this, R.anim.click);
        final StartBrowserOnCV startBrowser = new StartBrowserOnCV();

        clickAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                startBrowser.execute();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        z.startAnimation(clickAnimation);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {

        // This beams a URL
        NdefRecord urlRecord = NdefRecord.createUri(getString(R.string.url_to_vcard));
        NdefRecord[] urlVectRecord = new NdefRecord[]{urlRecord};
        NdefMessage ndefMessage = new NdefMessage(urlVectRecord);
        return ndefMessage;

    }

    @Override
    public void onNdefPushComplete(NfcEvent nfcEvent) {
        Toast.makeText(this, R.string.data_beamed_correctly_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                final Animation closeActivityAnimation = AnimationUtils.loadAnimation(this, R.anim.close_activity);
                final ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.startAnimation(closeActivityAnimation);
            }
        }
    } 
    public final void callActivity(Intent intent, Bundle arguments){
        startActivityForResult(intent,REQUEST_CODE,arguments);
    }

    private final class StartActivityRunnable extends AsyncTask<Void, Void, Void>{

        private Context mContext;

        public StartActivityRunnable(Context mContext){
            this.mContext = mContext;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Intent activityIntent = new Intent(mContext, QRActivity.class);
            Bundle options = ActivityOptions.makeCustomAnimation(mContext, R.anim.flip_side_in, R.anim.flip_side_out).toBundle();
            callActivity(activityIntent,options);
            return null;
        }
    }

    private final class StartBrowserOnCV extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            String url = getString(R.string.curriculum_url);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
            return null;
        }
    }

}

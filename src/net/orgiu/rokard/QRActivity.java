package net.orgiu.rokard;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by maneatorgiu on 16/01/14.
 */
public class QRActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_activity);

        //Keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Maximum Brightness
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = 1F;
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    private final void closeActivity(){
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.flip_side_in, R.anim.flip_side_out);
    }

    public void clickCloseActivity(View v){

        final Animation clickCloseAnimation = AnimationUtils.loadAnimation(this, R.anim.click);

        clickCloseAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                closeActivity();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        v.startAnimation(clickCloseAnimation);
    }
}
package net.orgiu.rokard;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by Roby on 19/01/14.
 */
public class StartChooserRunnable extends AsyncTask<Void,Void,Void>{

    private Context mContext;

    public StartChooserRunnable(Context mContext){
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mContext.getString(R.string.url_to_share));
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mContext.getString(R.string.share_url_object));
        mContext.startActivity(Intent.createChooser(shareIntent, mContext.getString(R.string.share_url_title)));
        return null;
    }
}

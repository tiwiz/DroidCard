package net.orgiu.rokard;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by Roby on 19/01/14.
 */
public class StartChooserTask extends AsyncTask<Void,Void,Void>{

    private Context mContext;

    public StartChooserTask(Context mContext){
        this.mContext = mContext;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final Context fContext = mContext;
        mContext = null; //releases the old context
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, fContext.getString(R.string.url_to_share));
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, fContext.getString(R.string.share_url_object));
        fContext.startActivity(Intent.createChooser(shareIntent, fContext.getString(R.string.share_url_title)));
        return null;
    }
}

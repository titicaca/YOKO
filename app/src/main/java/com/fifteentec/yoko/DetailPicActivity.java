package com.fifteentec.yoko;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

import java.net.URI;


public class DetailPicActivity extends Activity{


    ImageView mMainPicSource;
    ImageLoader mImageLoader= ImageLoader.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pic);

        mMainPicSource = (ImageView)findViewById(R.id.PicSource);

        Bundle bundle = this.getIntent().getExtras();
        String Path = bundle.getString("Path");
        String uri = ImageDownloader.Scheme.FILE.wrap(Path);
        mImageLoader.displayImage(uri,mMainPicSource);

        mMainPicSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailPicActivity.this.finish();
            }
        });
    }

}

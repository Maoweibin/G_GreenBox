package com.task.dd.greenbox.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.task.dd.greenbox.R;

/**
 * Created by dd on 2017/3/19.
 */

public class Set_Set_Activity extends Activity {
    private ImageView icon_back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        TextView centerTitle = (TextView) findViewById(R.id.centerTitle);
        centerTitle.setText("系统设置");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_set);
        icon_back= (ImageView) findViewById(R.id.icon_back);
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}

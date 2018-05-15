package com.task.dd.greenbox.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.task.dd.greenbox.Activity.Set_Inf_Activity;
import com.task.dd.greenbox.Activity.Set_Report_Activity;
import com.task.dd.greenbox.Activity.Set_pot_Activity;
import com.task.dd.greenbox.Activity.about_me;
import com.task.dd.greenbox.Activity.personal_center_Activity;
import com.task.dd.greenbox.R;
import com.task.dd.greenbox.tool.Util;

/**
 * Created by dd on 2016/12/9.
 */

public class SetFragment extends Fragment {
    private LinearLayout my_pot;
    private LinearLayout inf;
    private LinearLayout skin;
    private LinearLayout night;
    private LinearLayout report;
    private LinearLayout personal;
    private LinearLayout about;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_set,container,false);
        personal= (LinearLayout) view.findViewById(R.id.personal_center);
        my_pot= (LinearLayout) view.findViewById(R.id.last_flower);
        inf= (LinearLayout) view.findViewById(R.id.last_information);
        skin= (LinearLayout) view.findViewById(R.id.skin);
        night= (LinearLayout) view.findViewById(R.id.night_mode);
        report= (LinearLayout) view.findViewById(R.id.last_advise);
        about = (LinearLayout) view.findViewById(R.id.about_us);

        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), personal_center_Activity.class);
                startActivity(intent);
            }
        });
        my_pot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showToast(getContext(),"我的花盆");
                Intent intent=new Intent(getContext(), Set_pot_Activity.class);
                startActivity(intent);
            }
        });
        inf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showToast(getContext(),"暂无通知");
                Intent intent=new Intent(getContext(), Set_Inf_Activity.class);
                startActivity(intent);
            }
        });
        skin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showToast(getContext(),"各款皮肤正在设计中~");

            }
        });
        night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.showToast(getContext(),"夜间模式暂未开放~");

            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Set_Report_Activity.class);
                startActivity(intent);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), about_me.class);
                startActivity(intent);
            }
        });

        return view;
    }
}

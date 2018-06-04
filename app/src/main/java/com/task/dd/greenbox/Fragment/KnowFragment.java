package com.task.dd.greenbox.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.task.dd.greenbox.Activity.WebActivity;
import com.task.dd.greenbox.R;
import com.task.dd.greenbox.adapter.KnowAdapter;
import com.task.dd.greenbox.adapter.KnowVPAdapter;
import com.task.dd.greenbox.bean.KnowBean;
import com.task.dd.greenbox.jsonpull.NewKnowJson;
import com.task.dd.greenbox.tool.Util;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd on 2016/12/9.
 */

public class KnowFragment extends Fragment implements OnItemClickListener,KnowAdapter.Callback {
    private ListView knowlistView;
    private List<View> listView;
    private ArrayList<View> dots;
    private ViewPager viewPager;
    private View view1, view2, view3,view4;
    private int oldPosition = 0;// 记录上一次点的位置
    private static int  currentItem; // 当前页面
    private static final  String EXTRA_WEB="com.task.dd.greenbox.Fragment.KnowFragment";

    private String url="https://api.fengqiaoju.com/v1/articles/update/?page=1";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
        View view = inflater.inflate(R.layout.fragment_know,container,false);
        knowlistView= (ListView) view.findViewById(R.id.lv_fragment_know);


        try {
            KnowBean knowBean=new KnowBean();
            NewKnowJson knowJson=new NewKnowJson();
            knowBean=knowJson.knowPull(getData());
            KnowAdapter adapter= new KnowAdapter(getContext(),knowBean.getListKnowBean(),this);
            addIconView();//闪退
            addTodayView();
            knowlistView.setAdapter(adapter);
            knowlistView.setOnItemClickListener(this);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //KnowAsynTask knowAsynTask=new KnowAsynTask(knowlistView,getContext());
        //knowAsynTask.execute(url);   //s数据不采用；联网获取，采用本地数据


        return view;
    }

    private void addIconView() {
        View iconView =View.inflate(getContext(),R.layout.item_know_icon,null);
        viewPager= (ViewPager) iconView.findViewById(R.id.know_viewpager);
        listView=new ArrayList<>();
        LayoutInflater lf =LayoutInflater.from(getContext());
        view1 = lf.inflate(R.layout.know_vp1, null);
        view2 = lf.inflate(R.layout.know_vp2, null);
        view3 = lf.inflate(R.layout.know_vp3, null);
        view4 = lf.inflate(R.layout.know_vp4,null);
        listView.add(view1);
        listView.add(view2);
        listView.add(view3);
        listView.add(view4);
        dots = new ArrayList<>();
        dots.add(iconView.findViewById(R.id.view_dot_1));
        dots.add(iconView.findViewById(R.id.view_dot_2));
        dots.add(iconView.findViewById(R.id.view_dot_3));
        dots.add(iconView.findViewById(R.id.view_dot_4));
        dots.get(0).setBackgroundResource(R.drawable.dot_focused);

        KnowVPAdapter knowVPAdapter=new KnowVPAdapter(listView);
        viewPager.setAdapter(knowVPAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                dots.get(oldPosition).setBackgroundResource(
                        R.drawable.dot_normal);
                dots.get(position)
                        .setBackgroundResource(R.drawable.dot_focused);

                oldPosition = position;
                currentItem = position;
                switch (currentItem)
                {
                    case 0:
                        listView.get(0).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Util.showToast(getContext(),"兰  花中君子者也");
                                Intent i=new Intent(getActivity(), WebActivity.class);
                                String url="https://www.huabaike.com/sgzw/668.html";
                                i.putExtra(EXTRA_WEB,url);
                                startActivity(i);
                            }
                        });
                        break;
                    case 1:
                        listView.get(1).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Util.showToast(getContext(),"梅  花中忠贞者也");
                                Intent i=new Intent(getActivity(), WebActivity.class);
                                String url="https://www.huabaike.com/mbzw/805.html";
                                i.putExtra(EXTRA_WEB,url);
                                startActivity(i);
                            }
                        });
                        break;
                    case 2:
                        listView.get(2).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Util.showToast(getContext(),"茉莉  花中清纯者也");
                                Intent i=new Intent(getActivity(), WebActivity.class);
                                String url="https://www.huabaike.com/mbzw/1351.html";
                                i.putExtra(EXTRA_WEB,url);
                                startActivity(i);
                            }
                        });
                        break;
                    case 3:
                        listView.get(3).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Util.showToast(getContext(),"菊  花中隐士者也");
                                Intent i=new Intent(getActivity(), WebActivity.class);
                                String url="https://www.huabaike.com/sgzw/100.html";
                                i.putExtra(EXTRA_WEB,url);
                                startActivity(i);
                            }
                        });
                        break;
                }
            }



            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        knowlistView.addHeaderView(iconView);

        View shuisheng = iconView.findViewById(R.id.shuisheng);
        View caoben = iconView.findViewById(R.id.caoben);
        View muben = iconView.findViewById(R.id.muben);
        View qiugen = iconView.findViewById(R.id.qiugen);
        View lanke = iconView.findViewById(R.id.lanke);
        View sugen = iconView.findViewById(R.id.sugen);
        View tengben = iconView.findViewById(R.id.tengben);
        View duorou = iconView.findViewById(R.id.duorou);

        shuisheng.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), WebActivity.class);
                String url="https://www.huabaike.com/sszw";
                i.putExtra(EXTRA_WEB,url);
                startActivity(i);
            }
        });
        caoben.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), WebActivity.class);
                String url="https://www.huabaike.com/cbzw";
                i.putExtra(EXTRA_WEB,url);
                startActivity(i);
            }
        });
        muben.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), WebActivity.class);
                String url="https://www.huabaike.com/mbzw";
                i.putExtra(EXTRA_WEB,url);
                startActivity(i);
            }
        });
        qiugen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), WebActivity.class);
                String url="https://www.huabaike.com/qgzw";
                i.putExtra(EXTRA_WEB,url);
                startActivity(i);
            }
        });
        lanke.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), WebActivity.class);
                String url="https://www.huabaike.com/lkzw";
                i.putExtra(EXTRA_WEB,url);
                startActivity(i);
            }
        });
        sugen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), WebActivity.class);
                String url="https://www.huabaike.com/sgzw";
                i.putExtra(EXTRA_WEB,url);
                startActivity(i);
            }
        });
        tengben.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), WebActivity.class);
                String url="https://www.huabaike.com/tbzw";
                i.putExtra(EXTRA_WEB,url);
                startActivity(i);
            }
        });
        duorou.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), WebActivity.class);
                String url="https://www.huabaike.com/drzw";
                i.putExtra(EXTRA_WEB,url);
                startActivity(i);
            }
        });

    }

    private void addTodayView() {
        View todayView=View.inflate(getContext(),R.layout.item_know_today,null);
        knowlistView.addHeaderView(todayView);

		View every = todayView.findViewById(R.id.every);
		View xingzuo = todayView.findViewById(R.id.xingzuo);

		every.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i=new Intent(getActivity(), WebActivity.class);
				String url="http://huaban.com/boards/16034078/";
				i.putExtra(EXTRA_WEB,url);
				startActivity(i);
			}
		});
		xingzuo.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i=new Intent(getActivity(), WebActivity.class);
				String url="https://www.huabaike.com/hyjk/1674.html";
				i.putExtra(EXTRA_WEB,url);
				startActivity(i);
			}
		});
    }

    public String getData() throws UnsupportedEncodingException {
        InputStream in = getResources().openRawResource(R.raw.api);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] array = new byte[1024];
        int len = 0;
        try {
            while ((len = in.read(array))!= -1){
                bos.write(array,0,len);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        array= bos.toByteArray();
        String jsonString = new String(array,"utf-8");
       return  jsonString ;

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        try {
            KnowBean knowBean=new KnowBean();
            NewKnowJson knowJson=new NewKnowJson();
            knowBean=knowJson.knowPull(getData());
//            Toast.makeText(getContext(), "的item被点击了！，点击的位置是-->" + position+knowBean.getListKnowBean().get(position-2).getId(), Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getActivity(), WebActivity.class);
            String url=knowBean.getListKnowBean().get(position-2).getUrl();   //闪退
            i.putExtra(EXTRA_WEB,url);
            startActivity(i);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("position", String.valueOf(position));
        Log.e("id", String.valueOf(id));
        Log.e("View", String.valueOf(view));

    }

    @Override
    public void click(View v) throws UnsupportedEncodingException, JSONException {
        KnowBean knowBean=new KnowBean();
        NewKnowJson knowJson=new NewKnowJson();
        knowBean=knowJson.knowPull(getData());
        Intent i=new Intent(getActivity(), WebActivity.class);
        String url=knowBean.getListKnowBean().get((Integer) v.getTag()).getUrl();

		Log.e("url",url);

        i.putExtra(EXTRA_WEB,url);
        startActivity(i);
//        Toast.makeText(getContext(), "listview的内部的按钮被点击了！，位置是-->" +  knowBean.getListKnowBean().get((Integer) v.getTag()).getId()  , Toast.LENGTH_SHORT).show();

    }
}

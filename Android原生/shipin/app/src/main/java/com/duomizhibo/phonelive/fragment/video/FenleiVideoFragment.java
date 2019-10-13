package com.duomizhibo.phonelive.fragment.video;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.base.AbsFragment;
import com.duomizhibo.phonelive.ui.activity.NewMainActivity;
import com.duomizhibo.phonelive.widget.TCHorizontalScrollView;

import java.util.ArrayList;


public class FenleiVideoFragment extends AbsFragment  {

    private View mRootView;

    private TCHorizontalScrollView hs_fenlei;
    private ArrayAdapter<String> fenleiAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.new_fragment_fenlei_video, container, false);
        return mRootView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        hs_fenlei = (TCHorizontalScrollView) mRootView.findViewById(R.id.hs_fenlei);
        ArrayList<String> fenleiStrings=new ArrayList<String>();
        for(int i=0;i<15;i++){
            fenleiStrings.add("简介"+i);
        }

//        fenleiAdapter=new ArrayAdapter<String>(getActivity(),0,fenleiStrings){
//            @NonNull
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                if (convertView == null) {
//                    LayoutInflater inflater = LayoutInflater.from(getContext());
//                    convertView = inflater.inflate(R.layout.filter_layout,null);
//                }
//                return super.getView(position, convertView, parent);
//            }
//        };

    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }


}

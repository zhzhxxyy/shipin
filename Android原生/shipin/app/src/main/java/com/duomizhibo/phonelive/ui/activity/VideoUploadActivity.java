package com.duomizhibo.phonelive.ui.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.video.VideoTagAdapter;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.video.BaseData;
import com.duomizhibo.phonelive.bean.video.UploadParam;
import com.duomizhibo.phonelive.bean.video.VideoClass;
import com.duomizhibo.phonelive.bean.video.VideoClassList;
import com.duomizhibo.phonelive.ui.customviews.ActivityTitle;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.Uri2PathUtil;
import com.duomizhibo.phonelive.widget.nicespinner.NiceSpinner;
import com.nanchen.compresshelper.CompressHelper;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.InjectView;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Call;


public class VideoUploadActivity extends ToolBarBaseActivity {

    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;
    @InjectView(R.id.et_title)
    EditText et_title;
    @InjectView(R.id.et_money)
    EditText et_money;
    @InjectView(R.id.et_video_path)
    EditText et_video_path;
    @InjectView(R.id.tv_video_upload)
    TextView tv_video_upload;
    @InjectView(R.id.et_pic_path)
    EditText et_pic_path;
    @InjectView(R.id.tv_pic_upload)
    TextView tv_pic_upload;
    @InjectView(R.id.iv_pic)
    ImageView iv_pic;
    @InjectView(R.id.et_time)
    EditText et_time;
    @InjectView(R.id.et_actor)
    EditText et_actor;
    @InjectView(R.id.tv_submit)
    View tv_submit;
    @InjectView(R.id.nice_spinner)
    NiceSpinner nice_spinner;

    @InjectView(R.id.gv_tag)
    GridView gv_tag;

    private  List<String> spinnerData=new LinkedList<String>();
    private  VideoTagAdapter videoTagAdapter;
    private  List<VideoClass> tagList=new ArrayList<VideoClass>();
    private  VideoClassList videoClassList=null;
    private long clickTime=0;//点击时间
    private int minDuringClickTime=500;//最短的点击时间 毫秒
    @Override
    protected int getLayoutId() {
        return R.layout.new_activity_video_upload;
    }

    @Override
    public void initView() {
        mActivityTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nice_spinner.attachDataSource(spinnerData);
        nice_spinner.setBackgroundResource(R.drawable.textview_round_border);
        nice_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        videoTagAdapter=new VideoTagAdapter();
        videoTagAdapter.addItem(tagList);
        gv_tag.setAdapter(videoTagAdapter);

        tv_video_upload.setOnClickListener(this);
        tv_pic_upload.setOnClickListener(this);
        tv_submit.setOnClickListener(this);


    }

    @Override
    public void initData() {
        String tx= AppConfig.getAppConfig(this).get("Video.class");
        if(!StringUtils.isEmpty(tx)){
            try {
                VideoClassList temp=com.alibaba.fastjson.JSONObject.parseObject(tx, VideoClassList.class);
                if(temp!=null){
                    videoClassToView(temp);
                }
            } catch (Exception e) {
            }
        }
        getVideoClassData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_video_upload:
                //选择视频上传
                choiceVideoPre();
                break;
            case R.id.tv_pic_upload:
                //选择图片上传
                choicePicPre();
                break;
            case R.id.tv_submit:
                //提交
                addVideo();
                break;
        }


    }


    @Override
    protected boolean hasActionBar() {
        return false;
    }


    //获取分类的数据
    private void getVideoClassData(){
        PhoneLiveApi.videoClass(AppContext.getInstance().getToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                BaseData baseData= ApiUtils.getBaseData(response);
                VideoClassList temp=null;
                if(baseData.isSuccess()){
                    try {
                        temp=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), VideoClassList.class);
                    } catch (Exception e) {

                    }
                }
                if(temp==null){
                    return;
                }
                AppConfig.getAppConfig(VideoUploadActivity.this).set("Video.class",baseData.getData());
                videoClassToView(temp);
            }
        });
    }


    private void videoClassToView(VideoClassList temp){
        videoClassList=temp;
        spinnerData.clear();
        if(videoClassList.getClass_list()!=null&&videoClassList.getClass_list().size()>0){
            for(int i=0;i<videoClassList.getClass_list().size();i++){
                spinnerData.add(videoClassList.getClass_list().get(i).getName());
            }
        }
        nice_spinner.attachDataSource(spinnerData);

        tagList.clear();
        videoTagAdapter.clear();
        if(videoClassList.getTag_list()!=null&&videoClassList.getTag_list().size()>0){
            tagList.addAll(videoClassList.getTag_list());
        }
        videoTagAdapter.addItem(tagList);
        videoTagAdapter.notifyDataSetChanged();
    }

    //提交视频
    private void addVideo(){
        if(Math.abs(System.currentTimeMillis()-clickTime)<minDuringClickTime){
            return;
        }
        clickTime=System.currentTimeMillis();
        String classId="0";
        String className= nice_spinner.getText().toString();
        if(StringUtils.isEmpty(className)){
            AppContext.showToast("请选择分类!");
            if(videoClassList==null){
                getVideoClassData();
            }
            return;
        }
        List<VideoClass> class_list= videoClassList.getClass_list();
        if(class_list!=null){
            for(int i=0;i<class_list.size();i++){
                if(className.equals(class_list.get(i).getName())){
                    classId=class_list.get(i).getId()+"";
                    break;
                }
            }
        }
        String tag="";
        for(int j=0;j<tagList.size();j++){
           if(tagList.get(j).isCheck()){
               if(!StringUtils.isEmpty(tag)){
                   tag=tag+",";
               }
               tag=tag+tagList.get(j).getId()+"";
           }
        }
        String title=et_title.getText().toString().trim();
        String gold=et_money.getText().toString().trim();
        String videoPath=et_video_path.getText().toString().trim();
        String picPath=et_pic_path.getText().toString().trim();
        String play_time=et_time.getText().toString().trim();
        String actor=et_actor.getText().toString().trim();

        if(StringUtils.isEmpty(title)){
            AppContext.showToast("标题不能为空!");
            return;
        }
        if(StringUtils.isEmpty(videoPath)){
            AppContext.showToast("视频地址不为空!");
            return;
        }

        if(StringUtils.isEmpty(picPath)){
            AppContext.showToast("缩略图不能为空!");
            return;
        }

        showWaitDialog("提交中... ",false);
        PhoneLiveApi.addVideo(title,gold,tag,classId,videoPath,picPath,play_time,actor, AppContext.getInstance().getToken(), new StringCallback(){
            @Override
            public void onError(Call call, Exception e, int id) {
                AppContext.showToast("添加视频失败：网络请求出错!");
                hideWaitDialog();
            }

            @Override
            public void onResponse(String s, int id) {
                hideWaitDialog();
                BaseData baseData= ApiUtils.getBaseData(s);
                if(baseData.isSuccess()){
                    AppContext.showToast("添加视频成功!");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1000);
                }else{
                    AppContext.showToast(baseData.getRespMsg());
                }
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==REQUESTCODE_VIDEO){
               handleVideo(data);
            }else if(requestCode==REQUESTCODE_IMAGE){
               handleImage(data);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


    /*--------------上次文件---------------------*/
    private void uploadFile(String path,final TextView tv,final EditText et){
        UploadParam uploadParam=new UploadParam();
        uploadParam.setPost_url("http://spf.zhzhxxyy.com/Xuploader.php");
        uploadParam.setServerType("web_server");
        AppContext.showToast("上传开始");
        tv.setClickable(false);
        PhoneLiveApi.uploadFile(path, uploadParam, AppContext.getInstance().getToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                tv.setText("上传");
                tv.setClickable(true);
                AppContext.showToast("上传失败");
            }

            @Override
            public void onResponse(String response, int id) {
                tv.setText("上传");
                tv.setClickable(true);
                BaseData baseData= ApiUtils.getBaseData(response);
                if(baseData.isSuccess()){
                    et.setText(baseData.getData());
                    AppContext.showToast("上传成功");
                }else{
                    AppContext.showToast(baseData.getRespMsg());
                }
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                int j=(int)(progress*100);
                tv.setText(j+"%");
            }
        });

    }

   /*---------------选择视频----------------*/
    public static final int REQUESTCODE_VIDEO=1;
    private void choiceVideoPre(){
        PermissionGen.with(this)
                .addRequestCode(200)
                .permissions(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.RECORD_AUDIO
                ).request();
    }

    @PermissionSuccess(requestCode = 200)
    private void choiceVideo(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUESTCODE_VIDEO);
    }

    @PermissionFail(requestCode = 200)
    private void choiceVideoPermissionFail() {
        AppContext.showToast("请允许读取视频文件");
    }

    private void handleVideo(Intent data){
        Uri uri = data.getData();
        String path= Uri2PathUtil.getRealPathFromUri(this,uri);
        if(path!=null){
            uploadFile(path,tv_video_upload,et_video_path);
        }
    }


    /*---------------选择图片----------------*/
    public static final int REQUESTCODE_IMAGE=2;
    private void choicePicPre(){
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA
                ).request();

    }

    @PermissionSuccess(requestCode = 100)
    private void choicePic(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUESTCODE_IMAGE);
    }
    @PermissionFail(requestCode = 100)
    private void choicePicPermissionFail() {
       AppContext.showToast("请允许读取图片文件");
    }

    private void handleImage(Intent data){
        Uri uri = data.getData();
        String path= Uri2PathUtil.getRealPathFromUri(this,uri);
        if(path!=null){
            File oldFile=new File(path);
            File newFile=CompressHelper.getDefault(this).compressToFile(oldFile);
            iv_pic.setImageBitmap(BitmapFactory.decodeFile(newFile.getAbsolutePath()));
            uploadFile(newFile.getAbsolutePath(),tv_pic_upload,et_pic_path);
        }
    }




}

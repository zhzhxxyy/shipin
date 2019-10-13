package com.duomizhibo.phonelive.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.utils.ImageUtils;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.utils.FileUtil;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.widget.LoadUrlImageView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 头像上传
 */
public class UserSelectAvatarActivity extends ToolBarBaseActivity {
    @InjectView(R.id.av_edit_head)
    LoadUrlImageView mUHead;
    private final static int CROP = 400;

    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/PhoneLive/Portrait/";
    private Uri origUri;
    private Uri cropUri;
    private File protraitFile;
    private Bitmap protraitBitmap;
    private String protraitPath;
    private String theLarge;
    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        mUHead.setImageLoadUrl(getIntent().getStringExtra("uhead"));
    }
    @OnClick({R.id.iv_select_avatar_back,R.id.btn_avator_from_album,R.id.btn_avator_photograph})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_select_avatar_back:
                finish();
                break;
            case R.id.btn_avator_from_album://相册
                //拍照点击拍照无反应 20160906 wp
                if(android.os.Build.VERSION.SDK_INT >= 23) {
                    //摄像头权限检测
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //进行权限请求
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},
                                5);
                        return;
                    }else{
                        startImagePick();
                    }
                }else{
                    startImagePick();
                }
                break;
            case R.id.btn_avator_photograph:

                //拍照点击拍照无反应 20160906 wp
                if(android.os.Build.VERSION.SDK_INT >= 23) {
                    //摄像头权限检测
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //进行权限请求
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},
                                5);
                        return;
                    }else{
                        startTakePhoto();
                    }
                }else{
                    startTakePhoto();
                }


                break;
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 5: {
                // 判断权限请求是否通过
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    startTakePhoto();
                    return;
                }
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast3("您已拒绝使用摄像头权限,无法使用摄像头拍照,请去设置中修改",0);
                }else if(grantResults.length > 0 && grantResults[1] != PackageManager.PERMISSION_GRANTED){
                    showToast3("您拒绝读取文件权限,无法上传图片,请到设置中修改",0);
                }
                return;
            }
        }
    }
    /**
     * 选择图片裁剪
     */
    private void startImagePick() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"),
                    ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP);
        }
    }
    // 裁剪头像的绝对路径
    private Uri getUploadTempFile(Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(FILE_SAVEPATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            AppContext.showToastAppMsg(this, "无法保存上传的头像，请检查SD卡是否挂载");
            return null;
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath(this, uri);
        }
        String ext = FileUtil.getFileFormat(thePath);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;
        // 照片命名
        String cropFileName = "phonelive_crop_" + timeStamp + "." + ext;
        // 裁剪头像的绝对路径
        protraitPath = FILE_SAVEPATH + cropFileName;
        protraitFile = new File(protraitPath);

        cropUri = Uri.fromFile(protraitFile);
        return this.cropUri;
    }


    private void startTakePhoto() {
        Intent intent;
        // 判断是否挂载了SD卡
        String savePath = "";
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            savePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/phonelive/Camera/";
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        }

        // 没有挂载SD卡，无法保存文件
        if (StringUtils.isEmpty(savePath)) {
            AppContext.showToastShort("无法保存照片，请检查SD卡是否挂载");
            return;
        }

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String fileName = "phonelive_" + timeStamp + ".jpg";// 照片命名
        File out = new File(savePath, fileName);
        Uri uri = Uri.fromFile(out);
        origUri = uri;

        theLarge = savePath + fileName;// 该照片的绝对路径

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA);
    }
    /**
     * 拍照后裁剪
     *
     * @param data 原始图片
     */
    private void startActionCrop(Uri data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/*");
        intent.putExtra("output",this.getUploadTempFile(data));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", CROP);// 输出图片大小
        intent.putExtra("outputY", CROP);
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        startActivityForResult(intent,
                ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnIntent) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCAMERA:
                startActionCrop(origUri);// 拍照后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYCROP:
                startActionCrop(imageReturnIntent.getData());// 选图后裁剪
                break;
            case ImageUtils.REQUEST_CODE_GETIMAGE_BYSDCARD:
                uploadNewPhoto();
                break;
        }
    }
    /**
     * 上传新照片
     */
    private void uploadNewPhoto() {
        showWaitDialog("正在上传头像...",false);

        // 获取头像缩略图
        if (!StringUtils.isEmpty(protraitPath) && protraitFile.exists()) {
            protraitBitmap = ImageUtils
                    .loadImgThumbnail(protraitPath, 200, 200);
        } else {
            AppContext.showToastAppMsg(this, "图像不存在，上传失败");
        }
        if (protraitBitmap != null) {

            try {
                PhoneLiveApi.updatePortrait(getUserID(),getUserToken(),protraitFile,
                        new StringCallback(){

                            @Override
                            public void onError(Call call, Exception e,int id) {
                                AppContext.showToastAppMsg(UserSelectAvatarActivity.this,"上传头像失败");
                                hideWaitDialog();
                            }

                            @Override
                            public void onResponse(String response,int id) {
                                final JSONArray res = ApiUtils.checkIsSuccess(response);
                                if(null != res){
                                    AppContext.showToastAppMsg(UserSelectAvatarActivity.this, "头像修改成功");

                                    UserBean u = AppContext.getInstance().getLoginUser();
                                    //0921 修改上传头像后无法更新缩略图问题
                                    try {
                                        JSONObject object = res.getJSONObject(0);
                                        u.avatar       = object.getString("avatar");
                                        u.avatar_thumb = object.getString("avatar_thumb");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    mUHead.setImageLoadUrl(u.avatar);

                                    AppContext.getInstance().updateUserInfo(u);

                                }
                                hideWaitDialog();
                            }
                        });

            } catch (Exception e) {
                AppContext.showToast("图像不存在，上传失败");
            }
        }

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_head;
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }
}

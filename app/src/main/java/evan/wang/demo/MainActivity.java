package evan.wang.demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import evan.wang.R;
import evan.wang.utils.FileUtil;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    private RelativeLayout qqLayout;
    private RelativeLayout weixinLayout;
    //头像1
    private CircleImageView headImage1;
    //头像2
    private ImageView headImage2;
    //调用照相机返回图片临时文件
    private File tempFile;
    // 1: qq, 2: weixin
    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createHeadImageTempFile(savedInstanceState);
        headImage1 = (CircleImageView) findViewById(R.id.head_image1);
        headImage2 = (ImageView) findViewById(R.id.head_image2);
        qqLayout = (RelativeLayout) findViewById(R.id.qqLayout);
        weixinLayout = (RelativeLayout) findViewById(R.id.weixinLayout);
        qqLayout.setOnClickListener(this);
        weixinLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qqLayout:
                type = 1;
                uploadHeadImage();
                break;
            case R.id.weixinLayout:
                type = 2;
                uploadHeadImage();
                break;
        }
    }


    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        String[] titles = {"拍照", "从相册中选取", "取消"};
        List<Map<String, String>> datas = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            Map<String, String> itemData = new HashMap<>();
            itemData.put("name", titles[i]);
            datas.add(itemData);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, datas, R.layout.simple_text_list_item,
                new String[]{"name"}, new int[]{R.id.simple_text_name});
        DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setAdapter(adapter)
                .setGravity(Gravity.BOTTOM)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Intent intent;
                        switch (position) {
                            case 0:
                                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                                startActivityForResult(intent, REQUEST_CAPTURE);
                                break;
                            case 1:
                                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
                                break;
                        }
                    }
                })
                .create();
        dialogPlus.show();
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createHeadImageTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = FileUtil.getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    if (type == 1) {
                        headImage1.setImageBitmap(bitMap);
                    } else {
                        headImage2.setImageBitmap(bitMap);
                    }
                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......

                }
                break;
        }
    }


    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }


}

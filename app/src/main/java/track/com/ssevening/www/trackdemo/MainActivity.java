package track.com.ssevening.www.trackdemo;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {


    private TextView tv_product;

    private Handler handler = new Handler();
    private DownloadManager downloadManager;
    private DownloadCompleteReceiver downloadCompleteReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_product = (TextView) findViewById(R.id.tv_product);

        final ProductInfo productInfo = new ProductInfo();
        productInfo.channelName = "团购";
        productInfo.spm = "1234";
        productInfo.items = new ArrayList<>();
        ProductInfo.ItemInfo item = new ProductInfo.ItemInfo();
        item.id = "1";
        item.title = "苹果手机";
        item.url = "http://www.example.com";
        item.spm = "1";
        productInfo.items.add(item);

        // 控件点击和SPM打点
        tv_product.setOnClickListener(new TrackOnClickListener() {
            public Map<String, String> getTrackParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("productId", "1234567");
                return map;
            }

            public String getClickName() {
                return ControlNames.control_product_click;
            }

            @Override
            public void onClick(View v) {
                super.onClick(v);
                Intent i = new Intent(MainActivity.this, ProductActivity.class);
                startActivity(i);
            }

            @Override
            // SPM 页面名称
            public String getPage() {
                return getPageName();
            }

            @Override
            // SPM C字段
            public String getSpmCString() {
                return productInfo.spm;
            }

            @Override
            // SPM D字段
            public String getSpmDString() {
                return productInfo.items.get(0).spm;
            }

        });

        // 这种是不推荐的打点方式，因为每个打点都要自己去实现，开发者关心了打点
//        tv_product.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("productId", "1234567");
//                MyApp.getApp().trackClick(ControlNames.control_product_click, map);
//                Intent i = new Intent(MainActivity.this, ProductActivity.class);
//                startActivity(i);
//            }
//        });

        if (TrackNamesCheck.checkIfHaveMutiPageNames(PageNames.class)) {
            Toast.makeText(this, "有重复的页面名称，请检查！！！", Toast.LENGTH_LONG).show();
            // 重复的页面名称，属于错误，打自定义打点
            MyApp.trackEvent("MutiPageName");
            finish();
        }

//        这里post出去的线程也是在主线程中的。
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(100000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 1000);


        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadCompleteReceiver = new DownloadCompleteReceiver();
        registerReceiver(downloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        methodRequiresTwoPermission();


    }


    @Override
    public String getPageName() {
        return PageNames.TRACK_PAGE_HOME;
    }

    public void onProductClick(View view) {
        // 这里通过布局添加了一个点击事件
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadCompleteReceiver);
    }


    private final static int RC_STORAGE = 123;

    @AfterPermissionGranted(RC_STORAGE)
    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
            DownloadManager.Request down = new DownloadManager.Request(Uri.parse("https://ssevening.github.io/assets/weichat_qrcode.jpg"));
            //设置允许使用的网络类型，这里是移动网络和wifi都可以
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            //不显示下载界面
            down.setVisibleInDownloadsUi(false);
            //设置下载后文件存放的位置
            down.setDestinationInExternalFilesDir(MainActivity.this, Environment.DIRECTORY_DOWNLOADS, "qrCode.jpg");
            //将下载请求放入队列
            downloadManager.enqueue(down);
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "i need the storerage ",
                    RC_STORAGE, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, "returned_from_app_settings_to_activity", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d("MainActivity", "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    private void openFile(Uri uri) {
        try {
            ParcelFileDescriptor descriptor = getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor fileDescriptor = descriptor.getFileDescriptor();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
//                Log.v(TAG, " download complete! id : " + downId);
//                Toast.makeText(context, intent.getAction() + "id : " + downId, Toast.LENGTH_SHORT).show();
                DownloadManager.Query query = new DownloadManager.Query();
                //在广播中取出下载任务的id
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                query.setFilterById(id);
                Cursor c = downloadManager.query(query);
                if (c.moveToFirst()) {
                    //获取文件下载路径
                    int fileUriId = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);

                    String fileUri = c.getString(fileUriId);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        String fileName = Uri.parse(fileUri).getPath();
                        Log.e("fileName", fileName);
                        // /storage/emulated/0/Android/data/track.com.ssevening.www.trackdemo/files/Download/qrCode-4.jpg
                    } else {
                        /**Android 7.0以上的方式：请求获取写入权限，这一步报错**/
                        int fileNameId = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                        String fileName = c.getString(fileNameId);
                        Log.e("fileName", fileName);
                    }

                    // String filePath = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                    //如果文件名不为空，说明已经存在了，拿到文件名想干嘛都好
//                    if (filePath != null) {
//                        Intent installIntent = new Intent();
//                        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        installIntent.setAction(android.content.Intent.ACTION_VIEW);
//                        File downFile = new File(filePath);
//                        if (downFile.exists()) {
//                            installIntent.setDataAndType(Uri.fromFile(downFile),
//                                    "application/vnd.android.package-archive");
//                            startActivity(installIntent);
//                        }
//                    }
                }

            }
        }


    }


}

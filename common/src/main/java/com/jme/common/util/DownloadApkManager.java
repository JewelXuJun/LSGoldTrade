package com.jme.common.util;

import android.app.ProgressDialog;
import android.os.Environment;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zhangzhongqiang on 2015/9/25 0031.
 */
public class DownloadApkManager {

    public static File getFileFromServer(String path, ProgressDialog pd ,String verUrl) throws Exception {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            float all = (float) (conn.getContentLength() / (1024.00 * 1024.00));
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), verUrl.substring(verUrl.lastIndexOf("/") + 1));
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                float percent = (float) (total / (1024.00 * 1024.00));
                pd.setProgress(total);
                pd.setProgressNumberFormat(String.format("%.2fM/%.2fM", percent, all));
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

}

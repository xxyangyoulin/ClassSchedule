package com.mnnyang.gzuclassschedule.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 文件处理工具类<br>
 * 需要init<br>
 * Created by mnnyang on 17-4-8.
 */

public class FileUtils {

    private FileUtils() {
    }

    public static boolean saveFile(String path, String name, String content) {
        File dir = new File(path);
        if (!dir.isDirectory() || dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, name);

        BufferedWriter bufferWriter = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter= new FileWriter(file, true);
            bufferWriter= new BufferedWriter(fileWriter);
            bufferWriter.write(content);
            bufferWriter.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferWriter!=null){
                try {
                    bufferWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 获取硬盘缓存文件夹
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 字符串转换 MD5<br>
     * 图片URL中可能包含一些特殊字符，这些字符有可能在命名文件时是不合法的<br>
     * 调用一下hashKeyForDisk()方法，并把图片的URL传入到这个方法中，就可以得到对应的key了
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 创建文件夹
     */
    public static File createDir(String path) {
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 根据路径删除指定的目录或文件
     */
    public static boolean deleteFileOrDir(String filePath) {
        if (isSpace(filePath)) {
            return false;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
                // 为目录时调用删除目录方法
                return deleteDir(filePath);
            }
        }
    }

    /**
     * 删除文件夹及文件夹下的文件
     */
    public static boolean deleteDir(String dirPath) {
        boolean flag = false;
        // 如果dirPath不以文件分隔符结尾，自动添加文件分隔符
        if (!dirPath.endsWith(File.separator)) {
            dirPath = dirPath + File.separator;
        }
        File dirFile = new File(dirPath);

        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }

        flag = true;
        File[] files = dirFile.listFiles();
        // 遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                // 删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else {
                // 删除子目录
                flag = deleteDir(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前空目录
        return dirFile.delete();
    }

    /**
     * 删除指定地址的文件
     */
    public static boolean deleteFile(String filePath) {
        if (isSpace(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return file.isFile() && file.exists() && file.delete();
    }


    /**
     * 判断字符串trim空格后 是否为空
     */
    public static boolean isSpace(String str) {
        if (str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static File getSplashDir(Context context) {
        String splashPath = context.getFilesDir() + "/splash";
        return createDir(splashPath);
    }

    public static String getFilePathFromContentUri(Uri selectedVideoUri, ContentResolver contentResolver) {
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
//      也可用下面的方法拿到cursor
//      Cursor cursor = this.context.managedQuery(selectedVideoUri, filePathColumn, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

}

package me.sunhapper.spcharedittool.util;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import net.lingala.zip4j.core.ZipFile;

/**
 * Created by sunhapper on 2018/2/3.
 */

public class FileUtil {
  public static final String EMOJI="emoji";
  public static File getEmojiDir(Context context){
    File f = new File(context.getFilesDir(),EMOJI);
    if (!f.exists()) {
      f.mkdirs();
    }
    return f;
  }


  public static boolean unzipFromAssets(Context context,File dir, String name) {
    File path = new File(dir, name);

    File tmp = new File(dir, "android_tmp");
    File zip = new File(dir, name + ".zip");
    try {
      InputStream is = context.getAssets().open(name + ".zip");
      FileUtil.inputStreamToFile(is, zip);
      is.close();
      FileUtil.unzipAllFile(zip, tmp.getAbsolutePath());
      zip.delete();
      if (path.exists()) {
        FileUtil.deleteDir(path);
      }
      path.mkdirs();
      return FileUtil.renameFile(tmp, path);
    } catch (Exception e) {
      e.printStackTrace();
      if (tmp.exists()) {
        FileUtil.deleteDir(tmp);
      }
      return false;
    }
  }


  public static boolean deleteDir(File dir) {
    if (dir.isDirectory()) {
      File[] files = dir.listFiles();
      if (files != null) {
        for (File file : files) {
          boolean success = deleteDir(file);
          if (!success) {
            return false;
          }
        }
      }
    }
    return dir.delete();
  }

  public static boolean renameFile(File oldFile, File newFile) {
    if (oldFile == null || !oldFile.exists()) {
      return false;
    }
    if (newFile == null) {
      return false;
    }
    if (oldFile.equals(newFile)) {
      return false;
    }
    if (newFile.exists() && !newFile.delete()) {
      return false;
    }
    return oldFile.renameTo(newFile);
  }

  public static void unzipAllFile(File zip, String dir) throws Exception {
    ZipFile zipFile = new ZipFile(zip);
    zipFile.setFileNameCharset("utf-8");
    zipFile.extractAll(dir);
  }

  public static void inputStreamToFile(InputStream is, File file) throws IOException {
    if (!file.exists()) {
      file.createNewFile();
    }
    FileOutputStream fileOutputStream = new FileOutputStream(file);
    inputStreamToOutputStream(is, fileOutputStream);
    fileOutputStream.close();
  }

  public static void inputStreamToOutputStream(InputStream is, OutputStream os) throws IOException {
    byte[] buffer = new byte[8 * 1024];
    int count;
    while ((count = is.read(buffer)) > 0) {
      os.write(buffer, 0, count);
    }
  }
}

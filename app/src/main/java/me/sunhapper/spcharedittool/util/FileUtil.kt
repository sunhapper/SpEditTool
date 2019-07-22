package me.sunhapper.spcharedittool.util

import android.content.Context
import net.lingala.zip4j.core.ZipFile
import java.io.*

/**
 * Created by sunhapper on 2018/2/3.
 */

class FileUtil {
    companion object {
        val EMOJI = "emoji"
        fun getEmojiDir(context: Context): File {
            val f = File(context.filesDir, EMOJI)
            if (!f.exists()) {
                f.mkdirs()
            }
            return f
        }


        fun unzipFromAssets(context: Context, dir: File, name: String): Boolean {
            val path = File(dir, name)

            val tmp = File(dir, "android_tmp")
            val zip = File(dir, "$name.zip")
            try {
                val `is` = context.assets.open("$name.zip")
                inputStreamToFile(`is`, zip)
                `is`.close()
                unzipAllFile(zip, tmp.absolutePath)
                zip.delete()
                if (path.exists()) {
                    deleteDir(path)
                }
                path.mkdirs()
                return renameFile(tmp, path)
            } catch (e: Exception) {
                e.printStackTrace()
                if (tmp.exists()) {
                    deleteDir(tmp)
                }
                return false
            }

        }


        fun deleteDir(dir: File): Boolean {
            if (dir.isDirectory) {
                val files = dir.listFiles()
                if (files != null) {
                    for (file in files) {
                        val success = deleteDir(file)
                        if (!success) {
                            return false
                        }
                    }
                }
            }
            return dir.delete()
        }

        fun renameFile(oldFile: File?, newFile: File?): Boolean {
            if (oldFile == null || !oldFile.exists()) {
                return false
            }
            if (newFile == null) {
                return false
            }
            if (oldFile == newFile) {
                return false
            }
            return if (newFile.exists() && !newFile.delete()) {
                false
            } else oldFile.renameTo(newFile)
        }

        @Throws(Exception::class)
        fun unzipAllFile(zip: File, dir: String) {
            val zipFile = ZipFile(zip)
            zipFile.setFileNameCharset("utf-8")
            zipFile.extractAll(dir)
        }

        @Throws(IOException::class)
        fun inputStreamToFile(inputStream: InputStream, file: File) {
            if (!file.exists()) {
                file.createNewFile()
            }
            val fileOutputStream = FileOutputStream(file)
            inputStreamToOutputStream(inputStream, fileOutputStream)
            fileOutputStream.close()
        }

        @Throws(IOException::class)
        fun inputStreamToOutputStream(inputStream: InputStream, outputStream: OutputStream) {
            inputStream.copyTo(outputStream)
        }
    }

}

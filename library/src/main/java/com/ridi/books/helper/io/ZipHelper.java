package com.ridi.books.helper.io;

import com.ridi.books.helper.Log;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.MalformedInputException;

public final class ZipHelper {
    private static final int BUFFER_SIZE                = 8192;
    private static final int PROGRESS_UPDATE_STEP_SIZE  = BUFFER_SIZE / 16;

    public interface Listener {
        void onProgress(long unzippedBytes);
    }

    private ZipHelper() {
    }

    public static boolean unzip(File zipFile, File destDir, boolean deleteZip) {
        return unzip(zipFile, destDir, deleteZip, null);
    }
	
	public static boolean unzip(File zipFile, File destDir,
                                boolean deleteZip, Listener listener) {
        if (!zipFile.exists()) {
            return false;
        }
        try {
            return unzip(new BufferedInputStream(new FileInputStream(zipFile)), destDir, listener);
        } catch (Exception e) {
            Log.e(ZipHelper.class, "error while unzip", e);
            return false;
        } finally {
            if (deleteZip) {
                zipFile.delete();
            }
        }
    }

    public static boolean unzip(InputStream inputStream, File destDir) {
        return unzip(inputStream, destDir, null);
    }

    public static boolean unzip(InputStream inputStream, File destDir, Listener listener) {
        destDir.mkdirs();

        try {
            ZipArchiveInputStream in = new ZipArchiveInputStream(inputStream, "UTF8", true, true);
            ZipArchiveEntry entry;

            while (true) {
                try {
                    entry = in.getNextZipEntry();
                } catch (MalformedInputException e) {
                    Log.e(ZipHelper.class, "Filename encoding error", e);
                    // 파일명에서 인코딩 오류 발생 시에, 해당 파일 무시
                    continue;
                }

                if (entry == null) {
                    break;
                }
                if (entry.isDirectory()) {
                    continue;
                }

                String path = new File(destDir, entry.getName()).getPath();
                if (path.lastIndexOf('/') != -1) {
                    File d = new File(path.substring(0, path.lastIndexOf('/')));
                    d.mkdirs();
                }

                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path));
                byte[] buf = new byte[BUFFER_SIZE];
                int readSize;
                long prevBytesRead = in.getBytesRead();
                while ((readSize = in.read(buf)) > 0) {
                    out.write(buf, 0, readSize);
                    if (listener != null) {
                        long bytesRead = in.getBytesRead() - prevBytesRead;
                        if ((bytesRead / BUFFER_SIZE) % PROGRESS_UPDATE_STEP_SIZE == 0) {
                            listener.onProgress(bytesRead + prevBytesRead);
                        }
                    }
                }
                out.close();
            }
            in.close();

            return true;
        } catch (Exception e) {
            Log.e(ZipHelper.class, "error while unzip", e);
            return false;
        }
    }
}

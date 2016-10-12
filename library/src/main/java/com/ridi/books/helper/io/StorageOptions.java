package com.ridi.books.helper.io;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.ridi.books.helper.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public final class StorageOptions {
    private StorageOptions() {
    }

    public static List<String> getExternalSDCardPaths(Context context, boolean showRootPath) {
        List<String> mounts = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            File[] externalDirs = context.getExternalFilesDirs(null);
            File internalDir = context.getExternalFilesDir(null);
            for (File externalDir : externalDirs) {
                // externalDir이 null이거나 내장 SD 카드일 때는 무시
                if (externalDir == null || externalDir.equals(internalDir)) {
                    continue;
                }

                if (showRootPath) {
                    // 외장 SD 카드 내부 경로 -> /Android/data/com.initialcoms.ridi/files/
                    externalDir = externalDir.getParentFile().getParentFile().getParentFile().getParentFile();
                }

                mounts.add(externalDir.getPath());
            }

            if (!mounts.isEmpty()) {
                return mounts;
            }
        }

        mounts.addAll(readVoldsFromMountsFile());
        if (!mounts.isEmpty()) {
            compareMountsWithVold(mounts, readVoldFile());
        }
        if (mounts.isEmpty()) {
            mounts.addAll(readFuseFromMountsFile());
        }
        testAndCleanMountsList(mounts);

        // 내장 SD 카드는 마운트된 항목에서 제거한다
        Iterator<String> it = mounts.iterator();
        while (it.hasNext()) {
            String path = it.next();
            if (path.equalsIgnoreCase(Environment.getExternalStorageDirectory().getPath())) {
                it.remove();
            }
        }
        
        return mounts;
    }
    
    private static List<String> readVoldsFromMountsFile() {
		/*
		 * /proc/mounts 파일을 스캔하여, 아래와 같은 줄이 있는지 찾는다:
		 * /dev/block/vold/179:1 /mnt/sdcard vfat rw,dirsync,nosuid,nodev,noexec,relatime,uid=1000...
		 */
        Scanner scanner = null;
        try {
            List<String> mounts = new ArrayList<>();
            scanner = new Scanner(new File("/proc/mounts"));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.startsWith("/dev/block/vold/")) {
                    line = line.replaceAll("\\s+", " ");
                    String[] lineElements = line.split(" ");
                    // lineElements[1] 예) /mnt/media_rw/external_SD
                    mounts.add(lineElements[1]);
                }
            }
            return mounts;
        } catch (Exception e) {
            Log.e(StorageOptions.class, e);
            return Collections.emptyList();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
    
    private static List<String> readVoldFile() {
		/*
		 * /system/etc/vold.fstab 파일을 스캔하여, 아래와 같은 줄이 있는지 찾는다:
		 * dev_mount sdcard /mnt/sdcard 1 ...
		 */
        Scanner scanner = null;
        try {
            List<String> volds = new ArrayList<>();
            scanner = new Scanner(new File("/system/etc/vold.fstab"));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.startsWith("dev_mount")) {
                    line = line.replaceAll("\\s+", " ");
                    String[] lineElements = line.split(" ");
                    volds.add(lineElements[2]);
                }
            }
            return volds;
        } catch (Exception e) {
            Log.e(StorageOptions.class, e);
            return Collections.emptyList();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static List<String> readFuseFromMountsFile() {
		/*
		 * 최후의 수단 :
		 * /proc/mounts 파일을 스캔하여, /dev/fuse 로 시작하는 줄 중에 특정 형태의 경로를 찾는다.
		 */
        Scanner scanner = null;
        try {
            List<String> mounts = new ArrayList<>();
            scanner = new Scanner(new File("/proc/mounts"));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (line.startsWith("/dev/fuse")) {
                    line = line.replaceAll("\\s+", " ");
                    String[] lineElements = line.split(" ");
                    if (lineElements[1].startsWith("/storage/sdcard")
                            || lineElements[1].startsWith("/storage/external_")) {
                        mounts.add(lineElements[1]);
                    }
                }
            }
            return mounts;
        } catch (Exception e) {
            Log.e(StorageOptions.class, e);
            return Collections.emptyList();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private static void compareMountsWithVold(List<String> mounts, List<String> volds) {
        // Mounts 와 vold.fstab에서 서로 다른건 제거
        for (int i = 0; i < mounts.size(); i++) {
            String mount = mounts.get(i);
            if (!volds.contains(mount))
                mounts.remove(i--);
        }

        volds.clear();
    }
    
    private static void testAndCleanMountsList(List<String> mounts) {
		// Mount paths 의 유효성 테스트.
        Iterator<String> it = mounts.iterator();
        while (it.hasNext()) {
            File file = new File(it.next());
            if (!file.exists() || !file.isDirectory() || !file.canWrite()) {
                it.remove();
            }
        }
    }
}

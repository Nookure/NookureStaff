package com.nookure.staff.api.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarUtil {
    private static final Logger logger = Logger.getLogger(JarUtil.class.getName());
    public static final char JAR_SEPARATOR = '/';

    public static void copyFolderFromJar(String folderName, File destFolder, CopyOption option) throws IOException {
        copyFolderFromJar(folderName, destFolder, option, null);
    }

    public static void copyFolderFromJar(String folderName, File destFolder, CopyOption option, PathTrimmer trimmer)
            throws IOException {
        if (!destFolder.exists()) destFolder.mkdirs();

        byte[] buffer = new byte[1024];

        File fullPath = null;
        String path = JarUtil.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath();
        if (trimmer != null) path = trimmer.trim(path);
        try {
            if (!path.startsWith("file")) path = "file://" + path;

            fullPath = new File(new URI(path));
        } catch (URISyntaxException e) {
            logger.severe("Failed to get the path of the jar file");
        }
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fullPath));

        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            if (!entry.getName().startsWith(folderName + JAR_SEPARATOR)) continue;

            String fileName = entry.getName();

            if (fileName.charAt(fileName.length() - 1) == JAR_SEPARATOR) {
                File file = new File(destFolder + File.separator + fileName);
                if (file.isFile()) {
                    file.delete();
                }
                file.mkdirs();
                continue;
            }

            File file = new File(destFolder + File.separator + fileName);
            if (option == CopyOption.COPY_IF_NOT_EXIST && file.exists()) continue;

            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();

            if (!file.exists()) file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);

            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
        }

        zis.closeEntry();
        zis.close();
    }

    public enum CopyOption {
        COPY_IF_NOT_EXIST,
        REPLACE_IF_EXIST
    }

    @FunctionalInterface
    public interface PathTrimmer {
        String trim(String original);
    }
}

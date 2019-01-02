package com.ahmetkilic.ophelia.ea_utilities.tools;

import android.os.Environment;

import java.io.File;
import java.util.Date;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class FileUtils {

    /**
     * Get file name without extension from file path
     *
     * @param filePath file path string
     */
    public static String getNameFromPath(String filePath) {
        String name = "";
        if (filePath != null && !filePath.equals("")) {
            if (filePath.contains("/") && filePath.length() > filePath.lastIndexOf("/") + 1) {
                name = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
                if (name.contains(".") && name.length() > 1 && name.charAt(0) != '.') {
                    name = name.substring(0, name.lastIndexOf("."));
                }
            }
        }
        return name;
    }

    /**
     * Get file extension from file path
     *
     * @param filePath file path string
     */
    public static String getExtensionFromPath(String filePath) {
        String extension = "";
        if (filePath != null && filePath.length() > 0 && filePath.contains("."))
            extension = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
        return extension;
    }

    /**
     * Get file name with extension from file path
     *
     * @param filePath file path string
     */
    public static String getNameWithExtensionFromPath(String filePath) {
        String name = "";
        if (filePath != null && !filePath.equals("")) {
            if (filePath.contains("/") && filePath.length() > filePath.lastIndexOf("/") + 1) {
                name = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
            }
        }

        return name;
    }

    /**
     * Generate a file name to save. If modified date is not null, date's time appends to the name.
     *
     * @param filePath file path string
     */
    public static String getFileNameToSave(String filePath, Date dateModified) {
        return FileUtils.getNameFromPath(filePath) + getTimeFromDate(dateModified) + "." + FileUtils.getExtensionFromPath(filePath);
    }

    /**
     * Get date value to add to the name
     *
     * @param dateModified date
     * @return date value
     */
    public static String getTimeFromDate(Date dateModified) {
        String date = "";
        if (dateModified != null) {
            date = String.valueOf(dateModified.getTime());
        }
        return date;
    }

    /**
     * Delete a file. if it is a directory, delete all children and directory.
     *
     * @param dir file to delete
     */
    public static boolean deleteFileOrFolder(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteFileOrFolder(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    /**
     * Check if file is exists in device
     *
     * @param filePath file path string
     */
    public static boolean isFileExistsInDevice(String filePath) {
        if (filePath != null && filePath.length() > 0) {
            File file = new File(filePath);
            return file.isFile();
        } else
            return false;
    }

    /**
     * Get file from device if file is exists.Else, returns null
     *
     * @param filePath file path string
     */
    public static File getFileIfExists(String filePath) {
        if (filePath != null && filePath.length() > 0) {
            File file = new File(filePath);
            if (file.exists())
                return file;
            else
                return null;
        } else
            return null;
    }

    /**
     * Get downloads folder path
     *
     * @return path
     */
    public static String getDownloadsPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    }
}

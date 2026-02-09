package com.joseluu.tareafinal.manager;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.joseluu.tareafinal.model.ArchivoAdjunto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Helper class for managing file storage
 * Handles saving and deleting files based on SD preference
 */
public class FileStorageHelper {

    private static final String TAG = "FileStorageHelper";
    private static final String ATTACHMENTS_DIR = "task_attachments";

    /**
     * Get storage directory based on SD preference
     * Uses PreferenciasHelper.getRutaAlmacenamiento()
     * 
     * @param context Application context
     * @return Directory for storing attachments
     */
    public static File getStorageDirectory(Context context) {
        String basePath = PreferenciasHelper.getRutaAlmacenamiento(context);
        File dir = new File(basePath, ATTACHMENTS_DIR);

        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                Log.e(TAG, "Failed to create attachments directory: " + dir.getAbsolutePath());
            }
        }

        return dir;
    }

    /**
     * Save file from URI to app storage
     * 
     * @param context  Application context
     * @param fileUri  URI of the file to save
     * @param fileName Desired file name
     * @return Absolute path to saved file, or null on error
     */
    public static String saveFileToStorage(Context context, Uri fileUri, String fileName) {
        try {
            File storageDir = getStorageDirectory(context);
            File destinationFile = new File(storageDir, fileName);

            // If file with same name exists, add timestamp
            if (destinationFile.exists()) {
                String timestamp = String.valueOf(System.currentTimeMillis());
                String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf('.'));
                String ext = fileName.substring(fileName.lastIndexOf('.'));
                fileName = nameWithoutExt + "_" + timestamp + ext;
                destinationFile = new File(storageDir, fileName);
            }

            // Copy file from URI to destination
            InputStream inputStream = context.getContentResolver().openInputStream(fileUri);
            if (inputStream == null) {
                Log.e(TAG, "Failed to open input stream for URI: " + fileUri);
                return null;
            }

            OutputStream outputStream = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            Log.d(TAG, "File saved successfully: " + destinationFile.getAbsolutePath());
            return destinationFile.getAbsolutePath();

        } catch (Exception e) {
            Log.e(TAG, "Error saving file", e);
            return null;
        }
    }

    /**
     * Delete file from storage
     * 
     * @param filePath Absolute path to file
     * @return true if file was deleted, false otherwise
     */
    public static boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        File file = new File(filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                Log.d(TAG, "File deleted: " + filePath);
            } else {
                Log.e(TAG, "Failed to delete file: " + filePath);
            }
            return deleted;
        }

        return false;
    }

    /**
     * Delete all attachments for a task
     * 
     * @param archivos List of attachments to delete
     * @return Number of files successfully deleted
     */
    public static int deleteTaskAttachments(java.util.List<ArchivoAdjunto> archivos) {
        if (archivos == null || archivos.isEmpty()) {
            return 0;
        }

        int deletedCount = 0;
        for (ArchivoAdjunto archivo : archivos) {
            if (deleteFile(archivo.getRutaArchivo())) {
                deletedCount++;
            }
        }

        return deletedCount;
    }

    /**
     * Check if file exists at given path
     * 
     * @param filePath Absolute path to file
     * @return true if file exists
     */
    public static boolean fileExists(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        return new File(filePath).exists();
    }

    /**
     * Get file name from URI
     * 
     * @param context Application context
     * @param uri     File URI
     * @return File name, or "unknown" if unable to determine
     */
    public static String getFileName(Context context, Uri uri) {
        String fileName = "archivo_" + System.currentTimeMillis();

        try {
            android.database.Cursor cursor = context.getContentResolver().query(
                    uri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    fileName = cursor.getString(nameIndex);
                }
                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error getting file name", e);
        }

        return fileName;
    }
}

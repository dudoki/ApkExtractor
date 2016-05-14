package com.droid.app.extractor.util;

import java.io.*
import java.nio.channels.FileChannel

object FileUtils {

    /**
     * The number of bytes in a kilobyte.
     */
    internal val ONE_KB: Long = 1024

    /**
     * The number of bytes in a megabyte.
     */
    internal val ONE_MB = ONE_KB * ONE_KB

    /**
     * The number of bytes in a 50 MB.
     */
    internal val FIFTY_MB = ONE_MB * 50

    /**
     * Makes a directory, including any necessary but nonexistent parent
     * directories. If a file already exists with specified name but it is
     * not a directory then an IOException is thrown.
     * If the directory cannot be created (or does not already exist)
     * then an IOException is thrown.
     *
     * @param directory directory to create, must not be <code>null</code>
     * @throws NullPointerException if the directory is <code>null</code>
     * @throws IOException          if the directory cannot be created or the file already exists but is not a directory
     */
    fun forceMkdir(directory: File): Unit {
        if (directory.exists()) {
            if (!directory.isDirectory) {
                val message = "File $directory exists and is not a directory. Unable to create directory.";
                throw IOException(message);
            }
        } else {
            if (!directory.mkdirs()) {
                // Double-check that some other thread or process hasn't made the directory in the background
                if (!directory.isDirectory) {
                    val message = "Unable to create directory $directory";
                    throw IOException(message);
                }
            }
        }
    }

    /**
     * Copies a file to a new location preserving the file date.
     * <p>
     * This method copies the contents of the specified source file to the
     * specified destination file. The directory holding the destination file is
     * created if it does not exist. If the destination file exists, then this
     * method will overwrite it.
     * <p>
     * <strong>Note:</strong> This method tries to preserve the file's last
     * modified date/times using {@link File#setLastModified(long)}, however
     * it is not guaranteed that the operation will succeed.
     * If the modification operation fails, no indication is provided.
     *
     * @param srcFile  an existing file to copy, must not be <code>null</code>
     * @param destFile  the new file, must not be <code>null</code>
     *
     * @throws NullPointerException if source or destination is <code>null</code>
     * @throws IOException if source or destination is invalid
     * @throws IOException if an IO error occurs during copying
     * @see #copyFileToDirectory(File, File)
     */
    fun copyFile(srcFile: File, destFile: File) {
        copyFile(srcFile, destFile, true)
    }

    /**
     * Copies a file to a new location.
     * <p>
     * This method copies the contents of the specified source file
     * to the specified destination file.
     * The directory holding the destination file is created if it does not exist.
     * If the destination file exists, then this method will overwrite it.
     * <p>
     * <strong>Note:</strong> Setting <code>preserveFileDate</code> to
     * <code>true</code> tries to preserve the file's last modified
     * date/times using {@link File#setLastModified(long)}, however it is
     * not guaranteed that the operation will succeed.
     * If the modification operation fails, no indication is provided.
     *
     * @param srcFile  an existing file to copy, must not be <code>null</code>
     * @param destFile  the new file, must not be <code>null</code>
     * @param preserveFileDate  true if the file date of the copy
     *  should be the same as the original
     *
     * @throws NullPointerException if source or destination is <code>null</code>
     * @throws IOException if source or destination is invalid
     * @throws IOException if an IO error occurs during copying
     * @see #copyFileToDirectory(File, File, boolean)
     */
    fun copyFile(srcFile: File?, destFile: File?, preserveFileDate: Boolean) {
        if (srcFile == null) {
            throw NullPointerException("Source must not be null")
        }
        if (destFile == null) {
            throw NullPointerException("Destination must not be null")
        }
        if (srcFile.exists() == false) {
            throw FileNotFoundException("Source '$srcFile' does not exist")
        }
        if (srcFile.isDirectory) {
            throw IOException("Source '$srcFile' exists but is a directory")
        }
        if (srcFile.canonicalPath == destFile.canonicalPath) {
            throw IOException("Source '$srcFile' and destination '$destFile' are the same")
        }
        if (destFile.parentFile != null && destFile.parentFile.exists() == false) {
            if (destFile.parentFile.mkdirs() == false) {
                throw IOException("Destination '$destFile' directory cannot be created")
            }
        }
        if (destFile.exists() && destFile.canWrite() == false) {
            throw IOException("Destination '$destFile' exists but is read-only")
        }

        doCopyFile(srcFile, destFile, preserveFileDate)
    }

    internal fun doCopyFile(srcFile: File, destFile: File, preserveFileDate: Boolean) {
        if (destFile.exists() && destFile.isDirectory) {
            throw IOException("Destination '$destFile' exists but is a directory")
        }

        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        var input: FileChannel? = null
        var output: FileChannel? = null
        try {
            fis = FileInputStream(srcFile)
            fos = FileOutputStream(destFile)
            input = fis.channel
            output = fos.channel
            val size = input!!.size()
            var pos: Long = 0
            var count: Long = 0
            while (pos < size) {
                count = if (size - pos > FIFTY_MB) FIFTY_MB else size - pos
                pos += output!!.transferFrom(input, pos, count)
            }
        } finally {
            closeQuietly(output)
            closeQuietly(fos)
            closeQuietly(input)
            closeQuietly(fis)
        }

        if (srcFile.length() != destFile.length()) {
            throw IOException("Failed to copy full contents from '$srcFile' to '$destFile'")
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified())
        }
    }

    private fun closeQuietly(closeable: Closeable?) {
        try {
            closeable?.close()
        } catch (ioe: IOException) {
            // ignore
        }

    }
}

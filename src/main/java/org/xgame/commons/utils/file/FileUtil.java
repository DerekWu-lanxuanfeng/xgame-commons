package org.xgame.commons.utils.file;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.xgame.commons.exception.SystemException;
import sun.nio.ch.FileChannelImpl;

import java.io.*;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * @Name: FileUtil.class
 * @Description: //
 * @Create: DerekWu on 2018/9/2 16:21
 * @Version: V1.0
 */
public class FileUtil {


    /**
     * 获得package所在的目录
     * @param packageName
     * @return
     */
    public static File getPackageDirFile(String packageName) throws Exception {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        URL url = loader.getResource(packagePath);
        File file = new File(url.getPath());
        if (file == null) {
            throw new SystemException("# package " + packageName + " not exists...");
        }
        if (!file.isDirectory()) {
            throw new SystemException("# package " + packageName + " not directory...");
        }
        return file;
    }

    /**
     * 获取xml Document
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public static Document getXMLInputStream(String path) throws Exception {
        return new SAXReader().read(getFileInputStream(path));
    }

    /**
     * 获取文件的输入流
     *
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    public static InputStream getFileInputStream(String path) throws Exception {
//		String dir = Thread.currentThread().getContextClassLoader().getResource(path).toURI().getPath();
//
//		if ("/".equals(File.separator)) {
//			dir = "/" + dir;
//		}
        // System.out.println("---1---"+dir);
//		File file = new File(dir);

//		InputStream in = null;
//		if (file.exists()) {
//			in = new FileInputStream(file);
//		}
//		if (in == null) {
//			throw new FileNotFoundException();
//		}

//		return in;

        ClassPathResource one = new ClassPathResource(path);
        return one.getInputStream();
    }

    /**
     * 获取文件内容
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static String getFileText(String path) throws Exception {
        InputStream in = getFileInputStream(path);
        StringBuilder out = new StringBuilder();
        try {
            byte[] b = new byte[4096];
            for (int n; (n = in.read(b)) != -1;) {
                out.append(new String(b, 0, n));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        return out.toString();
    }

    /**
     * 获取文件内容
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String getFileText(File file) throws Exception {
        InputStream in = null;
        if (file.exists()) {
            in = new FileInputStream(file);
        }
        if (in == null) {
            throw new FileNotFoundException();
        }
        StringBuilder out = new StringBuilder();
        try {
            byte[] b = new byte[4096];
            for (int n; (n = in.read(b)) != -1;) {
                out.append( new String(b, 0, n));
            }
        } catch (Exception e) {

        } finally {
            in.close();
        }
        return out.toString();
    }

    /**
     * 写入文本
     * @param filePath
     * @param fileText
     */
    public static void writeText(String filePath, String fileText) {
        File fukeName = new File(filePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fukeName);
            byte[] bytes = fileText.getBytes("UTF-8");
            int length = bytes.length;
            fos.write(bytes, 0, length);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos!=null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得文件的md5码
     * @param file
     * @return
     * @throws Exception
     */
    public static String getFileMD5(File file) throws Exception {
        String value = null;
        FileInputStream in = new FileInputStream(file);
        FileChannel fileChannel = in.getChannel();
        try {
            MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16).toUpperCase();

            //手动释放内存
            // 加上这几行代码,手动unmap
            Method unmapMothod = FileChannelImpl.class.getDeclaredMethod("unmap", MappedByteBuffer.class);
            unmapMothod.setAccessible(true);
            unmapMothod.invoke(FileChannelImpl.class, byteBuffer);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath
     *            String 原文件路径 如：c:/fqf.txt
     * @param newPath
     *            String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) throws Exception {
        InputStream inStream = null;
        FileOutputStream fos = null;
        try {
            int byteread = 0;
            inStream = new FileInputStream(oldPath); // 读入原文件
            fos = new FileOutputStream(newPath);
            byte[] buffer = new byte[4096];
            while ((byteread = inStream.read(buffer)) != -1) {
                fos.write(buffer, 0, byteread);
            }
        } finally {
            if (inStream != null) {
                inStream.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * NIO way
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String filename) throws IOException {

        File f = new File(filename);
        if (!f.exists()) {
            throw new FileNotFoundException(filename);
        }

        FileChannel channel = null;
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(f);
            channel = fs.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {
                // do nothing
                // System.out.println("reading");
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
     *
     * @param filename
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static byte[] toByteArrayByMapped(String filename) throws Exception {

        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(filename, "r").getChannel();
            MappedByteBuffer byteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();
            byte[] result = new byte[(int) fc.size()];
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }

            //手动释放内存
            // 加上这几行代码,手动unmap
            Method unmapMothod = FileChannelImpl.class.getDeclaredMethod("unmap", MappedByteBuffer.class);
            unmapMothod.setAccessible(true);
            unmapMothod.invoke(FileChannelImpl.class, byteBuffer);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                fc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将byte数组写入文件
     * @param path
     * @param content
     * @throws Exception
     */
    public static void writeBytesToFile(String path, byte[] content) throws Exception {
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(content);
        fos.close();
    }

}

package com.ldl.test.util;

import java.io.BufferedInputStream;  
import java.io.BufferedOutputStream;  
import java.io.ByteArrayOutputStream;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;  
import java.net.SocketException;  
import java.net.UnknownHostException;  
import java.util.ArrayList;  
import java.util.List;  
  
import org.apache.commons.net.ftp.FTP;  
import org.apache.commons.net.ftp.FTPClient;  
import org.apache.commons.net.ftp.FTPFile;  
import org.apache.commons.net.ftp.FTPReply;  
  
/** 
 * FTPClient工具类 
 * @author happyqing 
 * @since 2016.7.20 
 */  
public class FtpUtil {  
      
    //private static Logger log = Logger.getLogger(FtpUtil.class);  
    private FTPClient ftp;  
  
    public FtpUtil() {  
        ftp = new FTPClient();  
        ftp.setControlEncoding("UTF-8"); //解决上传文件时文件名乱码  
    }  
      
    public FtpUtil(String controlEncoding) {  
        ftp = new FTPClient();  
        ftp.setControlEncoding(controlEncoding); //解决上传文件时文件名乱码  
    }  
      
    public void setTimeOut(int defaultTimeoutSecond, int connectTimeoutSecond, int dataTimeoutSecond){  
        try {  
            ftp.setDefaultTimeout(defaultTimeoutSecond * 1000);  
            //ftp.setConnectTimeout(connectTimeoutSecond * 1000); //commons-net-3.5.jar  
            ftp.setSoTimeout(connectTimeoutSecond * 1000); //commons-net-1.4.1.jar 连接后才能设置  
            ftp.setDataTimeout(dataTimeoutSecond * 1000);  
        } catch (SocketException e) { 
        	System.out.println("setTimeout Exception:"+e);
        }  
    }  
      
    public FTPClient getFTPClient(){  
        return ftp;  
    }  
      
    public void setControlEncoding(String charset){  
        ftp.setControlEncoding(charset);  
    }  
      
    public void setFileType(int fileType) throws IOException {  
        ftp.setFileType(fileType);  
    }  
  
    /** 
     * Connect to FTP server. 
     *  
     * @param host 
     *            FTP server address or name 
     * @param port 
     *            FTP server port 
     * @param user 
     *            user name 
     * @param password 
     *            user password 
     * @throws IOException 
     *             on I/O errors 
     */  
    public FTPClient connect(String host, int port, String user, String password) throws IOException {  
        // Connect to server.  
        try {  
            ftp.connect(host, port);  
        } catch (UnknownHostException ex) {  
            throw new IOException("Can't find FTP server '" + host + "'");  
        }  
  
        // Check rsponse after connection attempt.  
        int reply = ftp.getReplyCode();  
        if (!FTPReply.isPositiveCompletion(reply)) {  
            disconnect();  
            throw new IOException("Can't connect to server '" + host + "'");  
        }  
  
        if ("".equals(user)) {  
            user = "qiangdiao_ftp";  
        }  
  
        // Login.  
        if (!ftp.login(user, password)) {  
            disconnect();  
            throw new IOException("Can't login to server '" + host + "'");  
        }  
  
        // Set data transfer mode.  
        ftp.setFileType(FTP.BINARY_FILE_TYPE);  
        //ftp.setFileType(FTP.ASCII_FILE_TYPE);  
          
        /*ftp上传到服务器分主动模式和被动模式，网ip上传主动模式：ftp.enterLocalActiveMode()；（默认）
        	内网ip上传被动模式：ftp.enterLocalPassiveMode();*/
        // Use passive mode to pass firewalls.  
        //ftp.enterLocalPassiveMode();  
          
        return ftp;  
    }  
      
    /** 
     * Test connection to ftp server 
     *  
     * @return true, if connected 
     */  
    public boolean isConnected() {  
        return ftp.isConnected();  
    }  
      
    /** 
     * Disconnect from the FTP server 
     *  
     * @throws IOException 
     *             on I/O errors 
     */  
    public void disconnect() throws IOException {  
  
        if (ftp.isConnected()) {  
            try {  
                ftp.logout();  
                ftp.disconnect();  
            } catch (IOException ex) {  
            }  
        }  
    }  
      
    /** 
     * Get file from ftp server into given output stream 
     *  
     * @param ftpFileName 
     *            file name on ftp server 
     * @param out 
     *            OutputStream 
     * @throws IOException 
     */  
    public void retrieveFile(String ftpFileName, OutputStream out) throws IOException {  
        try {  
            // Get file info.  
            FTPFile[] fileInfoArray = ftp.listFiles(ftpFileName);  
            if (fileInfoArray == null || fileInfoArray.length == 0) {  
                throw new FileNotFoundException("File '" + ftpFileName + "' was not found on FTP server.");  
            }  
  
            // Check file size.  
            FTPFile fileInfo = fileInfoArray[0];  
            long size = fileInfo.getSize();  
            if (size > Integer.MAX_VALUE) {  
                throw new IOException("File '" + ftpFileName + "' is too large.");  
            }  
  
            // Download file.  
            if (!ftp.retrieveFile(ftpFileName, out)) {  
                throw new IOException("Error loading file '" + ftpFileName + "' from FTP server. Check FTP permissions and path.");  
            }  
  
            out.flush();  
  
        } finally {  
            if (out != null) {  
                try {  
                    out.close();  
                } catch (IOException ex) {  
                }  
            }  
        }  
    }  
  
    /** 
     * Put file on ftp server from given input stream 
     *  
     * @param ftpFileName 
     *            file name on ftp server 
     * @param in 
     *            InputStream 
     * @throws IOException 
     */  
    public void storeFile(String ftpFileName, InputStream in) throws IOException {  
        try {  
            if (!ftp.storeFile(ftpFileName, in)) {  
                throw new IOException("Can't upload file '" + ftpFileName + "' to FTP server. Check FTP permissions and path.");  
            }  
        } finally {  
            try {  
                in.close();  
            } catch (IOException ex) {  
            }  
        }  
    }  
      
    /** 
     * 修改名称 
     * @param from 
     * @param to 
     * @throws IOException 
     */  
    public boolean rename(String from, String to) throws IOException {  
        return ftp.rename(from, to);  
    }  
      
    /** 
     * Delete the file from the FTP server. 
     *  
     * @param ftpFileName 
     *            server file name (with absolute path) 
     * @throws IOException 
     *             on I/O errors 
     */  
    public void deleteFile(String ftpFileName) throws IOException {  
        if (!ftp.deleteFile(ftpFileName)) {  
            throw new IOException("Can't remove file '" + ftpFileName + "' from FTP server.");  
        }  
    }  
  
    /** 
     * Upload the file to the FTP server. 
     *  
     * @param ftpFileName 
     *            server file name (with absolute path) 
     * @param localFile 
     *            local file to upload 
     * @throws IOException 
     *             on I/O errors 
     */  
    public void upload(String ftpFileName, File localFile) throws IOException { 
        // File check.  
        if (!localFile.exists()) {  
            throw new IOException("Can't upload '" + localFile.getAbsolutePath() + "'. This file doesn't exist.");  
        }  
  
        // Upload.  
        InputStream in = null;  
        try {  
            in = new BufferedInputStream(new FileInputStream(localFile)); 
            if (!ftp.storeFile(ftpFileName, in)) {  
                throw new IOException("Can't upload file '" + ftpFileName + "' to FTP server. Check FTP permissions and path.");  
            }  
  
        } finally {  
            try {  
                in.close();  
            } catch (IOException ex) {
            	ex.printStackTrace();
            }  
        }  
    }  
      
    /** 
     * 上传目录（会覆盖) 
     * @param remotePath 远程目录 /home/test/a 
     * @param localPath 本地目录 D:/test/a 
     * @throws IOException 
     */  
    public void uploadDir(String remotePath, String localPath) throws IOException {  
        File file = new File(localPath);  
        if (file.exists()) {  
            if(!ftp.changeWorkingDirectory(remotePath)){  
                ftp.makeDirectory(remotePath);  //创建成功返回true，失败（已存在）返回false  
                ftp.changeWorkingDirectory(remotePath); //切换成返回true，失败（不存在）返回false  
            }  
            File[] files = file.listFiles();  
            for (File f : files) {  
                if (f.isDirectory() && !f.getName().equals(".") && !f.getName().equals("..")) {  
                    uploadDir(remotePath + File.separator + f.getName(), f.getPath());  
                } else if (f.isFile()) {  
                    upload(remotePath + File.separator + f.getName(), f);  
                }  
            }  
        }  
    }  
  
    /** 
     * Download the file from the FTP server. 
     *  
     * @param ftpFileName 
     *            server file name (with absolute path) 
     * @param localFile 
     *            local file to download into 
     * @throws IOException 
     *             on I/O errors 
     */  
    public void download(String ftpFileName, File localFile) throws IOException {  
        // Download.  
        OutputStream out = null;  
        try {  
            // Get file info.  
            FTPFile[] fileInfoArray = ftp.listFiles(ftpFileName);  
            if (fileInfoArray == null || fileInfoArray.length == 0) {  
                throw new FileNotFoundException("File " + ftpFileName + " was not found on FTP server.");  
            }  
  
            // Check file size.  
            FTPFile fileInfo = fileInfoArray[0];  
            long size = fileInfo.getSize();  
            if (size > Integer.MAX_VALUE) {  
                throw new IOException("File " + ftpFileName + " is too large.");  
            }  
  
            // Download file.  
            out = new BufferedOutputStream(new FileOutputStream(localFile));  
            if (!ftp.retrieveFile(ftpFileName, out)) {  
                throw new IOException("Error loading file " + ftpFileName + " from FTP server. Check FTP permissions and path.");  
            }  
  
            out.flush();  
        } finally {  
            if (out != null) {  
                try {  
                    out.close();  
                } catch (IOException ex) {  
                }  
            }  
        }  
    }  
      
    /** 
     * 下载目录（会覆盖) 
     * @param remotePath 远程目录 /home/test/a 
     * @param localPath 本地目录 D:/test/a 
     * @return 
     * @throws IOException 
     */  
    public void downloadDir(String remotePath, String localPath) throws IOException {  
        File file = new File(localPath);  
        if(!file.exists()){  
            file.mkdirs();  
        }  
        FTPFile[] ftpFiles = ftp.listFiles(remotePath);  
        for (int i = 0; ftpFiles!=null && i<ftpFiles.length; i++) {  
            FTPFile ftpFile = ftpFiles[i];  
            if (ftpFile.isDirectory() && !ftpFile.getName().equals(".") && !ftpFile.getName().equals("..")) {  
                downloadDir(remotePath + File.separator + ftpFile.getName(), localPath + File.separator + ftpFile.getName());  
            } else {  
                download(remotePath + File.separator + ftpFile.getName(), new File(localPath + File.separator + ftpFile.getName()));  
            }  
        }  
    }  
  
    /** 
     * List the file name in the given FTP directory. 
     *  
     * @param filePath 
     *            absolute path on the server 
     * @return files relative names list 
     * @throws IOException 
     *             on I/O errors 
     */  
    public List<String> listFileNames(String filePath) throws IOException {  
        List<String> fileList = new ArrayList<String>();  
  
        FTPFile[] ftpFiles = ftp.listFiles(filePath);  
        for (int i = 0; ftpFiles!=null && i<ftpFiles.length; i++) {  
            FTPFile ftpFile = ftpFiles[i];  
            if (ftpFile.isFile()) {  
                fileList.add(ftpFile.getName());  
            }  
        }  
          
        return fileList;  
    }  
      
    /** 
     * List the files in the given FTP directory. 
     *  
     * @param filePath 
     *            directory 
     * @return list 
     * @throws IOException 
     */  
    public List<FTPFile> listFiles(String filePath) throws IOException {  
        List<FTPFile> fileList = new ArrayList<FTPFile>();  
  
        FTPFile[] ftpFiles = ftp.listFiles(filePath);  
        for (int i = 0; ftpFiles!=null && i<ftpFiles.length; i++) {  
            FTPFile ftpFile = ftpFiles[i];  
//            FfpFileInfo fi = new FfpFileInfo();  
//            fi.setName(ftpFile.getName());  
//            fi.setSize(ftpFile.getSize());  
//            fi.setTimestamp(ftpFile.getTimestamp());  
//            fi.setType(ftpFile.isDirectory());  
            fileList.add(ftpFile);  
        }  
  
        return fileList;  
    }  
  
  
    /** 
     * Send an FTP Server site specific command 
     *  
     * @param args 
     *            site command arguments 
     * @throws IOException 
     *             on I/O errors 
     */  
    public void sendSiteCommand(String args) throws IOException {  
        if (ftp.isConnected()) {  
            try {  
                ftp.sendSiteCommand(args);  
            } catch (IOException ex) {  
            }  
        }  
    }  
  
    /** 
     * Get current directory on ftp server 
     *  
     * @return current directory 
     */  
    public String printWorkingDirectory() {  
        if (!ftp.isConnected()) {  
            return "";  
        }  
  
        try {  
            return ftp.printWorkingDirectory();  
        } catch (IOException e) {  
        }  
  
        return "";  
    }  
  
    /** 
     * Set working directory on ftp server 
     *  
     * @param dir 
     *            new working directory 
     * @return true, if working directory changed 
     */  
    public boolean changeWorkingDirectory(String dir) {  
        if (!ftp.isConnected()) {  
            return false;  
        }  
  
        try {  
            return ftp.changeWorkingDirectory(dir);  
        } catch (IOException e) {  
        }  
  
        return false;  
    }  
  
    /** 
     * Change working directory on ftp server to parent directory 
     *  
     * @return true, if working directory changed 
     */  
    public boolean changeToParentDirectory() {  
        if (!ftp.isConnected()) {  
            return false;  
        }  
  
        try {  
            return ftp.changeToParentDirectory();  
        } catch (IOException e) {  
        }  
  
        return false;  
    }  
  
    /** 
     * Get parent directory name on ftp server 
     *  
     * @return parent directory 
     */  
    public String printParentDirectory() {  
        if (!ftp.isConnected()) {  
            return "";  
        }  
  
        String w = printWorkingDirectory();  
        changeToParentDirectory();  
        String p = printWorkingDirectory();  
        changeWorkingDirectory(w);  
  
        return p;  
    }  
      
    /** 
     * 创建目录 
     * @param pathname 
     * @throws IOException 
     */  
    public boolean makeDirectory(String pathname) throws IOException {  
        return ftp.makeDirectory(pathname);  
    }  
    
    /** 
     * 查询ftp服务器上指定路径所有文件名 
     *  
     * @param url 
     *            FTP服务器hostname 
     * @param port 
     *            FTP服务器端口 
     * @param username 
     *            FTP登录账号 
     * @param password 
     *            FTP登录密码 
     * @param remotePath 
     *            FTP服务器上的相对路径 
     * @return 
     * @date 创建时间：2017年6月14日 下午5:06:57 
     */  
    public static List<String> listFTPFiles(String url, int port, String username, String password, String remotePath) {  
        ArrayList<String> resultList = new ArrayList<String>();  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(url, port);  
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器  
            ftp.login(username, password);// 登录  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return resultList;  
            }  
            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录  
            FTPFile[] fs = ftp.listFiles();  
            for (FTPFile ff : fs) {  
                resultList.add(ff.getName());  
                // if (ff.getName().equals(fileName)) {  
                // File localFile = new File(localPath + "/" + ff.getName());  
                // OutputStream is = new FileOutputStream(localFile);  
                // ftp.retrieveFile(ff.getName(), is);  
                // is.close();  
                // }  
            }  
            ftp.logout();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return resultList;  
    }  
      
    /** 
     * 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建 
     * @param url 
     * @param port 
     * @param username 
     * @param password 
     * @param remote 
     * @return 
     * @throws IOException  
     * @date 创建时间：2017年6月22日 上午11:51:33 
     */  
    public static boolean createDirectory(String url, int port, String username, String password, String remote)  
            throws IOException {  
      
        FTPClient ftp = new FTPClient();  
        int reply;  
        ftp.connect(url, port);  
        // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器  
        ftp.login(username, password);// 登录  
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);  
        reply = ftp.getReplyCode();  
        if (!FTPReply.isPositiveCompletion(reply)) {  
            ftp.disconnect();  
        }  
        //ftp.changeWorkingDirectory("/var/www/html");// 转移到FTP服务器目录  
      
        boolean success = true;  
        String directory = remote + "/";  
        // String directory = remote.substring(0, remote.lastIndexOf("/") + 1);  
        // 如果远程目录不存在，则递归创建远程服务器目录  
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(new String(directory), ftp)) {  
            int start = 0;  
            int end = 0;  
            if (directory.startsWith("/")) {  
                start = 1;  
            } else {  
                start = 0;  
            }  
            end = directory.indexOf("/", start);  
            String path = "";  
            String paths = "";  
            while (true) {  
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), "iso-8859-1");  
                path = path + "/" + subDirectory;  
                if (!existFile(path, ftp)) {  
                    if (makeDirectory(subDirectory, ftp)) {  
                        changeWorkingDirectory(subDirectory, ftp);  
                    } else {  
                        System.out.println("创建目录[" + subDirectory + "]失败");  
                        changeWorkingDirectory(subDirectory, ftp);  
                    }  
                } else {  
                    changeWorkingDirectory(subDirectory, ftp);  
                }  
      
                paths = paths + "/" + subDirectory;  
                start = end + 1;  
                end = directory.indexOf("/", start);  
                // 检查所有目录是否创建完毕  
                if (end <= start) {  
                    break;  
                }  
            }  
        }  
        return success;  
    }  
      
    /** 
     * 改变目录路径 
     * @param directory 
     * @param ftp 
     * @return  
     * @date 创建时间：2017年6月22日 上午11:52:13 
     */  
    public static boolean changeWorkingDirectory(String directory, FTPClient ftp) {  
        boolean flag = true;  
        try {  
            flag = ftp.changeWorkingDirectory(directory);  
            if (flag) {  
                System.out.println("进入文件夹" + directory + " 成功！");  
            } else {  
                System.out.println("进入文件夹" + directory + " 失败！");  
            }  
        } catch (IOException ioe) {  
            ioe.printStackTrace();  
        }  
        return flag;  
    }  
      
    /** 
     * 创建目录 
     * @param dir 
     * @param ftp 
     * @return  
     * @date 创建时间：2017年6月22日 上午11:52:40 
     */  
    public static boolean makeDirectory(String dir, FTPClient ftp) {  
        boolean flag = true;  
        try {  
            flag = ftp.makeDirectory(dir);  
            if (flag) {  
                System.out.println("创建文件夹" + dir + " 成功！");  
            } else {  
                System.out.println("创建文件夹" + dir + " 失败！");  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return flag;  
    }  
      
    /** 
     * 判断ftp服务器文件是否存在 
     * @param path 
     * @param ftp 
     * @return 
     * @throws IOException  
     * @date 创建时间：2017年6月22日 上午11:52:52 
     */  
    public static boolean existFile(String path, FTPClient ftp) throws IOException {  
        boolean flag = false;  
        FTPFile[] ftpFileArr = ftp.listFiles(path);  
        if (ftpFileArr.length > 0) {  
            flag = true;  
        }  
        return flag;  
    }
}  
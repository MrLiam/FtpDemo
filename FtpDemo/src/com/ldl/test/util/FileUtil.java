package com.ldl.test.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

	/**
	 * 获取一个路径下的所有文件(包含子文件夹)
	 *
	 * @param rootPath
	 */
	public static List<String> getAllFilePath(String rootPath) {
		List<String> filePathList = new ArrayList<>();
		File file = new File(rootPath);
		File[] files = file.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				getAllFilePath(files[i].getPath());
			} else {
				filePathList.add(files[i].getPath());
			}
		}

		return filePathList;
	}

	/**
	 * 获取一个路径下的所有文件(包含子文件夹) 可以指定过滤规则
	 *
	 * @param rootPath
	 */
	public static List<String> getAllFilePath(String rootPath, FileFilter fileFilter) {
		List<String> filePathList = new ArrayList<>();
		File file = new File(rootPath);
		File[] files = file.listFiles(fileFilter);

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				getAllFilePath(files[i].getPath(), fileFilter);
			} else {
				filePathList.add(files[i].getPath());
			}
		}

		return filePathList;
	}

	/**
	 * 删除空目录
	 * 
	 * @param dir
	 *            将要删除的目录路径
	 */
	public static void doDeleteEmptyDir(String dir) {
		boolean success = (new File(dir)).delete();
		if (success) {
			System.out.println("Successfully deleted empty directory: " + dir);
		} else {
			System.out.println("Failed to delete empty directory: " + dir);
		}
	}

	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * 
	 * @param dir
	 *            将要删除的文件目录
	 * @return boolean Returns "true" if all deletions were successful. If a
	 *         deletion fails, the method stops attempting to delete and returns
	 *         "false".
	 */
	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}

	/**
	 * 使用文件通道的方式复制文件
	 *
	 * @param srcPath
	 *            源文件 "F:/xxx/xxx/xxxx/54431b8d_92cf.jpg"
	 * @param descPath
	 *            复制到的新文件 "F:/xxx/xx
	 */
	public static void copy(String srcPath, String descPath) {
		File s = new File(srcPath);
		File t = new File(descPath + File.separator + s.getName());

		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
     * 使用文件通道的方式复制文件
     * @param srcFile
     *            源文件 "F:/xxx/xxx/xxxx/54431b8d_92cf.jpg"
     * @param descPath
     *            复制到的新文件 "F:/xxx/xx
     *            
     */
    public static void copy(File srcFile, String descPath) {
        File s = srcFile;
        File t = new File(descPath + File.separator + s.getName());
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();// 得到对应的文件通道
            out = fo.getChannel();// 得到对应的文件通道
            in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	/**
	 * 对当前文件夹下的文件改名
	 * 
	 * @param oldFilePath
	 * @param newName
	 */
	public static void reName(String oldFilePath, String newName) {
		String basePath = oldFilePath.substring(0, oldFilePath.lastIndexOf(File.separator));
		String descPaht = basePath + File.separator + newName;
		reNameTo(oldFilePath, descPaht);
	}

	/**
	 * 对当前文件夹下的文件改名
	 * 
	 * @param oldFilePath
	 * @param newName
	 */
	public static void reName(File oldFile, String newName) {
		String oldFilePath = oldFile.getPath();
		String basePath = oldFilePath.substring(0, oldFilePath.lastIndexOf("\\"));
		String descPaht = basePath + File.separator + newName;
		reNameTo(oldFile, descPaht);
	}

	/**
	 * 在目标目录下创建 文件夹
	 * 
	 * @param descPath
	 * @param dirName
	 * 
	 */
	public static void makeDir(String descPath, String dirName) {
		File file = new File(descPath);

		if (file != null) {
			new File(descPath + File.separator + dirName).mkdir();
		}
	}

	private static void reNameTo(File oldFile, String newFilePath) {
		oldFile.renameTo(new File(newFilePath));
	}

	private static void reNameTo(String oldFilePath, String newFilePath) {
		File oldFile = new File(oldFilePath);
		oldFile.renameTo(new File(newFilePath));
	}
	
	/**
     * 获取文件编码格式
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String getFileEncoding(String fileName) throws Exception {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
        int p = (bin.read() << 8) + bin.read();
        String code = null;
        switch (p) {
        case 0xefbb:
            code = "UTF-8";
            break;
        case 0xfffe:
            code = "Unicode";
            break;
        case 0xfeff:
            code = "UTF-16BE";
            break;
        default:
            code = "GBK";
        }

        return code;
    }
    
    /**
     * 复制文件夹
     * @param sourceDir
     * @param targetDir
     * @throws IOException
     */
    public static void copyDirectiory(String sourceDir, String targetDir)
            throws IOException {
        // 新建目标目录
        (new File(targetDir)).mkdirs();
        // 获取源文件夹当前下的文件或目录
        File[] file = (new File(sourceDir)).listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isFile()) {
                // 源文件
                File sourceFile = file[i];
                // 目标文件
                File targetFile = new File(new File(targetDir)
                        .getAbsolutePath()
                        + File.separator + file[i].getName());
                if (sourceFile.getName().indexOf(".vax") < 0)
                    copyFile(sourceFile, targetFile);
            }
            if (file[i].isDirectory()) {
                // 准备复制的源文件夹
                String dir1 = sourceDir + "/" + file[i].getName();
                // 准备复制的目标文件夹
                String dir2 = targetDir + "/" + file[i].getName();
                copyDirectiory(dir1, dir2);
            }
        }
    }
    
    /**
     * 复制文件
     * @param sourceFile
     * @param targetFile
     * @throws IOException
     */
    public static void copyFile(File sourceFile, File targetFile)
            throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);
        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);
        // 缓冲数组
        byte[] b = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(b)) != -1) {
            outBuff.write(b, 0, len);
        }
        // 刷新此缓冲的输出流
        outBuff.flush();
        // 关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }
    
    /**
     * 得到文件的扩展名
     * @param f
     * @return
     */
    public static String getFileExtension(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(i + 1).toLowerCase();
            }
        }
        return null;
    }
    
    /**
     * 得到文件名(排除文件扩展名)
     * @param f
     * @return
     */
    public static String getFileNameWithoutExt(File f) {
        if (f != null) {
            String filename = f.getName();
            int i = filename.lastIndexOf('.');
            if (i > 0 && i < filename.length() - 1) {
                return filename.substring(0, i);
            }
        }
        return null;
    }
    
    /**
     * 改变文件的扩展名
     * @param fileNM
     * @param ext
     * @return
     */
    public static String changeFileExt(String fileNM, String ext) {
        int i = fileNM.lastIndexOf('.');
        if (i >= 0)
            return (fileNM.substring(0, i) + ext);
        else
            return fileNM;
    }
    
    /**
     * 判断目录是否存在
     * @param strDir
     * @return
     */
    public static boolean existsDirectory(String strDir) {
        File file = new File(strDir);
        return file.exists() && file.isDirectory();
    }
    
    /**
     * 得到文件的大小
     * @param fileName
     * @return
     */
    public static int getFileSize(String fileName){
        
        File file = new File(fileName);
        FileInputStream fis = null;
        int size = 0 ;
        try {
            fis = new FileInputStream(file);
            size = fis.available();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return size ;
    }
    
   
    /**
     * 通过字符流向文件中追加内容
     * @param file
     * @param data
     */
    public static void bufferedWriterFile(File file, String data){
    	FileWriter fw= null;
    	BufferedWriter bufw= null;
        try{  
            fw=new FileWriter(file,true);  
            bufw=new BufferedWriter(fw);  
            bufw.write(data);  
            bufw.newLine(); 
            bufw.flush();
        }  
        catch(Exception e){  
            e.printStackTrace();  
        }finally {
        	 try {
				bufw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}  
    }
    
}

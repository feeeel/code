package com.jcone.rmsXmlCmd.common.utils.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtil {
	
	
	public static String getSystemPath(String filePath, char separatorChat){
		
		if(StringUtil.validateString(filePath)){			

			filePath = filePath.replace('\\', separatorChat);
			filePath = filePath.replace('/', separatorChat);
			
		} 
		return filePath;		
	}
	
	
	public static String setLastFilePathSp(String filePath, char separatorChat)
	{
		if(StringUtil.validateString(filePath)){
			String sp = String.valueOf(separatorChat);
			int nIndex = filePath.lastIndexOf(sp);
			if(filePath.length()-1 != nIndex)
			{
				filePath = filePath+sp;
			}			
		}		
		
		return filePath;
	}
	
	public static String setFirstFilePathSp(String filePath, char separatorChat)
	{
		if(StringUtil.validateString(filePath)){
			
			String sp = String.valueOf(separatorChat);
			String firstChar = filePath.substring(0, 1);
			if(firstChar.equals(sp))
			{
				filePath = filePath.substring(1);
			}
		
		}
		return filePath;
	}
	
	
	public static boolean isDirCheck(String filePath)
	{
		boolean bValue = false;
		File file = new File(filePath);
		if(file.exists())
		{
			bValue = isDirCheck(file);
		}
		
		return bValue;
	}
	
	
	public static boolean isDirCheck(File file)
	{
		return file.isDirectory();
	}
	

	public static String getFileExt(String filename)
	{
		int index = filename.lastIndexOf('.');

		if (index == -1)
			return null;
		else
			return filename.substring(index+1);
	}
	
	public static String getFilePath(String path, String fileSp)
    {
        int lastSp = path.lastIndexOf(fileSp);
        if(lastSp > 0)
        {
            return path.substring(0, lastSp + 1);
        }
        else
        {
            return path;
        }
    }

    public static String getFileName(String path, String fileSp)
    {
        int lastSp = path.lastIndexOf(fileSp);
        if(lastSp > 0)
        {
            return path.substring(lastSp + 1);
        }
        else
        {
            return path;
        }
    }
    
    public static String getSimpleFileName(String fileName)
    {
    	int index = fileName.lastIndexOf('.');

		if (index == -1)
			return null;
		else
			return fileName.substring(0, index);
    }
    
    public static String exceptLastFilePathSp(String path)
    {
    	int lastSp =  path.lastIndexOf(File.separatorChar);
    	String returnStr = null;
    	if(lastSp > 0)
    	{
    		returnStr = path.substring(0, lastSp);
    	}
    	else
    	{
    		returnStr = path;
    	}
    	return returnStr;
    }
    
    public static boolean makeDir(String filePath)
    {
    	filePath = exceptLastFilePathSp(filePath);
    	
    	boolean bDir = false;
    	File file = new File(filePath);
    	
    	if(isDirCheck(file))
    	{
    		bDir = true;    		
    	}
    	else
    	{
    		file.mkdirs();    		
    		bDir = true;
    	}
    	
    	return bDir;
    	
    }
    
    public static boolean chgFilerename(String fromFile, String toFile) 
    {
        boolean bCheck = false;
        try
        {
	        File f = new File(fromFile);
	        if(f.exists())
	        {
	            	bCheck = f.renameTo(new File(toFile));
	        }
        }
        catch(Exception e)
        {
        	
        }
        
       
        return bCheck;
    }
    
    public static void deleteFile(String filePath) throws FileNotFoundException
    {
    	deleteFile(new File(filePath));
    }
    
    public static void deleteFile(File file) throws FileNotFoundException
    {
    	if(file.exists())
    	{
    		file.delete();
    	}
    	else
    	{    	
    		throw new FileNotFoundException(file.getAbsolutePath()+ "������ ����ϴ�.");
    	}
    }
    
    public static void move(String srcPath, String tgPath) throws IOException
    {
    	copy(srcPath, tgPath);
    	deleteFile(srcPath);
    }
    
    public static void move(File fSrc, String tgPath) throws IOException
    {
    	copy(fSrc, tgPath);
    	deleteFile(fSrc);
    }    
    
    
    public static void copy(String szOrgPath, String szTargetPath) throws IOException
    {
    	copy(new File(szOrgPath), new File(szTargetPath));
    }
    
    public static void copy(File fOrg, String szTargetPath) throws IOException
    {
    	copy(fOrg, new File(szTargetPath));
    }
    
    
   public static void copy(File fOrg, File fTarget) throws IOException
   {
	   FileInputStream inputStream = new FileInputStream(fOrg);
	   if(!fTarget.isFile())
	   {
		   File fParent = new File(fTarget.getParent());
		   if(!fParent.exists())
		   {
			   fParent.mkdir();
		   }
		   
		   fTarget.createNewFile();
	   }
	   
	   FileOutputStream outputStream = new FileOutputStream(fTarget);
	   FileChannel fcin = inputStream.getChannel();
	   FileChannel fcout = outputStream.getChannel();
	   
	   long size = fcin.size();
	   
	   fcin.transferTo(0, size, fcout);
	   
	   fcout.close();
	   fcin.close();
	   outputStream.close();
	   inputStream.close();
   }
   
   public static String newCreatFile(String filePath) throws IOException
   {
	   File fTarget = new File(filePath);
	   if(!fTarget.isFile())
	   {
		   File fParent = new File(fTarget.getParent());
		   if(!fParent.exists())
		   {
			   fParent.mkdir();
		   }
		   
		   fTarget.createNewFile();
	   }  
	   
	   return fTarget.getAbsolutePath();
   }
   
   public static String rootPathExist(String rootPath, String filePath)
   {	   
	   String newPath = filePath.substring(rootPath.length());	   
	   return newPath;
   }



}

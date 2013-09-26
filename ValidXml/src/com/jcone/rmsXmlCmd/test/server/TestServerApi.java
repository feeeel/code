package com.jcone.rmsXmlCmd.test.server;

import java.io.File;

import com.jcone.rmsXmlCmd.common.utils.common.FileUtil;
import com.jcone.rmsXmlCmd.socket.param.ReceiveParamGet;
import com.jcone.rmsXmlCmd.socket.param.SendParamSet;



public class TestServerApi 
{
	private static TestServerApi obj=null;
	//private static final String serverPath = "/test/rndtest/jc1test/testsocket/testdata/";
	private static final String serverPath = "D:\\Temp\\serverRepo\\";
	//private static final String serverPath = "d:\\test\\111\\";
	
	public TestServerApi()
	{
		
	}
	
	public static TestServerApi getInstance()
	{
		FileUtil.makeDir(serverPath);
		obj = new TestServerApi();		
		return obj;
	}
	
	public SendParamSet lineCheck(ReceiveParamGet rpg, SendParamSet sps, String client_ip) throws Exception
	{
		try
		{
			rpg.clear();
			sps.reset();
			sps.addBooleanParam(true);
			sps.addParam("SUCCESS");
			
			String logMsg = "CLIENT SEND IP ;"+client_ip+" LINE CHECK OK";
			System.out.println(logMsg);
		}
		catch(Exception e)
		{
			sps.addBooleanParam(false);			
			sps.addParam(e.getMessage());
			
			String logMsg = "CLIENT SEND IP ;"+client_ip+" LINE CHECK ERROR";
			System.out.println(logMsg);
		}
		
		return sps;
	}
	
	public SendParamSet sendFile(ReceiveParamGet rpg, SendParamSet sps, String client_ip) throws Exception
	{
		try
		{
			String fileName = rpg.nextParam();
			fileName = FileUtil.getSystemPath(fileName, File.separatorChar);
			String recFileFullPath = serverPath+fileName;
			File recF = new File(recFileFullPath);
			FileUtil.makeDir(recF.getPath());
			long fileSize = rpg.nextLongParam();
			String tempFilePath = rpg.nextParam();
			
			FileUtil.copy(tempFilePath, recFileFullPath);
			
			rpg.clear();
			sps.reset();
			
			File checkFile = new File(recFileFullPath);
			if(checkFile.isFile())
			{	
				sps.addBooleanParam(true);
				sps.addParam("SUCCESS");
			}
			else
			{
				sps.addBooleanParam(false);
				sps.addParam("파일 서버 생성 도중 오류");				
			}			
			
			String logMsg = "CLIENT SEND IP ;"+client_ip+" SENDFILE : "+checkFile+" => OK";
			System.out.println(logMsg);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			sps.addBooleanParam(false);
			sps.addParam(e.getMessage());
			
			String logMsg = "CLIENT SEND IP ;"+client_ip+" => ERROR";
			System.out.println(logMsg);
		}
		
		return sps;
	}
	
	public SendParamSet sendFileCheck(ReceiveParamGet rpg, SendParamSet sps, String client_ip) throws Exception
	{
		try
		{
			
			String fileName = rpg.nextParam();
			fileName = FileUtil.getSystemPath(fileName, File.separatorChar);
			String recFileFullPath = serverPath+fileName;
			
			rpg.clear();
			sps.reset();
			
			File checkFile = new File(recFileFullPath);
			sps.addBooleanParam(true);
			if(checkFile.isFile())
			{
				sps.addLongParam(checkFile.length());				
			}
			else
			{
				sps.addLongParam(0L);						
			}
						
			String logMsg = "CLIENT SEND IP ;"+client_ip+" SENDFILECHECK : "+checkFile+" => OK";
			System.out.println(logMsg);
		}
		catch(Exception e)
		{
			sps.addBooleanParam(false);
			sps.addParam(e.getMessage());
			
			String logMsg = "CLIENT SEND IP ;"+client_ip+" => ERROR";
			System.out.println(logMsg);
		}
		
		return sps;
	}
	
	
}

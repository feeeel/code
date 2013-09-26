package com.jcone.rmsXmlCmd.test.run;

import java.io.File;
import java.util.ArrayList;

import com.jcone.rmsXmlCmd.common.utils.common.DateTime;
import com.jcone.rmsXmlCmd.test.client.SendFileListThread;

public class TestClientRun 
{
	private static ArrayList arrSendList = new ArrayList();
	
	public static void main(String[] args) 
	{
		try
		{
			String strDir = "D:\\Temp\\clientRepo\\";
			ArrayList arrSendFileList = getFileList(strDir);
			
			String serverIp = "192.168.10.253";
			int serverPort = 5000;
			long waitTime = 60*1000;
			int maxSize = 100;
			
			String startTime = DateTime.getTimeStampSecoundString();
			System.out.println("startTime ; "+startTime);
			System.out.println("arrSendFileList ; "+arrSendFileList.size());
			
			if(arrSendFileList.size() > 0)
			{
				SendFileListThread sendFileListThread = new SendFileListThread(serverIp, serverPort, waitTime, maxSize, arrSendFileList,strDir);
				sendFileListThread.run();
				
				while(sendFileListThread.isAlive())
				{				
					try
					{
						Thread.sleep(1000L);
					}
					catch(Exception e)
					{
						
					}
				}
			}
			
			String endTime = DateTime.getTimeStampSecoundString();
			int termDate = DateTime.daysBetween(startTime, endTime, "yyyyMMddHHmmss" );
			System.out.println("endTime ; "+endTime);
			System.out.println("termDate ; "+termDate);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static ArrayList getFileList(String _strDir)
    {
        String strFileName = "";
        int i = 0;
        File dirF = new File(_strDir);
        String arrDirList[] = dirF.list();
        File f = null;
   
        if(arrDirList != null)
        {
            int len = arrDirList.length;
           
            for(i = 0; i < len; i++)
            {
                strFileName =_strDir+"\\" + arrDirList[i];               
                f = new File(strFileName);               
                if(f.isDirectory())
                {
                    try
                    {                    	
                        getFileList(f.getAbsolutePath());                        
                    }
                    catch(Exception e)
                    { 
                    	e.printStackTrace();
                    	break;
                    }
                } 
                else
                {
                    if(f.length() != 0L)
                    {                        
                    	arrSendList.add(f);
                    }
                }
            }
        } 
        else
        {
        	arrSendList.add(_strDir);
        }        
        
        return arrSendList;
    }
}
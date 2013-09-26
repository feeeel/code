package com.jcone.rmsXmlCmd.test.client;

import java.io.File;
import java.util.ArrayList;

import com.jcone.rmsXmlCmd.socket.client.ClientWorkMng;
import com.jcone.rmsXmlCmd.socket.pool.SocketPoolMng;

public class SendFileListThread extends Thread
{
	/**
	 * @uml.property  name="server_Ip"
	 */
	private String server_Ip = null;
	/**
	 * @uml.property  name="server_Port"
	 */
	private int server_Port = 0;
	/**
	 * @uml.property  name="wait_Time"
	 */
	private long wait_Time = 0L;
	/**
	 * @uml.property  name="max_Size"
	 */
	private int max_Size = 0;
	/**
	 * @uml.property  name="send_File_list"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.io.File"
	 */
	private ArrayList send_File_list = null;
	/**
	 * @uml.property  name="root_Path"
	 */
	private String root_Path = null;
	
	public SendFileListThread(String serverIp, int serverPort, long waitTime,int maxSize, ArrayList arrSendFiles, String rootPath)
	{
		this.server_Ip = serverIp;
		this.server_Port = serverPort;
		this.wait_Time = waitTime;
		this.max_Size = maxSize;
		this.send_File_list = arrSendFiles;
		this.root_Path = rootPath;
	}
	
	public void run()
	{
		try
		{
			//System.out.println("SendFileListThread File Call send_File_list size => "+send_File_list.size());
			File sendFile = null;
			SendFileThread[] threads = new SendFileThread[send_File_list.size()];
			
			for(int i=0; i<send_File_list.size(); i++)
			{
				sendFile = (File)send_File_list.get(i);
				
				threads[i] = new SendFileThread(server_Ip,server_Port, wait_Time, max_Size, sendFile, root_Path);
				threads[i].start();				
				
				int num = i % 100; 
				if(num == 0)
				{
					try
					{
						Thread.sleep(1000L);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			
			ClientWorkMng cwm = ClientWorkMng.getInstance(max_Size, wait_Time);
			while(cwm.isActiveThread())
			{				
				try{
					Thread.sleep(1000L);
				}catch(Exception e){
					
				}
			}
			
			SocketPoolMng spm = SocketPoolMng.getInstance(server_Ip,server_Port, wait_Time, max_Size);
			while(spm.isUsedPool())
			{
				try{
					Thread.sleep(1000L);
				}catch(Exception e){
					
				}
			}
				
			spm.closeAll();
		
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}

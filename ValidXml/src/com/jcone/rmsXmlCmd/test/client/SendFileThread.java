package com.jcone.rmsXmlCmd.test.client;

import java.io.File;

import com.jcone.rmsXmlCmd.socket.client.ClientWorkAPI;
import com.jcone.rmsXmlCmd.socket.client.ClientWorkMng;
import com.jcone.rmsXmlCmd.socket.pool.SocketPoolMng;
import com.jcone.rmsXmlCmd.socket.pool.SocketPoolObj;

public class SendFileThread extends Thread 
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
	 * @uml.property  name="send_File"
	 */
	private File send_File = null;
	/**
	 * @uml.property  name="root_Path"
	 */
	private String root_Path = null;
	
	public SendFileThread(String serverIp, int serverPort, long waitTime,int maxSize, File sendFile, String rootPath)
	{
		this.server_Ip = serverIp;
		this.server_Port = serverPort;
		this.wait_Time = waitTime;
		this.max_Size = maxSize;
		this.send_File = sendFile;
		this.root_Path = rootPath;
	}
	
	public void run()
	{
		ClientWorkMng cwm = null;
		ClientWorkAPI cwa = null;
		SocketPoolMng spm = null;
		SocketPoolObj spo = null;
		try
		{
			String msg = null;
			
			cwm = ClientWorkMng.getInstance(max_Size, wait_Time+1000);
			cwa = cwm.getWorkObject();
			spm = SocketPoolMng.getInstance(server_Ip, server_Port, wait_Time, max_Size);
			spo = spm.getSocketObject();
			
			boolean bLine = cwa.checkLine(spo, server_Ip, server_Port, wait_Time, max_Size);
			
			if(bLine)
			{
				boolean bSendFileCheck = cwa.sendFileCheck(spo, server_Ip, server_Port, wait_Time, max_Size, send_File, root_Path);
				if(!bSendFileCheck)
				{
					boolean bSend = cwa.sendFile(spo, server_Ip, server_Port, wait_Time, max_Size, send_File, root_Path);					
				}
				else
				{
					msg = "=> "+send_File.getAbsolutePath()+"- 전송된 파일";
					System.out.println(msg);
				}
			}
			else
			{
				msg = "=> "+send_File.getAbsolutePath()+"- 라인체크 오류";
				System.out.println(msg);
				throw new Exception(msg);
			}			
			
			
		}
		catch (Exception e) 
		{				
			e.printStackTrace();
		}
		finally
		{
			if(cwm != null)
			{
				if ( cwa != null ) cwm.release(cwa);
			}
			
			if(spm != null)
			{
				if (spo != null) spm.release(spo);
			}
		}
	}
}
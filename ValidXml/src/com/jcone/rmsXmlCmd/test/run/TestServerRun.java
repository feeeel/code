package com.jcone.rmsXmlCmd.test.run;

import com.jcone.rmsXmlCmd.socket.server.ServerService;

public class TestServerRun
{
	/**
	 * 소켓 서버 테스트 클래스
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try
		{			
			System.out.println("Server Start");
			ServerService ss = new ServerService(5000);
			ss.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
package com.jcone.rmsXmlCmd.socket.server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerService extends Thread
{
	/**
	 * @uml.property  name="port"
	 */
	private int port;
	/**
	 * @uml.property  name="broker"
	 */
	private Thread broker;
	/**
	 * @uml.property  name="stop"
	 */
	private boolean stop = false;

	/**
	 * 소켓 서버를 생성
	 * 
	 * @param port 서버 포트
	 * @throws Exception
	 */
	public ServerService(int port) throws Exception
	{		
		this.port = port;		
		broker = new Thread(this);
	}
	
	/**
	 * stop 플래그를 false로 변경
	 */
	public void doStop()
	{
		stop = true;
	}	
	
	@Override
	public void start()
	{
		broker.start();
	}

	@Override
	public void run() 
	{
		ServerSocket listener;
		
		try 
		{ 
			listener = new ServerSocket(port);
			
			while (!stop) 
			{
			    Socket socket = listener.accept();

			    ServerAPIBroker serverAPIBroker = new ServerAPIBroker(socket);				
			    serverAPIBroker.start();
			}
			
			listener.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return;
		}
	}
}
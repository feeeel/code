package com.jcone.rmsXmlCmd.socket.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.jcone.rmsXmlCmd.socket.param.ReceiveParamGet;
import com.jcone.rmsXmlCmd.socket.param.SendParamSet;

public class ServerAPIBroker extends Thread
{
	/**
	 * @uml.property  name="socket"
	 */
	Socket Socket;
    /**
	 * @uml.property  name="clientIp"
	 */
    private String clientIp;
	/**
	 * @uml.property  name="in"
	 */
	private InputStream in;
	/**
	 * @uml.property  name="out"
	 */
	private OutputStream out;
	/**
	 * @uml.property  name="rpg"
	 * @uml.associationEnd  
	 */
	private ReceiveParamGet rpg;
	
	public ServerAPIBroker(Socket s) throws IOException
	{ 
		this.Socket = s;
		this.clientIp = (s.getInetAddress()).getHostAddress();
		this.in = s.getInputStream();
		this.out =s.getOutputStream();	
	}	

	@Override
	public void run() 
	{
		try
        {
			while(true) 
            {
				try
				{
					rpg = new ReceiveParamGet(in);
				}
				catch(Exception e)
				{
					break;
				}
				
				try
				{
					sendProtocol(rpg);
				}
				catch(Exception e)
				{
					sendErrorMessage(e);
				}
			}
		}
		finally 
        {
			if(in != null)
            {
				try 
                { 
                    in.close(); 
                } 
                catch (IOException ex)  { /* ignore */ }
            }
			
            if(out != null)
            {
				try 
                { 
                    out.close(); 
                } 
                catch (IOException ex) { /* ignore */ } 
            }
		}
	}
	
	private void sendErrorMessage(Exception e)
	{
		try
		{
			SendParamSet sps = new SendParamSet(out);
			sps.addParam(e.getMessage());
			sps.write();
		}
		catch (Exception ex) { /* ignore */ }
	}

	private void sendProtocol(ReceiveParamGet rpg) throws Exception
    {
		SendParamSet sps = new SendParamSet(out);
        ServerAPIBridge serverApiBridge = new ServerAPIBridge();
        serverApiBridge.service(rpg, sps, clientIp);       
    }
}
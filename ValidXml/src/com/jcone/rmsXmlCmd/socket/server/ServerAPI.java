package com.jcone.rmsXmlCmd.socket.server;

import com.jcone.rmsXmlCmd.socket.param.ReceiveParamGet;
import com.jcone.rmsXmlCmd.socket.param.SendParamSet;
import com.jcone.rmsXmlCmd.test.server.TestServerApi;

public class ServerAPI 
{
	private static final String m_szLineCheck = "LINECHECK";
	private static final String m_szSendFile = "SENDFILE";
	private static final String m_szSendFileCheck = "SENDFILECHECK";
	    
    public ServerAPI()
    {
        
    }
    
    public SendParamSet serviceAPI(ReceiveParamGet rpg, SendParamSet sps, String client_ip) throws Exception
    {
    	SendParamSet returnSps = null;
        String szProtocolName = rpg.nextParam();
        System.out.println("szProtocolName ; "+szProtocolName);      
        
       if(m_szLineCheck.equals(szProtocolName))
       {    	   
    	   returnSps = TestServerApi.getInstance().lineCheck(rpg, sps, client_ip);
       }
       else if(m_szSendFile.equals(szProtocolName))
       {
    	   returnSps = TestServerApi.getInstance().sendFile(rpg, sps, client_ip);
       }
       else if(m_szSendFileCheck.equals(szProtocolName))
       {
    	   returnSps = TestServerApi.getInstance().sendFileCheck(rpg, sps, client_ip);
       }
      
       return returnSps;
    }
}
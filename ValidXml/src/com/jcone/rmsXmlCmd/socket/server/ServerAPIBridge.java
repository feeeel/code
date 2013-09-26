package com.jcone.rmsXmlCmd.socket.server;

import com.jcone.rmsXmlCmd.socket.param.ReceiveParamGet;
import com.jcone.rmsXmlCmd.socket.param.SendParamSet;

public class ServerAPIBridge extends Thread
{
	/**
	 * 서비스 호출
	 * @param rpg 수신 파라미터
	 * @param sps 송신 파라미터(메세지 전송용)
	 * @param client_ip 요청 아이피
	 * @param apiClassName 서버 서비스 API 클레스 명
	 * @throws Exception
	 */
	public void service(ReceiveParamGet rpg, SendParamSet sps , String client_ip) throws Exception
	{        
		ServerAPI serverAPI = new ServerAPI();          
		SendParamSet returnSps =  serverAPI.serviceAPI(rpg, sps, client_ip);	        
		returnSps.write();
	}
	       
	public void sendErrorMessage(Exception e, SendParamSet sps) 
    {
        try 
        {
        	sps.addIntParam(1);
        	sps.addParam(e.getMessage());
            sps.write();
        } 
        catch (Exception ex) 
        {
            new Exception(ex.getMessage());
        }
    }
}
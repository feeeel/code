package com.jcone.rmsXmlCmd.socket.client;

import java.io.File;
import java.io.IOException;

import com.jcone.rmsXmlCmd.common.utils.common.FileUtil;
import com.jcone.rmsXmlCmd.socket.param.ReceiveParamGet;
import com.jcone.rmsXmlCmd.socket.param.SendParamSet;
import com.jcone.rmsXmlCmd.socket.pool.SocketPoolObj;

public class ClientWorkAPI 
{
/**
 * @uml.property  name="index"
 */
private Integer index = null;
	
	public ClientWorkAPI(Integer i)
	{
		this.index = i; 
	}
	
	/**
	 * @return
	 */
	public Integer getIndex() 
    {
        return index;
    }
	
	/**
	 * 파일 전송 
	 * @param server_ip 서버아이피
	 * @param serverPort 서버 포트
	 * @param waitTime 전송 후 대기시간
	 * @param maxSize 생성갯수 
	 * @param sendFile 전송할 파일
	 * @return
	 */
	public boolean sendFile(SocketPoolObj spo, String server_ip,int serverPort, long waitTime, int maxSize, File sendFile, String rootPath)
    {    	
    	boolean bReceive = false;
        SendParamSet sps = null;
        ReceiveParamGet rpg =null;
        try
        {
	        // 파라미터 셋팅을 위한 소캣 셋팅
	        sps = new SendParamSet(spo.getOutput());
	        
	        // 서버 서비스명 제일 먼저 셋팅
	        sps.addParam("SENDFILE");
	        // 파일명 셋팅
	        String fileName = FileUtil.rootPathExist(rootPath, sendFile.getAbsolutePath());
	        sps.addParam(fileName);	        
	        sps.addLongParam(sendFile.length());
	        // 전송파일 셋팅
	        sps.addFileParam(sendFile);
	        // 전송       	         
	        sps.write();
	      
	        rpg = new ReceiveParamGet(spo.getInput());
	        bReceive = rpg.nextBooleanParam();
	        String message = null;
	        if(bReceive)	           	
	        {
	        	message = rpg.nextParam();	  
	        }
	        else
	        {
	        	message = rpg.nextParam(); 
 
	           	String msg = "IP :"+server_ip+", PORT ; "+serverPort+", FILE NAME ; "+sendFile.getAbsolutePath()+" - 다음과 같은 사항으로 오류로 전송되지 않았습니다. ; "+message;
	           	System.out.println(msg);
	         }			        

        } 
        catch (IOException io) 
        {       	
        	String msg = "IP :"+server_ip+", PORT ; "+serverPort+",, FILE NAME ; "+sendFile.getAbsolutePath()+" - 파일 전송 시도중 서버를 접속할 수 없는 오류가 발생하였습니다.";
        	System.out.println(msg);
        	io.printStackTrace();
    	}
        catch(Exception e)
        {        	
        	String msg = "IP :"+server_ip+", PORT ; "+serverPort+",, FILE NAME ; "+sendFile.getAbsolutePath()+" - 파일 전송 도중 내부 오류가 발생하였습니다.";
        	System.out.println(msg);   	
        	e.printStackTrace();
        }
        finally
        {        	
        	if(sps != null)
     		{
        		sps.reset();   
     		}
     		
     		if(rpg != null)
     		{
     			try
     			{
     				rpg.clear();     
     			}
     			catch(Exception e)
     			{
     				
     			}
     		}        	

        }// end try
       
        return bReceive;
    }
	
	public boolean sendFileCheck(SocketPoolObj spo, String server_ip,int serverPort, long waitTime, int maxSize, File sendFile, String rootPath)
    {    	
    	boolean bReceive = false;
        SendParamSet sps = null;
        ReceiveParamGet rpg =null;
        try
        {    
	        // 파라미터 셋팅을 위한 소캣 셋팅
	        sps = new SendParamSet(spo.getOutput());
	        
	        // 서버 서비스명 제일 먼저 셋팅
	        sps.addParam("SENDFILECHECK");
	        // 파일명 셋팅
	        String fileName = FileUtil.rootPathExist(rootPath, sendFile.getAbsolutePath());
	        sps.addParam(fileName);
	        // 전송       	         
	        sps.write();
	        rpg = new ReceiveParamGet(spo.getInput());
	        boolean bErr = rpg.nextBooleanParam();
	        String message = null;
	        if(bErr)	           	
	        { 	
	        	long sendSize = rpg.nextLongParam();
		        if(sendFile.length() == sendSize)
		        {
		        	bReceive = true;
		        }
	        	      		
	        }
	        else
	        {	        	
	        	message = rpg.nextParam();	           	          	 
	           		           	 
	           	String msg = "IP :"+server_ip+", PORT ; "+serverPort+", FILE NAME ; "+sendFile.getAbsolutePath()+" - 다음과 같은 사항으로 오류로 전송되지 않았습니다. ; "+message;
	           	System.out.println(msg);
	           	
	           	throw new Exception(message);
	         }   	       
	       
    	} 
        catch (IOException io) 
        {        	
        	String msg = "IP :"+server_ip+", PORT ; "+serverPort+",, FILE NAME ; "+sendFile.getAbsolutePath()+" - 파일 전송 체크 시도중 서버를 접속할 수 없는 오류가 발생하였습니다.";
        	System.out.println(msg);   		
        	io.printStackTrace();
    	}
        catch(Exception e)
        {        	
        	String msg = "IP :"+server_ip+", PORT ; "+serverPort+",, FILE NAME ; "+sendFile.getAbsolutePath()+" -  파일 전송 체크  도중 내부 오류가 발생하였습니다.";
        	System.out.println(msg);   	
        	e.printStackTrace();
        }
        finally
        {        	
        	if(sps != null)
     		{
        		sps.reset();
      		}
     		
     		if(rpg != null)
     		{
     			try
     			{
     				rpg.clear();     	
     			}
     			catch(Exception e)
     			{
     				
     			}
     		}        	
  		

        }// end try
       
        return bReceive;
    }
	
	public boolean checkLine(SocketPoolObj spo, String server_ip,int serverPort, long waitTime, int maxSize)
	{
		boolean bReceive = false;
        
        SendParamSet sps = null;
        ReceiveParamGet rpg =null;
        try
        {    
	        // 파라미터 셋팅을 위한 소캣 셋팅
	        sps = new SendParamSet(spo.getOutput());
	        
	        // 서버 서비스명 제일 먼저 셋팅
	        sps.addParam("LINECHECK");
	        // 전송       	         
	        sps.write();
	         
	        rpg = new ReceiveParamGet(spo.getInput());
	        bReceive = rpg.nextBooleanParam();
	        String message = null;
	        if(bReceive)	           	
	        {	        	
	        	message = rpg.nextParam();	        	
	        }
	        else
	        {	        	
	        	message = rpg.nextParam();	            	           		           	 
	           	String msg = "IP :"+server_ip+", PORT ; "+serverPort+" - 다음과 같은 사항으로 오류로 전송되지 않았습니다. ; "+message;
	           	System.out.println(msg);
	        }       
    	} 
        catch (IOException io) 
        {        	
        	String msg = "IP :"+server_ip+", PORT ; "+serverPort+" - 라인 시도중 서버를 접속할 수 없는 오류가 발생하였습니다.";
        	System.out.println(msg);   		
        	io.printStackTrace();
    	}
        catch(Exception e)
        {        	
        	String msg = "IP :"+server_ip+", PORT ; "+serverPort+" -  라인 체크  도중 내부 오류가 발생하였습니다.";
        	System.out.println(msg);   	
        	e.printStackTrace();
        }
        finally
        {        	
        	if(sps != null)
     		{
        		sps.reset();        	
     		}
     		
     		if(rpg != null)
     		{
     			try
     			{
     				rpg.clear();     			
     			}
     			catch(Exception e)
     			{     				
     			}
     		}        	

        }// end try
       
        return bReceive;
	}
	

}

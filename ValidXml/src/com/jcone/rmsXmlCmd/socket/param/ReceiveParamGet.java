package com.jcone.rmsXmlCmd.socket.param;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ReceiveParamGet 
{
	/**
	 * @uml.property  name="in"
	 */
	private BufferedInputStream in;
	
	/**
	 * @uml.property  name="blockSize"
	 */
	private int blockSize = 0;
	/**
	 * @uml.property  name="totalSize"
	 */
	private int totalSize = -1;
	
	private static final int delimByte = 1; 

	/**
	 * @uml.property  name="bytes" multiplicity="(0 -1)" dimension="1"
	 */
	private byte [] bytes = new byte[1024];   
    /**
	 * @uml.property  name="nSize"
	 */
    private int nSize = 1024;   

	/**
	 * @uml.property  name="tempFilePaths"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.String"
	 */
	private ArrayList tempFilePaths = new ArrayList();
	
	/**
	 * 프로토콜 디코더
	 * @param in 입력 스트림
	 * @throws GenericException
	 */
	public ReceiveParamGet(InputStream input) throws Exception
	{ 
		this.in = new BufferedInputStream(input);
		StringBuffer token = new StringBuffer(10);

		int b = -1;
		try
		{			
			while( true )
			{				
				b =  in.read();				
				if( b == -1 )
				{
					if( token.length() == 0 )
						throw new Exception("Error Disconnect");
				}
				else if( b != delimByte )
				    token.append( (char)b );
				else
				    break;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new Exception("Connection closed");			
		}
		
		totalSize = Integer.parseInt(token.toString());
		
	}
	
	/**
	 * 다음 파라미터를 가져온다.
	 * @return String 파라미터
	 * @throws GenericException 작업도중 일어나는 경우에 발생하는 Exception
	 */
	public String nextParam() throws Exception
	{
		if (totalSize <= 0)
        {
			throw new Exception("No more tokens");
        }

		try 
        {
			String token = "";
			if(blockSize > 0) 
            {
				if((token = getParam()) == null)
                {
					throw new Exception("Unexpected end of stream");
                }
				return token;
			}
			else 
            {
				if((token = getParam()) == null)
                {
					throw new Exception("Unexpected end of stream");
                }
				else if(token.charAt(0) == 'F') 
                {	
                    // 파일일 경우
					// File Block
					long fileBlockSize = Long.parseLong(token.substring(1,token.length()));
					if(fileBlockSize > 0) 
                    {
						// 임시 파일로 만들어서 파일 경로만 전달한다.
						File file = File.createTempFile("jc1_", ".tmp");
						FileOutputStream fileOutStream = new FileOutputStream(file);
						byte[] buf = new byte[(int)Math.min(64*1024, fileBlockSize)];

						while(fileBlockSize > 0) 
                        {
							int readSize = in.read(buf, 0, (int)Math.min(64*1024, fileBlockSize));
							fileBlockSize -= readSize;
							totalSize -= readSize;
							fileOutStream.write(buf, 0, readSize);
							fileOutStream.flush();
                            
						}

						fileOutStream.close();
						tempFilePaths.add(file.getPath());
						return file.getPath();
					}
					else 
                    { 
                        // 빈 파일이 올 경우..
						File file = File.createTempFile("triton_", ".tmp");
						return file.getPath();
						//return null;
					}
				}
				else 
                {
					blockSize = Integer.parseInt(token);
					return nextParam();
				}
			}
		}
		catch (IOException e) 
        {
			try 
            { 
                in.skip(in.available()); 
            }
			catch (IOException ex) 
            { /* Empty */ }
			
			throw new Exception(e);
		}
	}

	/**
	 * Int 다음 파라미터를 가져온다.
	 * @return Int 토큰
	 * @throws GenericException 작업도중 일어나는 경우에 발생하는 Exception
	 */
	public int nextIntParam() throws Exception
	{
		if (totalSize <= 0)
        {
			throw new Exception("No more tokens");
        }

		try 
        { 
			return Integer.parseInt(nextParam()); 
		}
		catch (NumberFormatException e) 
        { 
			throw new Exception("Not Integer Type"); 
		}
	}

	/**
	 * long 다음 파라미터를 가져온다.
	 * @return Long 토큰
	 * @throws	 GenericException 	 
	 */
	public long nextLongParam() throws Exception
	{	
		if (totalSize <= 0)
        {
			throw new Exception("No more tokens");
        }

		try 
        {
			return Long.parseLong(nextParam()); 
		}
		catch (NumberFormatException e) 
        { 
			throw new Exception("Not Long Type"); 
		}
	}

	/**
	 * float 다음 파라미터를 가져온다.
	 * @return Float 토큰
	 * @throws GenericException 작업도중 일어나는 경우에 발생하는 Exception
	 */
	public float nextFloatParam() throws Exception
	{
		if (totalSize <= 0)
        {
			throw new Exception("No more tokens");
        }

		try 
        {
			return Float.parseFloat(nextParam());
		}
		catch (NumberFormatException e) 
        {
			throw new Exception("Not Float Type");
		}
	}

	/**
	 * boolean 다음 파라미터를 가져온다.
	 * @return Boolean 토큰
	 * @throws GenericException 작업도중 일어나는 경우에 발생하는 Exception
	 */
	public boolean nextBooleanParam() throws Exception
	{
		if (totalSize <= 0)
        {
			throw new Exception("No more tokens");
        }
		
        try 
        {
			if(nextParam().equals("1"))
            {
                return true;
            }
			else
            {
                return false;            
            }
		}
		catch (NumberFormatException e) 
        {
			throw new Exception("Not Float Type");
		}
	}

	/**
	 * char 다음 파라미터를 가져온다.
	 * @return Char 토큰
	 * @throws GenericException 작업도중 일어나는 경우에 발생하는 Exception
	 */
	public char nextCharParam() throws Exception
	{
		if (totalSize <= 0)
        {
			throw new Exception("No more tokens");
        }

		try 
        {
			return nextParam().charAt(0);
		}
		catch (NumberFormatException e) 
        {
			throw new Exception("Not Char Type");
		}
	}

	/**
	 * 모든 temp 파일들을 삭제한다.
	 * @throws GenericException 작업도중 일어나는 경우에 발생하는 Exception
	 */
	public void clear() throws Exception
	{
		try 
        {
			// delete all temp files
			for(int i=0 ; i<tempFilePaths.size() ; i++) 
            {
				new File((String)tempFilePaths.get(i)).delete();
			}
			in.skip(in.available());
		}
		catch (IOException e) 
        {
			throw new Exception(e);
		}
	}

	/**
	 * 모든 temp 파일들을 삭제하고, InputStream을 닫는다.
	 */
	public void close()
	{ 
		try 
        {
			// delete all temp files
			for(int i=0 ; i<tempFilePaths.size() ; i++) 
            {
				new File((String)tempFilePaths.get(i)).delete();
			}
			if (in != null)
            {
                in.close();
            }
		}
		catch (IOException e) 
        { 
            /* ignore */ 
        }
	}
	
	/**
	 * 토큰을 받아온다.
	 * @return	KSC5601로 인코딩된 토큰
	 * @throws GenericException
	 */
	private String getParam() throws Exception
	{
		if(totalSize <= 0)	
        {
            return null;
        }
        
        if( blockSize > nSize )
        {
            bytes = new byte [blockSize];
            nSize = blockSize;
        }
		
        int b = -1, idx = 0;

		try
		{
			while(true)
			{
				b = in.read();
				if( b == -1 )
				{
					if( idx == 0 )
                    {
					    return null;
                    }
					else
                    {
					    break;
                    }
				}
				else if( b != delimByte )
                {
				   bytes[idx++] = (byte)b;
                }
				else
                {
				    break;
                }
			}
			
			totalSize -= (idx + 1);
			blockSize -= (idx + 1);

    		return new String( bytes, 0, idx, "KSC5601" );
		}
		catch( Exception e )
		{
			throw new Exception(e.toString());
		}
	}
}
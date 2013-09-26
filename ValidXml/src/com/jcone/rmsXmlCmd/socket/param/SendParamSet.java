package com.jcone.rmsXmlCmd.socket.param;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class SendParamSet 
{
	/**
	 * @uml.property  name="blocks"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.jcone.rmsXmlCmd.socket.param.SendParamSet$Block"
	 */
	private ArrayList blocks;
	/**
	 * @uml.property  name="out"
	 */
	private OutputStream out;
	//private BufferedOutputStream out;
	private static final int delimByte = 1;
		
	/**
	 * 생성자
	 * @param out 출력스트림 
	 * @throws GenericException
	 */
	public SendParamSet(OutputStream outStream) throws Exception
	{ 		
		if(outStream == null)
		{			
			throw new Exception("null outputstream");
		}
		
		blocks = new ArrayList();		
		out = outStream;
	}
	
	/**
	 * String 파라미터를 추가한다.
	 * @param	str 추가할 토큰
	 */
	public void addParam(String str)
	{
        Block lastBlock;
        if( blocks.size() == 0 || ((Block)blocks.get(blocks.size()-1)).isFile == true )
        {
            lastBlock = new Block( str );
            lastBlock.value.append( (char)delimByte );
    		blocks.add( lastBlock );
        }
        else
        {
            lastBlock = (Block)blocks.get(blocks.size()-1);
            lastBlock.value.append( str );
            lastBlock.value.append( (char)delimByte );
       }

	}

	/**
	 * int 파라미터를 추가한다.
	 * @param	n 추가할 토큰
	 */
	public void addIntParam(int n)
	{ 
		addParam(Integer.toString(n));
	}

	/**
	 * long 파라미터를 추가한다.
	 * @param l 추가할 토큰
	 */
	public void addLongParam(long l)
	{ 
		addParam(Long.toString(l));
	}

	/**
	 * Float 파라미터를 추가한다.
	 * @param	f 추가할 토큰
	 */
	public void addFloatParam(float f)
	{ 
		addParam(Float.toString(f));
	}

	/**
	 * boolean 파라미터를 추가한다.
	 * @param	b 추가할 토큰
	 */
	public void addBooleanParam(boolean b)
	{
		if(b == true)	addParam("1");
		else			addParam("0");
	}

	/**
	 * char 파라미터를 추가한다.
	 * @param	c 추가할 토큰
	 */
	public void addCharParam(char c)
	{
		addParam(new Character(c).toString());
	}

	/**
	 * File 파라미터를 추가한다.
	 * @param	path 추가할 파일의 path
	 */
	public void addFileParam(String path) throws Exception
	{
	    File file = new File(path);
		if(!file.exists()) throw new Exception("cann't find file [" + file.getPath() +"]");
	    
	    blocks.add(new Block(path, true));
	}

	/**
	 * File 파라미터를 추가한다.
	 * @param	file 추가할 파일
	 */
	public void addFileParam(File file) throws Exception
	{
		if(!file.exists()) throw new Exception("cann't find file [" + file.getPath() +"]");

		blocks.add(new Block(file.getPath(), true));
	}

	/**
	 * 서버에 셋팅된 파라미터를 보낸다.
	 * 
	 * @return	the resultant byte array
	 */
	public void write() throws Exception
	{
		try 
        {
			int totalSize = 0;
			for(int i=0 ; i<blocks.size() ; i++) 
            {
				Block block = (Block)blocks.get(i);
				if(block.isFile == false) 
                {
					// String의 length()는 한글 1자의 길이를 1로 인식하기 때문에
					// 한글 1자가 2byte가 되도록 해야한다.
					block.blockSize = block.value.toString().getBytes().length;
				}
				else 
                {
					if(block.value == null || block.value.length() == 0)
                    {
						block.blockSize = 0;
                    }
					else
                    {
						block.blockSize = (new File(block.value.toString())).length();
						totalSize += 1;
                    }
				}
				totalSize += (block.blockSize +
						(new Long(block.blockSize)).toString().length() + 1);
			}

			// get Total Size
			out.write(Integer.toString(totalSize).getBytes());
			out.write(delimByte);

			for(int i=0 ; i<blocks.size() ; i++) 
            {
				Block block = (Block)blocks.get(i);
				if(block.isFile == false) 
                {
					// String Block
					// [string size][string]
					out.write(Long.toString(block.blockSize).getBytes());
					out.write(delimByte);
					out.write(block.value.toString().getBytes());
				}
				else 
                {
					// File Block
					// F[filesize][delim][filebody]
					out.write('F');
					out.write(Long.toString(block.blockSize).getBytes());
					out.write(delimByte);

					// null file이 아닐 경우 파일을 넣어 준다.
					if(block.value != null && block.value.length() > 0) 
                    {
						File file = new File(block.value.toString());
						if(file.exists() == true) 
                        {
							//int bufSize = (int)Math.max(64*1024, block.blockSize);
                            
							byte[] buf = new byte[64*1024];		// 64k

							FileInputStream fileInput =	new FileInputStream(block.value.toString());
							int n = 0;
							while ((n = fileInput.read(buf)) != -1) 
                            {
								out.write(buf, 0, n);
								out.flush();		// add by han1000 2006-04-10
							}
							fileInput.close();		// add by rainyzone 2004-11-08
						}
						else
                        {
							throw new Exception("cann't find file [" + file.getPath() +"]");
                        }
					}
				}
			}
			out.flush();
		}
		catch (IOException e) 
        {
			throw new Exception(e);
		}
		catch(Exception e) 
        {
			throw new Exception(e.toString());
		}
	}

	/**
	 * 파라미터를 초기화한다.
	 */
	public void reset()
	{
		blocks = new ArrayList(0);
	}
	
	public void close()
	{
		try
		{
			if(out != null)
			{
				out.close();
			}
		}
		catch(Exception e)
		{
			
		}
	}

	/**
	 *	inner class Block 
	 * 
	 */
	private static class Block
	{
		private StringBuffer value = new StringBuffer();
		private boolean isFile = false;
		private long blockSize = 0;

		/**
		 * 생성자
		 * @param	block 크기 
		 */
		private Block( String value )
		{
		    this.value = new StringBuffer(10240);
		    this.value.append(value);
			this.isFile = false;
		}
		/**
		 * 생성자
		 * @param value block 크기 
		 * @param isFile 파일인지 여부
		 */
		private Block(String value, boolean isFile)
		{
			this.value = new StringBuffer(10240);
			this.value.append(value);
			this.isFile = isFile;
		}
	}
	
}

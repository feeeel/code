/**========================================================
 *@FileName       : SocketPoolMng.java
 *Open Issues     : N/A
 *
 *@LastModifyDate : 2011. 10. 12.
 *@LastModifier   : 이명재
 *@LastVersion    : 1.0
 *Change History
 *    2011. 10. 12.    이명재    1.0    최초생성 
 =========================================================*/
package com.jcone.rmsXmlCmd.socket.pool;

import java.io.IOException;
import java.net.SocketException;
import java.util.Vector;

public class SocketPoolMng 
{
	/**
	 * @uml.property  name="serverIp"
	 */
	private String serverIp = "localhost";
	/**
	 * @uml.property  name="serverPort"
	 */
	private int serverPort = 5000; 
	/**
	 * @uml.property  name="currentSize"
	 */
	private int currentSize;                   // 현재 Pool Size
	/**
	 * @uml.property  name="usedCount"
	 */
	private int usedCount = 0;                 // 현재 사용중인 갯수
	/**
	 * @uml.property  name="connectErrCount"
	 */
	private int connectErrCount = 0;          // 풀생성 시 연결에러 갯수
	/**
	 * @uml.property  name="lastCheckTime"
	 */
	private long lastCheckTime;                // 마지막 체크 시간
	/**
	 * @uml.property  name="readyPool"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.jcone.rmsXmlCmd.socket.pool.SocketPoolObj"
	 */
	private Vector readyPool;        // 사용 가능한 SocketPoolObj 관리
	/**
	 * @uml.property  name="pool"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="com.jcone.rmsXmlCmd.socket.pool.SocketPoolObj"
	 */
	private Vector pool;             // 전체 SocketPoolObj 관리
	/**
	 * @uml.property  name="maxSize"
	 */
	private int maxSize=1;
	/**
	 * @uml.property  name="waitTime"
	 */
	private long waitTime=60*1000;
		
	/**
	 * @uml.property  name="checkTime"
	 */
	private final int checkTime = 60 * 1000;   // Socket 강제 회수 검사 시간
	/**
	 * @uml.property  name="maxRentTime"
	 */
	private final int maxRentTime = 5000;      // Socket rent 최대 시간
	private static SocketPoolMng manager = null;
		
	
	/**
	 * 생성자를 싱글톤 기법으로 생성
	 * @param server_ip 전송할 서버 IP
	 * @param port 전송할 서버 포트
	 * @param waitingTime 전송 후 대기 시간
	 * @param maxSize pool 생성 갯수
	 * @return SocketPoolMng  
	 * @throws IOException 
	 * @throws SocketException
	 */
	public static synchronized SocketPoolMng getInstance(String server_ip, int port, long waitingTime, int maxSize) throws IOException, SocketException {

		if ( manager == null ) {
			
			manager = new SocketPoolMng(server_ip, port, waitingTime, maxSize);

		} // end if ( manager == null )

		return manager;

	} // end public static synchronized SocketPoolManager getInstance()

	/**
	 * 생성자
	 * @param server_ip 전송할 서버 IP
	 * @param port 전송할 서버 포트
	 * @param waitingTime 전송 후 대기 시간
	 * @param maxSize pool 생성 갯수
	 * @throws IOException
	 * @throws SocketException
	 */
	private SocketPoolMng(String server_ip, int port, long waitingTime, int maxSize) throws IOException, SocketException 
	{		
		
		this.serverIp = server_ip;
		this.serverPort = port;
		this.waitTime = waitingTime;
		this.maxSize = maxSize;
		readyPool = new Vector();
		pool = new Vector();

		initPool();
		lastCheckTime = System.currentTimeMillis();

	} // end private SocketPoolManager()

	/**
	 * 소캣 풀을 초기화한다.
	 * @throws SocketException
	 * @throws IOException
	 */
	private void initPool() throws SocketException, IOException 
	{
		// 초기값 만큼 풀을 생성한다.
		SocketPoolObj obj = null;
		currentSize = 0;
		usedCount = 0;

		for (int i = 0; i < maxSize; i++) 
		{
			try 
			{
				obj = new SocketPoolObj(serverIp, serverPort);
				readyPool.addElement( obj );
				pool.addElement( obj );
				currentSize++;
			} 
			catch (SocketException e) 
			{
				connectErrCount++;
				if(connectErrCount >= 5 && currentSize == 0)
				{					
					throw new SocketException("Server IP ; "+serverIp+", PORT ; "+serverPort +" 소켓 생성을 되지 않습니다. 전송 서버 구동여부를 확인하십시오.");
				}			

			} // end try

		} // end for


		if (currentSize == 0) 
		{
			// 하나도 생성하지 못했으면 예외를 던진다.					
			throw new SocketException("Server IP ; "+serverIp+", PORT ; "+serverPort +" 소켓 생성을 되지 않습니다. 전송 서버 구동여부를 확인하십시오.");
		} // end if (currentSize == 0)

	} // end private void initPool() throws SocketException, IOException

	/**
	 * 새로운 SocketPoolObj 객체를 얻어온다.
	 *
	 * @return 생성된 SocketPoolObj 객체
	 * @throws IOException
	 * @throws SocketException
	 */
	private synchronized SocketPoolObj createSocketObject() throws IOException, SocketException 
	{
		SocketPoolObj obj = null;

		try 
		{
			obj = new SocketPoolObj(serverIp, serverPort);
			pool.addElement( obj );
			currentSize++;

		} catch (IOException e) {			

			throw e;

		} // end try

		return obj;

	} // end private SocketPoolObj createSocketObject() throws IOException, SocketException


	/**
	 *  전체 소켓을 닫는다.
	 */
	public synchronized void closeAll() 
	{		
		if(manager != null)
		{
			if(usedCount == 0)
			{
				SocketPoolObj obj;
				int length = pool.size();
		
				for (int i = 0; i < length; i++) 
				{
		
					obj = (SocketPoolObj) pool.elementAt(i);
					obj.close();
		
				} // end for
			
				manager = null;
			}
		}		

	} // end private closeAll()

	/**
	 * 소켓을 새로 연결한다.
	 *
	 * @throws SocketException
	 * @throws IOException
	 */
	
	/**
	 * 기존 풀에 연결된 소캣을 모두 해지하고 다시 연결을 하여 풀을 채운다.
	 * @throws SocketException
	 * @throws IOException
	 */
	public synchronized void reset() throws SocketException, IOException 
	{
		// 전체 소켓을 닫는다.
		closeAll();

		// 기본 풀들을 초기화 한다.
		readyPool.clear();
		pool.clear();

		// 새롭게 풀들을 설정한다.
		initPool();

	} // end public void reset() throws SocketException, IOException

	
	/**
	 * Pool에 있는 SocketPoolObj 하나를 얻어 온다.
	 * (만일 waitingTime값 만큼 기다린 다음 반납 된것이 없으면 null을 리턴)
	 * @return
	 * @throws IOException
	 */
	private SocketPoolObj getObject() throws IOException 
	{

		SocketPoolObj obj = null;
		if (readyPool.size() > 0) 
		{
			// readyPool에 남는 것이 있다면 할당한다.
			obj = (SocketPoolObj) readyPool.firstElement();
			readyPool.removeElementAt(0);

		} // end if (readyPool.size() > 0)
		else if (maxSize == 0 || currentSize < maxSize) 
		{
			// 남는것이 없고 현재 최대 개수만큼 생성되지 않았다면 새로 생성한다.
			obj = createSocketObject();

		} // end else if (maxSize == 0 || currentSize < maxSize)

		if (obj != null) 
		{
			usedCount++;
			obj.setuseTime(System.currentTimeMillis());

		} // end if (obj != null)

		return obj;

	} // end private SocketPoolObj getObject()
	
	/**
	 * 연경되어 있는 풀에 소캣객체를 얻는다.
	 * @return SocketPoolObj 소캣객체
	 * @throws IOException
	 */
	public synchronized SocketPoolObj getSocketObject() throws IOException 
	{

		long startTime = System.currentTimeMillis();
		long currentTime;
		SocketPoolObj obj = null;
		while ((obj = getObject()) == null) 
		{
			try
			{
				wait(waitTime);
			}
			catch(Exception e)
			{
				
			}
//			currentTime = System.currentTimeMillis();
//          
//			if (currentTime - lastCheckTime >= checkTime) 
//			{
//				// 만일 Pool이 비어 있고 소켓 체크시간이 1분이 넘었다면
//				// 잘못된 상황에서 반납 안된 소켓을 찾아서 강제 반납한다.
//				for (int i = 0; i < currentSize; i++) 
//				{
//
//					obj = (SocketPoolObj) pool.elementAt(i);
//
//					if (lastCheckTime - obj.getuseTime() >= maxRentTime) 
//					{
//						// 만일 체크 시간 보다 이전에 사용되어진 것이라면 강제 반납한다.
//						release(obj);
//
//					} // end if (lastCheckTime - obj.getRentTime() > 5000)
//
//				} // end for
//
//				lastCheckTime = System.currentTimeMillis();
//
//			} // end if (currentTime - lastCheckTime > 60 * 1000)
			
//			try 
//			{
//				// 기다리는 시간동안 wait() 한다.
//				// 하지만 기다리는 시간내에 notify 하면 깨어난다.
//				// 따라서 기다리는 시간 이내에 깨어 날 수도 있다.
//				wait(waitTime);
//
//			} catch (InterruptedException e) {}
//
//			if ((System.currentTimeMillis() - startTime) >= waitTime) 
//			{
//				// 기다리는 시간이 넘었다면 null을 리턴
//				//return null;				
//				throw new IOException("소켓 연결중 설정된 대기시간을 초과하여 오류가 발생하였습니다.");
//
//			} // end if ((System.currentTimeMillis() - startTime) >= timeout)

		} // end while ((obj = getObject()) == null)

		return obj;

	} // end public synchronized SocketPoolObj getSocketObject()

	
	/**
	 * 사용이 끝난 풀 객체를 반납한다.
	 * @param obj 반납할 객체
	 */
	public synchronized void release(SocketPoolObj obj) 
	{
//System.out.println("release before usedCount ; "+usedCount);
		obj.resetOkState();
		readyPool.addElement(obj);
		usedCount--;
//System.out.println("release after usedCount ; "+usedCount);
		//notify();
		notifyAll();	

	} // end public synchronized void release(SocketPoolObj obj)

		
	/**
	 * 소캣 풀 객체 중 연결이 끝어진 풀 객체를 반납 연결 해지한다.(연결 Exception 이 발생시 사용)
	 * @param obj 반납해지할 객체
	 */
	public synchronized void releaseToBad(SocketPoolObj obj) 
	{

		try {

			// 연결을 닫고 새로 연결을 한다.
			obj.close();
			obj.createSocket(serverIp, serverPort);

			// 연결에 성공하면 readyPool에 반납한다.
			release(obj);

		} catch (IOException e) {
			// 생성에 실패하면 pool에서 삭제하고 현재 크기를 줄인다.
			currentSize--;
			pool.removeElement(obj);
			obj = null;

			e.printStackTrace();

		} // end try

	} // end public synchronized void release(SocketPoolObj obj)
	
	/**
	 * 풀 객체를  사용중인지를 체크한다.(전체 풀 안에 소캣을 해지하기 전에 체크)
	 * @return
	 */
	public boolean isUsedPool()
	{
		boolean bCheck =true;
		if(usedCount == 0 && readyPool.size() == maxSize)
		{
			bCheck = false;
		}
		
		return bCheck;
	}

}

/**========================================================
 *@FileName       : SocketPoolObj.java
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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class SocketPoolObj 
{	
	/**
	 * @uml.property  name="useTime"
	 */
	private long useTime;                           // 사용시간
	/**
	 * @uml.property  name="isOk"
	 */
	private boolean isOk;                           // 작업 성공 여부
	
	/**
	 * @uml.property  name="socket"
	 */
	private Socket socket = null;                   // Socket
	/**
	 * @uml.property  name="output"
	 */
	private OutputStream output;            // OutputStream
	/**
	 * @uml.property  name="input"
	 */
	private InputStream input;              // InputStream
	
	/**
	 * 생성자
	 *
	 * @param i PoolObject의 인덱스 값
	 * @throws IOException
	 * @throws SocketException
	 */

	SocketPoolObj(String host, int port) throws IOException, SocketException 
	{		
		createSocket(host, port);

	} // end SocketPoolObject(Integer i) throws java.io.IOException, java.net.SocketException


	/**
	 * @return
	 */
	public Socket getSocket() {
		return socket;
	}


	/**
	 * @param socket
	 */
	public void setSocket(Socket socket) {
		this.socket = socket;
	}


	/**
	 * @return
	 */
	public OutputStream getOutput() {
		return output;
	}


	/**
	 * @param output
	 */
	public void setOutput(OutputStream output) {
		this.output = output;
	}


	/**
	 * @return
	 */
	public InputStream getInput() {
		return input;
	}


	/**
	 * @param input
	 */
	public void setInput(InputStream input) {
		this.input = input;
	}


	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

	/**
	 * 서버와 연결을 한다.
	 *
	 * @throws IOException
	 */

	void createSocket(String host, int port) throws IOException {
		
		socket = new Socket(host, port);
		setSocket(socket);
		output = (getSocket()).getOutputStream();		
		setOutput(output);
		input  =(getSocket()).getInputStream() ;
		setInput(input);
	
	} // end void createSocket() throws IOException

	/**
	 * Socket을 닫는다.
	 */

	void close() {

		try { if (output != null) output.close(); } catch (Exception e) {}
		try { if (input != null) input.close(); } catch (Exception e) {}
		try { if (socket != null) socket.close(); } catch (Exception e) {}

	} // end public void close()

	/**
	 * Pool에서 대여된 시간을 설정한다.
	 *
	 * @param time 대여된 시간
	 */

	public void setuseTime(long time) {

		useTime = time;

	} // end public void setRentTiem(long time)

	/**
	 * Pool에서 대여된 시간을 리턴한다.
	 *
	 * @return Pool에서 대여된 시간
	 */

	public long getuseTime() {

		return useTime;

	} // end public long getuseTime()

	/**
	 * 성공 여부를 재 설정한다.
	 */

	public void resetOkState() {

		isOk = false;

	} // end public void resetOkState()

	/**
	 * 성공여부를 리턴한다.
	 * @return   성공여부 (true : 성공, false : 실패);
	 * @uml.property  name="isOk"
	 */

	public boolean isOk() {

		return isOk;

	} // end public boolean isOk()
	

}

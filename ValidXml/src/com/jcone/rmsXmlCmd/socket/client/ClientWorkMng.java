/**========================================================
 *@FileName       : ClientWorkMng.java
 *Open Issues     : N/A
 *
 *@LastModifyDate : 2011. 10. 12.
 *@LastModifier   : 이명재
 *@LastVersion    : 1.0
 *Change History
 *    2011. 10. 12.    이명재    1.0    최초생성 
 =========================================================*/

package com.jcone.rmsXmlCmd.socket.client;

import java.util.Hashtable;
import java.util.Stack;

public class ClientWorkMng 
{
	private static ClientWorkMng manager = null;
	/**
	 * @uml.property  name="usedCnt"
	 */
	private int usedCnt = 0;
	/**
	 * @uml.property  name="pool"
	 * @uml.associationEnd  qualifier="index:java.lang.Integer com.jcone.rmsXmlCmd.socket.client.ClientWorkAPI"
	 */
	private Hashtable pool = new Hashtable();
	/**
	 * @uml.property  name="freeStack"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="java.lang.Integer"
	 */
	private Stack freeStack = new Stack();
	/**
	 * @uml.property  name="maxSize"
	 */
	private int maxSize = 1;
	/**
	 * @uml.property  name="waitTime"
	 */
	private long waitTime = 60*1000;
	
	/**
	 * 생성자
	 */
    private ClientWorkMng(int maxCount, long waitingTime) 
    {  
    	this.maxSize = maxCount;
    	this.waitTime = waitingTime;
        for(int i=0;i<maxSize;i++)
        {
        	Integer index = new Integer(i);
            pool.put( index, new ClientWorkAPI(index));
            freeStack.push(index);            
        }
    }
    
    /**
     * 싱글턴 타입 생성자
     * @return
     */
    public static synchronized ClientWorkMng getInstance(int maxCount, long waitingTime)
    {   
    	
        if ( manager == null ) {
            manager = new ClientWorkMng(maxCount, waitingTime);
        }
        return manager;    
    }
	
    /**
     * 
     * @return
     * @throws Exception
     */
    public ClientWorkAPI getWorkObject() throws Exception 
    {
    	Integer index = null;
	    long start = System.currentTimeMillis();
	    synchronized(this)
	    {
	    	while( freeStack.isEmpty())
	    	{    		
	    			// 나머지는 모두 아래 wait(timeout) 에서 대기함.
	    			// 아래에서 1 초를 wait 하게 하지만, 실제로는 그 이전에
	    			// notify() 에 의해 깨어나게 됨.
	    			wait(1000);	            
            }
	    	
	    	long end = System.currentTimeMillis();

// 무한 waiting 을 하지 못하게 하려면 아래 내용을 적용하면 됩니다.
//	    	if ( freeStack.isEmpty() && (end - start) >= waitTime) 
//	    	{        	   
//	    		throw new Exception("getWorkObject : timeout(" +waitTime + ") exceed");
//	    	}

	    	index =(Integer)freeStack.pop();           
        }
	        
	    ClientWorkAPI obj = (ClientWorkAPI)pool.get(index);
	    usedCnt++;      
    
	    return obj;
    }
	    
    public void release(ClientWorkAPI obj) 
    {
     	synchronized ( this )
    	{
    		freeStack.push(obj.getIndex());
    		usedCnt--;
    		//notify();
    		notifyAll(); 
    		// 만약 notifyAll() 을 하면 getPoolObject()에서 wait()에서 걸린
    		// 모든 Thread 가 일시에 일어나게 되어 쓸데없이 전부 while 문을 동시에
    		// 확인하게되고, 이는 CPU 부하를 야기할 것으로 생각되나,
    		// 일부 책에서는 notify() 대신 notifyAll() 을 권장하기도 함.
    		// 두가지를 모두 테스트해 보면 아주 재밌는 현상을 만날 수 있을 것임.
        }
    }   
	    
    public boolean isActiveThread()
    {
    	boolean active = true;
    	
    	if(usedCnt == 0 && freeStack.size() == maxSize)
    	{
    		active = false;
    	}
	    	
    	return active;
    }
}
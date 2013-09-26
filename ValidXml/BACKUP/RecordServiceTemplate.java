package com.jcone.rmsXmlCmd.common.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.jcone.rmsXmlCmd.common.dao.RecordDAO;
import com.jcone.rmsXmlCmd.common.persistence.SqlConfig;
import com.jcone.rmsXmlCmd.common.utils.marshalling.ObjectFactory;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList.ReturnRow;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList.ReturnRow.ReturnCol;

public abstract class RecordServiceTemplate
{
	protected final Log log = LogFactory.getLog(this.getClass());
	SqlMapClient sqlMapClient = null;
	RecordDAO dao = null;
	ObjectFactory factory = new ObjectFactory();
	
	String methodName = "";		// 실행시킬 메소드 이름
	Object o;					// 인보크될 객체 (RecordService 인스턴스)
	Boolean needRts;			// 예외 발생시 발생 메세지 반환 여부
	WorkRoot paramWorkRoot;
	Boolean b = false;			// 트렌젝션 처리 유무 (기본값 false)
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object getO() {
		return o;
	}

	public void setO(Object o) {
		this.o = o;
	}

	public WorkRoot getParamWorkRoot() {
		return paramWorkRoot;
	}

	public void setParamWorkRoot(WorkRoot paramWorkRoot) {
		this.paramWorkRoot = paramWorkRoot;
	}

	public Boolean getNeedRts() {
		return needRts;
	}

	public void setNeedRts(Boolean needRts) {
		this.needRts = needRts;
	}

	public Boolean getB() {
		return b;
	}

	public void setB(Boolean b) {
		this.b = b;
	}	
	
	public RecordServiceTemplate()
	{
		SqlConfig.instance();
		sqlMapClient = SqlConfig.getSqlMapInstance();
		dao = new RecordDAO(sqlMapClient);
	}

	protected abstract WorkRoot doit(String methodName, Object o, WorkRoot paramWorkRoot) throws Exception;
	
	/**
	 * 해당 작업을 시작
	 * 
	 * @param methodName 실행시킬 메소드 이름
	 * @param needRts 리턴메세지 필요 유뮤 true / false
	 * @param paramWorkRoot 
	 * @return
	 */
	public final WorkRoot proccess(String methodName, Boolean needRts) throws Exception
	{
		WorkRoot rtsWorkRoot = null;
		
		try 
		{
			sqlMapClient.startTransaction();
			rtsWorkRoot = (WorkRoot) doit(methodName, o, paramWorkRoot);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			if (needRts)
			{
				ReturnCol col = factory.createWorkRootReturnListReturnRowReturnCol();
				col.setId("SELECT_RESULT");
				col.setValue(e.getMessage());
				
				ReturnRow row = factory.createWorkRootReturnListReturnRow();
				row.getReturnCol().add(col);
				
				ReturnList list = factory.createWorkRootReturnList();
				list.setId(1);
				list.getReturnRow().add(row);
				
				paramWorkRoot.getReturnList().add(list);			
			}
			else
				throw e;
		}
		finally
		{
			try
			{
				if (b)
					sqlMapClient.commitTransaction();
				
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				
				if (needRts)
				{
					ReturnCol col = factory.createWorkRootReturnListReturnRowReturnCol();
					col.setId("SELECT_RESULT");
					col.setValue(e.getMessage());
					
					ReturnRow row = factory.createWorkRootReturnListReturnRow();
					row.getReturnCol().add(col);
					
					ReturnList list = factory.createWorkRootReturnList();
					list.setId(1);
					list.getReturnRow().add(row);
					
					paramWorkRoot.getReturnList().add(list);
				}
				else
					throw e;
			}
		}
		
		if (null==rtsWorkRoot)
			return paramWorkRoot;
		
		return rtsWorkRoot;
	}
}
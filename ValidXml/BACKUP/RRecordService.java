package com.jcone.rmsXmlCmd.common.service;

import java.lang.reflect.Method;

import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList.ReturnRow;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList.ReturnRow.ReturnCol;

public class RRecordService extends RecordServiceTemplate
{
	@Override
	protected WorkRoot doit(String methodName, Object o, WorkRoot paramWorkRoot) throws Exception
	{
		Method[] methods = o.getClass().getMethods();
		
		for (Method m : methods)
		{
		     if (m.getName().equals(methodName))
		     {
		    	 log.debug(m.getName() + " // " + m.getReturnType().getName());
		    	 return (WorkRoot) m.invoke(o, paramWorkRoot); 
		     }			
		}
		
		return paramWorkRoot;
	}
	
	public WorkRoot getServerTime(WorkRoot paramWorkRoot) throws Exception
	{
		String rtsStr = "";
		ReturnList list = factory.createWorkRootReturnList();
		list.setId(1);
		
		ReturnRow row = factory.createWorkRootReturnListReturnRow();
		
		rtsStr = dao.getServerTime();

		ReturnCol col = factory.createWorkRootReturnListReturnRowReturnCol();
		col.setId("SERVER_DATE_TIME");
		col.setValue(rtsStr);
		
		row.getReturnCol().add(col);
		
		list.getReturnRow().add(row);
		paramWorkRoot.getReturnList().add(list);
		return paramWorkRoot;
	}	
}
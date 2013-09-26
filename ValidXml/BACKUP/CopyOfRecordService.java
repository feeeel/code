package com.jcone.rmsXmlCmd.common.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.jcone.rmsXmlCmd.common.dao.RecordDAO;
import com.jcone.rmsXmlCmd.common.persistence.SqlConfig;
import com.jcone.rmsXmlCmd.common.utils.common.StringUtil;
import com.jcone.rmsXmlCmd.common.utils.marshalling.ObjectFactory;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ParamList.Param;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList.ReturnRow;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList.ReturnRow.ReturnCol;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CopyOfRecordService
{
	protected final Log log = LogFactory.getLog(this.getClass());
	SqlMapClient sqlMapClient = null;
	RecordDAO dao = null;
	ObjectFactory factory = new ObjectFactory();
	
	public CopyOfRecordService()
	{
		SqlConfig.instance();
		sqlMapClient = SqlConfig.getSqlMapInstance();
		dao = new RecordDAO(sqlMapClient);
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W001
	 * DESCRIPTION	: 서버시간조회
	 * 
	 * RFID 연계모듈의 시간정보를 조회 한다. 전달 값은 없으며 서버시간을 전송 한다.
	 * </pre>
	 * 
	 * @param paramWorkRoot
	 * @return
	 */
	public WorkRoot getServerTime(WorkRoot paramWorkRoot)
	{
		String rtsStr = "";
		ReturnList list = factory.createWorkRootReturnList();
		list.setId(1);
		
		ReturnRow row = factory.createWorkRootReturnListReturnRow();
		
		try
		{
			rtsStr = dao.getServerTime();

			ReturnCol col = factory.createWorkRootReturnListReturnRowReturnCol();
			col.setId("SERVER_DATE_TIME");
			col.setValue(rtsStr);
			
			row.getReturnCol().add(col);
		}
		catch (Exception e)
		{
			ReturnCol col = factory.createWorkRootReturnListReturnRowReturnCol();
			col.setId("SELECT_RESULT");
			col.setValue(e.getMessage());
			
			row.getReturnCol().add(col);
			
			e.printStackTrace();
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
		list.getReturnRow().add(row);
		paramWorkRoot.getReturnList().add(list);
		return paramWorkRoot;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W002
	 * DESCRIPTION	: 사용자조회
	 * 
	 * 부서코드와 사용자 로그인 ID로 사용자정보를 조회 한다.
	 * 조회된 정보가 없거나 같은 사용자 로그인 ID를 가진 여러 사용자 정보가 조회 될 수 있다.
	 * </pre>
	 * 
	 * @param paramWorkRoot
	 * @return
	 */
	public WorkRoot getUserInfo(WorkRoot paramWorkRoot)
	{
		try
		{
			//invokeMethod(o, "invokeMethodTest", paramWorkRoot);
			List<Param> paramList = paramWorkRoot.getParamList().get(0).getParam();
			HashMap paramMap = new HashMap();
			
			for (Param p : paramList)
				paramMap.put(p.getId(), p.getValue());
			
			List<Map> rtsList = dao.getUserInfo(paramMap);
			
			// return_list loop and setting
			for (int i=0, maxLoop=rtsList.size(), idx=i+1; i<maxLoop; i++)
			{
				ReturnList list = factory.createWorkRootReturnList();
				list.setId(idx++);
				
				Map<String, Object> rtsMap = rtsList.get(i);
				ReturnRow row = factory.createWorkRootReturnListReturnRow();
				
				// return_row loop and setting
				for (Map.Entry<String, Object> entry : rtsMap.entrySet())
				{
					if (null == entry.getValue())
						continue;
					
					ReturnCol col = factory.createWorkRootReturnListReturnRowReturnCol();
					col.setId(entry.getKey());
					col.setValue(entry.getValue().toString());
					
					row.getReturnCol().add(col);
				}
				
				list.getReturnRow().add(row);
				paramWorkRoot.getReturnList().add(list);
			}
		}
		catch (Exception e)
		{
			ReturnCol col = factory.createWorkRootReturnListReturnRowReturnCol();
			col.setId("SELECT_RESULT");
			col.setValue(e.getMessage());
			//col.setValue(StringUtil.exceptionToStr(e));
			//log.debug(e.getClass().getPackage().getName() + "." + e.getClass().getSimpleName());
			//log.debug("|"+e.getMessage()+"|");
			
			ReturnRow row = factory.createWorkRootReturnListReturnRow();
			row.getReturnCol().add(col);
			
			ReturnList list = factory.createWorkRootReturnList();
			list.setId(1);
			list.getReturnRow().add(row);
			
			paramWorkRoot.getReturnList().clear();
			paramWorkRoot.getReturnList().add(list);
			
			e.printStackTrace();
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		
		return paramWorkRoot;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W003
	 * DESCRIPTION	: 공통코드조회
	 * 
	 * 표준기록관리시스템의 공통코드 정보를 조회 한다.
	 * 공통코드 정보가 없거나 여러 공통코드 정보가 조회될 수 있다.
	 * </pre>
	 * 
	 * @param paramWorkRoot
	 * @return
	 */
	public WorkRoot getCommonCodeInfo(WorkRoot paramWorkRoot)
	{
		return null;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W004
	 * DESCRIPTION	: 위치정보조회
	 * 
	 * 사용자의 ID로 표준기록관리시스템의 서고, 서가 정보를 조회 한다.
	 * 사용자가 소속된 기록관의 정보만 조회 되며 조회된 정보가 없거나 여러 서고, 서가 정보가 조회 될 수 있다
	 * </pre>
	 * 
	 * @param paramWorkRoot
	 * @return
	 */
	public WorkRoot getRecordLocInfo(WorkRoot paramWorkRoot)
	{
		return null;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W005
	 * DESCRIPTION	: 서가배치
	 * 
	 * 기록물 철의 서가배치 정보를 저장 한다.
	 * 모든 PARAM_LIST의 서가배치 정보는 하나의 서가배치정보와 각각의 서가배치세부 정보로 저장 되며,
	 * 하나의 RETURN_LIST로 저장 결과를 반환 한다.
	 * </pre>
	 * 
	 * @param paramWorkRoot
	 * @return
	 */
	public WorkRoot setRecordLocInfo(WorkRoot paramWorkRoot)
	{
		return null;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W006
	 * DESCRIPTION	: 서가 재배치 정보를 등록
	 * 
	 * 기록물 철의 서가배치 정보를 수정 한다.
	 * PARAM_LIST는 하나 혹은 여러 개의 정보를 가질 수 있다.
	 * PARAM_LIST의 서가배치 정보는 각각의 서가배치세부 정보를 수정 하며, 각각의 RETURN_LIST로 수정 결과를 반환 한다.
	 * </pre>
	 * 
	 * @param paramWorkRoot
	 * @return
	 */
	public WorkRoot setRecordRelocInfo(WorkRoot paramWorkRoot)
	{
		return null;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W007
	 * DESCRIPTION	: 정수점검 계획을 조회
	 * 
	 * 사용자의 ID로 표준기록관리시스템의 정수점검 계획 정보를 조회 한다.
	 * 조회된 정보가 없거나 여러 정수점검계획 정보가 조회 될 수 있다.
	 * </pre>
	 * 
	 * @param paramWorkRoot
	 * @return
	 */
	public WorkRoot getInvenCheck(WorkRoot paramWorkRoot)
	{
		return null;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W008
	 * DESCRIPTION	: 정수점검 상세 조회
	 * 
	 * 사용자의 ID와 계획서 번호로 표준기록관리시스템의 정수점검 상세 정보를 조회 한다.
	 * 하나에서 여러 개의 정수점검상세 정보가 조회 될 수 있다.
	 * </pre>
	 * 
	 * @param paramWorkRoot
	 * @return
	 */
	public WorkRoot getDetailInvenCheck(WorkRoot paramWorkRoot)
	{
		return null;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W009
	 * DESCRIPTION	: 정수점검 상세 결과 등록
	 * 
	 * 사용자의 ID와 계획서번호로 정수점검상세결과 정보를 표준기록관리시스템에 등록 한다.
	 * PARAM_LIST는 하나 혹은 여러 개의 정보를 가질 수 있다.
	 * 모든 PARAM_LIST는 각각의 정수점검 상세 결과 등록 결과를 반환 한다.
	 * 정수점검 계획의 모든 정수점검 상세 결과가 등록되면 계획의 상태가 미점검에서 결과등록으로 바뀐다.
	 * </pre>
	 * 
	 * @param paramWorkRoot
	 * @return
	 */
	public WorkRoot setDetailInvenCheckResult(WorkRoot paramWorkRoot)
	{
		return null;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W010
	 * DESCRIPTION	: 정수점검 결과 등록
	 * 
	 * 사용자의 ID와 계획서번호로 정수점검 결과 정보를 표준기록관리시스템에 등록 한다.
	 * PARAM_LIST는 하나의 정보를 가질 수 있다.
	 * 모든 PARAM_LIST는 각각의 정수점검 상세 결과등록 결과를 반환 한다.
	 * 정수점검 계획의 모든 정수점검 상세 결과가 등록되어야 정수점검 결과를 등록 할 수 있다.
	 * </pre>
	 * 
	 * @param paramWorkRoot
	 * @return
	 */
	public WorkRoot setInvenCheckResult(WorkRoot paramWorkRoot)
	{
		return null;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W011
	 * DESCRIPTION	: 반출서작성
	 * 
	 * 기록물의 반출서를 작성 한다.
	 * PARAM_LIST는 하나 혹은 여러 개의 정보를 가질 수 있다.
	 * 모든 PARAM_LIST는 하나의 반출서로 작성되며 그 결과를 하나의 RETURN_LIST로 받는다.
	 * </pre>
	 * 
	 * @param paramWorkRoot
	 * @return
	 */
	public WorkRoot setRecordOutPaper(WorkRoot paramWorkRoot)
	{
		return null;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W012
	 * DESCRIPTION	: 반입서작성
	 * 
	 * 기록물의 반입서를 작성 한다.
	 * PARAM_LIST는 하나 혹은 여러 개의 정보를 가질 수 있다.
	 * 모든 PARAM_LIST는 하나의 반입서로 작성되며 그 결과를 하나의 RETURN_LIST로 받는다.
	 * </pre>
	 * 
	 * @param paramWorkRoot
	 * @return
	 */
	public WorkRoot setRecordInPaper(WorkRoot paramWorkRoot)
	{
		return null;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W013
	 * DESCRIPTION	: 유출예방
	 * 
	 * 태그 ID로 기록물의 이동가능 여부와 기록물 정보를 조회 한다.
	 * PARAM_LIST는 하나 혹은 여러 개의 정보를 가질 수 있으며, 각각의 조회 결과를 반환 받는다.
	 * </pre>
	 * 
	 * @param paramWorkRoot
	 * @return
	 */
	public WorkRoot getMoveFlag(WorkRoot paramWorkRoot)
	{
		return null;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W014
	 * DESCRIPTION	: 탐지조회
	 * 
	 * 기록물 철 제목으로 기록물 정보를 조회 한다.
	 * PARAM_LIST는 하나의 정보를 가질 수 있으며, 조회 결과가 없거나 여러 개의 조회 결과를 가질 수 있다.
	 * </pre>
	 * 
	 * @param paramWorkRoot
	 * @return
	 */
	public WorkRoot getRecordInfoByTitle(WorkRoot paramWorkRoot)
	{
		return null;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W015
	 * DESCRIPTION	: 기록물 정보 조회
	 * 
	 * 사용자 ID 와 태그 ID로 제목으로 기록물 정보를 조회 한다.
	 * PARAM_LIST는 하나 혹은 여러 개의 정보를 가질 수 있으며, 조회 결과가 없거나 여러 개의 조회 결과를 가질 수 있다.
	 * </pre>
	 * 
	 * @param paramWorkRoot 
	 * @return
	 */
	public WorkRoot getRecordInfoByTag(WorkRoot paramWorkRoot)
	{
		return null;
	}
	
	/*
	public void invokeMethodTest()
	{
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ invoke success !");
	}
	
	public void invokeMethod(Object o, String methodName, WorkRoot paramWorkRoot)
	{
		Method[] methods = o.getClass().getMethods();
		
		for (Method m : methods)
		{
		     if(m.getName().equals(methodName))
		     {
		    	 log.debug(m.getName() + " // " + m.getReturnType().getName());
		    	 
		         //try { m.invoke(o, paramWorkRoot); }
		         try { m.invoke( .getClass(). ); }
		         catch (Exception e) { e.printStackTrace(); }
		     }			
			
		}
	}
	*/		
}
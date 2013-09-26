package com.jcone.rmsXmlCmd.common.service;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ParamList;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ParamList.Param;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList.ReturnRow;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList.ReturnRow.ReturnCol;

@SuppressWarnings({"rawtypes", "unchecked"})
public class RecordService extends RecordServiceTemplate
{
	@Override
	protected WorkRoot doit(String methodName, Object o, WorkRoot paramWorkRoot) throws Exception
	{
		Method[] methods = ((RecordService)o).getClass().getMethods();
		
		for (Method m : methods)
		{
			log.debug(m.getName() + " // " + m.getReturnType().getName());
			if (m.getName().equals(methodName))
			{
				return (WorkRoot) m.invoke(o, paramWorkRoot); 
			}			
		}
		
		return paramWorkRoot;
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
	public WorkRoot getServerTime(WorkRoot paramWorkRoot) throws Exception
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
			list.getReturnRow().add(row);
			paramWorkRoot.getReturnList().add(list);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			ReturnCol col = factory.createWorkRootReturnListReturnRowReturnCol();
			col.setId("SELECT_RESULT");
			col.setValue(e.getCause().getMessage());
			
			row.getReturnCol().add(col);
			
			list.setId(1);
			list.getReturnRow().add(row);
			
			paramWorkRoot.getReturnList().add(list);
		}
		
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
		List<Param> paramList = paramWorkRoot.getParamList().get(0).getParam();
		HashMap paramMap = new HashMap();
		
		for (Param p : paramList)
			paramMap.put(p.getId(), p.getValue());
		
		try 
		{
			List<Map> rtsList = dao.getUserInfo(paramMap);
			
			// return_list loop and setting
			for (int i=0, maxLoop=rtsList.size(), idx=i+1; i<maxLoop; i++)
			{
				ReturnList list = factory.createWorkRootReturnList();
				list.setId(idx++);
				
				Map<String, Object> rtsMap = rtsList.get(i);
				list.getReturnRow().add(mapToRow(rtsMap));
				
				paramWorkRoot.getReturnList().add(list);
			}			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			
			paramWorkRoot.getReturnList().clear(); // 기존에 쌓인 반환값을 삭제
			
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
	public WorkRoot getCommonCodeInfo(WorkRoot paramWorkRoot) throws Exception
	{
		List<Map> rtsList = dao.getCommonCodeInfo();
		
		// return_list loop and setting
		for (int i=0, maxLoop=rtsList.size(), idx=i+1; i<maxLoop; i++)
		{
			ReturnList list = factory.createWorkRootReturnList();
			list.setId(idx++);
			
			Map<String, Object> rtsMap = rtsList.get(i);
			list.getReturnRow().add(mapToRow(rtsMap));
			
			paramWorkRoot.getReturnList().add(list);
		}		
		
		return paramWorkRoot;
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
	public WorkRoot getRecordLocInfo(WorkRoot paramWorkRoot) throws Exception
	{
		List<Param> paramList = paramWorkRoot.getParamList().get(0).getParam();
		HashMap paramMap = new HashMap();
		
		for (Param p : paramList)
			paramMap.put(p.getId(), p.getValue());
		
		List<Map> rtsList = dao.getRecordLocInfo(paramMap);
		
		// return_list loop and setting
		for (int i=0, maxLoop=rtsList.size(), idx=i+1; i<maxLoop; i++)
		{
			ReturnList list = factory.createWorkRootReturnList();
			list.setId(idx++);
			
			Map<String, Object> rtsMap = rtsList.get(i);
			list.getReturnRow().add(mapToRow(rtsMap));
			
			paramWorkRoot.getReturnList().add(list);
		}		
		
		return paramWorkRoot;
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
		// Param List를 가져온다.
		List<ParamList> ParamListList = paramWorkRoot.getParamList();
		
		for (int i=0, maxLoop=ParamListList.size(); i<maxLoop; i++)
		{
			ParamListList.get(i).getId();
			List<Param> paramList = ParamListList.get(i).getParam();
			
			HashMap paramMap = new HashMap();
			
			for (Param p : paramList)
				paramMap.put(p.getId(), p.getValue());
			
			ReturnCol col1 = factory.createWorkRootReturnListReturnRowReturnCol();
			ReturnCol col2 = factory.createWorkRootReturnListReturnRowReturnCol();
			
			try
			{
				dao.setRecordLocInfo(paramMap);
				
				col1.setId("INSERT_RESULT");
				col1.setValue("0");
				
				col2.setId("INSERT_LOG");
				col2.setValue("");
			} 
			catch (SQLException e)
			{
				col1.setId("INSERT_RESULT");
				col1.setValue("1");
				
				col2.setId("INSERT_LOG");
				col2.setValue(e.getCause().getMessage());
				
				e.printStackTrace();
			}
			
			ReturnRow row = factory.createWorkRootReturnListReturnRow();
			row.getReturnCol().add(col1);
			row.getReturnCol().add(col2);
			
			ReturnList list = factory.createWorkRootReturnList();
			list.setId(1);
			list.getReturnRow().add(row);
			
			paramWorkRoot.getReturnList().add(list);			
		}
	
		return paramWorkRoot;
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
	public WorkRoot setRecordRelocInfo(WorkRoot paramWorkRoot) throws Exception
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
	public WorkRoot getInvenCheck(WorkRoot paramWorkRoot) throws Exception
	{
		List<Param> paramList = paramWorkRoot.getParamList().get(0).getParam();
		HashMap paramMap = new HashMap();
		
		for (Param p : paramList)
			paramMap.put(p.getId(), p.getValue());
		
		List<Map> rtsList = dao.getInvenCheck(paramMap);
		
		// return_list loop and setting
		for (int i=0, maxLoop=rtsList.size(), idx=i+1; i<maxLoop; i++)
		{
			ReturnList list = factory.createWorkRootReturnList();
			list.setId(idx++);
			
			Map<String, Object> rtsMap = rtsList.get(i);
			list.getReturnRow().add(mapToRow(rtsMap));
			
			paramWorkRoot.getReturnList().add(list);
		}		
		
		return paramWorkRoot;		
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
	public WorkRoot getDetailInvenCheck(WorkRoot paramWorkRoot) throws Exception
	{
		List<Param> paramList = paramWorkRoot.getParamList().get(0).getParam();
		HashMap paramMap = new HashMap();
		
		for (Param p : paramList)
			paramMap.put(p.getId(), p.getValue());
		
		List<Map> rtsList = dao.getDetailInvenCheck(paramMap);
		
		// return_list loop and setting
		for (int i=0, maxLoop=rtsList.size(), idx=i+1; i<maxLoop; i++)
		{
			ReturnList list = factory.createWorkRootReturnList();
			list.setId(idx++);
			
			Map<String, Object> rtsMap = rtsList.get(i);
			list.getReturnRow().add(mapToRow(rtsMap));
			
			paramWorkRoot.getReturnList().add(list);
		}		
		
		return paramWorkRoot;	
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
	public WorkRoot setDetailInvenCheckResult(WorkRoot paramWorkRoot) throws Exception
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
	public WorkRoot setInvenCheckResult(WorkRoot paramWorkRoot) throws Exception
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
		List<ParamList> ParamListList = paramWorkRoot.getParamList();
		
		for (int i=0, maxLoop=ParamListList.size(); i<maxLoop; i++)
		{
			ParamListList.get(i).getId();
			List<Param> paramList = ParamListList.get(i).getParam();
			
			ReturnCol col1 = factory.createWorkRootReturnListReturnRowReturnCol();
			ReturnCol col2 = factory.createWorkRootReturnListReturnRowReturnCol();
			
			try
			{
				HashMap paramMap = new HashMap();
				
				String OUT_YYYY = "";
				String OUT_SNO = "";
				String OUT_ID = ""; 
				
				for (Param p : paramList)
				{
					if (p.getId().equals("OUT_YMD"))
					{
						OUT_YYYY = p.getValue().substring(0, 4);
						paramMap.put("OUT_YYYY", OUT_YYYY);
					}
					else
						paramMap.put(p.getId(), p.getValue());
				}
				
				OUT_SNO = dao.findCarryOutSno(paramMap);
				OUT_ID = OUT_YYYY + OUT_SNO;
				
				paramMap.put("OUT_SNO", OUT_SNO);
				paramMap.put("OUT_ID", OUT_ID);
				
				dao.setRecordLocInfo(paramMap);
				List<Map> findReadCarryOutTempRtsList = dao.findReadCarryOutTemp(paramMap);
				
				for (Map m : findReadCarryOutTempRtsList)
				{
					paramMap.putAll(m);
					dao.createInOutFolder(paramMap);
				}
				
				col1.setId("INSERT_RESULT");
				col1.setValue("0");
				
				col2.setId("UPDATE_LOG");
				col2.setValue("");
			} 
			catch (SQLException e)
			{
				col1.setId("INSERT_RESULT");
				col1.setValue("1");
				
				col2.setId("UPDATE_LOG");
				col2.setValue(e.getCause().getMessage());
				
				e.printStackTrace();
			}
			
			ReturnRow row = factory.createWorkRootReturnListReturnRow();
			row.getReturnCol().add(col1);
			row.getReturnCol().add(col2);
			
			ReturnList list = factory.createWorkRootReturnList();
			list.setId(1);
			list.getReturnRow().add(row);
			
			paramWorkRoot.getReturnList().add(list);			
		}
	
		return paramWorkRoot;			
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
	public WorkRoot setRecordInPaper(WorkRoot paramWorkRoot) throws Exception
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
	public WorkRoot getMoveFlag(WorkRoot paramWorkRoot) throws Exception
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
	public WorkRoot getRecordInfoByTitle(WorkRoot paramWorkRoot) throws Exception
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
	public WorkRoot getRecordInfoByTag(WorkRoot paramWorkRoot) throws Exception
	{
		return null;
	}
	
	/**
	 * Map 객체를 ReturnRow 객체로 변환후 반환
	 * @param paramMap
	 * @return
	 */
	public ReturnRow mapToRow(Map<String, Object> paramMap)
	{
		ReturnRow row = factory.createWorkRootReturnListReturnRow();
		
		// return_row loop and setting
		for (Map.Entry<String, Object> entry : paramMap.entrySet())
		{
			if (null == entry.getValue())
				continue;
			
			ReturnCol col = factory.createWorkRootReturnListReturnRowReturnCol();
			col.setId(entry.getKey());
			col.setValue(entry.getValue().toString());
			
			row.getReturnCol().add(col);
		}
		
		return row;
	}	
}
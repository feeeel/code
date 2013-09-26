package com.jcone.rmsXmlCmd.common.service;

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
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ParamList;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ParamList.Param;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList.ReturnRow;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReturnList.ReturnRow.ReturnCol;

@SuppressWarnings({"rawtypes", "unchecked"})
public class RecordService
{
	/**
	 * @uml.property  name="log"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected final Log log				= LogFactory.getLog(this.getClass());
	/**
	 * @uml.property  name="factory"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private ObjectFactory factory		= new ObjectFactory();
	/**
	 * @uml.property  name="_workRoot"
	 * @uml.associationEnd  
	 */
	private WorkRoot _workRoot			= null;
	/**
	 * @uml.property  name="sqlMapClient"
	 * @uml.associationEnd  
	 */
	private SqlMapClient sqlMapClient	= null;

	/** WorkRoot Setter, Getter */
	public WorkRoot getWorkRoot()
	{
		return _workRoot;
	}

	public void setWorkRoot(WorkRoot _workRoot)
	{
		this._workRoot = _workRoot;
	}

	/**
	 * <pre>
	 * WORK CODE	: W001
	 * DESCRIPTION	: 서버시간조회
	 * 
	 * RFID 연계모듈의 시간정보를 조회 한다. 전달 값은 없으며 서버시간을 전송 한다.
	 * </pre>
	 */
	public void getServerTime() throws Exception
	{
		ReturnList list = factory.createWorkRootReturnList();
		ReturnRow row = factory.createWorkRootReturnListReturnRow();

		try
		{
			SqlConfig.instance();
			sqlMapClient = SqlConfig.getSqlMapInstance();
			
			sqlMapClient.startTransaction();
			RecordDAO dao = new RecordDAO(sqlMapClient);
			
			ReturnCol col = factory.createWorkRootReturnListReturnRowReturnCol();
			col.setId("SERVER_DATE_TIME");
			col.setValue(dao.getServerTime());
			
			row.getReturnCol().add(col);
			
			list.setId(1);
			list.getReturnRow().add(row);
			
			_workRoot.getReturnList().add(list);
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
			
			_workRoot.getReturnList().clear();
			_workRoot.getReturnList().add(list);
			
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W002
	 * DESCRIPTION	: 사용자조회
	 * 
	 * 부서코드와 사용자 로그인 ID로 사용자정보를 조회 한다.
	 * 조회된 정보가 없거나 같은 사용자 로그인 ID를 가진 여러 사용자 정보가 조회 될 수 있다.
	 * </pre>
	 */
	public void getUserInfo()
	{
		try
		{
			SqlConfig.instance();
			sqlMapClient = SqlConfig.getSqlMapInstance();
			
			sqlMapClient.startTransaction();
			RecordDAO dao = new RecordDAO(sqlMapClient);
			
			// 파싱된 XML에서 PARAM_LIST를 가져온다.
			List<Param> paramList = _workRoot.getParamList().get(0).getParam();
			
			// paramList의 PARAM_ID, PARAM_VALUE를 HashMap에 담는다.
			HashMap paramMap = new HashMap();
			
			for (Param p : paramList)
				paramMap.put(p.getId(), p.getValue());

			// 쿼리 실행
			List<Map> rtsList = dao.getUserInfo(paramMap);
			
			// return_list loop and setting
			for (int i=0, maxLoop=rtsList.size(), idx=i+1; i<maxLoop; i++)
			{
				ReturnList list = factory.createWorkRootReturnList();
				list.setId(idx++);
				
				Map<String, Object> rtsMap = rtsList.get(i);
				list.getReturnRow().add(mapToRow(rtsMap));
				
				_workRoot.getReturnList().add(list);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W003
	 * DESCRIPTION	: 공통코드조회
	 * 
	 * 표준기록관리시스템의 공통코드 정보를 조회 한다.
	 * 공통코드 정보가 없거나 여러 공통코드 정보가 조회될 수 있다.
	 * </pre>
	 */
	public void getCommonCodeInfo()
	{
		try
		{
			SqlConfig.instance();
			sqlMapClient = SqlConfig.getSqlMapInstance();
			
			sqlMapClient.startTransaction();
			RecordDAO dao = new RecordDAO(sqlMapClient);
			
			List<Map> rtsList = dao.getCommonCodeInfo();
			
			// return_list loop and setting
			for (int i=0, maxLoop=rtsList.size(), idx=i+1; i<maxLoop; i++)
			{
				ReturnList list = factory.createWorkRootReturnList();
				list.setId(idx++);
				
				Map<String, Object> rtsMap = rtsList.get(i);
				list.getReturnRow().add(mapToRow(rtsMap));
				
				_workRoot.getReturnList().add(list);
			}				
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W004
	 * DESCRIPTION	: 위치정보조회
	 * 
	 * 사용자의 ID로 표준기록관리시스템의 서고, 서가 정보를 조회 한다.
	 * 사용자가 소속된 기록관의 정보만 조회 되며 조회된 정보가 없거나 여러 서고, 서가 정보가 조회 될 수 있다
	 * </pre>
	 */
	public void getRecordLocInfo() throws Exception
	{
		try
		{
			SqlConfig.instance();
			sqlMapClient = SqlConfig.getSqlMapInstance();
			
			sqlMapClient.startTransaction();
			RecordDAO dao = new RecordDAO(sqlMapClient);
			
			List<Param> paramList = _workRoot.getParamList().get(0).getParam();
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
				
				_workRoot.getReturnList().add(list);
			}				
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw e;
			}
		}
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
	 * @throws Exception
	 * @deprecated
	 * 
	 */
	public void createBooksfArng() throws Exception
	{
		try
		{
			SqlConfig.instance();
			sqlMapClient = SqlConfig.getSqlMapInstance();
			sqlMapClient.getDataSource().getConnection().setAutoCommit(false);
			
			/*
			
			XML 파라메터
			<param id="USER_ID">0000000001</param>
			<param id="TAG_ID">0544092CB48D3CA070C30D85</param> // 신규
			<param id="STAKRM_ID">001</param>
			<param id="BOOKSF_ID">001</param>
			<param id="SHELF_C_CNT">10</param>
			<param id="SHELF_R_CNT">10</param>
			<param id="PRESV_BOX_ID">001001001001001</param>
			<param id="ARNG_YMD">20121015</param>
			
			*/
			
			RecordDAO dao = new RecordDAO(sqlMapClient);
			
			List<ParamList> paramListList = _workRoot.getParamList();
			for (ParamList o : paramListList)
			{
				o.getId(); // PARAM_LIST ID
				
				List<Param> paramList = o.getParam();
				
				HashMap paramMap = new HashMap();
				for (Param p : paramList)
					paramMap.put(p.getId(), p.getValue());

				String _RECORD_CENTER_ID = (String) dao.getCommonUserInfo(paramMap).get("RECORD_CENTER_ID");
				String _ARNG_YMD = (String) paramMap.get("ARNG_YMD");
				String _ARNG_YYYY = _ARNG_YMD.substring(0, 4);
				
				paramMap.put("_RECORD_CENTER_ID", _RECORD_CENTER_ID);
				paramMap.put("_ARNG_YYYY", _ARNG_YYYY);
				
				String _SNO = dao.findBooksfArngSno(paramMap);		//순번
				String _ARNGSNO = StringUtil.concatZero(_SNO, 6);	//순번에 0을 총 사이즈만큼 붙임
				String _BOOKSF_ARNG_ID = _ARNG_YYYY + _ARNGSNO;		//서가배치번호(배치년도 + 순번(6자리))
				
				paramMap.put("_SNO", _SNO);
				paramMap.put("_ARNGSNO", _ARNGSNO);
				paramMap.put("_BOOKSF_ARNG_ID", _BOOKSF_ARNG_ID);
				
				try 
				{
					sqlMapClient.startTransaction();
					dao.createBooksfArng(paramMap);
				}
				catch (SQLException e)
				{
					e.printStackTrace();
					sqlMapClient.endTransaction();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}		
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
	 * @deprecated
	 */
	public void updateBooksfArng() throws Exception
	{
		
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
	 * @throws Exception
	 */
	public void findCntChkPlan() throws Exception
	{
		try
		{
			SqlConfig.instance();
			sqlMapClient = SqlConfig.getSqlMapInstance();
			
			sqlMapClient.startTransaction();
			RecordDAO dao = new RecordDAO(sqlMapClient);
			
			// 파싱된 XML에서 PARAM_LIST를 가져온다.
			List<Param> paramList = _workRoot.getParamList().get(0).getParam();
			
			// paramList의 PARAM_ID, PARAM_VALUE를 HashMap에 담는다.
			HashMap paramMap = new HashMap();
			
			for (Param p : paramList)
				paramMap.put(p.getId(), p.getValue());

			// 쿼리 실행
			List<Map> rtsList = dao.findCntChkPlan(paramMap);
			
			// return_list loop and setting
			for (int i=0, maxLoop=rtsList.size(), idx=i+1; i<maxLoop; i++)
			{
				ReturnList list = factory.createWorkRootReturnList();
				list.setId(idx++);
				
				Map<String, Object> rtsMap = rtsList.get(i);
				list.getReturnRow().add(mapToRow(rtsMap));
				
				_workRoot.getReturnList().add(list);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw e;
			}
		}
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
	 * @throws Exception
	 */
	public void findCntChkPlanDetl() throws Exception
	{
		try
		{
			SqlConfig.instance();
			sqlMapClient = SqlConfig.getSqlMapInstance();
			
			sqlMapClient.startTransaction();
			RecordDAO dao = new RecordDAO(sqlMapClient);
			
			// 파싱된 XML에서 PARAM_LIST를 가져온다.
			List<Param> paramList = _workRoot.getParamList().get(0).getParam();
			
			// paramList의 PARAM_ID, PARAM_VALUE를 HashMap에 담는다.
			HashMap paramMap = new HashMap();
			
			for (Param p : paramList)
				paramMap.put(p.getId(), p.getValue());
			
			// 쿼리 실행
			List<Map> rtsList = dao.findCntChkPlanDetl(paramMap);
			
			// return_list loop and setting
			for (int i=0, maxLoop=rtsList.size(), idx=i+1; i<maxLoop; i++)
			{
				ReturnList list = factory.createWorkRootReturnList();
				list.setId(idx++);
				
				Map<String, Object> rtsMap = rtsList.get(i);
				list.getReturnRow().add(mapToRow(rtsMap));
				
				_workRoot.getReturnList().add(list);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw e;
			}
		}
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
	 */
	public void UpdateCntChkDetlRslt()
	{
		try
		{
			SqlConfig.instance();
			sqlMapClient = SqlConfig.getSqlMapInstance();
			sqlMapClient.getDataSource().getConnection().setAutoCommit(false);
			
			RecordDAO dao = new RecordDAO(sqlMapClient);
			
			List<ParamList> paramListList = _workRoot.getParamList();
			
			for (ParamList o : paramListList)
			{
				ReturnCol colRts = factory.createWorkRootReturnListReturnRowReturnCol();
				ReturnCol colLog = factory.createWorkRootReturnListReturnRowReturnCol();
				
				List<Param> paramList = o.getParam();
				
				HashMap paramMap = new HashMap();
				
				for (Param p : paramList)
					paramMap.put(p.getId(), p.getValue());
				
				try
				{
					sqlMapClient.startTransaction();
					dao.updateCntChkDetlRslt(paramMap);
					sqlMapClient.commitTransaction();
					
					colRts.setId("UPDATE_RESULT");
					colRts.setValue("0");
					
					colLog.setId("UPDATE_LOG");
					colLog.setValue("");
				}
				catch (Exception e)
				{
					sqlMapClient.endTransaction();
					e.printStackTrace();
					
					colRts.setId("UPDATE_RESULT");
					colRts.setValue("1");
					
					colLog.setId("UPDATE_LOG");
					colLog.setValue(e.getCause().getMessage());
				}
				
				ReturnRow row = factory.createWorkRootReturnListReturnRow();
				row.getReturnCol().add(colRts);
				row.getReturnCol().add(colLog);
				
				ReturnList list = factory.createWorkRootReturnList();
				list.getReturnRow().add(row);
				list.setId(o.getId());
				
				_workRoot.getReturnList().add(list);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
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
	 */
	public void UpdateCntChkRslt() throws Exception
	{
		try
		{
			SqlConfig.instance();
			sqlMapClient = SqlConfig.getSqlMapInstance();
			sqlMapClient.startTransaction();
			
			RecordDAO dao = new RecordDAO(sqlMapClient);
			
			List<Param> paramList = _workRoot.getParamList().get(0).getParam();
			
			HashMap paramMap = new HashMap();
			
			for (Param p : paramList)
				paramMap.put(p.getId(), p.getValue());
			
			Map rtsMap = dao.findCntChkDetlRslt(paramMap);
			
			if (null != rtsMap)
			{
				// UPDATE(정수점검:점검상태(1-결과등록 or 2-점검완료),점검일자)
				paramMap.put("CHECK_STATE_CD", "1");
				paramMap.putAll(rtsMap);
				
				dao.UpdateCntChkRslt(paramMap);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw e;
			}
		}
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
	 */
	public void createCarryOut()
	{
		try
		{
			SqlConfig.instance();
			sqlMapClient = SqlConfig.getSqlMapInstance();
			sqlMapClient.getDataSource().getConnection().setAutoCommit(false);
			
			RecordDAO dao = new RecordDAO(sqlMapClient);
			
			List<ParamList> paramListList = _workRoot.getParamList();
			for (ParamList o : paramListList)
			{
				List<Param> paramList = o.getParam();
				
				HashMap paramMap = new HashMap();
				for (Param p : paramList)
					paramMap.put(p.getId(), p.getValue());

				String _RECORD_CENTER_ID = (String) dao.getCommonUserInfo(paramMap).get("RECORD_CENTER_ID");
				String _OUT_YMD = (String) paramMap.get("OUT_YMD");
				String _OUT_YYYY = _OUT_YMD.substring(0, 4);
				
				paramMap.put("_OUT_YYYY", _OUT_YYYY);
				
				String _SNO = dao.findCarryOutSno(paramMap);
				String _OUT_SNO = StringUtil.concatZero(_SNO, 6);
				String _OUT_ID = _OUT_YYYY + _OUT_SNO;
				
				paramMap.put("_RECORD_CENTER_ID", _RECORD_CENTER_ID);
				paramMap.put("_OUT_YMD", _OUT_YMD);
				paramMap.put("_OUT_SNO", _OUT_SNO);
				paramMap.put("_OUT_ID", _OUT_ID);
				
				ReturnCol colRts = factory.createWorkRootReturnListReturnRowReturnCol();
				ReturnCol colLog = factory.createWorkRootReturnListReturnRowReturnCol();
				
				try 
				{
					sqlMapClient.startTransaction();
					
		            //반출(TB_SROUT)등록
					dao.createCarryOut(paramMap);
					List<Map> carryOutFolderList = dao.findTemp(paramMap);
					
					paramMap.put("_OUT_FLAG", "1"); // 반출 플래그
					
					for (Map m : carryOutFolderList)
					{
						paramMap.putAll(m);
						
						//반출입기록물철(TB_SRINOUTFOLDER) 등록
						dao.createInOutFolder(paramMap);
						dao.updateCarryOutTemp(paramMap);
					}
					
					sqlMapClient.commitTransaction();
					sqlMapClient.endTransaction();
					
					colRts.setId("INSERT_RESULT");
					colRts.setValue("0");
					
					colLog.setId("UPDATE_LOG");
					colLog.setValue("");					
				}
				catch (SQLException e)
				{
					e.printStackTrace();
					sqlMapClient.endTransaction();
					
					colRts.setId("INSERT_RESULT");
					colRts.setValue("1");
					
					colLog.setId("UPDATE_LOG");
					colLog.setValue(e.getCause().getMessage());	
				}
				
				ReturnRow row = factory.createWorkRootReturnListReturnRow();
				row.getReturnCol().add(colRts);
				row.getReturnCol().add(colLog);
				
				ReturnList list = factory.createWorkRootReturnList();
				list.getReturnRow().add(row);
				list.setId(o.getId());
				
				_workRoot.getReturnList().add(list);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
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
	 */
	public void createCarryInWrite()
	{
		ReturnCol colRts = factory.createWorkRootReturnListReturnRowReturnCol();
		ReturnCol colLog = factory.createWorkRootReturnListReturnRowReturnCol();
		
		try
		{
			SqlConfig.instance();
			sqlMapClient = SqlConfig.getSqlMapInstance();
			sqlMapClient.getDataSource().getConnection().setAutoCommit(false);
			sqlMapClient.startTransaction();
			
			RecordDAO dao = new RecordDAO(sqlMapClient);
			
			List<ParamList> paramListList = _workRoot.getParamList();
			for (ParamList o : paramListList)
			{
				List<Param> paramList = o.getParam();
				
				HashMap paramMap = new HashMap();
				for (Param p : paramList)
					paramMap.put(p.getId(), p.getValue());
				
				String _RECORD_CENTER_ID = (String) dao.getCommonUserInfo(paramMap).get("RECORD_CENTER_ID");
				String _IN_YMD = (String) paramMap.get("IN_YMD");
				String _IN_YYYY = _IN_YMD.substring(0, 4);
				
				paramMap.put("_IN_YYYY", _IN_YYYY);
				paramMap.put("_RECORD_CENTER_ID", _RECORD_CENTER_ID);
				
				String _SNO = dao.findCarryInSno(paramMap);
				String _IN_SNO = StringUtil.concatZero(_SNO, 6);
				String _IN_ID = _IN_YYYY + _IN_SNO;
				
				paramMap.put("_IN_YMD", _IN_YMD);
				paramMap.put("_IN_SNO", _IN_SNO);
				paramMap.put("_IN_ID", _IN_ID);
				
				dao.createCarryIn(paramMap); // 반입(TB_SRIN)등록
				dao.updateInFolderFin(paramMap); // 반출입기록물철(TB_SRINOUTFOLDER) 등록
				
				sqlMapClient.commitTransaction();
				
				colRts.setId("INSERT_RESULT");
				colRts.setValue("0");
				
				colLog.setId("UPDATE_LOG");
				colLog.setValue("");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			
			colRts.setId("INSERT_RESULT");
			colRts.setValue("1");
			
			colLog.setId("UPDATE_LOG");
			colLog.setValue(e.getCause().getMessage());
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		ReturnRow row = factory.createWorkRootReturnListReturnRow();
		row.getReturnCol().add(colRts);
		row.getReturnCol().add(colLog);
		
		ReturnList list = factory.createWorkRootReturnList();
		list.getReturnRow().add(row);
		
		_workRoot.getReturnList().add(list);
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W013
	 * DESCRIPTION	: 유출예방
	 * 
	 * 태그 ID로 기록물의 이동가능 여부와 기록물 정보를 조회 한다.
	 * PARAM_LIST는 하나 혹은 여러 개의 정보를 가질 수 있으며, 각각의 조회 결과를 반환 받는다.
	 * </pre>
	 */
	public void getMoveFlag() throws Exception
	{
		try
		{
			SqlConfig.instance();
			sqlMapClient = SqlConfig.getSqlMapInstance();
			sqlMapClient.getDataSource().getConnection().setAutoCommit(false);
			
			RecordDAO dao = new RecordDAO(sqlMapClient);
			
			List<ParamList> paramListList = _workRoot.getParamList();
			for (ParamList o : paramListList)
			{
				List<Param> paramList = o.getParam();
				
				HashMap paramMap = new HashMap();
				for (Param p : paramList)
					paramMap.put(p.getId(), p.getValue());

				try
				{
					ReturnList list = factory.createWorkRootReturnList();
					
					HashMap rtsMap = (HashMap) dao.getMoveFlag(paramMap).get(0);
					list.getReturnRow().add(mapToRow(rtsMap));
					list.setId(o.getId());
					
					_workRoot.getReturnList().add(list);
				}
				catch (Exception e)
				{
					// 결과 값이 없을 경우
					e.printStackTrace();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw e;
			}
		}
	
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W014
	 * DESCRIPTION	: 탐지조회
	 * 
	 * 기록물 철 제목으로 기록물 정보를 조회 한다.
	 * PARAM_LIST는 하나의 정보를 가질 수 있으며, 조회 결과가 없거나 여러 개의 조회 결과를 가질 수 있다.
	 * </pre>
	 */
	public void getRecordInfoByTitle() throws Exception
	{
		try
		{
			SqlConfig.instance();
			sqlMapClient = SqlConfig.getSqlMapInstance();
			
			sqlMapClient.startTransaction();
			RecordDAO dao = new RecordDAO(sqlMapClient);
			
			List<Param> paramList = _workRoot.getParamList().get(0).getParam();
			HashMap paramMap = new HashMap();
			
			for (Param p : paramList)
				paramMap.put(p.getId(), p.getValue());
			
			List<Map> rtsList = dao.getRecordInfoByTitle(paramMap);
			
			// return_list loop and setting
			for (int i=0, maxLoop=rtsList.size(), idx=i+1; i<maxLoop; i++)
			{
				ReturnList list = factory.createWorkRootReturnList();
				list.setId(idx++);
				
				Map<String, Object> rtsMap = rtsList.get(i);
				list.getReturnRow().add(mapToRow(rtsMap));
				
				_workRoot.getReturnList().add(list);
			}				
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw e;
			}
		}
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W015
	 * DESCRIPTION	: 기록물 정보 조회
	 * 
	 * 사용자 ID 와 태그 ID로 제목으로 기록물 정보를 조회 한다.
	 * PARAM_LIST는 하나 혹은 여러 개의 정보를 가질 수 있으며, 조회 결과가 없거나 여러 개의 조회 결과를 가질 수 있다.
	 * </pre>
	 */
	public void getRecordInfoByTag() throws Exception
	{
		try
		{
			SqlConfig.instance();
			sqlMapClient = SqlConfig.getSqlMapInstance();
			sqlMapClient.startTransaction();
			
			RecordDAO dao = new RecordDAO(sqlMapClient);
			
			List<ParamList> paramListList = _workRoot.getParamList();
			
			int returnListIdx = 0;
			
			for (ParamList o : paramListList)
			{
				List<Param> paramList = o.getParam();
				
				HashMap paramMap = new HashMap();
				for (Param p : paramList)
					paramMap.put(p.getId(), p.getValue());
				
				List<Map> rtsList = dao.getRecordInfoByTag(paramMap);
				
				// return_list loop and setting
				for (int i=0, maxLoop=rtsList.size(); i<maxLoop; i++)
				{
					ReturnList list = factory.createWorkRootReturnList();
					list.setId(++returnListIdx);
					
					Map<String, Object> rtsMap = rtsList.get(i);
					list.getReturnRow().add(mapToRow(rtsMap));
					
					_workRoot.getReturnList().add(list);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				sqlMapClient.endTransaction();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw e;
			}
		}
	}
	
	
	/**
	 * Map 객체를 ReturnRow 객체로 변환후 반환
	 * @param paramMap
	 * @return
	 */
	private ReturnRow mapToRow(Map<String, Object> paramMap)
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
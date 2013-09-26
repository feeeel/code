package com.jcone.rmsXmlCmd.common.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibatis.sqlmap.client.SqlMapClient;

@SuppressWarnings("rawtypes")
public class RecordDAO
{
	/**
	 * @uml.property  name="sqlMapClient"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private SqlMapClient sqlMapClient;
	
	public RecordDAO(SqlMapClient sqlMapClient)
	{
		this.sqlMapClient = sqlMapClient;
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W001
	 * DESCRIPTION	: 서버시간조회
	 * 
	 * RFID 연계모듈의 시간정보를 조회 한다. 전달 값은 없으며 서버시간을 전송 한다.
	 * </pre>
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String getServerTime() throws SQLException
	{
		return (String) sqlMapClient.queryForObject("getServerTime");
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
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	public List getUserInfo(HashMap paramMap) throws SQLException
	{
		return (List) sqlMapClient.queryForList("getUserInfo", paramMap);
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
	 * @return
	 * @throws SQLException
	 */
	public List getCommonCodeInfo() throws SQLException
	{
		return (List) sqlMapClient.queryForList("getCommonCodeInfo");
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
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	public List getRecordLocInfo(HashMap paramMap) throws SQLException
	{
		return (List) sqlMapClient.queryForList("getRecordLocInfo", paramMap);
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
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	public List findCntChkPlan(HashMap paramMap) throws SQLException
	{
		return (List) sqlMapClient.queryForList("findCntChkPlan", paramMap);
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
	 * @param paramMap
	 * @throws SQLException
	 */
	public void updateCntChkDetlRslt(HashMap paramMap) throws SQLException
	{
		sqlMapClient.update("updateCntChkDetlRslt", paramMap);
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W010 - 01
	 * DESCRIPTION	: '정수점검 상세결과 등록' 완료 목록 조회
	 * </pre>
	 * 
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	public Map findCntChkDetlRslt(HashMap paramMap) throws SQLException
	{
		return (Map) sqlMapClient.queryForObject("findCntChkDetlRslt", paramMap);
	}
	
	/**
	 * <pre>
	 * WORK CODE	: W010 - 02
	 * DESCRIPTION	: 정수점검 결과 등록
	 * 
	 * 사용자의 ID와 계획서번호로 정수점검 결과 정보를 표준기록관리시스템에 등록 한다.
	 * PARAM_LIST는 하나의 정보를 가질 수 있다.
	 * 모든 PARAM_LIST는 각각의 정수점검 상세 결과등록 결과를 반환 한다.
	 * 정수점검 계획의 모든 정수점검 상세 결과가 등록되어야 정수점검 결과를 등록 할 수 있다.
	 * </pre>
	 * 
	 * @param paramMap
	 * @throws SQLException
	 */
	public void UpdateCntChkRslt(HashMap paramMap) throws SQLException
	{
		sqlMapClient.update("UpdateCntChkRslt", paramMap);
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
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	public List findCntChkPlanDetl(HashMap paramMap) throws SQLException
	{
		return (List) sqlMapClient.queryForList("findCntChkPlanDetl", paramMap);
	}
	
	/**
	 * 반출서순번
	 * 
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	public String findCarryOutSno(HashMap paramMap) throws SQLException
	{
		return (String) sqlMapClient.queryForObject("findCarryOutSno", paramMap);
	}
	
	/**
	 * 반출등록
	 * 
	 * @param paramMap
	 * @throws SQLException
	 */
	public void createCarryOut(HashMap paramMap) throws SQLException
	{
		sqlMapClient.insert("createCarryOut", paramMap);
	}

	/**
	 * 반출 철목록 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	public List findTemp(HashMap paramMap) throws SQLException
	{
		return (List) sqlMapClient.queryForList("findTemp", paramMap);
	}
	
	/**
	 * 반출입기록물철 등록
	 * 
	 * @param paramMap
	 * @throws SQLException
	 */
	public void createInOutFolder(HashMap paramMap) throws SQLException
	{
		sqlMapClient.insert("createCarryOut", paramMap);
		sqlMapClient.update("updateOutFolder", paramMap);
	}

	/**
	 * @param paramMap
	 * @throws SQLException
	 */
	public void updateCarryOutTemp(HashMap paramMap) throws SQLException
	{
		sqlMapClient.update("updateCarryOutTemp", paramMap);
	}
	
	/**
	 * 반입서순번
	 * 
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	public String findCarryInSno(HashMap paramMap) throws SQLException
	{
		return (String) sqlMapClient.queryForObject("findCarryInSno", paramMap);
	}
	
	/**
	 * 반입등록
	 * 
	 * @param paramMap
	 * @throws SQLException
	 */
	public void createCarryIn(HashMap paramMap) throws SQLException
	{
		sqlMapClient.insert("createCarryIn", paramMap);
	}
	
	/**
	 * 반입기록물수정
	 * 
	 * @param paramMap
	 * @throws SQLException
	 */
	public void updateInFolderFin(HashMap paramMap) throws SQLException
	{
		sqlMapClient.update("updateInFolderFin", paramMap);
		sqlMapClient.update("updateInFolder", paramMap);
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
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	public List getMoveFlag(HashMap paramMap) throws SQLException
	{
		return (List) sqlMapClient.queryForList("getMoveFlag", paramMap);
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
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	public List getRecordInfoByTitle(HashMap paramMap) throws SQLException
	{
		return (List) sqlMapClient.queryForList("getRecordInfoByTitle", paramMap);
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
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	public List getRecordInfoByTag(HashMap paramMap) throws SQLException
	{
		return (List) sqlMapClient.queryForList("getRecordInfoByTag", paramMap);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 서가배치:작성완료
	 * 
	 * @param paramMap
	 * @throws SQLException
	 */
	public void createBooksfArng(HashMap paramMap) throws SQLException
	{
		sqlMapClient.insert("createBooksfArng", paramMap);
	}
	
	/**
	 * 서가배치:일련번호구하기
	 * 
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	public String findBooksfArngSno(HashMap paramMap) throws SQLException
	{
		return (String) sqlMapClient.queryForObject("findBooksfArngSno", paramMap);
	}
	
	/**
	 * 일반 사용자 정보 조회
	 * 
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	public HashMap getCommonUserInfo(HashMap paramMap) throws SQLException
	{
		return (HashMap) sqlMapClient.queryForObject("getCommonUserInfo", paramMap);
	}
	
	/*
	public void setRecordLocInfo(HashMap paramMap) throws SQLException
	{
		sqlMapClient.insert("setRecordLocInfo", paramMap);
	}	
	
	public boolean setRecordRelocInfo(HashMap paramMap)
	{
		boolean result = true;
		
		try
		{			
			sqlMapClient.insert("setRecordRelocInfo", paramMap);
		}
		catch(Exception ex)
		{
			result = false;
		}
		
		return result;
	}	
	
	public List getInvenCheck(HashMap paramMap) throws SQLException
	{
		return (List) sqlMapClient.queryForList("getInvenCheck", paramMap);
	}
	
	public List getDetailInvenCheck(HashMap paramMap) throws SQLException
	{
		return (List) sqlMapClient.queryForList("getDetailInvenCheck", paramMap);
	}
	
	public boolean setDetailInvenCheckResult(HashMap paramMap)
	{
		boolean result = true;
		
		try
		{			
			sqlMapClient.insert("setDetailInvenCheckResult", paramMap);
		}
		catch(Exception ex)
		{
			result = false;
		}
		
		return result;
	}	
	
	public boolean setInvenCheckResult(HashMap paramMap)
	{
		boolean result = true;
		
		try
		{			
			sqlMapClient.insert("setInvenCheckResult", paramMap);
		}
		catch(Exception ex)
		{
			result = false;
		}
		
		return result;
	}
	
	public boolean setRecordInPaper(HashMap paramMap)
	{
		boolean result = true;
		
		try
		{			
			sqlMapClient.insert("setRecordInPaper", paramMap);
		}
		catch(Exception ex)
		{
			result = false;
		}
		
		return result;
	}
	
	public List getMoveFlag(HashMap paramMap) throws SQLException
	{
		return (List) sqlMapClient.queryForList("getMoveFlag", paramMap);
	}
	
	public List getRecordInfoByTitle(HashMap paramMap) throws SQLException
	{
		return (List) sqlMapClient.queryForList("getRecordInfoByTitle", paramMap);
	}
	
	public List getRecordInfoByTag(HashMap paramMap) throws SQLException
	{
		return (List) sqlMapClient.queryForList("getRecordInfoByTag", paramMap);
	}
	
	public void setRecordOutPaper(HashMap paramMap) throws SQLException
	{
		sqlMapClient.insert("carryOut.setRecordOutPaper", paramMap);
	}
	
	public List findReadCarryOutTemp(HashMap paramMap) throws SQLException
	{
		return (List) sqlMapClient.queryForList("carryOut.findReadCarryOutTemp", paramMap);
	}
	
	public String findCarryOutSno(HashMap paramMap) throws SQLException
	{
		return (String) sqlMapClient.queryForObject("carryOut.findCarryOutSno", paramMap);
	}
	
	public void createInOutFolder(HashMap paramMap) throws SQLException
	{
		sqlMapClient.insert("carryOut.createInOutFolder", paramMap);
	}
	*/
}
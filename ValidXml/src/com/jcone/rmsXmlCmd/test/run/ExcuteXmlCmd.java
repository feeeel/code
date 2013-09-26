package com.jcone.rmsXmlCmd.test.run;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jcone.rmsXmlCmd.common.constants.WorkCode;
import com.jcone.rmsXmlCmd.common.service.RecordService;
import com.jcone.rmsXmlCmd.common.utils.marshalling.SimpleMarshaller;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.ReceiveInfo;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.SendInfo;
import com.jcone.rmsXmlCmd.common.utils.marshalling.WorkRoot.WorkInfo;

public class ExcuteXmlCmd
{
	/**
	 * @uml.property  name="log"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected final Log log = LogFactory.getLog(this.getClass());

	/**
	 * @uml.property name="_workRoot"
	 * @uml.associationEnd
	 */
	private WorkRoot _workRoot;
	/**
	 * @uml.property name="_rtsWorkRoot"
	 * @uml.associationEnd
	 */
	private WorkRoot _rtsWorkRoot;

	/**
	 * @uml.property  name="_workCode"
	 */
	private String _workCode;
	/**
	 * @uml.property  name="_sourceFilePath"
	 */
	private String _sourceFilePath;
	/**
	 * @uml.property  name="_destFilePath"
	 */
	private String _destFilePath;

	/* Setter Method  */
	/**
	 * @param _sourceFilePath
	 */
	public void set_sourceFilePath(String _sourceFilePath)
	{
		this._sourceFilePath = _sourceFilePath;
	}

	/**
	 * @param _destFilePath
	 */
	public void set_destFilePath(String _destFilePath)
	{
		this._destFilePath = _destFilePath;
	}

	/**
	 * 작업 요청 내용이 들어있는 XML을 파싱하고 필수 정보 유효성 검사
	 * 
	 * @return 성공시 true, 실패시 false 반환
	 */
	private boolean requestWork()
	{
		log.info("Verify Target : " + _sourceFilePath);

		boolean isSuccess = false;

		try
		{
			SimpleMarshaller sm = new SimpleMarshaller(_sourceFilePath);
			_workRoot = sm.XmlToObject();

			WorkInfo workInfo = _workRoot.getWorkInfo();

			if (StringUtils.isBlank(workInfo.getWorkCode()))
			{

				throw new Exception(_sourceFilePath + "\nWorkCode 정보가 없습니다.");
			}
			else if (StringUtils.isBlank(workInfo.getWorkDiv()))
			{
				throw new Exception(_sourceFilePath + "\nWorkDiv 정보가 없습니다.");
			}
			else if (StringUtils.isBlank(workInfo.getWorkKey()))
			{
				throw new Exception(_sourceFilePath + "\nWorkKey 정보가 없습니다.");
			}

			SendInfo sendInfo = _workRoot.getSendInfo();

			if (null == sendInfo)
			{
				throw new Exception(_sourceFilePath + "\nSendInfo 정보가 없습니다.");
			}
			else if (StringUtils.isBlank(sendInfo.getIp()))
			{
				throw new Exception(_sourceFilePath + "\nSendInfo IP 정보가 없습니다.");
			}
			else if (StringUtils.isBlank(sendInfo.getPort()))
			{
				throw new Exception(_sourceFilePath + "\nSendInfo Port 정보가 없습니다.");
			}

			ReceiveInfo receiveInfo = _workRoot.getReceiveInfo();

			if (null == receiveInfo)
			{
				throw new Exception(_sourceFilePath + "\nReceiveInfo 정보가 없습니다.");
			}
			else if (StringUtils.isBlank(receiveInfo.getIp()))
			{
				throw new Exception(_sourceFilePath + "\nReceiveInfo IP 정보가 없습니다.");
			}
			else if (StringUtils.isBlank(receiveInfo.getPort()))
			{
				throw new Exception(_sourceFilePath + "\nReceiveInfo Port 정보가 없습니다.");
			}

			_workCode = _workRoot.getWorkInfo().getWorkCode();
			isSuccess = true;

			log.info("Verify Result : 스키마 이상없음");
		}
		catch (Exception e)
		{
			log.error("Error File    : " + _sourceFilePath);

			e.printStackTrace();
		}

		return isSuccess;
	}

	/**
	 * 파싱된 XML 정보중 WORK CODE 를 가지고 작업을 분류하여 분류된 개별 작업 실행 WC001 ~ WC015
	 * 
	 * @return 성공시 true, 실패시 false 반환
	 */
	private boolean processWork()
	{
		boolean isSuccess = true;
		RecordService service = new RecordService();
		service.setWorkRoot(_workRoot);

		try
		{
			switch (WorkCode.Code.valueOf(_workCode))
			{
				case WC001:
					service.getServerTime(); // 서버시간조회
					break;
				case WC002:
					service.getUserInfo(); // 사용자조회
					break;
				case WC003:
					service.getCommonCodeInfo(); // 공통코드조회
					break;
				case WC004:
					service.getRecordLocInfo(); // 위치정보조회
					break;
				case WC005:
					service.createBooksfArng(); // 서가배치 (!! 테스트 !!)
					break;
				case WC006:
					service.updateBooksfArng(); // 서가 재배치 정보를 등록 (!! 테스트 !!)
					break;
				case WC007:
					service.findCntChkPlan(); // 정수점검 계획을 조회
					break;
				case WC008:
					service.findCntChkPlanDetl(); // 정수점검 상세 조회
					break;
				case WC009:
					service.UpdateCntChkDetlRslt(); // 정수점검 상세 결과 등록 (!! 테스트 !!)
					break;
				case WC010:
					service.UpdateCntChkRslt(); // 정수점검 결과 등록 (!! 테스트 !!)
					break;
				case WC011:
					service.createCarryOut(); // 반출서작성
					break;
				case WC012:
					service.createCarryInWrite(); // 반입서작성
					break;
				case WC013:
					service.getMoveFlag(); // 유출예방 (!! 테스트 !!)
					break;
				case WC014:
					service.getRecordInfoByTitle(); // 탐지조회 (!! 테스트 !!)
					break;
				case WC015:
					service.getRecordInfoByTag(); // 기록물 정보 조회 (!! 테스트 !!)
					break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			isSuccess = false;
		}
		finally
		{
			_rtsWorkRoot = service.getWorkRoot();
		}

		return isSuccess;
	}

	/**
	 * 처리 결과를 XML로 반환 (파일쓰기)
	 */
	private void responseWork()
	{
		SimpleMarshaller sm;

		if(null != _destFilePath)
			sm = new SimpleMarshaller(_rtsWorkRoot, _destFilePath);

		else
			sm = new SimpleMarshaller(_rtsWorkRoot);

		sm.ObjectToXml();
	}

	/**
	 * 작업 실행 WorkRoot을 XML로 변환후 반환
	 */
	public void excute(boolean b)
	{
		if (b)
		{
			boolean isSuccess = requestWork();

			if (isSuccess)
				isSuccess = processWork();

			if (isSuccess)
				responseWork();
		}
		else
		{
			requestWork();
		}
	}
}
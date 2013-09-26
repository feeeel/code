package com.jcone.rmsXmlCmd.common.constants;

public class WorkCode
{
	 /**
	 * @author    Administrator
	 */
	public enum Code
	 {
		 /**
		 * @uml.property  name="wC001"
		 * @uml.associationEnd  
		 */
		WC001, // 서버시간 조회, 서버 시간을 조회
		 /**
		 * @uml.property  name="wC002"
		 * @uml.associationEnd  
		 */
		WC002, // 사용자 조회, 사용자 정보를 조회
		 /**
		 * @uml.property  name="wC003"
		 * @uml.associationEnd  
		 */
		WC003, // 공통코드 조회, 공통코드 정보를 조회
		 /**
		 * @uml.property  name="wC004"
		 * @uml.associationEnd  
		 */
		WC004, // 위치정보 조회, 서가, 서고 정보를 조회
		 /**
		 * @uml.property  name="wC005"
		 * @uml.associationEnd  
		 */
		WC005, // 서가배치, 서가 배치 정보를 등록
		 /**
		 * @uml.property  name="wC006"
		 * @uml.associationEnd  
		 */
		WC006, // 서가배치(재배치), 서가 재배치 정보를 등록
		 /**
		 * @uml.property  name="wC007"
		 * @uml.associationEnd  
		 */
		WC007, // 정수점검 계획조회, 정수점검 계획을 조회
		 /**
		 * @uml.property  name="wC008"
		 * @uml.associationEnd  
		 */
		WC008, // 정수점검 상세조회, 정수점검 계획의 상세 목록을 조회
		 /**
		 * @uml.property  name="wC009"
		 * @uml.associationEnd  
		 */
		WC009, // 정수점검 상세 결과등록, 기록물의 정수점검 결과를 등록
		 /**
		 * @uml.property  name="wC010"
		 * @uml.associationEnd  
		 */
		WC010, // 정수점검 결과등록, 정수점검 계획의 결과를 등록
		 /**
		 * @uml.property  name="wC011"
		 * @uml.associationEnd  
		 */
		WC011, // 반출서 작성, 기록물의 반출서를 작성
		 /**
		 * @uml.property  name="wC012"
		 * @uml.associationEnd  
		 */
		WC012, // 반입서 작성, 기록물의 반입서를 작성
		 /**
		 * @uml.property  name="wC013"
		 * @uml.associationEnd  
		 */
		WC013, // 유출예방, 기록물의 이동가능 여부를 조회
		 /**
		 * @uml.property  name="wC014"
		 * @uml.associationEnd  
		 */
		WC014, // 탐지조회, 기록물 철의 제목으로 기록물 정보 조회
		 /**
		 * @uml.property  name="wC015"
		 * @uml.associationEnd  
		 */
		WC015; // 기록물 정보 조회, 태그정보로 기록물 정보 조회
	 }
}
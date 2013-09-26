package com.jcone.rmsXmlCmd.common.utils.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil 
{
    public static String concatZero(String str, int str_length) {
        
        int count = str.length();
        if (count > str_length) return str;

        for (int i = count; i < str_length; i++) {      
            str = "0" + str;                                  
        }
        return str;                                           
    }
	
	
	 public static boolean validateString(String strString)
	 {
	        boolean flag = true;
	        
	        if (strString == "" || strString == null || strString.length() ==0) {
	            return false;
	        }
	        
	        return flag;        
	 }
	 
	 public static int strTokenCnt(String str, String szSection)
	 {
    	Pattern pattern = Pattern.compile(szSection);
    	Matcher matcher = pattern.matcher(str);
    	int nPlusCount = 0;
    	for( int i = 0; matcher.find(i); i = matcher.end())
    	{
    		nPlusCount++;
    	}

         return nPlusCount;         
          
	 }
	 
	
    public static String[] getTokenArray(String str, String strDelim)
    {
        if (str == null || str.length() == 0) return null;
        StringTokenizer st = new StringTokenizer(str, strDelim);

        String[] arrToken = new String[st.countTokens()];
        for (int i=0; i<arrToken.length; i++)
            arrToken[i] = st.nextToken().trim();

        return ( arrToken.length == 0 ) ? null : arrToken;
    }
    
   
    public static String[] String2Array( String szSrc, String szDelim, int nCount )
    {     
        return  szSrc.split(szDelim,nCount);       
    }
    
    public static boolean matchesString(String orgStr, String checkStr)
    {
    	return orgStr.matches(checkStr);
    }
    
    public static boolean checkInputStr(String str)
    {
    	boolean bReturn = false;
    	if(str.matches("[a-zA-Z0-9]*"))
    	{
    		bReturn = true;
    	}
    	return bReturn;
    }
    
    /**
     * 8859_1 --> KSC5601.
     */
    public static String charChg(String str, String orgChar, String chgChar )
    {
        String chagStr = null;
        
        if (str == null ) return null;
        //if (korean == null ) return "";
        
        chagStr = new String(str);
        try { 
        	chagStr = new String(new String(str.getBytes(orgChar), chgChar));
        }
        catch( UnsupportedEncodingException e ){
        	chagStr = new String(str);
        }
        return chagStr;
    }
    
    public static String exceptionToStr(Exception e)
    {
		try
		{
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return "\r\n" + sw.toString() + "\r\n";
		}
		catch (Exception e2)
		{
			return "bad stack2string";
		}
    }
}
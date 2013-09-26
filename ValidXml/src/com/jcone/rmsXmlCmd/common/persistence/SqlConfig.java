package com.jcone.rmsXmlCmd.common.persistence;

import java.io.Reader;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;


public class SqlConfig {
    private static SqlMapClient dbMap = null;
    private static SqlConfig instance_ = null;
    
    private SqlConfig() throws Exception
    {
        Reader reader = null;
        String resource = null;
        try {           
                resource = "com/jcone/rmsXmlCmd/common/persistence/sqlmap_config.xml";
                                                
             	reader = Resources.getResourceAsReader(resource);
             	dbMap = SqlMapClientBuilder.buildSqlMapClient(reader);
             	reader.close();           
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        } finally {
            if (reader != null)
                reader.close();
            reader = null;
            resource = null;
        }
    }
    
    public static SqlConfig instance()
    {
        try {
            if (instance_ == null) {
                synchronized (SqlConfig.class) {
                    if (instance_ == null)
                        instance_ = new SqlConfig();
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return instance_;
    }

    /**
     * Return SqlMapClient for SVR schema
     *
     * @return
     */
    public static SqlMapClient getSqlMapInstance() 
    {
        return dbMap;
    }
  
}
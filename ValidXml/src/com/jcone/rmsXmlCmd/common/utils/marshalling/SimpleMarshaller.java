package com.jcone.rmsXmlCmd.common.utils.marshalling;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;


/**
 * @author Administrator
 */
public class SimpleMarshaller
{
	/**
	 * @uml.property  name="_sourceFile"
	 */
	private File _sourceFile;
	/**
	 * @uml.property  name="_destFile"
	 */
	private File _destFile;
	
	/**
	 * @uml.property  name="_targetWorkRoot"
	 * @uml.associationEnd  
	 */
	private WorkRoot _targetWorkRoot;
	/**
	 * @uml.property  name="context"
	 * @uml.associationEnd  
	 */
	private JAXBContext context;
	/**
	 * @uml.property  name="factory"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private ObjectFactory factory = new ObjectFactory();
	
    public SimpleMarshaller()
    { /* Empty */ }

    /**
     * @param paramfilePath 변경할 파일의 절대경로
     */
    public SimpleMarshaller(String paramfilePath)
    {
    	_sourceFile = new File(paramfilePath);
    }

    /**
     * @param paramWorkRoot 변경할 객체
     * @param paramfilePath 출력할 파일의 절대 경로
     */
    public SimpleMarshaller(WorkRoot paramWorkRoot, String paramfilePath)
    {
    	_targetWorkRoot = paramWorkRoot;
    	_destFile = new File(paramfilePath);
    }
    
    /**
     * @param paramWorkRoot 변경할 객체
     */
    public SimpleMarshaller(WorkRoot paramWorkRoot)
    {
    	_targetWorkRoot = paramWorkRoot;
    }
    
    /**
     * XML convert to Object
     * @throws JAXBException 
     */
    public WorkRoot XmlToObject() throws JAXBException
    {
    	WorkRoot resultObject = factory.createWorkRoot();
    	
		context = JAXBContext.newInstance(WorkRoot.class);
		Unmarshaller um = context.createUnmarshaller();
		
		resultObject = (WorkRoot) um.unmarshal(_sourceFile);
			
		return resultObject;
    }
    
    /**
     * Object convert to XML
     */
    public void ObjectToXml()
    {
    	try
    	{
			context = JAXBContext.newInstance(WorkRoot.class);
			Marshaller m = context.createMarshaller();
			
			// output pretty printed
			m.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			m.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			
			m.marshal(_targetWorkRoot, System.out);
			
			if(null != _destFile)
				m.marshal(_targetWorkRoot, _destFile);
		}
    	catch (Exception e)
    	{
			e.printStackTrace();
		}
    }
}
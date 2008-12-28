package net.sf.gilead.core.beanlib.transformer;

import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Reader for XML additional custom transfomers
 * @author Alexandre Pretyman & Bruno Marchesson 
 *
 */
public class CustomTransformerXmlReader
{
	//----
	// Constants
	//----
	/**
	 * XML root element tag
	 */
	private static final String ROOT_ELEMENT_NAME = "customBeanTransformers";
	
	/**
	 * XML child element tag
	 */
	private static final String CLASS_ELEMENT_NAME = "class";
	
	/**
	 * XML file name : must be in application classpath
	 */
	private static final String TRANSFORMER_XML_FILENAME = "gilead-transformers.xml";

	//----
	// Attributes
	//----
	/**
	 * Log channel
	 */
	private static Log _log = LogFactory.getLog(CustomTransformerXmlReader.class);

	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/**
	 * Read transformer class names from gilead-transformer.xml file
	 */
	public static List<String> readTransformersFromXml()
	{
		List<String> transformerList = new ArrayList<String>();
		
		try 
		{
			Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(TRANSFORMER_XML_FILENAME);

			if (urls != null)
			{
				while (urls.hasMoreElements())
				{
					final URL url = urls.nextElement();
					SAXReader reader = new SAXReader();
					Document document = reader.read(url);
					Element root = document.getRootElement();
					if (root.getName().equals(ROOT_ELEMENT_NAME) == false)
					{
						_log.warn(url + " root element is not <"
								+ ROOT_ELEMENT_NAME + ">, skipping file "
								+ url.toString());
						continue; // while (urls.hasMoreElements())
					}
	
					// Iterator over url
					Iterator<Element> i = root.elementIterator(CLASS_ELEMENT_NAME); 
					while (i.hasNext())
					{
						Element qualifiedName = i.next();
						transformerList.add(qualifiedName.getStringValue());
					}				
				}
			}
		} 
		catch (Exception e)
		{
			_log.error("Error reading union custom transformers file" ,e);
			throw new RuntimeException("Error reading union custom transformers file" ,e);
		}

		if (transformerList.isEmpty())
		{
			return null;
		}
		else
		{
			return transformerList;
		}
	}
}

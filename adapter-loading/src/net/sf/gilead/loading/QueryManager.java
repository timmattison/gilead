package net.sf.gilead.loading;

import java.beans.PropertyDescriptor;
import java.util.Collection;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;

/**
 * Main loading class.
 * It acts as a Facade for the interface loading functionality
 * @author bruno.marchesson
 *
 */
public class QueryManager
{
	//----
	// Constants
	//----
	/**
	 * Default root name
	 */
	public static final String DEFAULT_ROOT_NAME = "item";
	
	//----
	// Attributes
	//----
	private static Logger _log = LoggerFactory.getLogger(QueryManager.class);
	
	/**
	 * The query generator
	 */
	protected IQueryGenerator _queryGenerator;
	
	/**
	 * Hibernate session factory
	 */
	protected SessionFactory _sessionFactory;
	
	//----
	// Properties
	//----
	/**
	 * @return the queryGenerator
	 */
	public IQueryGenerator getQueryGenerator()
	{
		return _queryGenerator;
	}

	/**
	 * @param queryGenerator the queryGenerator to set
	 */
	public void setQueryGenerator(IQueryGenerator queryGenerator)
	{
		_queryGenerator = queryGenerator;
	}
	
	/**
	 * @return the _sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return _sessionFactory;
	}

	/**
	 * @param factory the _sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory factory)
	{
		_sessionFactory = factory;
	}

	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/**
	 * Generate loading query for the argument class
	 */
	public IQuery generateQuery(Class<?> loadingInterface)
	{
		return generateQuery(loadingInterface, DEFAULT_ROOT_NAME);
	}
	
	/**
	 * Generate loading query for the argument class
	 */
	public IQuery generateQuery(Class<?> loadingInterface, 
								String rootName)
	{
	//	Precondition checking
	//
		if (_queryGenerator == null)
		{
			throw new RuntimeException("No Query Generator !");
		}
		
		if(_sessionFactory == null)
		{
			throw new RuntimeException("Missing persistence util !");
		}
		
	//	Check loading interface
	//
		Class<?> persistentClass = LoadingHelper.getPersistentClass(loadingInterface);
		
		_log.info("Generating query for " + loadingInterface.getName());
		
	//	Create query and hierarchy stack
	//
		IQuery query = _queryGenerator.createQuery(_sessionFactory, loadingInterface, rootName);
		FetchContext fetchContext = new FetchContext();
		
	//	Iterate over properties
	//
		PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(loadingInterface);
		for (PropertyDescriptor descriptor : descriptors)
		{
			generateQueryFor(fetchContext, persistentClass, descriptor, query);
		}
		
		return query;
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Generate query for associated descriptor
	 */
	protected void generateQueryFor(FetchContext fetchContext,
									Class<?> persistentClass,
									PropertyDescriptor descriptor, 
									IQuery query)
	{
	//	Check the Hibernate metadata to know if the property
	//	is association or not
	//
		ClassMetadata metadata = _sessionFactory.getClassMetadata(persistentClass);
		Type propertyType = metadata.getPropertyType(descriptor.getName());
		if (propertyType == null)
		{
			return;
		}
		
		if ((propertyType.isAssociationType() == false) &&
			(propertyType.isCollectionType() == false))
		{
		//	Neither an association nor a collection : do not fetch...
		//
			return;
		}
		
		if (fetchContext.hasFetchedType(propertyType))
		{
			_log.info("Type already fetched " + propertyType);
			return;
		}
		else
		{
			if(_log.isDebugEnabled())
			{
				_log.debug("Fetch needed for " + descriptor.getName());
			}
			fetchContext.addFetchType(propertyType);
		}
		
	//	Add fetch instruction
	//
		_queryGenerator.addFetchInstruction(fetchContext.getHierarchyStack(),
											descriptor, query);
		
	//	Get property persistent class
	//
		Class<?> propertyClass = descriptor.getPropertyType();
		if (Collection.class.isAssignableFrom(propertyClass))
		{
		//	Get collection type
		//
			Class<?>[] collectionType = LoadingHelper.getCollectionTypes(
											descriptor.getReadMethod().getGenericReturnType());
			if (collectionType != null)
			{
				propertyClass = collectionType[0];
			}
		}
		
		Class<?> propertyPersistentClass = LoadingHelper.getPersistentClass(propertyClass);
		
	//	In depth fetching on properties
	//
		fetchContext.getHierarchyStack().push(descriptor);
		
		PropertyDescriptor[] descriptors = PropertyUtils.getPropertyDescriptors(propertyClass);
		for (PropertyDescriptor member : descriptors)
		{
			generateQueryFor(fetchContext, propertyPersistentClass, member, query);
		}
		
		fetchContext.getHierarchyStack().pop();
	}
}

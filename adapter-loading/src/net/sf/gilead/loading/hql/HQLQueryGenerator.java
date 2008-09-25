package net.sf.gilead.loading.hql;

import java.beans.PropertyDescriptor;
import java.util.Stack;

import net.sf.gilead.loading.IQuery;
import net.sf.gilead.loading.IQueryGenerator;
import net.sf.gilead.loading.LoadingHelper;
import net.sf.gilead.loading.annotations.Join;
import net.sf.gilead.loading.annotations.Join.JoinType;

import org.hibernate.SessionFactory;

/**
 * Query generator using HQL
 * 
 * @author bruno.marchesson
 * 
 */
public class HQLQueryGenerator implements IQueryGenerator
{
	//----
	// Attributes
	//----
	/**
	 * The root name (select) of the query
	 */
	private String _root;
	
	//-------------------------------------------------------------------------
	//
	// Implementation of IQueryGenerator
	//
	//-------------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.loading.IQueryGenerator#createQuery(net.sf.hibernate4gwt.core.IPersistenceUtil)
	 */
	public IQuery createQuery(SessionFactory sessionFactory,
						 	  Class<?> loadingInterface,
							  String root)
	{
		_root = root;
		
	//	Create query
	//
		HQLQuery query = new HQLQuery();
		query.getRequest().append("from ");
		query.getRequest().append(LoadingHelper.getPersistentClass(loadingInterface).getSimpleName());
		query.getRequest().append(' ');
		query.getRequest().append(_root);
		
		return query;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.loading.IQueryGenerator#addFetchInstruction(java.lang.Class, java.beans.PropertyDescriptor, net.sf.gilead.loading.IQuery)
	 */
	public void addFetchInstruction(Stack<PropertyDescriptor> hierarchyStack,
									PropertyDescriptor descriptor, 
									IQuery query)
	{
	//	Get join strategy
	//
		Join annotation = descriptor.getReadMethod().getAnnotation(Join.class);
		
	//	Generate fetch instruction
	//
		HQLQuery hqlQuery = (HQLQuery) query;
		if ((annotation == null) ||
			(annotation.value() == JoinType.INNER))
		{
			hqlQuery.getRequest().append(" inner join");
		}
		else if (annotation.value() == JoinType.LEFT_OUTER)
		{
			hqlQuery.getRequest().append(" left outer join");
		}
		else if (annotation.value() == JoinType.RIGHT_OUTER)
		{
			hqlQuery.getRequest().append(" right outer join");
		}
		else  // if (annotation.value() == JoinType.FULL)
		{
			hqlQuery.getRequest().append(" full join");
		}

		hqlQuery.getRequest().append(" fetch ");
		
	//	Add property name
	//
		hqlQuery.getRequest().append(_root);
		hqlQuery.getRequest().append('.');
		
		for (PropertyDescriptor stackDescriptor : hierarchyStack)
		{
			hqlQuery.getRequest().append(stackDescriptor.getName());
			hqlQuery.getRequest().append('.');
		}
		hqlQuery.getRequest().append(descriptor.getName());
	}
	
	//-------------------------------------------------------------------------
	//
	// Internal methods
	//
	//-------------------------------------------------------------------------
}

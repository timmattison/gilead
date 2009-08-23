package net.sf.gilead.core.loading.gwt.server;


import javax.servlet.ServletException;

import net.sf.gilead.core.TestHelper;
import net.sf.gilead.core.loading.gwt.client.InitService;
import net.sf.gilead.gwt.PersistentRemoteService;
import net.sf.gilead.test.DAOFactory;
import net.sf.gilead.test.dao.IMessageDAO;
import net.sf.gilead.test.domain.interfaces.IMessage;

public class InitServiceImpl extends PersistentRemoteService implements InitService 
{
	//-------------------------------------------------------------------------
	//
	// Service init
	//
	//-------------------------------------------------------------------------
	@Override
	public void init() throws ServletException
	{
		super.init();
		
	//	Init Hibernate context
	//
		setBeanManager(TestHelper.initStatelessBeanManager());
		
	//	Init DB if needed
	//
		if (TestHelper.isInitialized() == false)
		{
			TestHelper.initializeDB();
		}
	}
	
	//-------------------------------------------------------------------------
	//
	// Service methods
	//
	//-------------------------------------------------------------------------
	/*
	 * (non-Javadoc)
	 * @see net.sf.gilead.core.loading.gwt.client.InitService#loadTestMessage()
	 */
	public IMessage loadTestMessage()
	{
	//	Load last message
	//
		IMessageDAO messageDAO = DAOFactory.getMessageDAO();
		return messageDAO.loadLastMessage();
	}

}

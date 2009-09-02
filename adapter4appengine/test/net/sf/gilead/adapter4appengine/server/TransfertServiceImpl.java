/**
 * 
 */
package net.sf.gilead.adapter4appengine.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import net.sf.gilead.adapter4appengine.client.TransfertService;
import net.sf.gilead.adapter4appengine.server.domain.TestEntity;
import net.sf.gilead.adapter4appengine.server.domain.TestEntity2;

/**
 * Transfert remote service implementation
 * @author bruno.marchesson
 *
 */
public class TransfertServiceImpl extends RemoteServiceServlet 
								  implements TransfertService
{
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = -8483136917626625347L;

	/* (non-Javadoc)
	 * @see net.sf.gilead.adapter4appengine.gwt.client.TransfertService#sendAndReceive(net.sf.gilead.adapter4appengine.gwt.server.domain.TestEntity)
	 */
	public TestEntity sendAndReceive(TestEntity entity)
	{
		return entity;
	}

	
	public TestEntity2 sendAndReceiveNew(TestEntity2 entity) {
		
		return entity;
	}

}

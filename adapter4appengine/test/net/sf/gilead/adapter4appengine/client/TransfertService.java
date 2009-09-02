package net.sf.gilead.adapter4appengine.client;

import net.sf.gilead.adapter4appengine.server.domain.TestEntity;
import net.sf.gilead.adapter4appengine.server.domain.TestEntity2;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Transfert remote service interface
 * @author bruno.marchesson
 *
 */
public interface TransfertService extends RemoteService
{
	/**
	 * Send and receive the test entity
	 */
	public TestEntity sendAndReceive(TestEntity entity);
	
	/**
	 * Send and receive the new test entity 
	 */
	public TestEntity2 sendAndReceiveNew (TestEntity2 entity);
}

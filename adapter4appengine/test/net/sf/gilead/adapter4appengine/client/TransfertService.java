package net.sf.gilead.adapter4appengine.client;

import net.sf.gilead.adapter4appengine.server.domain.TestEntity;

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
}

/**
 * 
 */
package net.sf.gilead.adapter4appengine.client;

import net.sf.gilead.adapter4appengine.server.domain.TestEntity;
import net.sf.gilead.adapter4appengine.server.domain.TestEntity2;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Transfert remote service interface
 * @author bruno.marchesson
 * @generated generated asynchronous callback interface to be used on the client side
 *
 */

public interface TransfertServiceAsync {

	/**,
	 * @gwt.callbackReturn the test entity
	 * @param  callback the callback that will be called to receive the return value (see <code>@gwt.callbackReturn</code> tag)
	 * @generated generated method with asynchronous callback parameter to be used on the client side
	 */
	void sendAndReceive(TestEntity entity, AsyncCallback<TestEntity> callback);

	public void sendAndReceiveNew (TestEntity2 entity, AsyncCallback<TestEntity2> callback);	
}

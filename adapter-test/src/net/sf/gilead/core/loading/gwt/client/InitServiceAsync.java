/**
 * 
 */
package net.sf.gilead.core.loading.gwt.client;

import net.sf.gilead.test.domain.interfaces.IMessage;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author bruno.marchesson
 * @generated generated asynchronous callback interface to be used on the client side
 *
 */
public interface InitServiceAsync {

	/**
	 * Initialize the test environment and load a test message
	 * @return
	 * @param  callback the callback that will be called to receive the return value
	 * @generated generated method with asynchronous callback parameter to be used on the client side
	 */
	void loadTestMessage(AsyncCallback<IMessage> callback);

}

package net.sf.gilead.core.loading.gwt.client;

import net.sf.gilead.test.domain.stateless.Message;

import com.google.gwt.user.client.rpc.RemoteService;

public interface InitService extends RemoteService
{
	/**
	 * Initialize the test environment and load a test message
	 * @return
	 */
	public Message loadTestMessage();
	
}

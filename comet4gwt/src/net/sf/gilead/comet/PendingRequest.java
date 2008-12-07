package net.sf.gilead.comet;

import org.apache.catalina.CometEvent;

import com.google.gwt.user.server.rpc.RPCRequest;

/**
 * Pending request class
 * @author Vincent Legendre
 */
public class PendingRequest
{
	//----
	// Attributes
	//----
	/**
	 * The incoming GW request
	 */
	protected RPCRequest rpcRequest;
	
	/**
	 * The associated Comet Event
	 */
	protected CometEvent event;
	
	//----
	// Properties
	//----
	/**
	 * @return the rpcRequest
	 */
	public RPCRequest getRpcRequest()
	{
		return rpcRequest;
	}

	/**
	 * @return the event
	 */
	public CometEvent getEvent()
	{
		return event;
	}


	/**
	 * Constructor
	 * @param rpcRequest
	 * @param event
	 */
	public PendingRequest(RPCRequest rpcRequest, CometEvent event)
	{
		this.rpcRequest = rpcRequest;
		this.event = event;
	}
}
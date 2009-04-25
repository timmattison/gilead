/**
 * 
 */
package net.sf.gilead.adapter4appengine;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.ISerializationAdapter;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCCopy_GWT16;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Dedicated remote service servlet
 * 
 * @author bruno.marchesson
 * 
 */
public class EngineRemoteService extends RemoteServiceServlet {
	/**
	 * Serialization helper
	 */
	private static final long serialVersionUID = 4349105865708683490L;
	
	/**
	 * Constructor
	 */
	public EngineRemoteService()
	{
		RPCCopy_GWT16.setSerializationAdapter(new DataNucleausSerializationAdapter());
	}

	/**
	 * Process call override. Use RPC_Copy instead.
	 */
	@Override
	public String processCall(String payload) throws SerializationException {
		try {
			RPCRequest rpcRequest = RPCCopy_GWT16.decodeRequest(payload, this
					.getClass(), this);
			onAfterRequestDeserialized(rpcRequest);
			return RPCCopy_GWT16.invokeAndEncodeResponse(this, rpcRequest
					.getMethod(), rpcRequest.getParameters(), rpcRequest
					.getSerializationPolicy());
		} catch (IncompatibleRemoteServiceException ex) {
			log(
					"An IncompatibleRemoteServiceException was thrown while processing this call.",
					ex);
			return RPC.encodeResponseForFailure(null, ex);
		}
	}
}

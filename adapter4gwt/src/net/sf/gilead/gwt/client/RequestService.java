package net.sf.gilead.gwt.client;

import java.util.List;
import java.util.Map;

import net.sf.gilead.gwt.client.parameters.IRequestParameter;
import net.sf.gilead.pojo.base.ILightEntity;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Remote request service declaration
 * @author bruno.marchesson
 * 
 *
 */
@RemoteServiceRelativePath("RequestService")
public interface RequestService<T extends ILightEntity> extends RemoteService
{
	//-------------------------------------------------------------------------
	//
	// Public interface
	//
	//-------------------------------------------------------------------------
	/**
	 * Execute a request on server.
	 */
	public List<T> executeRequest(String query, List<IRequestParameter> parameters);
	
	/**
	 * Execute a request on server.
	 */
	public List<T> executeRequest(String query, Map<String, IRequestParameter> parameters);
}

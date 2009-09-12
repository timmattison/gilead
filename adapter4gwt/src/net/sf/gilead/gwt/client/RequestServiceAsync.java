/**
 * 
 */
package net.sf.gilead.gwt.client;

import java.util.List;
import java.util.Map;

import net.sf.gilead.gwt.client.parameters.IRequestParameter;
import net.sf.gilead.pojo.base.ILightEntity;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Request service async interface.
 * @author bruno.marchesson
 *
 */
public interface RequestServiceAsync<T extends ILightEntity>
{
	/**
	 * @see net.sf.gilead.gwt.client.RequestService#executeRequest(java.lang.String, java.util.List)
	 */
	void executeRequest(String query, List<IRequestParameter> parameters,
						AsyncCallback<List<T>> callback);

	/**
	 * @see net.sf.gilead.gwt.client.RequestService#executeRequest(java.lang.String, java.util.Map)
	 */
	void executeRequest(String query,
						Map<String, IRequestParameter> parameters,
						AsyncCallback<List<T>> callback);
}

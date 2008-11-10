package org.gwtwidgets.server.spring.gilead;

import java.lang.reflect.Method;

import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.gwt.GileadRPCHelper;

import org.gwtwidgets.server.spring.GWTRPCServiceExporter;

import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;

/**
 * Incorporates <a href="http://gilead.sourceforge.net/">Gilead's</a>
 * <code>HibernateBeanManager</code>. The concept is similar to
 * <code>PersistentRemoteService</code>: RPC objects are merged into the
 * current Hibernate session and are detached on their way out. Instances must
 * be provided with a <code>PersistentBeanManager</code>.
 * 
 * @author George Georgovassilis, g.georgovassilis[at]gmail.com
 * 
 */
public class GileadRPCServiceExporter extends GWTRPCServiceExporter {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 5952918134393451380L;

	/**
	 * The associated Persistent bean manager
	 */
	private PersistentBeanManager beanManager;

	/**
	 * @return the associated bean manager
	 */
	public PersistentBeanManager getBeanManager() {
		return beanManager;
	}

	/**
	 * @param beanManager the bean manager to set
	 */
	public void setBeanManager(PersistentBeanManager beanManager) {
		this.beanManager = beanManager;
	}

	
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		if (getBeanManager() == null)
			throw new Exception("PersistentBeanManager not set");
	}

	/**
	 * Invocation override
	 */
	public String invokeMethodOnService(Object service, Method targetMethod,
			Object[] targetParameters, RPCRequest rpcRequest) throws Exception {
		GileadRPCHelper.parseInputParameters(rpcRequest, beanManager,
				getThreadLocalRequest().getSession());
		Object result = targetMethod.invoke(service, targetParameters);
		result = GileadRPCHelper.parseReturnValue(result, beanManager);
		String encodedResult = RPC.encodeResponseForSuccess(rpcRequest
				.getMethod(), result, rpcRequest.getSerializationPolicy());
		return encodedResult;
	}
}

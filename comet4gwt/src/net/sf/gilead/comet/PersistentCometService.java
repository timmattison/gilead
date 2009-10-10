/*
 * Copyright 2008 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sf.gilead.comet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.gilead.annotations.Comet;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.gwt.GileadRPCHelper;

import org.apache.catalina.CometEvent;
import org.apache.catalina.CometProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;

/**
 * Base class for GWT remote service using Persistent Entity
 * 
 * @author bruno.marchesson
 * @author vincent legendre TODO use a logging lib
 */
public abstract class PersistentCometService extends CometRemoteService
		implements CometProcessor {
	// ----
	// Attribute
	// ----
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 1153492878621786165L;

	/**
	 * Logger channel
	 */
	private static Logger _log = LoggerFactory.getLogger(PersistentCometService.class);

	/**
	 * The Persistent bean manager
	 */
	protected PersistentBeanManager _beanManager;

	// ----
	// Properties
	// ----
	/**
	 * @return the Hibernate Bean Manager
	 */
	public PersistentBeanManager getBeanManager() {
		return _beanManager;
	}

	/**
	 * @param manager
	 *            the Hibernate Bean Manager to set
	 */
	public void setBeanManager(PersistentBeanManager manager) {
		_beanManager = manager;
	}

	// -------------------------------------------------------------------------
	// Constructor / init
	// -------------------------------------------------------------------------
	/**
	 * Empty constructor
	 */
	public PersistentCometService() {
		super();
		// Default Hibernate Lazy Manager
		//
		_beanManager = PersistentBeanManager.getInstance();
	}

	/**
	 * Base constructor
	 */
	public PersistentCometService(PersistentBeanManager lazyManager) {
		super();
		_beanManager = lazyManager;
	}

	// -------------------------------------------------------------------------
	// Remote service servlet override
	// -------------------------------------------------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.catalina.CometProcessor#event(org.apache.catalina.CometEvent)
	 */
	public void event(CometEvent p_event) throws IOException, ServletException {
		// Store the request & response objects in thread-local storage.
		//
		getPerThreadRequest().set(p_event.getHttpServletRequest());
		getPerThreadResponse().set(p_event.getHttpServletResponse());

		if (p_event.getEventType() == CometEvent.EventType.BEGIN) {
			if (_log.isDebugEnabled()) {
				_log.debug(p_event.getEventType().toString());
			}
		} else if (p_event.getEventType() == CometEvent.EventType.READ) {
			try {
				// Get payload
				String payload = readRequest(p_event);
				RPCRequest rpcRequest = null;

				// Decode request
				//
				rpcRequest = RPC.decodeRequest(payload, this.getClass(), this);
				// TODO not sure of that: parameters are stored into rpcRequest
				// ?
				// session ?
				GileadRPCHelper.parseInputParameters(rpcRequest, _beanManager,
						getThreadLocalRequest().getSession());

				// Is it a Comet or not ?
				Comet comet = getComet(rpcRequest.getMethod());
				if (comet != null && !haveData(rpcRequest)) {
					// if its the event request then wait for events
					if (comet.timeout() != 0) {
						p_event.setTimeout(comet.timeout());
					}
					// save this request for processing later.
					setCurrentPendingRequest(new PendingRequest(rpcRequest,
							p_event));
				} else {
					// otherwise process the RPC call as usual
					// sendResponse( event, rpcRequest );
					// Invoke method
					//
					invokeService(rpcRequest, p_event);
				}
			} catch (Throwable e) {
				_log.warn("Comet Event error !", e);
				// Give a subclass a chance to either handle the exception or
				// rethrow it
				//
				writeResponse(p_event.getHttpServletResponse(),
						"Server Error : " + e.getMessage());
			}
		} else if (p_event.getEventType() == CometEvent.EventType.END) {
			PendingRequest abortedRequest = getCurrentPendingRequest();
			boolean isAborted = false;
			if (abortedRequest != null) {
				if (abortedRequest.event != null) {
					isAborted = true;
				}
				setCurrentPendingRequest(null);
			}
			p_event.close();
			if (isAborted) {
				onCurrentRequestAbort(abortedRequest);
			}
		} else if (p_event.getEventType() == CometEvent.EventType.ERROR) {
			if (p_event.getEventSubType() == CometEvent.EventSubType.TIMEOUT) {
				invokeService(getCurrentPendingRequest());
			} else if (p_event.getEventSubType() == CometEvent.EventSubType.CLIENT_DISCONNECT) {
				if (getCurrentPendingRequest() != null) {
					onCurrentRequestAbort(getCurrentPendingRequest());
				}
			} else {
				_log.error("server event error: " + p_event.getEventSubType());
				setCurrentPendingRequest(null);
				writeResponse(p_event.getHttpServletResponse(),
						"server event error: " + p_event.getEventSubType());
				p_event.close();
			}
		}
		// null the thread-locals to avoid holding request/response
		//
		getPerThreadRequest().set(null);
		getPerThreadResponse().set(null);
	}

	/**
	 * if a PendingRequest class was created, you should call
	 * 'invokeService(PendingRequest)'
	 * 
	 * @param p_rpcRequest
	 * @param p_event
	 * @throws IOException
	 */
	@Override
	protected void invokeService(RPCRequest p_rpcRequest, CometEvent p_event)
			throws IOException {
		if (p_rpcRequest == null || p_event == null) {
			return;
		}
		
		if (_log.isDebugEnabled())
		{
			_log.debug("invokeService: "+ p_rpcRequest.getMethod().getName());
		}
		// store perThreadRequest/Response to make this methods re-entrant
		//
		HttpServletRequest oldRequest = getPerThreadRequest().get();
		HttpServletResponse oldResponse = getPerThreadResponse().get();
		getPerThreadRequest().set(p_event.getHttpServletRequest());
		getPerThreadResponse().set(p_event.getHttpServletResponse());

		try {
			String responsePayload = null;
			try {
				try {
					Object returnValue = p_rpcRequest.getMethod().invoke(this, p_rpcRequest.getParameters());

					returnValue = GileadRPCHelper.parseReturnValue(returnValue,
							_beanManager);

					// Encode response
					//  
					responsePayload = RPC.encodeResponseForSuccess(p_rpcRequest.getMethod(),
									returnValue,
									p_rpcRequest.getSerializationPolicy());

				}
				catch (IllegalAccessException e) 
				{
			        SecurityException securityException = new SecurityException(
			            "Blocked attempt to access inaccessible method "
			                + p_rpcRequest.getMethod()
			                + " on target " + this);
			        securityException.initCause(e);
			        throw securityException;
				}
				catch (InvocationTargetException e)
				{
					// Clone exception if needed
					Exception exception = (Exception) GileadRPCHelper.parseReturnValue(e.getCause(), _beanManager);
					
					responsePayload = RPC.encodeResponseForFailure(p_rpcRequest.getMethod(), 
													    exception,
														p_rpcRequest.getSerializationPolicy());
				}
			} catch (SerializationException e) {
				_log.error("Serialization exception : " + e.getMessage(), e);e.printStackTrace();
				responsePayload = "server error: " + e.getMessage();
			}
			writeResponse(p_event.getHttpServletResponse(), responsePayload);
			p_event.close();
		} finally {
			getPerThreadRequest().set(oldRequest);
			getPerThreadResponse().set(oldResponse);
		}
	}
}

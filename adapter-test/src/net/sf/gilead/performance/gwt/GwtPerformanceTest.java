/**
 * 
 */
package net.sf.gilead.performance.gwt;

import net.sf.gilead.test.domain.interfaces.IUser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Performance test for GWT
 * @author bruno.marchesson
 *
 */
public class GwtPerformanceTest extends GWTTestCase
{
	//-------------------------------------------------------------------------
	//
	// Test init
	//
	//-------------------------------------------------------------------------
	/**
	 * Get module name
	 */
	public String getModuleName()
	{
		return "net.sf.gilead.Test";
	}
	
	//-------------------------------------------------------------------------
	//
	// Test methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Test clone user and messages
	 */
	public void testCloneUserAndMessage()
	{
		// Setup an asynchronous event handler.
		Timer timer = new Timer()
		{
			public void run()
			{
				// Call performance service
				PerformanceServiceAsync remoteService = (PerformanceServiceAsync) GWT.create(PerformanceService.class);
				((ServiceDefTarget) remoteService).setServiceEntryPoint( GWT.getModuleBaseURL() + "/PerformanceService");
				
				final long start = System.currentTimeMillis();
				remoteService.loadUserAndMessages(new AsyncCallback<IUser>()
				{
					public void onFailure(Throwable caught)
					{
						assertFalse(caught.toString(), false);

						// tell the test system the test is now done
						finishTest();
					}

					public void onSuccess(IUser result)
					{
						long end = System.currentTimeMillis();
						assertNotNull(result);
						
						System.out.println("Received user after " + (end - start) + " ms.");
						
						// tell the test system the test is now done
						finishTest();
					}
			
				});

			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(5000);

		// Schedule the event and return control to the test system.
		timer.schedule(100);
	}

}

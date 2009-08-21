/**
 * 
 */
package net.sf.gilead.adapter4appengine.client;

import net.sf.gilead.adapter4appengine.server.domain.TestEntity;

import com.google.appengine.api.datastore.Text;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Test emulation througt GWT serialization process.
 * @author bruno.marchesson
 *
 */
public class GwtEmulationTest extends GWTTestCase
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
		return "net.sf.gilead.adapter4appengine.Test";
	}
	
	//-------------------------------------------------------------------------
	//
	// Test methods
	//
	//-------------------------------------------------------------------------
	/**
	 * Test clone user and messages in stateful mode
	 */
	public void testEmulationSupport()
	{
		// Create test entity
		final TestEntity entity = new TestEntity();
		entity.setText(new Text("test text"));
		
		// Setup an asynchronous event handler.
		Timer timer = new Timer()
		{
			public void run()
			{
				// Call performance service
				TransfertServiceAsync remoteService = (TransfertServiceAsync) GWT.create(TransfertService.class);
				((ServiceDefTarget) remoteService).setServiceEntryPoint( GWT.getModuleBaseURL() + "/TransfertService");
				
				remoteService.sendAndReceive(entity, new AsyncCallback<TestEntity>()
				{
					public void onFailure(Throwable caught)
					{
						assertFalse(caught.toString(), false);

						// tell the test system the test is now done
						finishTest();
					}

					public void onSuccess(TestEntity result)
					{
						assertNotNull(result);
						assertNotNull(result.getText());
						assertEquals(entity.getText(), result.getText());
						assertEquals(entity.getText().getValue(), result.getText().getValue());
						
						// tell the test system the test is now done
						finishTest();
					}
				});
			}
		};

		// Set a delay period significantly longer than the
		// event is expected to take.
		delayTestFinish(100000);

		// Schedule the event and return control to the test system.
		timer.schedule(100);
	}
}

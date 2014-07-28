package at.diamonddogs.service.net;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import at.diamonddogs.data.dataobjects.WebRequest;
import at.diamonddogs.data.dataobjects.WebRequestFutureContainer;

/**
 * {@link WebRequestMap} manages {@link WebRequest}s and issues events whenever
 * {@link WebRequest} are added or removed.
 */
public class WebRequestMap {
	/**
	 * The action that is used to sinal {@link BroadcastReceiver}s every time a
	 * {@link WebRequest} has been started or finished
	 */
	public static final String ACTION_ACTIVE_WEBREQUESTS = "at.diamonddogs.service.net.HttpService.ACTION_ACTIVE_WEBREQUESTS";

	/**
	 * The intent extra is meant to be used in conjunction with
	 * {@link HttpService#ACTION_ACTIVE_WEBREQUESTS}. It holds all currently
	 * active {@link WebRequest}s
	 */
	public static final String INTENT_EXTRA_WEBREQUEST_COUNT = "at.diamonddogs.service.net.HttpService.INTENT_EXTRA_WEBREQUEST_COUNT";

	/**
	 * The singleton instance of this {@link WebRequestMap}
	 */
	private static WebRequestMap INSTANCE;

	/**
	 * A {@link Context}
	 */
	private Context context;

	/**
	 * The actual {@link WebRequest} storage
	 */
	private Map<String, WebRequestFutureContainer> webRequests;

	/**
	 * Used to obtain the Singleton instance of {@link WebRequestMap}
	 * 
	 * @param c
	 *            a {@link Context} that is used to obtain the application
	 *            context
	 * @return the Singleton instance of {@link WebRequestMap}
	 */
	public static final synchronized WebRequestMap getInstance(Context c) {
		if (INSTANCE == null) {
			INSTANCE = new WebRequestMap(c);
		}
		return INSTANCE;
	}

	private WebRequestMap(Context c) {
		super();
		this.context = c.getApplicationContext();
		webRequests = Collections.synchronizedMap(new HashMap<String, WebRequestFutureContainer>());
	}

	/**
	 * Puts an Object into the map and notifies {@link BroadcastReceiver}s that
	 * are listening to the {@link WebRequestMap#ACTION_ACTIVE_WEBREQUESTS}
	 * event.
	 * 
	 * @see HashMap#put(Object, Object)
	 */
	public WebRequestFutureContainer put(String key, WebRequestFutureContainer value) {
		sendActiveWebRequestsIntent();
		return webRequests.put(key, value);
	}

	/**
	 * Removes an Object from the map and notifies {@link BroadcastReceiver}s
	 * that are listening to the {@link WebRequestMap#ACTION_ACTIVE_WEBREQUESTS}
	 * event.
	 * 
	 * @see HashMap#put(Object, Object)
	 */
	public WebRequestFutureContainer remove(String key) {
		sendActiveWebRequestsIntent();
		return webRequests.remove(key);
	}

	/**
	 * Obtains the {@link WebRequestFutureContainer} object known by id
	 * 
	 * @param id
	 *            the id
	 * @see HashMap#remove(Object)
	 * @return the {@link WebRequestFutureContainer} object
	 */
	public WebRequestFutureContainer getWebRequestFutureContainerById(String id) {
		return webRequests.get(id);
	}

	/**
	 * Checks if the {@link WebRequestFutureContainer} known by id is currently
	 * managed by {@link WebRequestMap}
	 * 
	 * @param id
	 *            the id to check
	 * @return <code>true</code> if the {@link WebRequestFutureContainer} is
	 *         managed, <code>false</code> otherwise
	 */
	public boolean hasWebRequestFutureContainerWithId(String id) {
		return webRequests.containsKey(id);
	}

	/**
	 * Cancels the {@link WebRequest} contained in the
	 * {@link WebRequestFutureContainer} known by id with interruption
	 * permission
	 * 
	 * @param id
	 *            the id of the {@link WebRequestFutureContainer} to interrupt
	 * @return <code>true</code> or <code>false</code> depding on the success of
	 *         the cancel operation
	 */
	public boolean cancelWebRequestFutureContainerById(String id) {
		return cancelWebRequestFutureContainerById(id, true);
	}

	/**
	 * Cancels the {@link WebRequest} contained in the
	 * {@link WebRequestFutureContainer} known by id
	 * 
	 * @param id
	 *            the id of the {@link WebRequestFutureContainer} to interrupt
	 * @param mayInterruptIfRunning
	 *            cancel with interrupt?
	 * @return <code>true</code> or <code>false</code> depding on the success of
	 *         the cancel operation
	 */
	public boolean cancelWebRequestFutureContainerById(String id, boolean mayInterruptIfRunning) {
		WebRequestFutureContainer container = webRequests.get(id);
		boolean canceled = container.getFuture().cancel(mayInterruptIfRunning);
		container.getWebRequest().setCancelled(true);
		remove(id);
		return canceled;
	}

	/**
	 * Gets the number of {@link WebRequestFutureContainer}s managed by
	 * {@link WebRequestMap}
	 * 
	 * @return the number of active {@link WebRequestFutureContainer}s
	 */
	public int getNumberOfActiveWebRequests() {
		return webRequests.size();
	}

	private void sendActiveWebRequestsIntent() {
		Intent i = new Intent(ACTION_ACTIVE_WEBREQUESTS);
		i.putExtra(INTENT_EXTRA_WEBREQUEST_COUNT, webRequests.size());
		LocalBroadcastManager.getInstance(context).sendBroadcastSync(i);
	}
}

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

	private static WebRequestMap INSTANCE;

	private Context context;
	private Map<String, WebRequestFutureContainer> webRequests;

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

	public WebRequestFutureContainer put(String key, WebRequestFutureContainer value) {
		sendActiveWebRequestsIntent();
		return webRequests.put(key, value);
	}

	public WebRequestFutureContainer remove(String key) {
		sendActiveWebRequestsIntent();
		return webRequests.remove(key);
	}

	public WebRequestFutureContainer getWebRequestFutureContainerById(String id) {
		return webRequests.get(id);
	}

	public boolean hasWebRequestFutureContainerWithId(String id) {
		return webRequests.containsKey(id);
	}

	public boolean cancelWebRequestFutureContainerById(String id) {
		return cancelWebRequestFutureContainerById(id, true);
	}

	public boolean cancelWebRequestFutureContainerById(String id, boolean mayInterruptIfRunning) {
		WebRequestFutureContainer container = webRequests.get(id);
		boolean canceled = container.getFuture().cancel(mayInterruptIfRunning);
		container.getWebRequest().setCancelled(true);
		remove(id);
		return canceled;
	}

	public int size() {
		return webRequests.size();
	}

	private void sendActiveWebRequestsIntent() {
		Intent i = new Intent(ACTION_ACTIVE_WEBREQUESTS);
		i.putExtra(INTENT_EXTRA_WEBREQUEST_COUNT, webRequests.size());
		LocalBroadcastManager.getInstance(context).sendBroadcastSync(i);
	}
}

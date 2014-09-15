/*
 * Copyright (C) 2014 Spherical Elephant GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.diamonddogs.service.net;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import at.diamonddogs.data.dataobjects.WebRequest;
import at.diamonddogs.data.dataobjects.WebRequestFutureContainer;

/**
 * {@link WebRequestMap} manages {@link WebRequest}s and issues events whenever
 * {@link WebRequest} are added or removed.
 */
public class WebRequestMap {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebRequestMap.class.getSimpleName());
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
		LOGGER.debug("Adding To WebRequestMap " + key, new Throwable());
		WebRequestFutureContainer ret = webRequests.put(key, value);
		sendActiveWebRequestsIntent();
		return ret;
	}

	/**
	 * Removes an Object from the map and notifies {@link BroadcastReceiver}s
	 * that are listening to the {@link WebRequestMap#ACTION_ACTIVE_WEBREQUESTS}
	 * event.
	 * 
	 * @see HashMap#put(Object, Object)
	 */
	public WebRequestFutureContainer remove(String key) {
		LOGGER.debug("Removing From WebRequestMap " + key, new Throwable());
		WebRequestFutureContainer ret = webRequests.remove(key);
		sendActiveWebRequestsIntent();
		return ret;
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
		LOGGER.debug("Sending Active Webrequests Broadcast: " + webRequests.size());
		Intent i = new Intent(ACTION_ACTIVE_WEBREQUESTS);
		i.putExtra(INTENT_EXTRA_WEBREQUEST_COUNT, webRequests.size());
		LocalBroadcastManager.getInstance(context).sendBroadcast(i);
	}

	public static final void registerBroadcastReceiver(Context c, BroadcastReceiver bcr) {
		LocalBroadcastManager.getInstance(c).registerReceiver(bcr, new IntentFilter(ACTION_ACTIVE_WEBREQUESTS));
	}

	public static final void unregisterBroadcastReceiver(Context c, BroadcastReceiver bcr) {
		LocalBroadcastManager.getInstance(c).unregisterReceiver(bcr);
	}
}

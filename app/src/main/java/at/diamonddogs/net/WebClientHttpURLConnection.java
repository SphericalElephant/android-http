/*
 * Copyright (C) 2012, 2013 the diamond:dogs|group
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
package at.diamonddogs.net;

import android.content.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import at.diamonddogs.data.adapter.ReplyAdapter;
import at.diamonddogs.data.adapter.ReplyAdapter.Status;
import at.diamonddogs.data.dataobjects.Authentication;
import at.diamonddogs.data.dataobjects.WebReply;
import at.diamonddogs.exception.WebClientException;
import at.diamonddogs.util.Utils;

/**
 * This {@link WebClient} will be used on Gingerbread and above. Please do not
 * use this class directly, use {@link WebClientFactory} instead.
 */
public class WebClientHttpURLConnection extends WebClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebClientHttpURLConnection.class.getSimpleName());

	/**
	 * An instance of {@link HttpsURLConnection} handling the request
	 */
	private HttpURLConnection connection;

	/**
	 * Stores the number of retries for a request
	 */
	private int retryCount = 0;

	/**
	 * Default {@link WebClient} constructor
	 *
	 * @param context a {@link Context} object
	 */
	public WebClientHttpURLConnection(Context context) {
		super(context);
		SSLSocketFactory sslSocketFactory = SSLHelper.getInstance().SSL_FACTORY_JAVA;
		if (sslSocketFactory == null) {
			LOGGER.warn("No SSL Connection possible");
		} else {
			HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
		}
	}

	@Override
	public synchronized ReplyAdapter call() {

		ReplyAdapter listenerReply = null;
		if (webRequest == null) {
			throw new WebClientException("WebRequest must not be null!");
		}
		retryCount = webRequest.getNumberOfRetries();
		boolean abortLoop = false;
		do {
			try {
				WebReply reply;

				connection = (HttpURLConnection) webRequest.getUrl().openConnection();
				configureConnection();
				reply = runRequest();

				listenerReply = createListenerReply(webRequest, reply, null, Status.OK);

				abortLoop = true;
			} catch (Throwable tr) {

				if (retryCount >= 0) {
					try {
						Thread.sleep(webRequest.getRetryInterval());
					} catch (InterruptedException e) {
						LOGGER.error("WebRequest thread interrupted: " + webRequest, e);
					}
				}
				listenerReply = createListenerReply(webRequest, null, tr, Status.FAILED);
				LOGGER.info("Error running WebRequest: " + webRequest.getUrl() + " " + webRequest.getId(), tr);

				retryCount--;
			} finally {
				closeConnection();
			}
		} while (retryCount >= 0 && !abortLoop);

		if (webClientReplyListener != null) {
			webClientReplyListener.onWebReply(this, listenerReply);
		}
		return listenerReply;
	}

	private void closeConnection() {
		if (connection != null) {
			connection.disconnect();
			connection = null;
		}
	}

	private void configureConnection() throws ProtocolException {
		buildHeader();
		setAuthHeader();
		setRequestType();

		connection.setInstanceFollowRedirects(webRequest.isFollowRedirects());
		connection.setReadTimeout(webRequest.getReadTimeout());
		connection.setConnectTimeout(webRequest.getConnectionTimeout());
		connection.setInstanceFollowRedirects(webRequest.isFollowRedirects());
	}

	private void setRequestType() throws ProtocolException {
		switch (webRequest.getRequestType()) {
			case POST:
				connection.setRequestMethod("POST");
				connection.setDoOutput(true);
				break;
			case PUT:
				connection.setRequestMethod("PUT");
				connection.setDoOutput(true);
				break;
			case PATCH:
				// TODO: patch is not supported by HttpUrlConnection!
				connection.setRequestMethod("PATCH");
				connection.setDoOutput(true);
				break;
			case DELETE:
				// TODO: does not support setDoOutput(true)
				connection.setRequestMethod("DELETE");
				break;
			case GET:
				connection.setRequestMethod("GET");
				break;
			case HEAD:
				connection.setRequestMethod("HEAD");
				break;
		}
	}

	@Override
	protected void buildHeader() {
		Map<String, String> header = webRequest.getHeader();
		if (header != null) {
			for (String field : header.keySet()) {
				if (webRequest.isAppendHeader()) {
					if (!connection.getHeaderFields().containsKey(field)) {
						connection.addRequestProperty(field, header.get(field));
					}
				} else {
					if (!connection.getHeaderFields().containsKey(field)) {
						connection.setRequestProperty(field, header.get(field));
					}
				}
			}
		}
	}

	private void setAuthHeader() {
		Authentication auth = webRequest.getAuthentication();
		if (auth != null) {
			String cred = Utils.encrypt(auth.getUser() + ":" + auth.getPassword());
			connection.setRequestProperty("Authorization", "Basic " + cred);
		}
	}

	private WebReply runRequest() throws IOException {

		int statusCode = connection.getResponseCode();

		WebReply reply;

		switch (statusCode) {
			case HttpURLConnection.HTTP_PARTIAL:
			case HttpURLConnection.HTTP_OK:
				LOGGER.debug("WebRequest OK: " + webRequest);
				publishFileSize(connection.getContentLength());
				reply = handleResponseOk(connection.getInputStream(), statusCode, connection.getHeaderFields());
				break;
			case HttpURLConnection.HTTP_NO_CONTENT:
				reply = handleResponseOk(null, statusCode, connection.getHeaderFields());
				break;
			case HttpURLConnection.HTTP_NOT_MODIFIED:
				reply = handleResponseNotModified(statusCode, connection.getHeaderFields());
				break;
			default:
				LOGGER.debug("WebRequest DEFAULT: " + webRequest + " status code: " + statusCode);
				if (connection != null) {
					try {
						reply = handleResponseNotOk(connection.getInputStream(), statusCode, connection.getHeaderFields());
					} catch (Throwable tr) {
						LOGGER.debug("Error reading input stream, trying error stream!");
						reply = handleResponseNotOk(connection.getErrorStream(), statusCode, connection.getHeaderFields());
					}
				} else {
					reply = handleResponseNotOk(null, statusCode, connection.getHeaderFields());
				}

				break;
		}

		return reply;
	}
}

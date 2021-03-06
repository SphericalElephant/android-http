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

import java.io.IOException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpDeleteWithEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import at.diamonddogs.data.adapter.ReplyAdapter;
import at.diamonddogs.data.adapter.ReplyAdapter.Status;
import at.diamonddogs.data.dataobjects.Authentication;
import at.diamonddogs.data.dataobjects.WebReply;
import at.diamonddogs.data.dataobjects.WebRequest.Type;
import at.diamonddogs.exception.WebClientException;

/**
 * This {@link WebClient} will be used on Froyo and below. Please do not use
 * this class directly, use {@link WebClientFactory} instead.
 */
public class WebClientDefaultHttpClient extends WebClient implements HttpRequestRetryHandler, RedirectHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebClientDefaultHttpClient.class.getSimpleName());

	/**
	 * The instance of {@link DefaultHttpClient} handling the request
	 */
	private DefaultHttpClient httpClient;

	/**
	 * Request parameters
	 */
	private HttpRequestBase requestBase;

	/**
	 * Default {@link WebClient} constructor
	 * 
	 * @param context
	 *            a {@link Context} object
	 */
	public WebClientDefaultHttpClient(Context context) {
		super(context);
		SSLSocketFactory sslSocketFactory = SSLHelper.getInstance().SSL_FACTORY_APACHE;
		if (sslSocketFactory != null) {
			sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sslSocketFactory, 443));
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
			httpClient = new DefaultHttpClient(ccm, params);
		} else {
			httpClient = new DefaultHttpClient();
		}
		httpClient.setHttpRequestRetryHandler(this);
		if (followProtocolRedirect) {
			httpClient.setRedirectHandler(this);
		}
	}

	@Override
	public ReplyAdapter call() {

		ReplyAdapter listenerReply = null;
		HttpResponse response = null;
		try {
			WebReply reply;

			if (webRequest == null) {
				throw new WebClientException("WebRequest must not be null!");
			}

			setUpRequestBaseAccordingToRequestType();
			setAuthenticationParameters();

			configureConnection();

			LOGGER.info("Running RequestBase: " + requestBase);
			response = httpClient.execute(requestBase);
			reply = runRequest(response);

			listenerReply = createListenerReply(webRequest, reply, null, Status.OK);
		} catch (Throwable tr) {
			// TODO: passing a null reply will cause an nullpointer when calling
			// cacheObjectToFile
			listenerReply = createListenerReply(webRequest, null, tr, Status.FAILED);
			LOGGER.info("Error running webrequest: " + webRequest.getUrl() + " status: "
					+ (response == null ? "" : response.getStatusLine().getStatusCode()), tr);
		}
		if (webClientReplyListener != null) {
			webClientReplyListener.onWebReply(this, listenerReply);
		}
		return listenerReply;
	}

	private void setAuthenticationParameters() throws AuthenticationException {

		Authentication auth = webRequest.getAuthentication();
		if (webRequest.getAuthentication() != null) {
			LOGGER.info("Attaching auth info: " + auth);
			// @formatter:off
			requestBase.addHeader(new BasicScheme().authenticate(
					new UsernamePasswordCredentials(
						auth.getUser(),
						auth.getPassword()
					),
					requestBase
				)
			);
			// @formatter:on
		}
	}

	private void setUpRequestBaseAccordingToRequestType() throws Throwable {
		Type requestType = webRequest.getRequestType();

		if (requestType == Type.GET) {
			requestBase = new HttpGet(webRequest.getUrl().toURI());
		} else if (requestType == Type.HEAD) {
			requestBase = new HttpHead(webRequest.getUrl().toURI());
		} else if (requestType == Type.POST) {
			HttpPost post = new HttpPost(webRequest.getUrl().toURI());
			attachData(post);
		} else if (requestType == Type.PUT) {
			HttpPut put = new HttpPut(webRequest.getUrl().toURI());
			attachData(put);
		} else if (requestType == Type.PATCH) {
			HttpPatch patch = new HttpPatch(webRequest.getUrl().toURI());
			attachData(patch);
		} else if (requestType == Type.DELETE) {
			HttpDeleteWithEntity delete = new HttpDeleteWithEntity(webRequest.getUrl().toURI());
			attachData(delete);
		}
	}

	private void attachData(HttpEntityEnclosingRequestBase enclosingRequestBase) throws Throwable {
		// we need to remove the content length header, to prevent
		// httpClient.execute(...) from failing
		if (webRequest.getHeader() != null) {
			webRequest.removeHeaderField("Content-Length");
		}
		attachHttpEntity(enclosingRequestBase);
		requestBase = enclosingRequestBase;
	}

	private void attachHttpEntity(HttpEntityEnclosingRequestBase enclosingRequestBase) throws Throwable {
		HttpEntity entity = webRequest.getHttpEntity();

		if (entity != null) {
			enclosingRequestBase.setEntity(entity);
		}
	}

	private WebReply runRequest(HttpResponse response) throws IOException {
		int statusCode = response.getStatusLine().getStatusCode();
		WebReply reply = null;

		switch (statusCode) {
		case HttpStatus.SC_PARTIAL_CONTENT:
		case HttpStatus.SC_OK:
			LOGGER.debug("WebRequest OK: " + webRequest);
			publishFileSize(response.getEntity().getContentLength());
			reply = handleResponseOk(response.getEntity().getContent(), statusCode, convertHeaders(response.getAllHeaders()));
			break;
		case HttpStatus.SC_NOT_MODIFIED:
			LOGGER.debug("WebRequest Not modified: " + webRequest);
			reply = handleResponseNotModified(statusCode, convertHeaders(response.getAllHeaders()));
			break;
		case HttpStatus.SC_NO_CONTENT:
			reply = handleResponseOk(null, statusCode, convertHeaders(response.getAllHeaders()));
			break;
		default:
			LOGGER.debug("WebRequest DEFAULT: " + webRequest);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				reply = handleResponseNotOk(entity.getContent(), statusCode, convertHeaders(response.getAllHeaders()));
			} else {
				reply = handleResponseNotOk(null, statusCode, convertHeaders(response.getAllHeaders()));
			}

			break;
		}
		return reply;
	}

	private Map<String, List<String>> convertHeaders(Header[] headers) {
		HashMap<String, List<String>> ret = new HashMap<String, List<String>>();
		for (Header h : headers) {
			String key = h.getName();
			String value = h.getValue();
			if (ret.containsKey(key)) {
				ret.get(key).add(value);
			} else {
				List<String> values = new ArrayList<String>(10);
				values.add(value);
				ret.put(key, values);
			}
		}
		return ret;
	}

	private void configureConnection() throws ProtocolException {
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, webRequest.getConnectionTimeout());
		HttpConnectionParams.setSoTimeout(params, webRequest.getReadTimeout());
		HttpClientParams.setRedirecting(params, webRequest.isFollowRedirects());
		requestBase.setParams(params);
		buildHeader();
	}

	@Override
	protected void buildHeader() {
		Map<String, String> header = webRequest.getHeader();
		if (header != null) {
			for (String field : header.keySet()) {
				if (webRequest.isAppendHeader()) {
					if (!requestBase.containsHeader(field)) {
						requestBase.addHeader(field, header.get(field));
					}
				} else {
					if (!requestBase.containsHeader(field)) {
						requestBase.setHeader(field, header.get(field));
					}
				}
			}
		}
	}

	@Override
	public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
		LOGGER.error("executionCount:" + executionCount + " NumberOfRetries: " + webRequest.getNumberOfRetries() + " exception: "
				+ exception.toString());
		if (executionCount >= webRequest.getNumberOfRetries()) {
			return false;
		}
		// @formatter:off
		if ((exception instanceof NoHttpResponseException) || (exception instanceof ConnectTimeoutException)
				|| (exception instanceof ConnectionPoolTimeoutException) || (exception instanceof SocketTimeoutException)) {
			return true;
		}
		// @formatter:on
		return false;
	}

	@Override
	public URI getLocationURI(HttpResponse response, HttpContext context) throws org.apache.http.ProtocolException {
		Header[] headers = response.getHeaders("location");
		if (headers[0] != null) {
			try {
				LOGGER.error("getLocationURI: " + headers[0].getValue());
				return new URI(headers[0].getValue());
			} catch (URISyntaxException e) {
				LOGGER.error("error parsing url", e);
				return null;
			}
		}
		return null;
	}

	@Override
	public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
		if (!webRequest.isFollowRedirects()) {
			return false;
		}
		int status = response.getStatusLine().getStatusCode();
		LOGGER.debug("isRedirectRequested: " + status);
		if (status == HttpStatus.SC_MOVED_PERMANENTLY || status == HttpStatus.SC_MOVED_TEMPORARILY) {
			return true;
		}
		return false;
	}
}

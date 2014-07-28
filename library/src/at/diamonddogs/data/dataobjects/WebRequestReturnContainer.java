/*
 * Copyright (C) 2012, 2013, 2014 the diamond:dogs|group
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
package at.diamonddogs.data.dataobjects;

import java.util.List;
import java.util.Map;

/**
 * A wrapper object wrapping the {@link WebRequest}s id, the payload (in case of
 * a synchronous {@link WebRequest}) and the state of the operation.
 * {@link WebRequestReturnContainer} must be returned by any public method,
 * capable of running {@link WebRequest}s.
 */
public final class WebRequestReturnContainer {
	/**
	 * A flag indiciating the success of an operation
	 */
	private boolean successful;

	/**
	 * The id of the {@link WebRequest}
	 */
	private String id;

	/**
	 * The payload of the {@link WebRequest}. For synchronous {@link WebRequest}
	 * s, this is the result of the webrequest, for asynchronous
	 * {@link WebRequest} payload will always be null
	 */
	private Object payload;

	/**
	 * May contain the {@link Throwable} object that caused this
	 * {@link WebRequest} not to be successful (if
	 * {@link WebRequestReturnContainer#isSuccessful()} returns
	 * <code>false</code>)
	 */
	private Throwable throwable;

	/**
	 * The HTTP status code returned by the webserver
	 */
	private int httpStatusCode;

	/**
	 * The reply header
	 */
	private Map<String, List<String>> replyHeader;

	public WebRequestReturnContainer() {

	}

	@SuppressWarnings("javadoc")
	public boolean isSuccessful() {
		return successful;
	}

	@SuppressWarnings("javadoc")
	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}

	@SuppressWarnings("javadoc")
	public String getId() {
		return id;
	}

	@SuppressWarnings("javadoc")
	public void setId(String id) {
		this.id = id;
	}

	@SuppressWarnings("javadoc")
	public Object getPayload() {
		return payload;
	}

	@SuppressWarnings("javadoc")
	public void setPayload(Object payload) {
		this.payload = payload;
	}

	@SuppressWarnings("javadoc")
	public Throwable getThrowable() {
		return throwable;
	}

	@SuppressWarnings("javadoc")
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	@SuppressWarnings("javadoc")
	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	@SuppressWarnings("javadoc")
	public void setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	@SuppressWarnings("javadoc")
	public Map<String, List<String>> getReplyHeader() {
		return replyHeader;
	}

	@SuppressWarnings("javadoc")
	public void setReplyHeader(Map<String, List<String>> replyHeader) {
		this.replyHeader = replyHeader;
	}

}
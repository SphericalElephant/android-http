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
package at.diamonddogs.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.Toast;
import at.diamonddogs.callback.GenericCallback.ServerReply;
import at.diamonddogs.data.dataobjects.WebRequest;
import at.diamonddogs.http.R;
import at.diamonddogs.net.WebClient.HTTPStatus;
import at.diamonddogs.service.processor.ServiceProcessorMessageUtil;

/**
 * A generic {@link Callback} to be used when issuing {@link WebRequest}s. Clean
 * way of handling non successful {@link WebRequest}s.
 */
public abstract class GenericCallback<T extends ServerReply> implements Callback {
	public static final Logger LOGGER = LoggerFactory.getLogger(GenericCallback.class);

	private int expectedHttpStatusCode;
	private Context context;
	private boolean showToast = true;
	private int toastMessage = R.string.general_webrequestfailed;

	/**
	 * Constructs a {@link GenericCallback}
	 * 
	 * @param context
	 *            a {@link Context}
	 * @param expectedHttpStatusCode
	 *            the expected {@link HTTPStatus}
	 */
	public GenericCallback(Context context, int expectedHttpStatusCode) {
		this.expectedHttpStatusCode = expectedHttpStatusCode;
		this.context = context.getApplicationContext();
	}

	/**
	 * Constructs a {@link GenericCallback} with a custom toast error
	 * 
	 * @param context
	 *            a {@link Context}
	 * @param expectedHttpStatusCode
	 *            the expected {@link HTTPStatus}
	 * @param showToast
	 *            should the {@link Toast} be shown
	 * @param toastMessage
	 *            the {@link Toast}'s message
	 */
	public GenericCallback(Context context, int expectedHttpStatusCode, boolean showToast, int toastMessage) {
		this.expectedHttpStatusCode = expectedHttpStatusCode;
		this.context = context.getApplicationContext();
		this.showToast = showToast;
		this.toastMessage = toastMessage;
	}

	@Override
	public boolean handleMessage(Message msg) {

		if (!ServiceProcessorMessageUtil.isSuccessful(msg)) {
			Throwable tr = ServiceProcessorMessageUtil.getThrowable(msg);
			LOGGER.warn("Processing WebRequest was not successful!", tr);
			showToast();
			return onException(tr);
		}

		Object o = ServiceProcessorMessageUtil.getPayLoad(msg);

		if (!(o instanceof ServerReply)) {
			throw new IllegalArgumentException("Payloads that want to use GenericCallback need to implement ServerReply");
		}
		ServerReply serverReply = (ServerReply) o;

		if (!serverReply.isSuccess()) {
			String errorMessage = serverReply.getError();
			LOGGER.warn("Server reports error while processing WebRequest: " + errorMessage);
			showToast();
			return onServerReplyFailed(errorMessage);
		}

		int httpStatus = ServiceProcessorMessageUtil.getHttpStatusCode(msg);
		if (httpStatus != expectedHttpStatusCode) {
			LOGGER.warn("Server returned an unexpected HTTP status " + httpStatus);
			showToast();
			return onUnexpectedHttpStatusCode(expectedHttpStatusCode, httpStatus);
		}

		return onSuccess(ServiceProcessorMessageUtil.<T> getCastedPayLoad(msg));
	}

	/**
	 * Called when {@link ServiceProcessorMessageUtil#isSuccessful} returns
	 * <code>false</code>
	 * 
	 * @param tr
	 *            the {@link Throwable} linked to the problem which caused the
	 *            {@link WebRequest} to fail
	 * @return <code>true</code> or <code>false</code> depending on if the
	 *         {@link WebRequest} counts as handled or not.
	 */
	protected boolean onException(Throwable tr) {
		return true;
	}

	/**
	 * Called when the server reports {@link WebRequest} failure.
	 * 
	 * @param errorMessage
	 *            the error message generated by the server
	 * @return <code>true</code> or <code>false</code> depending on if the
	 *         {@link WebRequest} counts as handled or not.
	 */
	protected boolean onServerReplyFailed(String errorMessage) {
		return true;
	}

	/**
	 * Called when the expected http status code and the actual http status code
	 * don't match
	 * 
	 * @param expectedStatusCode
	 *            the expected status code
	 * @param actualStatusCode
	 *            the actual status code
	 * @return <code>true</code> or <code>false</code> depending on if the
	 *         {@link WebRequest} counts as handled or not.
	 */
	protected boolean onUnexpectedHttpStatusCode(int expectedStatusCode, int actualStatusCode) {
		return true;
	}

	protected abstract boolean onSuccess(T data);

	private void showToast() {
		if (showToast) {
			Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * This interface has to be implemented by the payload type if
	 * {@link GenericCallback} is to be used to pre process a server reply.
	 * 
	 */
	public interface ServerReply {
		/**
		 * Returns information on if the server deems the operation a success
		 * 
		 * @return <code>true</code> if the server reckons that the request was
		 *         a success, <code>false</code> otherwise
		 */
		public boolean isSuccess();

		/**
		 * A human readable error message created by the server
		 * 
		 * @return a {@link String}
		 */
		public String getError();
	}

}

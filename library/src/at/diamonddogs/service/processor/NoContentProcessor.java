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
package at.diamonddogs.service.processor;

import android.content.Context;
import android.os.Handler;
import at.diamonddogs.data.adapter.ReplyAdapter;
import at.diamonddogs.data.adapter.ReplyAdapter.Status;
import at.diamonddogs.data.dataobjects.Request;
import at.diamonddogs.data.dataobjects.WebReply;
import at.diamonddogs.exception.ProcessorExeception;
import at.diamonddogs.net.WebClient.HTTPStatus;
import at.diamonddogs.util.CacheManager.CachedObject;

public class NoContentProcessor extends ServiceProcessor<Void> {
	public static final int ID = 158151;

	@Override
	public void processWebReply(Context c, ReplyAdapter r, Handler handler) {
		try {
			if (r.getStatus() == Status.OK) {
				int httpStatusCode = ((WebReply) r.getReply()).getHttpStatusCode();
				if (httpStatusCode != HTTPStatus.HTTP_NO_CONTENT) {
					handler.sendMessage(createErrorMessage(
						new ProcessorExeception("Request had content, or wrong status code status code was " + httpStatusCode), r));
				} else {
					handler.sendMessage(createReturnMessage(r, null));
				}
			} else {
				handler.sendMessage(createErrorMessage(r.getThrowable(), r));
			}
		} catch (Throwable tr) {
			handler.sendMessage(createErrorMessage(tr, r));
		}
	}

	@Override
	public void processCachedObject(CachedObject cachedObject, Handler handler, Request request) {
		// there is no cache support for requests with no content
	}

	@Override
	public int getProcessorID() {
		return ID;
	}

}

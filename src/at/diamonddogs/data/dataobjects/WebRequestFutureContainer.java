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

import java.util.concurrent.Future;

public final class WebRequestFutureContainer {
	private final WebRequest webRequest;
	private final Future<?> future;

	public WebRequestFutureContainer(WebRequest webRequest, Future<?> future) {
		this.webRequest = webRequest;
		this.future = future;
	}

	public WebRequest getWebRequest() {
		return webRequest;
	}

	public Future<?> getFuture() {
		return future;
	}

}
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
package at.diamonddogs.data.dataobjects;

import at.diamonddogs.http.entity.JSONHttpEntity;

import com.google.gson.Gson;

/**
 * Use this special form of {@link WebRequest} if you want to transport JSON
 * data. Uses gson to serialise the request data.
 */
public class JSONWebRequest<T> extends WebRequest {
	private final Gson gson = new Gson();

	public JSONWebRequest(T data) {
		try {
			JSONHttpEntity e = new JSONHttpEntity(gson.toJson(data));
		} catch (Throwable tr) {
			throw new RuntimeException(tr);
		}
	}

}

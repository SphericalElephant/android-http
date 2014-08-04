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
package at.diamonddogs.data.adapter.json;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONObject;

import at.diamonddogs.data.adapter.WebRequestAdapter;
import at.diamonddogs.data.dataobjects.WebRequest;
import at.diamonddogs.http.entity.JSONHttpEntity;

import com.google.gson.Gson;

/**
 * Wraps a usual {@link WebRequest} with the required data to send a JSON
 * {@link WebRequest}.
 */
public class JSONRequestAdapter implements WebRequestAdapter<WebRequest> {
	private final WebRequest webRequest;
	private final Gson gson = new Gson();

	public JSONRequestAdapter(WebRequest webRequest, JSONObject input) throws UnsupportedEncodingException {
		this.webRequest = webRequest;
		JSONHttpEntity e = new JSONHttpEntity(input);
		this.webRequest.setHttpEntity(e);
	}

	public JSONRequestAdapter(WebRequest webRequest, JSONArray input) throws UnsupportedEncodingException {
		this.webRequest = webRequest;
		JSONHttpEntity e = new JSONHttpEntity(input);
		this.webRequest.setHttpEntity(e);
	}

	public JSONRequestAdapter(WebRequest webRequest, String input) throws UnsupportedEncodingException {
		this.webRequest = webRequest;
		JSONHttpEntity e = new JSONHttpEntity(input);
		this.webRequest.setHttpEntity(e);
	}

	public JSONRequestAdapter(WebRequest webRequest, Object input) throws UnsupportedEncodingException {
		this.webRequest = webRequest;
		JSONHttpEntity e = new JSONHttpEntity(gson.toJson(input));
		this.webRequest.setHttpEntity(e);
	}

	/**
	 * Obtain the JSONized {@link WebRequest}
	 */
	@Override
	public WebRequest getRequest() {
		return webRequest;
	}
}

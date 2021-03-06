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
package at.diamonddogs.http.entity;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A specialized form of {@link StringEntity} that takes care of JSON writing
 * for you.
 */
public class JSONHttpEntity extends StringEntity {

	public JSONHttpEntity(JSONObject input, String contentType, String charset) throws UnsupportedEncodingException {
		this(input.toString(), contentType, charset);
	}

	public JSONHttpEntity(JSONArray input, String contentType, String charset) throws UnsupportedEncodingException {
		this(input.toString(), contentType, charset);
	}

	public JSONHttpEntity(String s, String contentType, String charset) throws UnsupportedEncodingException {
		super(s, charset);
		setContentType(contentType);
	}

	public JSONHttpEntity(JSONObject input, String contentType) throws UnsupportedEncodingException {
		this(input, contentType, null);
	}

	public JSONHttpEntity(JSONArray input, String contentType) throws UnsupportedEncodingException {
		this(input, contentType, null);
	}

	public JSONHttpEntity(String s, String contentType) throws UnsupportedEncodingException {
		this(s, contentType, null);
	}

	public JSONHttpEntity(JSONObject input) throws UnsupportedEncodingException {
		this(input.toString());
	}

	public JSONHttpEntity(JSONArray input) throws UnsupportedEncodingException {
		this(input.toString());
	}

	public JSONHttpEntity(String s) throws UnsupportedEncodingException {
		super(s);
		setContentType("application/json");
	}

}

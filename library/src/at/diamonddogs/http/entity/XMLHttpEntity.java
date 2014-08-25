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

/**
 * A simple {@link StringEntity} wrapper for XML data, please note that this
 * wrapper merely sets the content type and does no XML validation, checking,
 * etc.
 * 
 */
public class XMLHttpEntity extends StringEntity {
	public XMLHttpEntity(String s) throws UnsupportedEncodingException {
		this(s, false);
	}

	public XMLHttpEntity(String s, boolean readAbleByCasualUsers) throws UnsupportedEncodingException {
		super(s);
		if (readAbleByCasualUsers) {
			setContentType("text/xml");
		} else {
			setContentType("application/xml");
		}
	}

	public XMLHttpEntity(String s, String charset) throws UnsupportedEncodingException {
		this(s, charset, false);
	}

	public XMLHttpEntity(String s, String charset, boolean readAbleByCasualUsers) throws UnsupportedEncodingException {
		super(s, charset);
		if (readAbleByCasualUsers) {
			setContentType("text/xml");
		} else {
			setContentType("application/xml");
		}
	}
}

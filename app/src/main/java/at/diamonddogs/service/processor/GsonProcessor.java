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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * This processor uses Gson (http://code.google.com/p/google-gson/) to parse
 * pojos. Due to the dynamic nature of this processor, users will have to
 * provide a processorId to the constructor.
 * 
 * @param <CLAZZ>
 *            the {@link Class} of the {@link Object} to be parsed
 */
public class GsonProcessor<OUTPUT> extends DataProcessor<String, OUTPUT> {
	private static final boolean VERBOSE = true;
	private static final Logger LOGGER = LoggerFactory.getLogger(GsonProcessor.class.getSimpleName());

	protected final int processorId;
	protected final Class<OUTPUT> clazz;
	protected final Gson gson;

	/**
	 * Constructor
	 * 
	 * @param clazz
	 *            the class instance of the {@link Object} to be parsed
	 * @param processorId
	 *            a app unique processorId to be used by this processor
	 */
	public GsonProcessor(Class<OUTPUT> clazz, int processorId) {
		this.clazz = clazz;
		this.processorId = processorId;
		this.gson = buildGson();
	}

	/**
	 * Hook method to provide a specialized {@link Gson} instance, returns new
	 * Gson() by default.
	 * 
	 * @return
	 */
	protected Gson buildGson() {
		return new Gson();
	}

	@Override
	protected String createParsedObjectFromByteArray(byte[] data) {
		return new String(data);
	}

	@Override
	protected OUTPUT parse(String inputObject) {
		if (VERBOSE) {
			LOGGER.info("Json String: " + inputObject);
		}
		return gson.fromJson(inputObject, clazz);
	}

	@Override
	public int getProcessorID() {
		return this.processorId;
	}
}

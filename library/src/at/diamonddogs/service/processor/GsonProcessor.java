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

public class GsonProcessor<CLAZZ extends Class<?>, OUTPUT> extends DataProcessor<String, OUTPUT> {
	private static final Logger LOGGER = LoggerFactory.getLogger(GsonProcessor.class.getSimpleName());

	protected final int processorId;
	protected final CLAZZ clazz;
	protected final Gson gson = new Gson();

	public GsonProcessor(CLAZZ clazz, int processorId) {
		this.clazz = clazz;
		this.processorId = processorId;
	}

	@Override
	protected String createParsedObjectFromByteArray(byte[] data) {
		return new String(data);
	}

	@Override
	protected OUTPUT parse(String inputObject) {
		LOGGER.info("Json String: " + inputObject);
		return gson.fromJson(inputObject, clazz);
	}

	@Override
	public int getProcessorID() {
		return this.processorId;
	}
}

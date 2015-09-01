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
package at.diamonddogs.ui.annotation;

import at.diamonddogs.data.dataobjects.WebRequest;

/**
 * To be implemented by all classes that wish to use the UI annotations to
 * control the behavior of UI components during {@link WebRequest} execution.
 */
public interface UiAnnotationProcessable {
	/**
	 * Gets called every time processing is necessary. The processor that is
	 * passed to this method can be used to automatically process UI changes
	 * according to annotations.
	 * 
	 * @param processor
	 *            the {@link UiAnnotationProcessor} to be used when processing
	 *            the ui
	 */
	public void processUiAnnotations(UiAnnotationProcessor processor) throws IllegalAccessException,
		IllegalArgumentException;
}

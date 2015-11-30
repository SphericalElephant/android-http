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
 */package at.diamonddogs.ui.annotation;

/**
 * This interface needs to be implemented by all classes that wish to process
 * the Ui
 */
public interface UiAnnotationProcessor {

	/**
	 * Called whenever the Ui should be processed
	 * 
	 * @param o
	 *            the {@link Object} to be processed
	 */
	 void process(Object o);

}

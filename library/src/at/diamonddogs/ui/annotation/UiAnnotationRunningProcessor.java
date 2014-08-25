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

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.view.View;
import android.widget.TextView;
import at.diamonddogs.data.dataobjects.WebRequest;

/**
 * This processor is used when {@link WebRequest}s start running
 */
public final class UiAnnotationRunningProcessor implements UiAnnotationProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(UiAnnotationRunningProcessor.class.getSimpleName());

	@Override
	public void process(Object o) {
		try {
			setWebRequestsRunningState(o);
		} catch (Throwable tr) {
			LOGGER.error("Error while processing", tr);
		}
	}

	private void setWebRequestsRunningState(Object o) throws IllegalAccessException, IllegalArgumentException {
		Field[] fields = o.getClass().getDeclaredFields();
		for (Field f : fields) {
			f.setAccessible(true);
			Object obj = f.get(o);
			if (obj instanceof View) {
				LOGGER.info("View found");
				View v = (View) obj;
				handleViewAnnotationsRunning(f, v);
			} else if (f.isAnnotationPresent(SearchForUiElements.class)) {
				LOGGER.info("SearchForUiElements found, recursive lookup");
				setWebRequestsRunningState(obj);
			}

			f.setAccessible(false);
		}
	}

	private void handleViewAnnotationsRunning(Field f, View v) {
		if (f.isAnnotationPresent(ClearUiElementOnWebRequest.class)) {
			LOGGER.info("CleanUiElementOnWebRequest annotation found");
			if (v instanceof TextView) {
				((TextView) v).setText("");
			}
		}
		if (f.isAnnotationPresent(DisableUiElementOnWebRequest.class)) {
			LOGGER.info("DisableUiElementOnWebRequest annotation found");
			v.setEnabled(false);
		}
		if (f.isAnnotationPresent(HideUiElementOnWebRequest.class)) {
			LOGGER.info("HideUiElementOnWebRequest annotation found");
			v.setVisibility(View.INVISIBLE);
		}
	}

}

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

import android.view.View;
import android.widget.TextView;

public final class UiAnnotationProcessor {

	public UiAnnotationProcessor() {
		super();
	}

	public void setWebRequestsRunningState(Object o) throws IllegalAccessException, IllegalArgumentException {
		Field[] fields = o.getClass().getFields();
		for (Field f : fields) {

			f.setAccessible(true);
			Object obj = f.get(o);
			if (obj instanceof View) {
				View v = (View) obj;
				handleViewAnnotations(f, v);
			} else {
				setWebRequestsRunningState(o);
			}

			f.setAccessible(false);
		}
	}

	private void handleViewAnnotations(Field f, View v) {
		if (f.isAnnotationPresent(ClearUiElementOnWebRequest.class)) {
			if (v instanceof TextView) {
				((TextView) v).setText("");
			}
		}
		if (f.isAnnotationPresent(DisableUiElementOnWebRequest.class)) {
			v.setEnabled(false);
		}
		if (f.isAnnotationPresent(HideUiElementOnWebRequest.class)) {
			v.setVisibility(View.INVISIBLE);
		}
	}

	public void setWebRequestsNotRunningState(Field f, View v) throws IllegalAccessException, IllegalArgumentException {
	}
}

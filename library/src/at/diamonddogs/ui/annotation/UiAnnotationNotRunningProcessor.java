package at.diamonddogs.ui.annotation;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.view.View;

public class UiAnnotationNotRunningProcessor implements UiAnnotationProcessor {
	private static final Logger LOGGER = LoggerFactory.getLogger(UiAnnotationNotRunningProcessor.class.getSimpleName());

	@Override
	public void process(Object o) {
		try {
			setWebRequestsNotRunningState(o);
		} catch (Throwable tr) {
			LOGGER.error("Error while processing", tr);
		}
	}

	private void setWebRequestsNotRunningState(Object o) throws IllegalAccessException, IllegalArgumentException {
		Field[] fields = o.getClass().getFields();
		for (Field f : fields) {

			f.setAccessible(true);
			Object obj = f.get(o);
			if (obj instanceof View) {
				View v = (View) obj;
				handleViewAnnotationsNotRunning(f, v);
			} else if (f.isAnnotationPresent(SearchForUiElements.class)) {
				setWebRequestsNotRunningState(o);
			}

			f.setAccessible(false);
		}
	}

	private void handleViewAnnotationsNotRunning(Field f, View v) {
		if (f.isAnnotationPresent(DisableUiElementOnWebRequest.class)) {
			v.setEnabled(true);
		}
		if (f.isAnnotationPresent(HideUiElementOnWebRequest.class)) {
			v.setVisibility(View.VISIBLE);
		}
	}
}

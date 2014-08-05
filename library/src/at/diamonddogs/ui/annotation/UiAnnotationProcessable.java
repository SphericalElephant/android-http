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

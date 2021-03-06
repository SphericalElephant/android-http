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
package at.diamonddogs.ui.receiver;

import at.diamonddogs.ui.annotation.UiAnnotationProcessable;

/**
 * To be implemented by any class that wants to hide, show or provide the state
 * of indeterminate progress widgets
 */
public interface IndeterminateProgressControl extends UiAnnotationProcessable {
	/**
	 * Shows the indeterminate progress
	 */
	public void showIndeterminateProgress();

	/**
	 * Hides the indeterminate progress
	 */
	public void hideIndeterminateProgress();

	/**
	 * Returns the current state of indeterminate progress
	 * 
	 * @return <code>true</code> if the progress is showing, <code>false</code>
	 *         otherwise
	 */
	public boolean isIndeterminateProgressShowing();
}

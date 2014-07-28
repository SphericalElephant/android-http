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
package at.diamonddogs.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import at.diamonddogs.data.dataobjects.WebRequest;
import at.diamonddogs.service.net.WebRequestMap;

/**
 * A small {@link BroadcastReceiver} that can be used to control the
 * indeterminate progress for {@link WebRequest}s
 */
public class ProgressReceiver extends BroadcastReceiver {
	private IndeterminateProgressControl indeterminateProgressControl;

	@Override
	public void onReceive(Context context, Intent intent) {
		int activeWebRequests = intent.getIntExtra(WebRequestMap.INTENT_EXTRA_WEBREQUEST_COUNT, -1);
		if (activeWebRequests > 0) {
			if (!indeterminateProgressControl.isIndeterminateProgressShowing()) {
				indeterminateProgressControl.showIndeterminateProgress();
			}
		} else {
			indeterminateProgressControl.hideIndeterminateProgress();
		}
	}

	/**
	 * To be implemented by any class that wants to hide, show or provide the
	 * state of indeterminate progress widgets
	 */
	public interface IndeterminateProgressControl {
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
		 * @return <code>true</code> if the progress is showing,
		 *         <code>false</code> otherwise
		 */
		public boolean isIndeterminateProgressShowing();
	}
}

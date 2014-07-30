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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import at.diamonddogs.data.dataobjects.WebRequest;
import at.diamonddogs.service.net.WebRequestMap;

/**
 * A small {@link BroadcastReceiver} that can be used to control the
 * indeterminate progress for {@link WebRequest}s
 */
public class ProgressReceiver extends BroadcastReceiver {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProgressReceiver.class.getSimpleName());

	private IndeterminateProgressControl indeterminateProgressControl;
	private Handler handler;

	public ProgressReceiver(IndeterminateProgressControl indeterminateProgressControl) {
		super();
		this.handler = new Handler();
		this.indeterminateProgressControl = indeterminateProgressControl;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		int activeWebRequests = intent.getIntExtra(WebRequestMap.INTENT_EXTRA_WEBREQUEST_COUNT, -1);
		LOGGER.info("Active Request: " + activeWebRequests);

		if (activeWebRequests > 0) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					LOGGER.info("Attempting to show progress");
					if (!indeterminateProgressControl.isIndeterminateProgressShowing()) {
						indeterminateProgressControl.showIndeterminateProgress();
					}
				}
			});

		} else {
			handler.post(new Runnable() {

				@Override
				public void run() {
					LOGGER.info("Attempting to hide progress");
					if (indeterminateProgressControl.isIndeterminateProgressShowing()) {
						indeterminateProgressControl.hideIndeterminateProgress();
					}
				}
			});
		}
	}
}

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
import at.diamonddogs.ui.annotation.UiAnnotationNotRunningProcessor;
import at.diamonddogs.ui.annotation.UiAnnotationRunningProcessor;

/**
 * A small {@link BroadcastReceiver} that can be used to control the
 * indeterminate progress for {@link WebRequest}s. Must be initialized on the Ui
 * (Main) Thread
 */
public class ProgressReceiver extends BroadcastReceiver {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProgressReceiver.class.getSimpleName());

	private static final int PROGRESS_THRESHOLD_MS = 1000;

	private IndeterminateProgressControl indeterminateProgressControl;
	private final UiAnnotationRunningProcessor uiAnnotationRunningProcessor = new UiAnnotationRunningProcessor();
	private final UiAnnotationNotRunningProcessor uiAnnotationNotRunningProcessor = new UiAnnotationNotRunningProcessor();
	private Handler handler;
	private Runnable currentRunnable;

	public ProgressReceiver(IndeterminateProgressControl indeterminateProgressControl) {
		super();
		this.handler = new Handler();
		this.indeterminateProgressControl = indeterminateProgressControl;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		int activeWebRequests = intent.getIntExtra(WebRequestMap.INTENT_EXTRA_WEBREQUEST_COUNT, -1);
		LOGGER.info("Active Request: " + activeWebRequests);

		if (currentRunnable != null) {
			handler.removeCallbacks(currentRunnable);
		}

		if (activeWebRequests > 0) {
			handler.post(currentRunnable = new Runnable() {

				@Override
				public void run() {
					try {
						currentRunnable = null;
						LOGGER.info("Attempting to show progress");
						if (!indeterminateProgressControl.isIndeterminateProgressShowing()) {
							indeterminateProgressControl.showIndeterminateProgress();
							indeterminateProgressControl.processUiAnnotations(uiAnnotationRunningProcessor);
						}
					} catch (Throwable tr) {
						LOGGER.error("Error while running UI processing.", tr);
					}
				}
			});

		} else {
			handler.postDelayed(currentRunnable = new Runnable() {

				@Override
				public void run() {
					try {
						LOGGER.info("Attempting to hide progress");
						currentRunnable = null;
						if (indeterminateProgressControl.isIndeterminateProgressShowing()) {
							indeterminateProgressControl.hideIndeterminateProgress();
							indeterminateProgressControl.processUiAnnotations(uiAnnotationNotRunningProcessor);
						}
					} catch (Throwable tr) {
						LOGGER.error("Error while running UI processing.", tr);
					}
				}
			}, PROGRESS_THRESHOLD_MS);
		}
	}
}

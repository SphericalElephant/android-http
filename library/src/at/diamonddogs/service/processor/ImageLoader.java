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
package at.diamonddogs.service.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.ImageView;
import at.diamonddogs.data.dataobjects.WebRequest;
import at.diamonddogs.service.net.HttpServiceAssister;
import at.diamonddogs.service.processor.ImageProcessor.ImageProcessHandler;

/**
 * A utility class that facilitates image loading, image display, default image
 * display and user retry.
 */
public class ImageLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageLoader.class.getSimpleName());

	private final HttpServiceAssister assister;
	private final Handler.Callback callback;
	private boolean allowUserRetry = true;

	public ImageLoader(HttpServiceAssister assister, Handler.Callback callback) {
		super();
		this.assister = assister;
		this.callback = callback;
	}

	public void loadImage(ImageView imageView, OnClickListener onClickListener, String url, Animation fadeInAnimation, int defaultImage) {
		ImageProcessor.ImageProcessHandler handler = new ImageProcessor.ImageProcessHandler(imageView, url, fadeInAnimation, defaultImage);
		handler.setCallback(new AllowRetryCallback(imageView, onClickListener, handler));
		// @formatter:off 
		assister.runWebRequest(
			handler,
			ImageProcessor.getDefaultImageRequest(url),
			new ImageProcessor()
		);
		// @formatter:on
	}

	public void loadImage(ImageView imageView, OnClickListener onClickListener, String url, int defaultImage) {
		ImageProcessor.ImageProcessHandler handler = new ImageProcessor.ImageProcessHandler(imageView, url, defaultImage);
		handler.setCallback(new AllowRetryCallback(imageView, onClickListener, handler));
		// @formatter:off 
		assister.runWebRequest(
			handler,
			ImageProcessor.getDefaultImageRequest(url),
			new ImageProcessor()
		);
		// @formatter:on
	}

	public void loadImage(ImageView imageView, OnClickListener onClickListener, String url, Animation fadeInAnimation) {
		ImageProcessor.ImageProcessHandler handler = new ImageProcessor.ImageProcessHandler(imageView, url, fadeInAnimation);
		handler.setCallback(new AllowRetryCallback(imageView, onClickListener, handler));
		// @formatter:off 
		assister.runWebRequest(
			handler,
			ImageProcessor.getDefaultImageRequest(url),
			new ImageProcessor()
		);
		// @formatter:on
	}

	public void loadImage(ImageView imageView, OnClickListener onClickListener, String url) {
		ImageProcessor.ImageProcessHandler handler = new ImageProcessor.ImageProcessHandler(imageView, url);
		handler.setCallback(new AllowRetryCallback(imageView, onClickListener, handler));
		// @formatter:off 
		assister.runWebRequest(
			handler,
			ImageProcessor.getDefaultImageRequest(url),
			new ImageProcessor()
		);
		// @formatter:on
	}

	public void setAllowUserRetry(boolean allowUserRetry) {
		this.allowUserRetry = allowUserRetry;
	}

	public final class AllowRetryCallback implements Callback {
		private ImageView imageView;
		private OnClickListener onClickListener;
		private ImageProcessHandler handler;

		public AllowRetryCallback(ImageView imageView, OnClickListener onClickListener, ImageProcessHandler handler) {
			super();
			this.imageView = imageView;
			this.onClickListener = onClickListener;
			this.handler = handler;
		}

		@Override
		public boolean handleMessage(Message msg) {
			if (allowUserRetry && !ServiceProcessorMessageUtil.isSuccessful(msg)) {
				LOGGER.info("Image download was not successful, allowing the user to retry!");
				final WebRequest oldWebRequest = ServiceProcessorMessageUtil
					.getWebRequest(msg);
				imageView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						LOGGER.info("The user is retrying the image download");

						// @formatter:off 
						assister.runWebRequest(
							handler,
							ImageProcessor.getDefaultImageRequest(
								oldWebRequest.getUrl().toString()
							),
							new ImageProcessor()
						);
						// @formatter:on
					}
				});
				return false;
			} else {
				LOGGER.info("Image download succeded, restoring old OnClickListener for image view");
				imageView.setOnClickListener(onClickListener);
				return callback.handleMessage(msg);
			}
		}
	}
}

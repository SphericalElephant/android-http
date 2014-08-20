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

import android.os.Handler;
import android.view.animation.Animation;
import android.widget.ImageView;
import at.diamonddogs.service.net.HttpServiceAssister;

public class ImageLoader {
	private final HttpServiceAssister assister;
	private final Handler.Callback callback;

	public ImageLoader(HttpServiceAssister assister, Handler.Callback callback) {
		super();
		this.assister = assister;
		this.callback = callback;
	}

	public void loadImage(ImageView imageView, String url, Animation fadeInAnimation, int defaultImage) {
		ImageProcessor.ImageProcessHandler handler = new ImageProcessor.ImageProcessHandler(imageView, url, fadeInAnimation, defaultImage);
		handler.setCallback(callback);
		// @formatter:off 
		assister.runWebRequest(
			handler,
			ImageProcessor.getDefaultImageRequest(url),
			new ImageProcessor()
		);
		// @formatter:on
	}

	public void loadImage(ImageView imageView, String url, int defaultImage) {
		ImageProcessor.ImageProcessHandler handler = new ImageProcessor.ImageProcessHandler(imageView, url, defaultImage);
		handler.setCallback(callback);
		// @formatter:off 
		assister.runWebRequest(
			handler,
			ImageProcessor.getDefaultImageRequest(url),
			new ImageProcessor()
		);
		// @formatter:on
	}

	public void loadImage(ImageView imageView, String url, Animation fadeInAnimation) {
		ImageProcessor.ImageProcessHandler handler = new ImageProcessor.ImageProcessHandler(imageView, url, fadeInAnimation);
		handler.setCallback(callback);
		// @formatter:off 
		assister.runWebRequest(
			handler,
			ImageProcessor.getDefaultImageRequest(url),
			new ImageProcessor()
		);
		// @formatter:on
	}

	public void loadImage(ImageView imageView, String url) {
		ImageProcessor.ImageProcessHandler handler = new ImageProcessor.ImageProcessHandler(imageView, url);
		handler.setCallback(callback);
		// @formatter:off 
		assister.runWebRequest(
			handler,
			ImageProcessor.getDefaultImageRequest(url),
			new ImageProcessor()
		);
		// @formatter:on
	}
}

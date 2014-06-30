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

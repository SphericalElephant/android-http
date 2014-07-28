package at.diamonddogs.data.dataobjects;

import java.util.concurrent.Future;

public final class WebRequestFutureContainer {
	private final WebRequest webRequest;
	private final Future<?> future;

	public WebRequestFutureContainer(WebRequest webRequest, Future<?> future) {
		this.webRequest = webRequest;
		this.future = future;
	}

	public WebRequest getWebRequest() {
		return webRequest;
	}

	public Future<?> getFuture() {
		return future;
	}

}
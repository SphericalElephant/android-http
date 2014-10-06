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
package at.diamonddogs.util;

import android.content.Context;
import android.os.AsyncTask;
import at.diamonddogs.util.CacheManager.CacheAlarmReceiver;

/**
 * Alternative to registering a {@link CacheAlarmReceiver} using
 * {@link CacheManager#enableScheduledCacheCleaner(android.content.Context)}.
 * Will not schedule cache cleaning but rather clean the cache when requested.
 */
public class CleanCacheAsyncTask extends AsyncTask<Context, Void, Void> {

	public static final void runClean(Context c) {
		CleanCacheAsyncTask t = new CleanCacheAsyncTask();
		t.execute(c.getApplicationContext());
	}

	private CleanCacheAsyncTask() {

	}

	@Override
	protected Void doInBackground(Context... params) {
		CacheManager.getInstance().cleanExpired(params[0]);
		return null;
	}
}

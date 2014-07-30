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
package at.diamonddogs.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import at.diamonddogs.service.net.HttpServiceAssister;

public class HttpListFragment extends ListFragment {
	protected HttpServiceAssister assister;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		assister = new HttpServiceAssister(getActivity());
	}

	@Override
	public void onResume() {
		super.onResume();
		assister.bindService();
	}

	@Override
	public void onPause() {
		super.onPause();
		assister.unbindService();
	}
}

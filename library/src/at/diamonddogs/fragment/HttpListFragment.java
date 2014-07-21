package at.diamonddogs.fragment;

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

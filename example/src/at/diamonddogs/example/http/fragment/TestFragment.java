package at.diamonddogs.example.http.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import at.diamonddogs.example.http.R;
import at.diamonddogs.ui.annotation.HideUiElementOnWebRequest;

public class TestFragment extends Fragment {
	@HideUiElementOnWebRequest
	private TextView testText;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.testfragment, container, false);
		testText = (TextView) v.findViewById(R.id.testfragment_testText);
		return v;
	}
}

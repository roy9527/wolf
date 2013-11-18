package com.roy.wolf.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.roy.wolf.R;
import com.roy.wolf.activity.MainActivity;
import com.roy.wolf.handler.SendActivityHandler;
import com.roy.wolf.net.RequestListener;

public class SendActivityFragment extends Fragment {

	private View view;

	public SendActivityFragment(Bundle bundle) {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.send_activity_fragment, null);
		init();
		return view;
	}

	private void init() {
		final EditText title = (EditText) view.findViewById(R.id.send_msg_title);
		final EditText msg = (EditText) view.findViewById(R.id.send_msg_msg);

		view.findViewById(R.id.send_ok).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						String t = title.getText().toString();
						String m = msg.getText().toString();
						SendActivityHandler sah = new SendActivityHandler(getActivity());
						sah.setParams(t, m);
						sah.onRequest(new RequestListener(){

							@Override
							public void onCallBack(Object data) {
								
							}

							@Override
							public void onError(Object error) {
								
							}}, false);
						if (getActivity() instanceof MainActivity) {
							((MainActivity) getActivity()).showFragment(
									"send_fragment", null, false);
						}
					}
				});

		view.findViewById(R.id.send_cancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						if (getActivity() instanceof MainActivity) {
							((MainActivity) getActivity()).showFragment(
									"send_fragment", null, false);
						}
					}
				});

	}
}

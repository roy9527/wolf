package com.roy.wolf.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.roy.wolf.R;
import com.roy.wolf.activity.MainActivity;
import com.roy.wolf.handler.SendEventHandler;
import com.roy.wolf.net.RequestListener;

public class SendEventFragment extends Fragment {

	private View view;

	Bundle bundle;

	public SendEventFragment(Bundle bundle) {
		this.bundle = bundle;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.send_activity_fragment, null);
		init();
		return view;
	}

	private void init() {
		final EditText title = (EditText) view
				.findViewById(R.id.send_msg_title);
		final EditText msg = (EditText) view.findViewById(R.id.send_msg_msg);
		msg.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					view.findViewById(R.id.send_ok).performClick();
					return true;
				} else if (actionId == EditorInfo.IME_ACTION_NEXT) {
					msg.requestFocus();
				}
				return false;
			}
		});
		view.findViewById(R.id.send_ok).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {

						String t = title.getText().toString();
						String m = msg.getText().toString();
						SendEventHandler sah = new SendEventHandler(
								getActivity());
						String cod = bundle.getInt("Longitude") + "/" + bundle.getInt("Latitude");
						sah.setParams(t, m, cod);
						sah.onRequest(new RequestListener() {

							@Override
							public void onCallBack(Object data) {

							}

							@Override
							public void onError(Object error) {

							}
						}, false);
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

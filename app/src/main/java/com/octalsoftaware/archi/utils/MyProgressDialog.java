package com.octalsoftaware.archi.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;

import com.octalsoftaware.archi.R;


public class MyProgressDialog extends AlertDialog {

	public MyProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void show() {

		super.show();
		setContentView(R.layout.customprogressbar);
		getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

	}

	@Override
	public void onBackPressed()
	{
		cancel();
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

}
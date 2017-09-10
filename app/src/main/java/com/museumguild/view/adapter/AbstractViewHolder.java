package com.museumguild.view.adapter;

import android.view.View;

public abstract class AbstractViewHolder {
	public AbstractViewHolder(View view) {
		initView(view);
	}

	public abstract void initView(View view);

	public abstract void initValue(final int positon);
}
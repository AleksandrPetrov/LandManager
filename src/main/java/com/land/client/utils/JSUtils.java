package com.land.client.utils;

public class JSUtils {
	public static native String getString(String name) /*-{
		return eval("$wnd." + name);
	}-*/;
}

package com.museumguild.utils;

public class Event {
	public static final int EVENT_LOGINED = 0x1;
	public static final int EVENT_SPOT = 0x2;
	private int key;
	private Object value;
	
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(boolean value) {
		this.value = value;
	}
	public Event(int key , Object value)
	{
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		return "Event [key=" + key + ", value=" + value + "]";
	}
	
}

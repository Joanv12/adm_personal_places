package com.upv.adm.adm_personal_shapes.classes;

public class CustomListItem implements IListItem {

	private String key;
	private String text;
	
	public CustomListItem(String key, String value) {
		this.key = key;
		this.text = value;
	}
	public String getKey() {
		return key;
	}
	public String getValue() {
		return text;
	}
	public String toString() {
		return text;
	}
}

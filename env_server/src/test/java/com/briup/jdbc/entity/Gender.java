package com.briup.jdbc.entity;

public enum Gender {
	MALE,FEMALE;
	
	@Override
	public String toString() {
		if(this== MALE) {
			return "男";
		}else if(this == FEMALE) {
			return "女";
		}
		return "";
	}
}

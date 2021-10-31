package com.iterator.beubassclientmod;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Constants {
	public static Date SERVER_EXPIRE_DATE;

	static {
		try {
			SERVER_EXPIRE_DATE = new SimpleDateFormat("yyyy-MM-dd").parse("2021-10-27");
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}

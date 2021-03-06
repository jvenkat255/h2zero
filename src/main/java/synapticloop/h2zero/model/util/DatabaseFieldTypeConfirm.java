package synapticloop.h2zero.model.util;

/*
 * Copyright (c) 2017-2018 Synapticloop.
 * 
 * All rights reserved.
 * 
 * This code may contain contributions from other parties which, where 
 * applicable, will be listed in the default build file for the project 
 * ~and/or~ in a file named CONTRIBUTORS.txt in the root of the project.
 * 
 * This source code and any derived binaries are covered by the terms and 
 * conditions of the Licence agreement ("the Licence").  You may not use this 
 * source code or any derived binaries except in compliance with the Licence.  
 * A copy of the Licence is available in the file named LICENSE.txt shipped with 
 * this source code or binaries.
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DatabaseFieldTypeConfirm {
	public static final Map<String, Set<String>> FIELD_VALIDATION_LOOKUP = new HashMap<String, Set<String>>();
	static {
		Set<String> sqliteSet = new HashSet<String>();
		sqliteSet.add("int");
		sqliteSet.add("integer");
		sqliteSet.add("tinyint");
		sqliteSet.add("smallint");
		sqliteSet.add("mediumint");
		sqliteSet.add("bigint");
		sqliteSet.add("character");
		sqliteSet.add("varchar");
		sqliteSet.add("nchar");
		sqliteSet.add("nvarchar");
		sqliteSet.add("text");
		sqliteSet.add("clob");
		sqliteSet.add("blob");
		sqliteSet.add("real");
		sqliteSet.add("double");
		sqliteSet.add("float");
		sqliteSet.add("numeric");
		sqliteSet.add("decimal");
		sqliteSet.add("boolean");
		sqliteSet.add("date");
		sqliteSet.add("datetime");

		FIELD_VALIDATION_LOOKUP.put("sqlite3", sqliteSet);

		Set<String> mysqlSet = new HashSet<String>();
		mysqlSet.add("bigint");
		mysqlSet.add("blob");
		mysqlSet.add("bool");
		mysqlSet.add("boolean");
		mysqlSet.add("date");
		mysqlSet.add("datetime");
		mysqlSet.add("time");
		mysqlSet.add("dec");
		mysqlSet.add("decimal");
		mysqlSet.add("double");
		mysqlSet.add("float");
		mysqlSet.add("int");
		mysqlSet.add("longtext");
		mysqlSet.add("mediumblob");
		mysqlSet.add("mediumtext");
		mysqlSet.add("timestamp");
		mysqlSet.add("tinyint");
		mysqlSet.add("varchar");

		FIELD_VALIDATION_LOOKUP.put("mysql", mysqlSet);
	}

	public static boolean getIsValidFieldTypeForDatabase(String database, String fieldType) {
		return(FIELD_VALIDATION_LOOKUP.get(database).contains(fieldType));
	}
}

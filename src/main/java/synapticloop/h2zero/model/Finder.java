package synapticloop.h2zero.model;

/*
 * Copyright (c) 2013-2015 synapticloop.
 * 
 * All rights reserved.
 *
 * This source code and any derived binaries are covered by the terms and
 * conditions of the Licence agreement ("the Licence").  You may not use this
 * source code or any derived binaries except in compliance with the Licence.
 * A copy of the Licence is available in the file named LICENCE shipped with
 * this source code or binaries.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * Licence for the specific language governing permissions and limitations
 * under the Licence.
 */

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import synapticloop.h2zero.exception.H2ZeroParseException;
import synapticloop.h2zero.model.field.BaseField;
import synapticloop.h2zero.model.util.FieldLookupHelper;
import synapticloop.h2zero.model.util.JSONKeyConstants;
import synapticloop.h2zero.util.JsonHelper;
import synapticloop.h2zero.util.NamingHelper;

public class Finder extends BaseQueryObject {
//	private String name;
//	private String selectClause;
//	private String whereClause;
//	private String orderBy;
//
//	private ArrayList<BaseField> whereFields = new ArrayList<BaseField>();
//	private LinkedHashMap<String, BaseField> uniqueWhereFields = new LinkedHashMap<String, BaseField>();
//	private ArrayList<BaseField> selectFields = new ArrayList<BaseField>();
//
	private boolean unique = false;
//	private boolean cache = false;
//	private boolean hasInFields = false;
//	private ArrayList<BaseField> inWhereFields = new ArrayList<BaseField>();
	private boolean hasWhereFieldAliases = false;

	public Finder(BaseSchemaObject baseSchemaObject, JSONObject jsonObject) throws H2ZeroParseException {
		super(baseSchemaObject, jsonObject);
		this.orderBy = JsonHelper.getStringValue(jsonObject, JSONKeyConstants.ORDER_BY, null);
		this.selectClause = JsonHelper.getStringValue(jsonObject, JSONKeyConstants.SELECT_CLAUSE, null);
		// if we have a select clause then we are returning a bean...

		// now for the select fields
		if(null != selectClause) {
			populateSelectFields(jsonObject);
		}

		this.unique = JsonHelper.getBooleanValue(jsonObject, JSONKeyConstants.UNIQUE, unique);

		// now for the where clauses
		this.whereClause = JsonHelper.getStringValue(jsonObject, JSONKeyConstants.WHERE_CLAUSE, null);
		// we may not have any whereFields
		try {
			JSONArray whereFieldArray = jsonObject.getJSONArray(JSONKeyConstants.WHERE_FIELDS);

			if(null == whereClause && whereFieldArray.length() > 0) {
				throw new H2ZeroParseException("Finder '" + name + "' cannot have '" + JSONKeyConstants.WHERE_FIELDS + "' when there is no '" + JSONKeyConstants.WHERE_CLAUSE + "'.");
			}

			for (int i = 0; i < whereFieldArray.length(); i++) {
				// at this point we need to check to see whether we are getting an array of objects, or just plain Strings
				String whereFieldName = null;
				String whereFieldAlias = null;

				if(null != whereFieldArray.optJSONObject(i)) {
					JSONObject whereFieldObject = whereFieldArray.getJSONObject(i);
					whereFieldName = whereFieldObject.getString(JSONKeyConstants.NAME);
					whereFieldAlias = whereFieldObject.getString(JSONKeyConstants.ALIAS);
					hasWhereFieldAliases = true;
				} else {
					whereFieldName = whereFieldArray.getString(i);
				}

				BaseField baseField = null;
				if(hasWhereFieldAliases) {
					// we need to create a new BaseField identical to the current one - as it is currently cached
					baseField = FieldLookupHelper.getBaseField(baseSchemaObject, whereFieldName).copy();
				} else {
					baseField = FieldLookupHelper.getBaseField(baseSchemaObject, whereFieldName);
				}

				this.hasInFields = FieldLookupHelper.hasInFieldDesignator(whereFieldName);

				if(hasWhereFieldAliases) {
					baseField.setAlias(whereFieldAlias);
				}

				if(null == baseField) {
					throw new H2ZeroParseException("Could not look up where field '" + whereFieldName + "', for finder '" + name + "'.");
				}

				whereFields.add(baseField);
				if(baseField.getIsInField()) {
					inWhereFields.add(baseField);
				}

				if(!uniqueWhereFields.containsKey(whereFieldName)) {
					uniqueWhereFields.put(whereFieldName, baseField);
				}
			}
		} catch (JSONException ojjsonex) {
			// do nothing
		}

		if(null == name) {
			throw new H2ZeroParseException("The finder '" + JSONKeyConstants.NAME + "' attribute cannot be null.");
		}
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void populateSelectFields(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray fieldJson = new JSONArray();
		try {
			fieldJson = jsonObject.getJSONArray(JSONKeyConstants.SELECT_FIELDS);
		} catch (JSONException ojjsonex) {
			throw new H2ZeroParseException("Cannot create the '" + JSONKeyConstants.SELECT_CLAUSE + "' finder of '" + name + "' finder without '" + JSONKeyConstants.SELECT_FIELDS + "'.");
		}

		for (int i = 0; i < fieldJson.length(); i++) {
			String type = null;
			JSONObject fieldObject = null;

			try {
				fieldObject = fieldJson.getJSONObject(i);
				type = fieldObject.getString(JSONKeyConstants.TYPE);

				// check to ensure that the field has a name
				fieldObject.getString(JSONKeyConstants.NAME);
			} catch (JSONException ojjsonex) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("Could not parse the '" + JSONKeyConstants.SELECT_FIELDS + "' array.\n");
				stringBuilder.append("Was expecting the format to be:\n");
				stringBuilder.append("\"" + JSONKeyConstants.SELECT_FIELDS + "\": [\n");
				stringBuilder.append("  { \"name\": \"<fieldName1>\", \"type\": \"<type>\" },\n");
				stringBuilder.append("  { \"name\": \"<fieldName2>\", \"type\": \"<type>\" },\n");
				stringBuilder.append("]\n");
				throw new H2ZeroParseException(stringBuilder.toString());
			}

			if(null != type) {
				String firstUpper = NamingHelper.getFirstUpper(type);
				try {
					Class forName = Class.forName("synapticloop.h2zero.model.field." + firstUpper + "Field");
					Constructor constructor = forName.getConstructor(JSONObject.class);
					BaseField baseField = (BaseField)constructor.newInstance(fieldObject);

					selectFields.add(baseField);

				} catch (Exception ex) {
					throw new H2ZeroParseException(ex.getMessage());
				}
			}
		}
	}

	public String getName() { return(name); }
	public String getFinderTagName() { return(NamingHelper.getFirstUpper(name)); }
	public String getWhereClause() { return(whereClause); }
	public void setWhereClause(String whereClause) { this.whereClause = whereClause; }
	public String getOrderBy() { return(orderBy); }
	public void setOrderBy(String orderBy) { this.orderBy = orderBy; }
	public boolean getUnique() { return(unique); }
	public ArrayList<BaseField> getWhereFields() { return(whereFields); }
	public ArrayList<BaseField> getSelectFields() { return(selectFields); }
	public ArrayList<BaseField> getInWhereFields() { return(inWhereFields); }
	public boolean getHasInFields() { return(hasInFields); }
	public String getSelectClause() { return selectClause; }
	public void setSelectClause(String selectClause) { this.selectClause = selectClause; }

	public String getStaticName() { return(NamingHelper.getStaticName(name)); }
	public Collection<BaseField> getUniqueWhereFields() { return(this.uniqueWhereFields.values()); }

	/**
	 * Whether this finder has aliases for the where fields, or it is just straight where field string array
	 * 
	 * @return whether this finder has where field aliases
	 */
	public boolean getHasWhereFieldAliases() { return hasWhereFieldAliases;}


	@Override
	public String getBaseQueryObjectType() {
		// TODO Auto-generated method stub
		return null;
	}
}

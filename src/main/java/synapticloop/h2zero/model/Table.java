package synapticloop.h2zero.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import synapticloop.h2zero.exception.H2ZeroParseException;
import synapticloop.h2zero.model.field.BaseField;
import synapticloop.h2zero.model.util.JSONKeyConstants;
import synapticloop.h2zero.util.JsonHelper;
import synapticloop.h2zero.util.NamingHelper;


public class Table {
	public static final String JSON_KEY_FIELD_FINDERS = "fieldFinders";
	public static final String JSON_KEY_FIELDS = "fields";
	public static final String JSON_KEY_NAME = "name";

	private JSONObject jsonObject = null;

	private String name = null;
	private String engine = "innodb";
	private String charset = "UTF8";
	private ArrayList<String> comments = new ArrayList<String>();

	private boolean cacheable = false;
	private boolean cacheFindAll = false;
	private boolean hasLargeObject = false;

	// a list of all of the fields that this table has
	private ArrayList<BaseField> fields = new ArrayList<BaseField>();
	// all fields that are not marked as secure
	private ArrayList<BaseField> nonSecureFields = new ArrayList<BaseField>();
	// all fields that are marked as secure
	private ArrayList<BaseField> secureFields = new ArrayList<BaseField>();

	private HashMap<String, BaseField> fieldLookup = new HashMap<String, BaseField>();
	private HashMap<String, BaseField> inFieldLookup = new HashMap<String, BaseField>();
	private HashMap<String, BaseField> setFieldLookup = new HashMap<String, BaseField>();
	private HashMap<String, BaseField> whereFieldLookup = new HashMap<String, BaseField>();

	private ArrayList<BaseField> nonNullFields = new ArrayList<BaseField>();
	private ArrayList<BaseField> nonPrimaryFields = new ArrayList<BaseField>();

	private ArrayList<Finder> finders = new ArrayList<Finder>();
	private ArrayList<Updater> updaters = new ArrayList<Updater>();
	private ArrayList<Inserter> inserters = new ArrayList<Inserter>();
	private ArrayList<Deleter> deleters = new ArrayList<Deleter>();
	private ArrayList<Constant> constants = new ArrayList<Constant>();
	private ArrayList<Counter> counters = new ArrayList<Counter>();
	private ArrayList<Question> questions = new ArrayList<Question>();

	private ArrayList<String> autoGeneratedUniqueFinders = new ArrayList<String>();
	private ArrayList<String> autoGeneratedMultipleFinders = new ArrayList<String>();
	
	// TODO - remove this please
	private boolean hasDeprecatedFinder = false;
	private LinkedHashMap<String, String> deprecatedFinders = new LinkedHashMap<String, String>();

	public Table(JSONObject jsonObject) throws H2ZeroParseException {
		this.jsonObject = jsonObject;

		this.name = JsonHelper.getStringValue(jsonObject, JSON_KEY_NAME, null);
		this.engine = JsonHelper.getStringValue(jsonObject, "engine", engine);
		this.charset = JsonHelper.getStringValue(jsonObject, "charset", charset);
		this.cacheable = JsonHelper.getBooleanValue(jsonObject, "cacheable", cacheable);
		this.cacheFindAll = JsonHelper.getBooleanValue(jsonObject, "cacheFindAll", cacheFindAll);
		String tempComments = JsonHelper.getStringValue(jsonObject, "comment", null);
		if(null != tempComments) {
			String[] split = tempComments.split("\\n");
			for (int i = 0; i < split.length; i++) {
				comments.add(split[i]);
			}
		}

		if(null == name) {
			throw new H2ZeroParseException("The table 'name' attribute cannot be null.");
		}

		// now for the fields
		populateFields(jsonObject);
	}

	public void populateActions() throws H2ZeroParseException {
		populateFieldFinders(jsonObject);
		populateFinders(jsonObject);
		populateUpdaters(jsonObject);
		populateDeleters(jsonObject);
		populateInserters(jsonObject);
		populateConstants(jsonObject);
		populateCounters(jsonObject);
		populateQuestions(jsonObject);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void populateFields(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray fieldJson = new JSONArray();
		try {
			fieldJson = jsonObject.getJSONArray(JSON_KEY_FIELDS);
		} catch (JSONException ojjsonex) {
			throw new H2ZeroParseException("Cannot create a table without 'fields'.");
		}

		for (int i = 0; i < fieldJson.length(); i++) {
			String type = null;
			String name = null;
			boolean isForeignKey = false;

			JSONObject fieldObject = null;
			try {
				fieldObject = fieldJson.getJSONObject(i);
				name = fieldObject.getString(JSON_KEY_NAME);
				if(null == name) {
					throw new H2ZeroParseException("Cannot have a field with a null name.");
				}

				// at this point we want to see if it is a foreign key...
				isForeignKey = fieldObject.optBoolean("foreignKey", false);

				type = fieldObject.getString("type");
				if(isForeignKey && type != null) {
					throw new H2ZeroParseException("Foreign key reference '" + this.name + "." + name + "' has a 'foreignKey' and 'type' keys.  The 'type' key is not required.");
				}

				// now for the autogenerated finders
				String autoFinder = fieldObject.optString("finder", null);
				if(null != autoFinder) {
					this.hasDeprecatedFinder  = true;
					if(autoFinder.compareToIgnoreCase("unique") == 0) {
						deprecatedFinders.put(name, "unique");
						autoGeneratedUniqueFinders.add(name);
					} else if(autoFinder.compareToIgnoreCase("multiple") == 0) {
						deprecatedFinders.put(name, "multiple");
						autoGeneratedMultipleFinders.add(name);
					} else {
						// TODO - probably want to change this into a switch??
						throw new H2ZeroParseException("Found an auto generate finder on '" + this.name + "." + name + "' with a value of '" + autoFinder + "'.  The allowable values are 'unique' or 'multiple'.");
					}
				}
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse the 'fields' array.");
			}

			//			if(isForeignKey) {
			//				type if 
			//			}

			if(null != type) {
				String firstUpper = NamingHelper.getFirstUpper(type);
				try {
					Class forName = Class.forName("synapticloop.h2zero.model.field." + firstUpper + "Field");
					Constructor constructor = forName.getConstructor(JSONObject.class, boolean.class);

					BaseField inBaseField = (BaseField)constructor.newInstance(fieldObject, true);

					constructor = forName.getConstructor(JSONObject.class);
					BaseField baseField = (BaseField)constructor.newInstance(fieldObject);

					if(!baseField.getNullable()) {
						nonNullFields.add(baseField);
					}

					if(!baseField.getPrimary()) {
						nonPrimaryFields.add(baseField);
					}

					if(baseField.getIsLargeObject()) {
						hasLargeObject = true;
					}

					fields.add(baseField);
					fieldLookup.put(name, baseField);
					inFieldLookup.put(name, inBaseField);

					BaseField setBaseField = (BaseField)constructor.newInstance(fieldObject);
					setBaseField.suffixJavaName("Set");
					BaseField whereBaseField = (BaseField)constructor.newInstance(fieldObject);
					whereBaseField.suffixJavaName("Where");

					setFieldLookup.put(name, setBaseField);
					whereFieldLookup.put(name, whereBaseField);
				} catch (ClassNotFoundException cnfex) {
					throw new H2ZeroParseException(cnfex.getMessage());
				} catch (SecurityException sex) {
					throw new H2ZeroParseException(sex.getMessage());
				} catch (NoSuchMethodException nsmex) {
					throw new H2ZeroParseException(nsmex.getMessage());
				} catch (IllegalArgumentException iaex) {
					throw new H2ZeroParseException(iaex.getMessage());
				} catch (InstantiationException iex) {
					throw new H2ZeroParseException(iex.getMessage());
				} catch (IllegalAccessException iaex) {
					throw new H2ZeroParseException(iaex.getMessage());
				} catch (InvocationTargetException itex) {
					throw new H2ZeroParseException(itex.getMessage());
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private void populateFieldFinders(JSONObject jsonObject) throws H2ZeroParseException {
		// now for the autogenerated finders
		JSONArray finderJson = new JSONArray();
		try {
			finderJson = jsonObject.getJSONArray(JSON_KEY_FIELD_FINDERS);
		} catch (JSONException ojjsonex) {
			// do nothing - no finders is ok
			return;
		}

		for(int i = 0; i < finderJson.length(); i++) {
			JSONObject fieldFinderObject = finderJson.optJSONObject(i);
			if(null == fieldFinderObject) {
				throw new H2ZeroParseException("Found a fieldFinders json array on table '" + this.name + "', however the value at index '" + i + "' is not a json object.");
			}

			Iterator keys = fieldFinderObject.keys();
			// should only be one key
			String name = (String)keys.next();
			String autoFinder = fieldFinderObject.optString(name);

			if(null != autoFinder) {
				if(autoFinder.compareToIgnoreCase("unique") == 0) {
					autoGeneratedUniqueFinders.add(name);
				} else if(autoFinder.compareToIgnoreCase("multiple") == 0) {
					autoGeneratedMultipleFinders.add(name);
				} else {
					// TODO - probably want to change this into a switch??
					throw new H2ZeroParseException("Found an auto generate finder on '" + this.name + "." + name + "' with a value of '" + autoFinder + "'.  The allowable values are 'unique' or 'multiple'.");
				}
			}
		}
	}

	private void populateFinders(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray finderJson = new JSONArray();
		try {
			finderJson = jsonObject.getJSONArray("finders");
		} catch (JSONException ojjsonex) {
			// do nothing - no finders is ok
		}

		// now go through and addFinders based on the multiple and unique finders
		for (String multipleFinder : autoGeneratedMultipleFinders) {
			JSONObject autoFinder = new JSONObject();
			try {
				autoFinder.put(JSONKeyConstants.JSON_KEY_NAME, "findBy" + NamingHelper.getFirstUpper(multipleFinder));
				autoFinder.put(JSONKeyConstants.JSON_KEY_WHERE_CLAUSE, "where " + multipleFinder + " = ?");
				autoFinder.put(JSONKeyConstants.JSON_KEY_UNIQUE, false);
				autoFinder.put(JSONKeyConstants.JSON_KEY_WHERE_FIELDS, new JSONArray().put(multipleFinder));

				finders.add(new Finder(autoFinder, this));
			} catch (JSONException jsonEx) {
				throw new H2ZeroParseException("Could not generate the multiple finder for '" + multipleFinder + "'.", jsonEx);
			}
		}

		for (String uniqueFinder : autoGeneratedUniqueFinders) {
			JSONObject autoFinder = new JSONObject();
			try {
				autoFinder.put(JSONKeyConstants.JSON_KEY_NAME, "findBy" + NamingHelper.getFirstUpper(uniqueFinder));
				autoFinder.put(JSONKeyConstants.JSON_KEY_WHERE_CLAUSE, "where " + uniqueFinder + " = ?");
				autoFinder.put(JSONKeyConstants.JSON_KEY_UNIQUE, true);
				autoFinder.put(JSONKeyConstants.JSON_KEY_WHERE_FIELDS, new JSONArray().put(uniqueFinder));

				finders.add(new Finder(autoFinder, this));
			} catch (JSONException jsonEx) {
				throw new H2ZeroParseException("Could not generate the multiple finder for '" + uniqueFinder + "'.", jsonEx);
			}
		}

		for (int i = 0; i < finderJson.length(); i++) {
			try {
				JSONObject finderObject = finderJson.getJSONObject(i);
				finders.add(new Finder(finderObject, this));
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse finder.");
			}
		}
	}

	private void populateUpdaters(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray updaterJson = new JSONArray();
		try {
			updaterJson = jsonObject.getJSONArray("updaters");
		} catch (JSONException ojjsonex) {
			// do nothing - no finders is ok
		}

		for (int i = 0; i < updaterJson.length(); i++) {
			try {
				JSONObject updaterObject = updaterJson.getJSONObject(i);
				updaters.add(new Updater(updaterObject, this));
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse updaters.");
			}
		}
	}

	private void populateDeleters(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray deleterJson = new JSONArray();
		try {
			deleterJson = jsonObject.getJSONArray("deleters");
		} catch (JSONException ojjsonex) {
			// do nothing - no finders is ok
		}

		for (int i = 0; i < deleterJson.length(); i++) {
			try {
				JSONObject deleterObject = deleterJson.getJSONObject(i);
				deleters.add(new Deleter(deleterObject, this));
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse deleters.");
			}
		}
	}

	private void populateInserters(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray inserterJson = new JSONArray();
		try {
			inserterJson = jsonObject.getJSONArray("inserters");
		} catch (JSONException ojjsonex) {
			// do nothing - no finders is ok
		}

		for (int i = 0; i < inserterJson.length(); i++) {
			try {
				JSONObject inserterObject = inserterJson.getJSONObject(i);
				inserters.add(new Inserter(inserterObject, this));
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse inserters.");
			}
		}
	}

	private void populateConstants(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray constantJson = new JSONArray();
		try {
			constantJson = jsonObject.getJSONArray("constants");
		} catch (JSONException ojjsonex) {
			// do nothing - no constants is ok
		}

		for (int i = 0; i < constantJson.length(); i++) {
			try {
				JSONObject constantsObject = constantJson.getJSONObject(i);
				constants.add(new Constant(constantsObject, this));
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse constants.");
			}
		}
	}

	/**
	 * Populate all of the counters that are being generated for a table.  A counter is a simple query that returns one
	 * and only one integer value for the query which is assumed to be a count
	 * 
	 * @param jsonObject The jsonObject to parse for questions
	 * @throws H2ZeroParseException if something went wrong with the parsing
	 */
	private void populateCounters(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray counterJson = new JSONArray();
		try {
			counterJson = jsonObject.getJSONArray("counters");
		} catch (JSONException ojjsonex) {
			// do nothing - no finders is ok
		}

		for (int i = 0; i < counterJson.length(); i++) {
			try {
				JSONObject counterObject = counterJson.getJSONObject(i);
				counters.add(new Counter(counterObject, this));
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse counter JSON Array.");
			}
		}
	}

	/**
	 * Populate all of the questions that are being generated for a table.  A question is a simple query that returns one
	 * and only one boolean true/false value
	 * 
	 * @param jsonObject The jsonObject to parse for questions
	 * @throws H2ZeroParseException if something went wrong with the parsing
	 */
	private void populateQuestions(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray questionJson = new JSONArray();
		try {
			questionJson = jsonObject.getJSONArray(JSONKeyConstants.JSON_KEY_QUESTIONS);
		} catch (JSONException ojjsonex) {
			// do nothing - no questions is ok
		}

		for (int i = 0; i < questionJson.length(); i++) {
			try {
				JSONObject questionObject = questionJson.getJSONObject(i);
				questions.add(new Question(questionObject, this));
			} catch (JSONException ojjsonex) {
				throw new H2ZeroParseException("Could not parse questions JSON Array.");
			}
		}
	}

	public String getName() { return(this.name); }
	public String getUpperName() { return(this.name.toUpperCase()); }
	public String getEngine() { return(this.engine); }
	public String getCharset() { return(this.charset); }
	public ArrayList<String> getComments() { return comments; }

	public ArrayList<BaseField> getFields() { return(fields); }

	public ArrayList<Finder> getFinders() { return(finders); }
	public ArrayList<Updater> getUpdaters() { return(updaters); }
	public ArrayList<Inserter> getInserters() { return(inserters); }
	public ArrayList<Deleter> getDeleters() { return(deleters); }
	public ArrayList<Constant> getConstants() { return(constants); }
	public ArrayList<Counter> getCounters() { return(counters); }

	public ArrayList<BaseField> getNonNullFields() { return(nonNullFields); }
	public ArrayList<BaseField> getNonPrimaryFields() { return(nonPrimaryFields); }
	public boolean getCacheable() { return(cacheable); }
	public boolean getCacheFindAll() { return(cacheFindAll); }
	public BaseField getField(String name) { return(fieldLookup.get(name)); }
	public BaseField getInField(String name) { return(inFieldLookup.get(name)); }
	public BaseField getSetField(String name) { return(setFieldLookup.get(name)); }
	public BaseField getWhereField(String name) { return(whereFieldLookup.get(name)); }
	public String getJavaClassName() { return(NamingHelper.getFirstUpper(name)); }
	public String getJavaFieldName() { return(NamingHelper.getSecondUpper(name)); }
	public boolean getHasNonNullConstructor() { return(nonNullFields.size() != fields.size()); }
	public boolean getHasLargeObject() { return hasLargeObject; }

	public boolean getIsConstant() { return(constants.size() > 0); }

	public String getDropTableDefinition() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("drop table if exists " + name + ";\n");
		return (stringBuilder.toString());
	}

	public String getCreateTableDefinition() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("create table if not exists " + name + " (\n");
		// now for the fields
		Iterator<BaseField> iterator = fields.iterator();

		while (iterator.hasNext()) {
			BaseField baseField = iterator.next();
			stringBuilder.append(baseField.getCreateField());
			if(iterator.hasNext()) {
				stringBuilder.append(",");
			}
		}

		stringBuilder.append(") engine=" + engine + " default charset=" + charset + ";\n");
		return (stringBuilder.toString());
	}

	// TODO - this needs to be removed - when deprecation is complete
	public boolean getHasDeprecatedFinder() { return hasDeprecatedFinder; }
	public LinkedHashMap<String, String> getDeprecatedFinders() { return(deprecatedFinders); }
}

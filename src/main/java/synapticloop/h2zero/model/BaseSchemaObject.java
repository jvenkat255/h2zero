package synapticloop.h2zero.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import synapticloop.h2zero.exception.H2ZeroParseException;
import synapticloop.h2zero.model.field.BaseField;
import synapticloop.h2zero.model.util.JSONKeyConstants;
import synapticloop.h2zero.util.JsonHelper;
import synapticloop.h2zero.util.NamingHelper;
import synapticloop.h2zero.util.SimpleLogger;

public abstract class BaseSchemaObject {
	protected JSONObject jsonObject;
	protected String name = null;
	private int findAllStatementCacheSize = 1024; // 1024 is the default size

	private List<Finder> finders = new ArrayList<Finder>(); // a list of all of the finders

	protected List<String> autoGeneratedUniqueFinders = new ArrayList<String>(); // a list of all of the automatically generated 'unique' result finders
	protected List<String> autoGeneratedMultipleFinders = new ArrayList<String>(); // a list of all of the automatically generated 'multiple' result finders

	protected Map<String, BaseField> fieldLookup = new HashMap<String, BaseField>(); // a quick lookup map of all of the fields for this table
	protected Map<String, BaseField> inFieldLookup = new HashMap<String, BaseField>(); // a quick lookup for all of the 'in' fields

	protected List<BaseField> fields = new ArrayList<BaseField>();  // a list of all of the fields on this table

	protected Set<String> referencedFieldTypes = new HashSet<String>(); // this is a set of all of the referenced field types

	public BaseSchemaObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
		this.findAllStatementCacheSize = JsonHelper.getIntValue(jsonObject, JSONKeyConstants.FINDALL_STATEMENT_CACHE_SIZE, 1024);
	}

	public abstract boolean getIsTable();
	public abstract boolean getIsView();

	@SuppressWarnings("rawtypes")
	protected void populateFieldFinders(JSONObject jsonObject) throws H2ZeroParseException {
		// now for the auto-generated finders
		JSONArray finderJson = new JSONArray();
		try {
			finderJson = jsonObject.getJSONArray(JSONKeyConstants.FIELD_FINDERS);
		} catch (JSONException ojjsonex) {
			// do nothing - no finders is ok
			return;
		}

		for(int i = 0; i < finderJson.length(); i++) {
			JSONObject fieldFinderObject = finderJson.optJSONObject(i);
			if(null == fieldFinderObject) {
				throw new H2ZeroParseException("Found a '" + JSONKeyConstants.FIELD_FINDERS +  "' json array on table '" + this.name + "', however the value at index '" + i + "' is not a json object.");
			}

			Iterator keys = fieldFinderObject.keys();
			// should only be one key
			String autoFinderName = (String)keys.next();
			String autoFinder = fieldFinderObject.optString(autoFinderName);

			if(null != autoFinder) {
				if(autoFinder.compareToIgnoreCase(JSONKeyConstants.UNIQUE) == 0 || autoFinder.compareToIgnoreCase(JSONKeyConstants.SINGLE) == 0) {
					autoGeneratedUniqueFinders.add(autoFinderName);
				} else if(autoFinder.compareToIgnoreCase(JSONKeyConstants.MULTIPLE) == 0) {
					autoGeneratedMultipleFinders.add(autoFinderName);
				} else {
					// TODO - probably want to change this into a switch??
					throw new H2ZeroParseException("Found an auto generate finder on '" + this.name + "." + autoFinderName + "' with a value of '" + autoFinder + "'.  The allowable values are '" + JSONKeyConstants.UNIQUE + "', '" + JSONKeyConstants.SINGLE + "' or '" + JSONKeyConstants.MULTIPLE + "'.");
				}
			}
		}
	}

	protected void populateFinders(JSONObject jsonObject) throws H2ZeroParseException {
		JSONArray finderJson = new JSONArray();
		try {
			finderJson = jsonObject.getJSONArray(JSONKeyConstants.FINDERS);
		} catch (JSONException ojjsonex) {
			// do nothing - no finders is ok
		}

		// now go through and addFinders based on the multiple and unique finders
		for (String multipleFinder : autoGeneratedMultipleFinders) {
			generateAutomaticFinder(multipleFinder, false);
		}

		for (String uniqueFinder : autoGeneratedUniqueFinders) {
			generateAutomaticFinder(uniqueFinder, true);
		}

		for (int i = 0; i < finderJson.length(); i++) {
			try {
				JSONObject finderObject = finderJson.getJSONObject(i);
				finders.add(new Finder(this, finderObject));
			} catch (JSONException jsonex) {
				throw new H2ZeroParseException("Could not parse finder.", jsonex);
			}
		}
	}

	private void generateAutomaticFinder(String uniqueFinder, boolean unique) throws H2ZeroParseException {
		JSONObject autoFinder = new JSONObject();
		String[] fields = uniqueFinder.split(",");
		JSONArray whereFieldsArray = new JSONArray();

		try {
			StringBuilder fieldNameBuilder = new StringBuilder();
			fieldNameBuilder.append("findBy");
			StringBuilder whereClauseBuilder = new StringBuilder();
			whereClauseBuilder.append("where ");

			for (int i = 0; i < fields.length; i++) {
				String field = fields[i].trim();
				fieldNameBuilder.append(NamingHelper.getFirstUpper(field));

				whereClauseBuilder.append(field);
				whereClauseBuilder.append(" = ?");
				if(i != 0) {
					whereClauseBuilder.append(", ");
				}

				whereFieldsArray.put(field);
			}

			autoFinder.put(JSONKeyConstants.NAME, fieldNameBuilder.toString());

			autoFinder.put(JSONKeyConstants.WHERE_CLAUSE, whereClauseBuilder.toString());
			autoFinder.put(JSONKeyConstants.UNIQUE, unique);
			autoFinder.put(JSONKeyConstants.WHERE_FIELDS, whereFieldsArray);

			Finder finder = new Finder(this, autoFinder);
			finder.setIsAutoFinder(true);
			finders.add(finder);
		} catch (JSONException jsonex) {
			throw new H2ZeroParseException("Could not generate the field finder for '" + uniqueFinder + "'.", jsonex);
		}
	}

	protected void logFatalFieldParse(Exception exception, String message, String firstUpper) throws H2ZeroParseException {
		SimpleLogger.logFatal(SimpleLogger.LoggerType.PARSE, exception.getClass().getSimpleName() + ": on table or view '" + this.name + "', throwing upwards..., for field synapticloop.h2zero.model.field." + firstUpper + "Field");
		throw new H2ZeroParseException(message, exception);
	}

	/**
	 * Go through all of the fields and populate the referenced field types
	 */
	protected void populateReferencedFieldTypes() {
		for (BaseField baseField : fields) {
			referencedFieldTypes.add(baseField.getSqlJavaType());
		}
	}

	/**
	 * Whether this table requires the import for this specific field type
	 * 
	 * @param fieldType the type of the field
	 * 
	 * @return whether an import statement is required for this table
	 */
	public boolean requiresImport(String fieldType) {
		return(referencedFieldTypes.contains(fieldType));
	}


	public String getName() { return(this.name); }
	public String getUpperName() { return(this.name.toUpperCase()); }
	public String getJavaClassName() { return(NamingHelper.getFirstUpper(name)); }
	public String getJavaFieldName() { return(NamingHelper.getSecondUpper(name)); }

	public BaseField getField(String name) { return(fieldLookup.get(name)); }
	public BaseField getInField(String name) { return(inFieldLookup.get(name)); }

	public List<Finder> getFinders() { return(finders); }

	public int getFindAllStatementCacheSize() { return findAllStatementCacheSize; }

}

package synapticloop.h2zero.validator.finder;

import java.util.List;

import synapticloop.h2zero.model.Database;
import synapticloop.h2zero.model.Options;
import synapticloop.h2zero.model.Table;
import synapticloop.h2zero.model.field.BaseField;
import synapticloop.h2zero.validator.BaseValidator;

public class FinderAutoIndexValidator extends BaseValidator {
	public void validate(Database database, Options options) {
		List<Table> tables = database.getTables();
		for (Table table : tables) {
			List<BaseField> fields = table.getFields();
			for (BaseField baseField : fields) {
				if(baseField.getIsAutoGeneratedFinder() && !(baseField.getIndex() || baseField.getUnique())) {
					addWarnMessage("Auto generated finder for field '" + table.getName() + "." + baseField.getName() + "' is neither marked as unique nor indexed which may slow down database lookups.");
				}
			}
		}
	}
}

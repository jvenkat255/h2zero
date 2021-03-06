package synapticloop.h2zero.validator.constant;

import java.util.List;

import synapticloop.h2zero.model.Database;
import synapticloop.h2zero.model.Options;
import synapticloop.h2zero.model.Table;
import synapticloop.h2zero.model.util.JSONKeyConstants;
import synapticloop.h2zero.validator.BaseValidator;

/**
 * Validate that a constant table does not have any 'deleter' objects on it.  This will add a FATAL warning to the 
 * validation stream
 * 
 * @author synapticloop
 *
 */
public class ConstantDeleterValidator extends BaseValidator {

	@Override
	public void validate(Database database, Options options) {

		List<Table> tables = database.getTables();
		for (Table table : tables) {
			if(table.getIsConstant() && !table.getDeleters().isEmpty()) {
				addFatalMessage("Constant model '" + table.getName() + "' has '" + JSONKeyConstants.DELETERS + "' which are not allowed, as you may not change the underlying database tables dynamically.");
			}
		}
	}

}

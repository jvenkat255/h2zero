package synapticloop.h2zero.validator.counter;

import java.util.List;

import synapticloop.h2zero.model.Counter;
import synapticloop.h2zero.model.Database;
import synapticloop.h2zero.model.Options;
import synapticloop.h2zero.model.Table;
import synapticloop.h2zero.validator.BaseValidator;

public class CounterSelectClauseValidator extends BaseValidator {

	@Override
	public void validate(Database database, Options options) {

		List<Table> tables = database.getTables();
		for (Table table : tables) {
			List<Counter> counters = table.getCounters();
			for (Counter counter : counters) {
				String selectClause = counter.getSelectClause();
				if(null != selectClause && !selectClause.toLowerCase().contains("select")) {
					addWarnMessage("Counter '" + table.getName() + "." + counter.getName() + "' has a selectClause that does not start with 'select', so I am going to add one.");
					counter.setSelectClause(" select " + selectClause);
				}
			}
		}
	}

}

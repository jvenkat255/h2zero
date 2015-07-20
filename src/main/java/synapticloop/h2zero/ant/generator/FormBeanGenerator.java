package synapticloop.h2zero.ant.generator;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import synapticloop.h2zero.model.Database;
import synapticloop.h2zero.model.Options;
import synapticloop.h2zero.model.Table;
import synapticloop.h2zero.model.form.Form;
import synapticloop.h2zero.util.SimpleLogger;
import synapticloop.h2zero.util.SimpleLogger.LoggerType;
import synapticloop.templar.Parser;
import synapticloop.templar.exception.FunctionException;
import synapticloop.templar.exception.ParseException;
import synapticloop.templar.exception.RenderException;
import synapticloop.templar.utils.TemplarContext;

public class FormBeanGenerator extends Generator {

	public FormBeanGenerator(Database database, Options options, File outFile, boolean verbose) {
		super(database, options, outFile, verbose);
	}

	@Override
	public void generate() throws RenderException, ParseException {
		if(!options.hasGenerator(Options.OPTION_FORMBEANS)) {
			return;
		}

		Parser javaCreateDefaultFormBeanParser = getParser("/java-create-default-form-bean-create.templar");
		Parser javaCreateFormBeanParser = getParser("/java-create-form-bean.templar");

		TemplarContext templarContext = null;
		try {
			templarContext = getDefaultTemplarContext();
		} catch (FunctionException fex) {
			throw new RenderException("Could not instantiate the function.", fex);
		}

		// now for the tables
		List<Table> tables = database.getTables();
		Iterator<Table> tableIterator = tables.iterator();

		while (tableIterator.hasNext()) {
			Table table = tableIterator.next();
			templarContext.add("table", table);
			SimpleLogger.logInfo(LoggerType.GENERATE_FORM_BEANS, "Generating for table '" + table.getName() + "'.");

			// the default form beans
			String pathname = outFile + "/src/main/java/" + database.getPackagePath() + "/form/auto/" + table.getJavaClassName() + "CreateFormBean.java";
			renderToFile(templarContext, javaCreateDefaultFormBeanParser, pathname);
		}

		// now for the forms
		List<Form> forms = database.getForms();
		Iterator<Form> formsIterator = forms.iterator();

		while (formsIterator.hasNext()) {
			Form form = formsIterator.next();
			templarContext.add("form", form);

			if(options.hasGenerator(Options.OPTION_FORMBEANS)) {
				String pathname = outFile + "/src/main/java/" + database.getPackagePath() + "/form/" + form.getName() + "Bean.java";
				renderToFile(templarContext, javaCreateFormBeanParser, pathname);
			}
		}

	}

}

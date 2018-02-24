package synapticloop.sample.h2zero.sqlite3.taglib.user;

// - - - - thoughtfully generated by synapticloop h2zero - - - - 
//    with the use of synapticloop templar templating language
//           (java-create-taglib-question.templar)

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import synapticloop.sample.h2zero.sqlite3.model.util.Constants;
import synapticloop.sample.h2zero.sqlite3.model.User;
import synapticloop.sample.h2zero.sqlite3.finder.UserFinder;
import synapticloop.h2zero.base.taglib.BaseVarTag;


import synapticloop.sample.h2zero.sqlite3.question.UserQuestion;

@SuppressWarnings("serial")
public class DoesUserNameExistTag extends BaseVarTag {
	// the binder is unused in code, but will generate compile problems if this 
	// class is no longer referenced in the h2zero file. Just a nicety for
	// removing dead code
	@SuppressWarnings("unused")
	private static final String BINDER = Constants.USER_BINDER;
	private static final Logger LOGGER = LoggerFactory.getLogger(DoesUserNameExistTag.class);


	@Override
	public int doStartTag() throws JspException {
		pageContext.setAttribute(var, UserQuestion.doesUserNameExistSilent());
		return(EVAL_BODY_INCLUDE);
	}
}
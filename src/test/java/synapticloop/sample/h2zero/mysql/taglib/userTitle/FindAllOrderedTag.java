package synapticloop.sample.h2zero.mysql.taglib.userTitle;

// - - - - thoughtfully generated by synapticloop h2zero - - - - 
//    with the use of synapticloop templar templating language
//           (java-create-taglib-finder.templar)

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import synapticloop.sample.h2zero.mysql.model.util.Constants;
import synapticloop.sample.h2zero.mysql.model.UserTitle;
import synapticloop.sample.h2zero.mysql.finder.UserTitleFinder;
import synapticloop.h2zero.extension.taglib.BaseVarTag;


@SuppressWarnings("serial")
public class FindAllOrderedTag extends BaseVarTag {
	// the binder is unused in code, but will generate compile problems if this 
	// class is no longer referenced in the h2zero file. Just a nicety for
	// removing dead code
	@SuppressWarnings("unused")
	private static final String BINDER = Constants.USER_TITLE_BINDER;
	private static final Logger LOGGER = LoggerFactory.getLogger(FindAllOrderedTag.class);


	@Override
	public int doStartTag() throws JspException {
		pageContext.setAttribute(var, UserTitleFinder.findAllOrderedSilent());
		return(EVAL_BODY_INCLUDE);
	}
}
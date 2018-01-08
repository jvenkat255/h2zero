package synapticloop.sample.h2zero.taglib.user;

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


import synapticloop.sample.h2zero.model.util.Constants;
import synapticloop.sample.h2zero.model.User;
import synapticloop.sample.h2zero.finder.UserFinder;
import synapticloop.h2zero.base.taglib.BaseVarTag;


@SuppressWarnings("serial")
public class FindByNmUsernameTag extends BaseVarTag {
	// the binder is unused in code, but will generate compile problems if this 
	// class is no longer referenced in the h2zero file. Just a nicety for
	// removing dead code
	@SuppressWarnings("unused")
	private static final String BINDER = Constants.USER_BINDER;
	private static final Logger LOGGER = LoggerFactory.getLogger(FindByNmUsernameTag.class);

	private String nmUsername = null;

	@Override
	public int doStartTag() throws JspException {
		pageContext.setAttribute(var, UserFinder.findByNmUsernameSilent(nmUsername));
		return(EVAL_BODY_INCLUDE);
	}
	public void setNmUsername(String nmUsername) {
		this.nmUsername = nmUsername;
	}

}
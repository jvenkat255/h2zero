package synapticloop.sample.h2zero.taglib.userTitle;

// - - - - thoughtfully generated by synapticloop h2zero - - - - 
//    with the use of synapticloop templar templating language
//     (java-create-taglib-finder-find-by-primary-key.templar)

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import synapticloop.sample.h2zero.model.UserTitle;
import synapticloop.sample.h2zero.model.util.Constants;

import synapticloop.h2zero.base.taglib.BaseVarTag;

@SuppressWarnings("serial")
public class FindByPrimaryKeyTag extends BaseVarTag {
	// the binder is unused in code, but will generate compile problems if this 
	// class is no longer referenced in the h2zero file. Just a nicety for
	// removing dead code
	@SuppressWarnings("unused")
	private static final String BINDER = Constants.USER_TITLE_BINDER;

	private static final Logger LOGGER = LoggerFactory.getLogger(FindByPrimaryKeyTag.class);


	private Long primaryKey = null;

	@Override
	public int doStartTag() throws JspException {
		pageContext.setAttribute(var, UserTitle.ALL_LOOKUP.get(primaryKey));
		return(EVAL_BODY_INCLUDE);
}

	public Long getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

}
package synapticloop.sample.h2zero.sqlite3.taglib.userPet;

// - - - - thoughtfully generated by synapticloop h2zero - - - - 
//    with the use of synapticloop templar templating language
//     (java-create-taglib-finder-find-by-primary-key.templar)

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import synapticloop.sample.h2zero.sqlite3.model.UserPet;
import synapticloop.sample.h2zero.sqlite3.finder.UserPetFinder;
import synapticloop.sample.h2zero.sqlite3.model.util.Constants;

import synapticloop.h2zero.base.taglib.BaseVarTag;

@SuppressWarnings("serial")
public class FindByPrimaryKeyTag extends BaseVarTag {
	// the binder is unused in code, but will generate compile problems if this 
	// class is no longer referenced in the h2zero file. Just a nicety for
	// removing dead code
	@SuppressWarnings("unused")
	private static final String BINDER = Constants.USER_PET_BINDER;

	private static final Logger LOGGER = LoggerFactory.getLogger(FindByPrimaryKeyTag.class);


	private Long primaryKey = null;

	@Override
	public int doStartTag() throws JspException {
		pageContext.setAttribute(var, UserPetFinder.findByPrimaryKeySilent(primaryKey));
		return(EVAL_BODY_INCLUDE);
}

	public Long getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(Long primaryKey) {
		this.primaryKey = primaryKey;
	}

}
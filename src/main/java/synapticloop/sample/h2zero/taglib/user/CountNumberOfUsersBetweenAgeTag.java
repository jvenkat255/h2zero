package synapticloop.sample.h2zero.taglib.user;

// - - - - thoughtfully generated by synapticloop h2zero - - - - 
//    with the use of synapticloop templar templating language
//           (java-create-taglib-counter.templar)

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.log4j.Logger;

import synapticloop.sample.h2zero.model.util.Constants;
import synapticloop.sample.h2zero.model.User;
import synapticloop.sample.h2zero.finder.UserFinder;

import synapticloop.sample.h2zero.counter.UserCounter;

@SuppressWarnings("serial")
public class CountNumberOfUsersBetweenAgeTag extends BodyTagSupport {
	// the binder is unused in code, but will generate compile problems if this 
	// class is no longer referenced in the h2zero file. Just a nicety for
	// removing dead code
	@SuppressWarnings("unused")
	private static final String BINDER = Constants.USER_binder;
	private static final Logger LOGGER = Logger.getLogger(CountNumberOfUsersBetweenAgeTag.class);

	private String var = null;
	private boolean removeVar = false;

	private Integer numAgeFrom = null;
	private Integer numAgeTo = null;

	public int doStartTag() throws JspException {
		pageContext.setAttribute(var, UserCounter.countNumberOfUsersBetweenAgeSilent(numAgeFrom, numAgeTo));
		return(EVAL_BODY_INCLUDE);
	}

	public int doEndTag() throws JspException {
		if(removeVar) {
			pageContext.removeAttribute(var, PageContext.PAGE_SCOPE);
		}
		return(EVAL_PAGE);
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public void setRemoveVar(boolean removeVar) {
		this.removeVar = removeVar;
	}

	public boolean getRemoveVar() {
		return removeVar;
	}

	public void setNumAgeFrom(Integer numAgeFrom) {
		this.numAgeFrom = numAgeFrom;
	}

	public void setNumAgeTo(Integer numAgeTo) {
		this.numAgeTo = numAgeTo;
	}

}
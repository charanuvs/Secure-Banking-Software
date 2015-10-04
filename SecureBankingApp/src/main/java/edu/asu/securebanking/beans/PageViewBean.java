package edu.asu.securebanking.beans;

import edu.asu.securebanking.constants.AppConstants;

/**
 * @author Vikranth
 *
 */
public class PageViewBean {

	/**
	 * Default constructor
	 */
	public PageViewBean() {
		this.title = AppConstants.DOC_TITLE;
	}

	/**
	 * @param pageTitle
	 */
	public PageViewBean(final String pageTitle) {
		this();
		this.pageTitle = pageTitle;
	}

	/**
	 * @param pageTitle
	 * @param subPageTitle
	 */
	public PageViewBean(final String pageTitle, final String subPageTitle) {
		this();
		this.pageTitle = pageTitle;
		this.subPageTitle = subPageTitle;
	}

	/**
	 * @param title
	 * @param pageTitle
	 * @param subPageTitle
	 */
	public PageViewBean(final String title, final String pageTitle,
			final String subPageTitle) {
		this.title = title;
		this.pageTitle = pageTitle;
		this.subPageTitle = subPageTitle;
	}

	private String message;

	private String title;

	private String pageTitle;

	private String subPageTitle;

	private boolean isValid = true;

	private boolean isSuccess;

	/**
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return success
	 */
	public boolean isSuccess() {
		return isSuccess;
	}

	/**
	 * @param isSuccess
	 */
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	/**
	 * @return valid
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * @param isValid
	 */
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	/**
	 * @return pageTitle
	 */
	public String getPageTitle() {
		return pageTitle;
	}

	/**
	 * @param pageTitle
	 */
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	/**
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return subPageTitle
	 */
	public String getSubPageTitle() {
		return subPageTitle;
	}

	/**
	 * @param subPageTitle
	 */
	public void setSubPageTitle(String subPageTitle) {
		this.subPageTitle = subPageTitle;
	}
}
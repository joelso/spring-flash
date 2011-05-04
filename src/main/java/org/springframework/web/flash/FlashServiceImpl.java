package org.springframework.web.flash;

import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.springframework.util.Assert;
import org.springframework.web.flash.Flash.FlashEntry;
import org.springframework.web.flash.Flash.FlashEntryState;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Joel Soderstrom <joel.soderstrom@gmail.com>
 * 
 */
public class FlashServiceImpl implements FlashService {

	public static final String DEFAULT_SESSION_ATTR = Flash.class.getName();
	public static final String DEFAULT_REQ_ATTR = "flash";
	
	private String sessionAttributeName = DEFAULT_SESSION_ATTR;
	private String reqAttributeName = DEFAULT_REQ_ATTR;

	/**
	 * Setter for session attribute name that contains the FlashStateHolder.
	 * Default is {@link #DEFAULT_SESSION_ATTR}
	 * 
	 * @param sessionAttributeName
	 */
	public void setSessionAttributeName(String sessionAttributeName) {
		this.sessionAttributeName = sessionAttributeName;
	}

	/**
	 * Sets name of request attribute that will contain the flash map for the
	 * current request. Default is {@link #DEFAULT_REQ_ATTR}
	 * 
	 * @param reqAttributeName
	 */
	public void setReqAttributeName(String reqAttributeName) {
		this.reqAttributeName = reqAttributeName;
	}

	@Override
	public void initFlash(HttpSession session) {
		Flash flash = new Flash();
		session.setAttribute(this.sessionAttributeName, flash);
	}

	@Override
	public void setCurrentToSaved(HttpSession session) {
		for (FlashEntry entry : getFlashSafe(session).getEntries()) {
			if (entry.getState() == FlashEntryState.CURRENT) {
				entry.setState(FlashEntryState.SAVED);
			}
		}
	}

	@Override
	public boolean isInitiated(HttpSession session) {
		return getFlash(session) != null;
	}

	@Override
	public void addToModel(HttpSession session, ModelAndView mav) {
		// request.setAttribute(reqAttributeName,
		// getFlashSafe(request.getSession()));
		mav.addObject(reqAttributeName, getFlashClone(session));
	}

	@Override
	public void removeNonSaved(HttpSession session) {
		Iterator<FlashEntry> iter = getFlashSafe(session).getEntries()
				.iterator();
		while (iter.hasNext()) {
			FlashEntryState entryState = iter.next().getState();
			if (entryState == FlashEntryState.CURRENT || entryState == FlashEntryState.PREVIOUS) {
				iter.remove();
			}
		}
	}

	@Override
	public void setSavedToPrevious(HttpSession session) {
		for (FlashEntry entry : getFlashSafe(session).getEntries()) {
			if (entry.getState() == FlashEntryState.SAVED) {
				entry.setState(FlashEntryState.PREVIOUS);
			}
		}
	}

	public Flash getFlashSafe(HttpSession session) {
		Flash flash = getFlash(session);
		Assert
				.notNull(flash,
						"Flash does not exist in session, has flash not been initialized?");
		return flash;
	}

	public Flash getFlash(HttpSession session) {
		Object object = session.getAttribute(this.sessionAttributeName);
		return object == null ? null : (Flash) object;
	}

	private Flash getFlashClone(HttpSession session) {
		return new Flash(getFlashSafe(session));
	}

}

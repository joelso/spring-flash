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
public class FlashUtil {

	public static final String SESSION_ATTR = Flash.class.getName();
	public static final String REQUEST_ATTR = "flash";
	
    /**
     * Creates a new Flash object for the given session.
     * Should be invoked if Flash couldn't be found in
     * current session.
     * @param session
     */
	public static void initFlash(HttpSession session) {
		Flash flash = new Flash();
		session.setAttribute(SESSION_ATTR, flash);
	}

    /**
     * Changes the state of entries from CURRENT to SAVED.
     * @param session
     */
	public static void setCurrentToSaved(HttpSession session) {
		for (FlashEntry entry : getFlashSafe(session).getEntries()) {
			if (entry.getState() == FlashEntryState.CURRENT) {
				entry.setState(FlashEntryState.SAVED);
			}
		}
	}
	
    /**
     * Check if flash is initialized for the given session.
     * @param session
     * @return true if initialized
     */
	public static boolean isInitiated(HttpSession session) {
		return getFlash(session) != null;
	}
	
    /**
     * Adds a clone of the flash object to the request 
     * as an attribute so that it will be available for views.
     * @param request
     */
	public static void addToModel(HttpSession session, ModelAndView mav) {
		mav.addObject(REQUEST_ATTR, getFlashClone(session));
	}
	
    /**
     * Removes all flash entries of state CURRENT and PREVIOUS.
     * @param session
     */
	public static void removeNonSaved(HttpSession session) {
		Iterator<FlashEntry> iter = getFlashSafe(session).getEntries()
				.iterator();
		while (iter.hasNext()) {
			FlashEntryState entryState = iter.next().getState();
			if (entryState == FlashEntryState.CURRENT || entryState == FlashEntryState.PREVIOUS) {
				iter.remove();
			}
		}
	}
	
    /**
     * Changes the state of entries from SAVED to PREVIOUS.
     * @param session
     */
	public static void setSavedToPrevious(HttpSession session) {
		for (FlashEntry entry : getFlashSafe(session).getEntries()) {
			if (entry.getState() == FlashEntryState.SAVED) {
				entry.setState(FlashEntryState.PREVIOUS);
			}
		}
	}
	
    /**
     * Get Flash object for the given session.
     * @param session
     * @return
     */
	public static Flash getFlash(HttpSession session) {
		Object object = session.getAttribute(SESSION_ATTR);
		return object == null ? null : (Flash) object;
	}

	private static Flash getFlashSafe(HttpSession session) {
		Flash flash = getFlash(session);
		Assert
		.notNull(flash,
				"Flash does not exist in session, has flash not been initialized?");
		return flash;
	}

	private static Flash getFlashClone(HttpSession session) {
		return new Flash(getFlashSafe(session));
	}

}

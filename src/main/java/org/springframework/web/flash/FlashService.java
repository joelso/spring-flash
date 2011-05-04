package org.springframework.web.flash;

import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author Joel Soderstrom <joel.soderstrom@gmail.com>
 *
 */
public interface FlashService {

    /**
     * Creates a new Flash object for the given session.
     * Should be invoked if Flash couldn't be found in
     * current session.
     * @param session
     */
    void initFlash(HttpSession session);

    /**
     * Changes the state of entries from CURRENT to SAVED.
     * @param session
     */
    void setCurrentToSaved(HttpSession session);

    /**
     * Changes the state of entries from SAVED to PREVIOUS.
     * @param session
     */
    void setSavedToPrevious(HttpSession session);

    /**
     * Check if flash is initialized for the given session.
     * @param session
     * @return true if initialized
     */
    boolean isInitiated(HttpSession session);

    /**
     * Adds a clone of the flash object to the request 
     * as an attribute so that it will be available for views.
     * @param request
     */
    void addToModel(HttpSession session, ModelAndView mav);

    /**
     * Removes all flash entries of state CURRENT and PREVIOUS.
     * @param session
     */
    void removeNonSaved(HttpSession session);

    /**
     * Get Flash object for the given session.
     * @param session
     * @return
     */
    Flash getFlash(HttpSession session);
}

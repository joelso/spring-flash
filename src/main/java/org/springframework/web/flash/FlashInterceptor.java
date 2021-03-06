package org.springframework.web.flash;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author Joel Soderstrom <joel.soderstrom@gmail.com>
 *
 */
public class FlashInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();

        if(!FlashUtil.isInitiated(session)) {
            // Initiate flash if one doesn't exist in current session
            FlashUtil.initFlash(session);
        }

        // Change state SAVED -> PREVIOUS
        FlashUtil.setSavedToPrevious(session);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView mav) throws Exception {
        if(isRedirect(mav)) {
        	
        	/*
        	 * Flash entries that has been created during 
        	 * current request should be saved if the handler 
        	 * returns a redirect-view.
        	 * 
        	 * Hence the state moves from CURRENT -> SAVED
        	 */
        	
        	FlashUtil.setCurrentToSaved(request.getSession());
        } else if(mav != null) {
        	
        	/*
        	 * No redirect, just render the view and the 
        	 * flash object as an attribute. 
        	 */
        	FlashUtil.addToModel(request.getSession(), mav);
        }
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception exception) {

        // Clean up flash entries with state != SAVED
    	FlashUtil.removeNonSaved(request.getSession());
    }
    
    private boolean isRedirect(ModelAndView mav) {
        return mav != null && mav.isReference() && mav.getViewName().startsWith("redirect:");
    }
}

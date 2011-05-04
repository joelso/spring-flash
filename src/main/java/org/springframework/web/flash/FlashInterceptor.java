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

    private FlashService flashService = new FlashServiceImpl();

    public void setFlashService(FlashService flashService) {
        this.flashService = flashService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();

        if(!flashService.isInitiated(session)) {
            // Initiate flash if one doesn't exist in current session
            flashService.initFlash(session);
        }

        // Change state SAVED -> PREVIOUS
        flashService.setSavedToPrevious(session);
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
        	
            flashService.setCurrentToSaved(request.getSession());
        } else if(mav != null) {
        	
        	/*
        	 * No redirect, just render the view and the 
        	 * flash object as an attribute. 
        	 */
            flashService.addToModel(request.getSession(), mav);
        }
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception exception) {

        // Clean up flash entries with state != SAVED
        flashService.removeNonSaved(request.getSession());
    }
    
    private boolean isRedirect(ModelAndView mav) {
        return mav != null && mav.isReference() && mav.getViewName().startsWith("redirect:");
    }
}

package org.springframework.web.flash;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author Joel Soderstrom <joel.soderstrom@gmail.com>
 *
 */
public class FlashArgumentResolver implements WebArgumentResolver {

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
            NativeWebRequest webRequest) throws Exception {

        if(!methodParameter.getParameterType().isAssignableFrom(Flash.class)) {
            return UNRESOLVED;
        }

        Object nativeReq = webRequest.getNativeRequest();
        if(!(nativeReq instanceof HttpServletRequest)) {
            throw new IllegalArgumentException("FlashArgumentResolver only supports HttpServletRequest");
        }

        HttpServletRequest request = (HttpServletRequest) nativeReq;
        Flash flash = FlashUtil.getFlash(request.getSession());

        return flash == null ? UNRESOLVED : flash;
    }


}

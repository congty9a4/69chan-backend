package com.congty9a4.backend.util;

import com.congty9a4.backend.exception.error.ErrorCode;
import com.congty9a4.backend.exception.error.AppException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class SecurityUtils {

    public static String getCurrentUserId(){
        SecurityContext context = SecurityContextHolder.getContext();
        return extractPrincipal(context.getAuthentication());
    }

    // retrieve id
    private static String extractPrincipal(Authentication authentication){
        if (authentication == null)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        else if (authentication.getPrincipal() instanceof UserDetails userDetails){
            return userDetails.getUsername();
        } else if (authentication.getPrincipal() instanceof String s){
            return s;
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }


}


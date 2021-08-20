package de.chronies.user.service.config.interceptors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class BearerTokenInterceptor implements HandlerInterceptor {

    private final BearerTokenWrapper tokenWrapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {

        final String authorizationHeaderValue = request.getHeader("Authorization");

        if (authorizationHeaderValue != null && authorizationHeaderValue.toLowerCase().startsWith("bearer")) {
            String token = authorizationHeaderValue.substring(7);
            tokenWrapper.setToken(token);
        }

        return true;
    }

}

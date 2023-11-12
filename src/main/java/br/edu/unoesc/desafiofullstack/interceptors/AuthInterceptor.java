package br.edu.unoesc.desafiofullstack.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import br.edu.unoesc.desafiofullstack.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        final HttpSession session = request.getSession();
        final boolean isUserAuthenticated = authService.isUserAuthenticated(session);

        String path = String.valueOf(request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE));
        final boolean isLoginPath = path.equals("/login") || path.equals("/cadastro");

        if (isLoginPath && isUserAuthenticated) {
            response.sendRedirect("/");
            return false;
        } else if (!isLoginPath && !isUserAuthenticated) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
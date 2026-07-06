package com.foodapp.FoodDeliveryApp_2.config; // Adjust to your package package structure

import com.foodapp.FoodDeliveryApp_2.entity.User;
import com.foodapp.FoodDeliveryApp_2.repository.UserRepository; // Adjust to your repository name
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepo; // Use your actual repository interface variable

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        if (authentication instanceof OAuth2AuthenticationToken token) {
            var attributes = token.getPrincipal().getAttributes();
            String email = (String) attributes.get("email");
            String socialId = authentication.getName(); // Fallback ID string

            // 1. Look up the user by email or social identifier key
            User user = null;
            if (email != null) {
                user = userRepo.findByUsername(email).orElse(null);
            }
            if (user == null) {
                user = userRepo.findByUsername(socialId).orElse(null);
            }

            // 2. Provision the user profile permanent entry if it doesn't exist yet
            if (user == null) {
                user = new User();
                user.setUsername(email != null ? email : socialId);
                user.setEmail(email != null ? email : "No public email");
                user.setDisplayName((String) attributes.getOrDefault("name", attributes.getOrDefault("login", "Social User")));
                user.setRole("ROLE_USER");
                user.setPassword(UUID.randomUUID().toString()); // Satisfy database column constraints
                user.setAddress(""); 
                user.setPhoneNumber("");
                userRepo.save(user);
            }

            // 3. Keep the persistent profile information synchronized inside HTTP Session context variables
            HttpSession session = request.getSession();
            session.setAttribute("displayName", user.getDisplayName());
            session.setAttribute("savedAddress", user.getAddress());
            session.setAttribute("savedPhone", user.getPhoneNumber());
        }

        // 4. Redirect safely to home page or targeted landing sequence
        super.setDefaultTargetUrl("/home");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
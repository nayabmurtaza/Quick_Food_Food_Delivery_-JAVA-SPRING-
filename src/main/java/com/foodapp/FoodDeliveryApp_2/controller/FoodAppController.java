package com.foodapp.FoodDeliveryApp_2.controller;

import com.foodapp.FoodDeliveryApp_2.entity.*;
import com.foodapp.FoodDeliveryApp_2.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class FoodAppController {

    @Autowired
    private RestaurantRepository restaurantRepo;

    @Autowired
    private MenuRepository menuRepo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AddressRepository addressRepo;

    @Autowired
    private PastOrderRepository pastOrderRepo;

    private User getAuthenticatedUser(Principal principal) {
        if (principal == null) return null;
        String username = principal.getName();
        User user = userRepo.findByUsername(username).orElse(null);
        
        if (user == null && principal instanceof OAuth2AuthenticationToken token) {
            String email = (String) token.getPrincipal().getAttributes().get("email");
            if (email != null) {
                user = userRepo.findByUsername(email).orElse(null);
            }
        }
        return user;
    }

    @GetMapping({"/", "/home"})
    public String showHome(Model model, Principal principal, HttpSession session) {
        List<Restaurant> restaurants = restaurantRepo.findAll();
        model.addAttribute("allRestaurants", restaurants);
        
        if (principal != null && session.getAttribute("displayName") == null) {
            User user = getAuthenticatedUser(principal);
            if (user != null && user.getDisplayName() != null) {
                session.setAttribute("displayName", user.getDisplayName());
            }
        }
        return "home";
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }
    
    @GetMapping("/profile")
    public String showProfilePage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        
        User user = getAuthenticatedUser(principal);
        
        if (user == null) {
            if (principal instanceof OAuth2AuthenticationToken token) {
                var attributes = token.getPrincipal().getAttributes();
                String email = (String) attributes.get("email");
                
                user = new User();
                user.setUsername(principal.getName());
                user.setDisplayName((String) attributes.getOrDefault("name", attributes.getOrDefault("login", "Social User")));
                user.setEmail(email != null ? email : "No public email available");
                user.setAddress("No address set yet");
                user.setPhoneNumber("No phone number set yet");
            } else {
                throw new RuntimeException("User profile record not found in system storage.");
            }
        }
        
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @RequestParam("displayName") String displayName,
            @RequestParam("address") String address,
            @RequestParam("phoneNumber") String phoneNumber,
            Principal principal,
            HttpSession session) {
        
        if (principal == null) {
            return "redirect:/login";
        }
        
        User user = getAuthenticatedUser(principal);
        
        if (user == null && principal instanceof OAuth2AuthenticationToken token) {
            String email = (String) token.getPrincipal().getAttributes().get("email");
            user = new User();
            user.setUsername(email != null ? email : principal.getName());
            user.setEmail(email != null ? email : "No explicit email");
            user.setRole("ROLE_USER");
            user.setPassword(java.util.UUID.randomUUID().toString()); 
        }
        
        if (user == null) {
            throw new RuntimeException("User profile record not found in system storage.");
        }
        
        user.setDisplayName(displayName);
        user.setAddress(address);
        user.setPhoneNumber(phoneNumber);
        userRepo.save(user);
        
        session.setAttribute("displayName", displayName);
        session.setAttribute("savedAddress", address);
        session.setAttribute("savedPhone", phoneNumber);
        
        return "redirect:/profile?success=true";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            Model model) {
        
        if (userRepo.findByUsername(username).isPresent()) {
            model.addAttribute("error", "Username already taken!");
            return "signup";
        }

        User user = new User(username, encoder.encode(password), email, "ROLE_USER");
        user.setPhoneNumber(phoneNumber);
        userRepo.save(user);

        return "redirect:/login?registered=true";
    }

    @GetMapping("/menu")
    public String showMenu(
            @RequestParam("restaurantId") int restaurantId, 
            @RequestParam(value = "conflict", required = false, defaultValue = "false") boolean conflict,
            @RequestParam(value = "conflictMenuId", required = false) Integer conflictMenuId,
            Model model, 
            HttpSession session,
            Principal principal) {
        
        Restaurant restaurant = restaurantRepo.findById(restaurantId).orElse(null);
        List<Menu> menus = menuRepo.findByRestaurantId(restaurantId);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("allMenusByRestaurant", menus);
        
        model.addAttribute("conflictDetected", conflict);
        model.addAttribute("conflictMenuId", conflictMenuId);

        if (principal != null && session.getAttribute("displayName") == null) {
            User user = getAuthenticatedUser(principal);
            if (user != null && user.getDisplayName() != null) {
                session.setAttribute("displayName", user.getDisplayName());
            }
        }

        return "menu";
    }

    @GetMapping("/cart")
    public String handleCart(
            @RequestParam(value = "action", required = false, defaultValue = "view") String action,
            @RequestParam(value = "menuId", required = false) Integer menuId,
            @RequestParam(value = "restaurantId", required = false) Integer restaurantId,
            @RequestParam(value = "quantity", required = false) Integer quantity,
            @RequestParam(value = "forceClear", required = false, defaultValue = "false") boolean forceClear,
            HttpSession session) {

        Cart cart = (Cart) session.getAttribute("cart");
        Integer activeRestaurantId = (Integer) session.getAttribute("restaurantId");

        // 1. IF THE CART IS EXPLICITLY EMPTY OR EMPTY AFTER REMOVALS, RESET RESTAURANT TRACKING
        if (cart == null || cart.getItems().isEmpty()) {
            cart = new Cart();
            session.setAttribute("cart", cart);
            if (restaurantId != null) {
                session.setAttribute("restaurantId", restaurantId);
                activeRestaurantId = restaurantId;
                Restaurant restaurant = restaurantRepo.findById(restaurantId).orElse(null);
                if (restaurant != null) {
                    session.setAttribute("restaurantName", restaurant.getName());
                }
            }
        }

        // 2. CONFLICT CHECK: Cart is not empty, and item is from a DIFFERENT restaurant
        if ("add".equalsIgnoreCase(action) && !cart.getItems().isEmpty() && restaurantId != null && !restaurantId.equals(activeRestaurantId)) {
            if (forceClear) {
                cart = new Cart(); 
                session.setAttribute("cart", cart);
                session.setAttribute("restaurantId", restaurantId);
                activeRestaurantId = restaurantId;
                
                Restaurant restaurant = restaurantRepo.findById(restaurantId).orElse(null);
                if (restaurant != null) {
                    session.setAttribute("restaurantName", restaurant.getName());
                }
            } else {
                // Return to the new restaurant's menu with a pop-up confirmation trigger
                return "redirect:/menu?restaurantId=" + restaurantId + "&conflict=true&conflictMenuId=" + menuId;
            }
        }

        // 3. EXECUTE ACTIONS
        if ("add".equalsIgnoreCase(action) && menuId != null) {
            Menu menu = menuRepo.findById(menuId).orElse(null);
            if (menu != null) {
                cart.addItem(new CartItem(menu.getMenuId(), menu.getItemName(), menu.getPrice(), quantity != null ? quantity : 1));
            }
            return "redirect:/cart/view";
        } 
        
        else if ("update".equalsIgnoreCase(action) && menuId != null && quantity != null) {
            cart.updateItem(menuId, quantity);
            // If an update lowers quantity to 0 and empties the cart, clean up restaurant session data
            if (cart.getItems().isEmpty()) {
                session.removeAttribute("restaurantId");
                session.removeAttribute("restaurantName");
            }
            return "redirect:/cart/view";
        } 
        
        else if ("remove".equalsIgnoreCase(action) && menuId != null) {
            cart.removeItem(menuId);
            // If removal empties the cart completely, clean up restaurant session data
            if (cart.getItems().isEmpty()) {
                session.removeAttribute("restaurantId");
                session.removeAttribute("restaurantName");
            }
            return "redirect:/cart/view";
        }

        return "redirect:/cart/view";
    }

    @GetMapping("/cart/view")
    public String viewCart() {
        return "cart-view"; 
    }
    
    // ------------------- SAVED ADDRESSES -------------------
    @GetMapping("/addresses")
    public String showSavedAddresses(Model model, Principal principal) {
        User user = getAuthenticatedUser(principal);
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("savedAddresses", addressRepo.findByUser(user));
        return "addresses";
    }

    @PostMapping("/addresses/add")
    public String addNewAddress(
            @RequestParam("label") String label, 
            @RequestParam("addressLine") String addressLine, 
            @RequestParam("phoneNumber") String phoneNumber, 
            Principal principal,
            @RequestParam(value = "redirect", required = false, defaultValue = "addresses") String redirect) {
        
        User user = getAuthenticatedUser(principal);
        if (user == null) {
            return "redirect:/login";
        }
        
        Address newAddress = new Address(label, addressLine, phoneNumber, user);
        addressRepo.save(newAddress);
        
        if ("checkout".equalsIgnoreCase(redirect)) {
            return "redirect:/checkout";
        }
        return "redirect:/addresses?success=true";
    }

    // ------------------- ORDER HISTORY -------------------
    @GetMapping("/orders")
    public String showOrderHistory(Model model, Principal principal) {
        User user = getAuthenticatedUser(principal);
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("pastOrders", pastOrderRepo.findByUserOrderByOrderDateDesc(user));
        return "orders";
    }
    
    // ------------------- CHECKOUT ROUTE -------------------
    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, Principal principal) {
        User user = getAuthenticatedUser(principal);
        if (user == null) {
            return "redirect:/login";
        }

        List<Address> userAddresses = addressRepo.findByUser(user);
        model.addAttribute("savedAddresses", userAddresses);
        
        return "checkout"; 
    }
    
    // ------------------- PLACE ORDER -------------------
    @PostMapping("/placeOrder")
    public String processOrder(
            @RequestParam("selectedAddressId") Long addressId,
            @RequestParam("paymentMethod") String paymentMethod,
            HttpSession session,
            Model model,
            Principal principal) {
        
        User user = getAuthenticatedUser(principal);
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (user == null || cart == null || cart.getItems().isEmpty()) {
            return "redirect:/home";
        }

        Address deliveryAddr = addressRepo.findById(addressId).orElse(null);
        if (deliveryAddr == null) {
            return "redirect:/checkout?error=invalid_address";
        }

        PastOrder historicalOrder = new PastOrder();
        historicalOrder.setUser(user);
        historicalOrder.setOrderDate(LocalDateTime.now());
        historicalOrder.setTotalPrice(cart.getTotalPrice());
        historicalOrder.setDeliveryAddress("[" + deliveryAddr.getLabel() + "] " + deliveryAddr.getAddressLine() + " (Phone: " + deliveryAddr.getPhoneNumber() + ")");

        for (CartItem item : cart.getItems().values()) {
            PastOrderItem orderItem = new PastOrderItem(item.getName(), item.getQuantity(), item.getPrice(), historicalOrder);
            historicalOrder.getItems().add(orderItem);
        }

        pastOrderRepo.save(historicalOrder);

        Integer restaurantId = (Integer) session.getAttribute("restaurantId");
        if (restaurantId != null) {
            Restaurant restaurant = restaurantRepo.findById(restaurantId).orElse(null);
            model.addAttribute("restaurant", restaurant);
        }

        session.removeAttribute("cart");
        session.removeAttribute("restaurantId");
        session.removeAttribute("restaurantName");
        return "order-preparing";
    }
}
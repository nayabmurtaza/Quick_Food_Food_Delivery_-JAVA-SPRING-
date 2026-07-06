# QUICK FOODS | Full-Stack Spring Boot Food Marketplace 🍔🚀

An enterprise-grade, data-driven web application modeling a multi-merchant food delivery marketplace. Engineered natively on the Spring Boot framework, the platform leverages Spring Security for robust identity isolation (including native Form and OAuth2 providers), Thymeleaf for state-aware view resolution, and decoupled client-side event loops to maintain stateful shopping operations across diverse merchant contexts.

---

## 🎥 System Demonstration

<img width="1783" height="812" alt="spring_20s (2)" src="https://github.com/user-attachments/assets/db8084b0-0311-44f6-98d1-ba27f9325a42" />

---

## ✨ System Architecture & Workflow

### 1. Unified Authentication Matrix (Form & OAuth2)
User registration and authorization workflows are managed via an isolated security perimeter integrating custom local registrations with third-party identity ecosystems. The application handles standard credential forms alongside direct OAuth2 intercept vectors for Google and GitHub access profiles.

***Sign Up***
<img width="1898" height="866" alt="Screenshot 2026-07-04 190200" src="https://github.com/user-attachments/assets/66624e0d-c911-4ea3-b87e-9f3c76088ba2" />

***Login***
<img width="1897" height="870" alt="Screenshot 2026-07-04 190229" src="https://github.com/user-attachments/assets/06b94cd3-49f3-4a4e-8a78-aebb5ab33566" />


*   **State-Aware View Resolution:** The application uses structural Spring Security dialect expressions (`sec:authorize="isAuthenticated()"` and `isAnonymous()`) to intercept and reshape navigation layouts in real time based on active security context footprints.
*   **Dynamic Identity Fallbacks:** Session tracking engines actively read server-side context variables to inject personalized labels (`session.displayName`), gracefully retreating to generic token identifiers (`#authentication.name`) if profile states are unpopulated.

### 2. Context-Isolated Menu Layout Matrix
When a consumer navigates to a designated restaurant node, the application queries underlying data stores to compile and project menu arrays dynamically using structural data iteration flags (`th:each="menu : ${allMenusByRestaurant}"`).

<img width="1896" height="862" alt="Screenshot 2026-07-04 193624" src="https://github.com/user-attachments/assets/4882acba-dfa6-4332-9db6-47bbbae6ec7e" />
### 3. Cross-Restaurant Cart Session Guardrails
State coherence within the consumer shopping experience enforces a strict, isolated single-merchant constraint pipeline to protect backend transaction items.

*   **The Isolation Rule:** Users are blocked from mixing inventory origins within an individual transaction space. If an item linked to a foreign merchant reference is passed into the checkout parameters, structural context checking activates an inline modal barrier (`#cartConflictModal`).
*   **Asynchronous UX Remediation:** When an isolation conflict fires, users can retain their existing cart state or authorize a full state flush via `forceClear='true'`. If overridden, clean client-side routing routines execute history state resets via browser utility operations (`window.history.replaceState`) to clean active endpoint parameters.

<img width="1900" height="864" alt="addtocart" src="https://github.com/user-attachments/assets/ce1e081e-1752-43b0-83db-32cb15cb6100" />

### 4. Asynchronous Preparation Tracking & Historic Logs
Once an order pipeline successfully completes execution, transactions transition to active tracking workflows and historic analysis layers.

*   **Transactional Progress Tracking:** Users transition instantly to an active preparation display (`orderStatus.html`) rendering a dynamic cooking animation and localized delivery calculations (`restaurant.eta`) powered by non-blocking visual rendering frameworks.
*   **Historic Transaction Ledgers:** Past orders are loaded from application persistence layers through automated temporal transformations (`#temporals.format(order.orderDate, 'dd MMMM yyyy, hh:mm a')`). The resulting dashboard exposes chronological purchase keys (`#QF-` + id), row-by-row purchase details, aggregate costs, and location metadata.

<img width="1898" height="863" alt="Screenshot 2026-07-04 194930" src="https://github.com/user-attachments/assets/e304b302-4f90-431b-b3ea-3f6324351db0" />

---

***Checkout Page***
<img width="1273" height="581" alt="Screenshot 2026-07-06 195510" src="https://github.com/user-attachments/assets/01c1250f-1f56-4d5f-b585-d17755c40f13" />

***Order Success***
<img width="1276" height="578" alt="Screenshot 2026-07-06 195547" src="https://github.com/user-attachments/assets/761a1ed7-0bf8-47ba-aba3-f65e43b8574a" />

***Saved Addresses***
<img width="1272" height="577" alt="Screenshot 2026-07-06 195716" src="https://github.com/user-attachments/assets/ea6afc0b-48d4-4f12-9ee4-64c4606d0188" />

---


## 🛠️ Technology Stack & Dependencies

*   **Backend Application Core:** Java Enterprise Edition, Spring Boot
*   **Identity & Access Architecture:** Spring Security Core (Form-Based Auth, OAuth2 Social Client Providers)
*   **View Processing Engine:** Thymeleaf Template Dialect Layout Engines
*   **Client Scripting & Interface Logic:** HTML5 DOM API, CSS3 Layout Modules (Flexbox Structural Constraints), Vanilla JavaScript (ES6+ Context Scopes)
*   **Asset Providers:** FontAwesome Vector Icon System (v6.4.0 CDN distribution channels)

---

## 📂 Project Package Structure

```text
src/
 └── main/
      ├── java/com/quickfoods/
      │    ├── config/              # Security configurations (Form login & OAuth2 pipelines)
      │    ├── controllers/         # Web routing mappings (Auth, Profile, Menu, Cart engines)
      │    ├── models/              # Persistence entities and state domain models
      │    └── services/            # Transaction coordination and business rules handling
      └── resources/
           ├── templates/           # Server-parsed Thymeleaf views (login, menu, history, profile)
           └── static/              # Shared asset sheets and core presentation definitions
🚀 Setting Up the Application Locally1. Environmental ConfigurationsEnsure your target system runs a minimum environment consisting of JDK 17+ and Maven 3.8+. Create your matching relational schema inside your database of choice, and align configuration parameters within your local environment configurations or resource files:Propertiesspring.datasource.url=jdbc:mysql://localhost:3306/quickfoods_db
spring.datasource.username=YOUR_DATABASE_USERNAME
spring.datasource.password=YOUR_DATABASE_PASSWORD

# Secure Client Integration Identifiers (OAuth2)
spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
spring.security.oauth2.client.registration.github.client-id=YOUR_GITHUB_CLIENT_ID
spring.security.oauth2.client.registration.github.client-secret=YOUR_GITHUB_CLIENT_SECRET
2. Compilation and LaunchClone the master repository branch locally:Bashgit clone [https://github.com/yourusername/quick-foods.git](https://github.com/yourusername/quick-foods.git)
cd quick-foods
Trigger Maven compilation handlers to fetch explicit dependencies and build the artifact layout:Bashmvn clean install
Boot the application profile engine via the local command shell interface:Bashmvn spring-boot:run
Access the web dashboard tracking entry routing point inside your web browser: http://localhost:8080/login💡 Summary Checklist for Making Media ContentTo keep your files organized, create a folder named images in your root project directory, with a subfolder inside it named screenshots. Here is exactly what you need to create:File NameFormatTarget Location/DimensionDurationsystem-walkthrough.gifGIFUnder the main ## System Demonstration header. Full desktop browser layout frame width.15-18 secondsauth-gateway.pngPNGUnder ### 1. Unified Authentication Matrix. Clear view of forms and social buttons.Staticmenu-resolution-grid.pngPNGUnder ### 2. Context-Isolated Menu Layout Matrix. Showing items, descriptions, and grid setup.Staticcart-conflict-modal.gifGIFUnder ### 3. Cross-Restaurant Cart Session Guardrails. Focused crop on the warning pop-up element trigger.5-7 secondshistorical-ledgers.pngPNGUnder ### 4. Asynchronous Preparation Tracking & Historic Logs. Captures the order history container log view.Static
---

## 📄 Industry-Standard Resume Presentation Segment

Add this clean, high-impact layout block straight to your resume's **Projects** section to get maximum parsed value from automated ATS screeners:

### **QUICK FOODS — Full-Stack Web Application**
*Full-Stack Software Engineer / Application Security Lead* | **[Link to GitHub Repository]**

*   **Technologies Used:** Java, Spring Boot, Spring Security (Form Auth, OAuth2), Thymeleaf Layout Engine, JavaScript (ES6+), HTML5, CSS3, Maven, Git.
*   **Architected & Developed** a robust multi-merchant food ordering web platform leveraging Spring Boot and Thymeleaf template layout engines to manage synchronous system state and highly responsive web layouts.
*   **Engineered** a secure enterprise identity matrix combining standard Form-Login structures with advanced OAuth2 integrations (Google & GitHub APIs), decoupling secure public entry points from authenticated consumer pathways.
*   **Devised** a custom cart-isolation architecture that implements cross-restaurant session context checks, protecting transaction state and dynamically generating user remediation modals during data boundary violations.
*   **Optimized** interface responsiveness using client-side JavaScript DOM handling, replacing heavy framework overhead to control asynchronous view interactions, modal dismissals, and page state navigation rewrites.
*   **Formulated** high-performance, dynamic server-side rendering pipelines utilizing Thymeleaf security dialects and temporal formatters, accelerating database-driven rendering cycles for structured transaction data, past orders, and custom profile properties.

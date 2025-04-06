package org.example.myfirstproject.APITest;




import org.example.myfirstproject.Controllers.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
@WebMvcTest(HomeController.class)
@AutoConfigureMockMvc(addFilters = false)  // Изключваме филтрите за по-лесно тестване
public class HomeControllerAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    void testAdminHome() throws Exception {
        mockMvc.perform(get("/adminHome"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminHome"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"USER"})
    void testUserHome() throws Exception {
        mockMvc.perform(get("/userHome"))
                .andExpect(status().isOk())
                .andExpect(view().name("userHome"));
    }

    @Test
    void testHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
}

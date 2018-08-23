package com.boichenko.phonebook.rest;

import com.boichenko.phonebook.model.Contact;
import com.boichenko.phonebook.model.User;
import com.boichenko.phonebook.service.AuthService;
import com.boichenko.phonebook.service.ContactsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactsControllerTest {

    @Mock
    private ContactsService contactsServiceMock;
    @Mock
    private AuthService authServiceMock;
    @InjectMocks
    private ContactsController contactsController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(contactsController)
                .build();
    }

    @Test
    public void getContacts() throws Exception {
        User user = new User();
        user.setId(123);
        when(authServiceMock.authenticateByToken(anyString())).thenReturn(user);

        Contact contact = new Contact();
        contact.setId(456);
        contact.setFirstName("foo");
        contact.setLastName("bar");
        contact.setPhones(Collections.emptyList());
        when(contactsServiceMock.getContacts(user)).thenReturn(Collections.singletonList(contact));

        mockMvc.perform(get("/contacts").header("Authorization", "some_auth_header"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.[0].id").value("456"))
                .andExpect(jsonPath("$.[0].firstName").value("foo"))
                .andExpect(jsonPath("$.[0].lastName").value("bar"));
    }

    @Test
    public void getContact() {
    }

    @Test
    public void createContact() {
    }

    @Test
    public void updateContact() {
    }

    @Test
    public void deleteContact() {
    }

    @Test
    public void addPhone() {
    }


}
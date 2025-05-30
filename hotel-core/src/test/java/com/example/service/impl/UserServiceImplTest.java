package com.example.service.impl;

import com.example.dao.security.PrivilegeDAO;
import com.example.dao.security.UserDAO;
import com.example.entity.security.Privilege;
import com.example.entity.security.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private PrivilegeDAO privilegeDAO;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGenerateToken() {
        // GIVEN
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "ivan", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // WHEN
        String token = userService.generateToken(userDetails);

        // THEN
        assertNotNull(token);
    }

    @Test
    void testExtractUsername() {
        // GIVEN
        String token = userService.generateToken(new org.springframework.security.core.userdetails.User(
                "ivan", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));

        // WHEN
        String username = userService.extractUsername(token);

        // THEN
        assertEquals("ivan", username);
    }

    @Test
    void testIsTokenExpired() {
        // GIVEN
        String token = userService.generateToken(new org.springframework.security.core.userdetails.User(
                "ivan", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));

        // WHEN
        boolean isExpired = userService.isTokenExpired(token);

        // THEN
        assertFalse(isExpired);
    }

    @Test
    void testValidateToken() {
        // GIVEN
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "ivan", "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        String token = userService.generateToken(userDetails);

        // WHEN
        boolean isValid = userService.validateToken(token, userDetails);

        // THEN
        assertTrue(isValid);
    }

    @Test
    void testLoadUserByUsername() {
        // GIVEN
        User user = new User(1, "ivan", "password", "ROLE_USER");
        when(userDAO.findByUsername("ivan")).thenReturn(user);

        // WHEN
        UserDetails result = userService.loadUserByUsername("ivan");

        // THEN
        assertEquals("ivan", result.getUsername());
        assertEquals("password", result.getPassword());
        assertEquals(1, result.getAuthorities().size());
        verify(userDAO).findByUsername("ivan");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // GIVEN
        when(userDAO.findByUsername("ivan")).thenReturn(null);

        // WHEN & THEN
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("ivan"));
        verify(userDAO).findByUsername("ivan");
    }

    @Test
    void testUserExists() {
        // GIVEN
        when(userDAO.findByUsername("ivan")).thenReturn(new User(1,
                "ivan", "password", "ROLE_USER"));

        // WHEN
        boolean exists = userService.userExists("ivan");

        // THEN
        assertTrue(exists);
        verify(userDAO).findByUsername("ivan");
    }

    @Test
    void testAddUser() {
        // GIVEN
        User user = new User(1, "ivan", "password", "ROLE_USER");

        // WHEN
        userService.addUser(user);

        // THEN
        verify(userDAO).create(user);
    }

    @Test
    void testAddUser_EmptyUsername() {
        // GIVEN
        User user = new User(1, "", "password", "ROLE_USER");

        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> userService.addUser(user));
    }

    @Test
    void testAddUser_ShortPassword() {
        // GIVEN
        User user = new User(1, "ivan", "123", "ROLE_USER");

        // WHEN & THEN
        assertThrows(IllegalArgumentException.class, () -> userService.addUser(user));
    }

    @Test
    void testAssignPrivilegeToUser() {
        // GIVEN
        User user = new User(1, "ivan", "password", "ROLE_USER");
        Privilege privilege = new Privilege();
        privilege.setPrivilegeName("READ");
        when(userDAO.read(1)).thenReturn(user);
        when(privilegeDAO.read(1)).thenReturn(privilege);

        // WHEN
        userService.assignPrivilegeToUser(1, 1);

        // THEN
        assertTrue(user.getPrivileges().contains(privilege));
        verify(userDAO).update(user);
    }

    @Test
    void testCreatePrivilege() {
        // GIVEN
        String privilegeName = "READ";

        // WHEN
        userService.createPrivilege(privilegeName);

        // THEN
        Privilege privilege = new Privilege();
        privilege.setPrivilegeName(privilegeName);
        verify(privilegeDAO).create(privilege);
    }
}
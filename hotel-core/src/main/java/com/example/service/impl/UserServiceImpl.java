package com.example.service.impl;

import com.example.dao.security.PrivilegeDAO;
import com.example.dao.security.UserDAO;
import com.example.entity.security.Privilege;
import com.example.entity.security.User;
import com.example.service.UserService;
import com.example.utils.MessageSources;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    @Value("${jwt.secret.key}")
    private String SECRET_KEY;

    private final UserDAO userDAO;
    private final PrivilegeDAO privilegeDAO;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserDAO userDAO, PrivilegeDAO privilegeDAO) {
        this.userDAO = userDAO;
        this.privilegeDAO = privilegeDAO;
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    @Override
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public boolean isTokenExpired(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration().before(new Date());
    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username);

        if (user == null) {
            logger.error(MessageSources.NOT_EXIST);
            throw new UsernameNotFoundException(username);
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));

        if (user.getPrivileges() != null) {
            for (Privilege privilege : user.getPrivileges()) {
                authorities.add(new SimpleGrantedAuthority(privilege.getPrivilegeName()));
            }
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean userExists(String username) {
        return userDAO.findByUsername(username) != null;
    }

    @Override
    @Transactional
    public void addUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }
        if (user.getPassword() == null || user.getPassword().length() < 4) {
            throw new IllegalArgumentException("Пароль должен содержать не менее 4 символов");
        }
        userDAO.create(user);
    }

    @Override
    @Transactional
    public void assignPrivilegeToUser(int userId, int privilegeId) {
        User user = userDAO.read(userId);
        Privilege privilege = privilegeDAO.read(privilegeId);

        if (user != null && privilege != null) {
            try {
                user.getPrivileges().add(privilege);
                userDAO.update(user);
            } catch (Exception e) {
                logger.error(MessageSources.FAILURE_UPDATE, e);
            }
        }
    }

    @Override
    @Transactional
    public void createPrivilege(String privilegeName) {
        Privilege privilege = new Privilege();
        privilege.setPrivilegeName(privilegeName);
        privilegeDAO.create(privilege);
    }
}

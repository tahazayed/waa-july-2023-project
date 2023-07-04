package com.example.alumni.service.impl;

import com.example.alumni.entity.Role;
import com.example.alumni.entity.UniversityMember;
import com.example.alumni.service.RoleService;
import com.example.alumni.service.UniversityMemberService;
import com.example.alumni.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.alumni.entity.User;
import com.example.alumni.repository.UserRepository;

import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UniversityMemberService universityMemberService;

    @Override
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User add(User user) throws IllegalAccessException {

        Optional<UniversityMember> universityMember = universityMemberService.getByEmail(user.getEmail());
        if(universityMember.isPresent()) {
            List<Role> roles = new ArrayList<>();
            roles.add(roleService.getByRole(universityMember.get().getRole()));
            user.setRoles(roles);
            String salt = BCrypt.gensalt();

            user.setPassword(BCrypt.hashpw(user.getPassword(), salt));
            return userRepository.save(user);
        }
        else {
            throw new IllegalAccessException("You are not a member of MIU");
        }
    }

    @Override
    public Pair<Boolean, User> update(User user) {
        boolean exists = userRepository.existsById(user.getId());
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id: " + user.getId()));
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        // existingUser.setEmail(user.getEmail());
        // existingUser.setPassword(user.getPassword());
        userRepository.save(existingUser);
        return Pair.of(exists, existingUser);
    }

    @Override
    public boolean delete(Long id) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            userRepository.delete(existingUser);
            return true;
        }
        return false;
    }
}

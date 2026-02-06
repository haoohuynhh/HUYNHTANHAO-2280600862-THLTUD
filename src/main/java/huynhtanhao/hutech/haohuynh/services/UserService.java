package huynhtanhao.hutech.haohuynh.services;

import huynhtanhao.hutech.haohuynh.entities.User;
import huynhtanhao.hutech.haohuynh.repositories.IRoleRepository;
import huynhtanhao.hutech.haohuynh.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
    public void save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        huynhtanhao.hutech.haohuynh.entities.Role role = roleRepository
                .findRoleById(huynhtanhao.hutech.haohuynh.constants.Role.USER.value);
        if (role != null) {
            user.getRoles().add(role);
        }
        userRepository.save(user);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
    public void setDefaultRole(String username) {
        userRepository.findByUsername(username)
                .getRoles()
                .add(roleRepository
                        .findRoleById(huynhtanhao.hutech.haohuynh.constants.Role.USER.value));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
    public void saveOauthUser(String email, @NotNull String username) {
        if (userRepository.findByUsername(username) != null) {
            return;
        }
        var existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            existingUser.setProvider(huynhtanhao.hutech.haohuynh.constants.Provider.GOOGLE.value);
            userRepository.save(existingUser);
            return;
        }
        var user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(username));
        user.setProvider(huynhtanhao.hutech.haohuynh.constants.Provider.GOOGLE.value);
        user.getRoles().add(roleRepository.findRoleById(huynhtanhao.hutech.haohuynh.constants.Role.USER.value));
        userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public Optional<User> findByPhone(String phone) {
        return Optional.ofNullable(userRepository.findByPhone(phone));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}

package huynhtanhao.hutech.haohuynh.services;

import huynhtanhao.hutech.haohuynh.entities.User;
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

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = { Exception.class, Throwable.class })
    public void save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder()
                .encode(user.getPassword()));
        userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
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
        // user.getRoles().add(roleRepository.findRoleById(Role.USER.value));
        userRepository.save(user);
    }
}

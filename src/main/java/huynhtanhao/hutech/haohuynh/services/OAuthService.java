package huynhtanhao.hutech.haohuynh.services;

import huynhtanhao.hutech.haohuynh.entities.User;
import huynhtanhao.hutech.haohuynh.repositories.IRoleRepository;
import huynhtanhao.hutech.haohuynh.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService extends OidcUserService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        return processUser(oidcUser, userRequest);
    }

    private OidcUser processUser(OidcUser oidcUser, OidcUserRequest userRequest) {
        String email = oidcUser.getAttribute("email");

        User user = userRepository.findByEmail(email);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setUsername(email);
            user.setProvider("GOOGLE");
            user.setPassword("");

            var role = roleRepository.findRoleById(huynhtanhao.hutech.haohuynh.constants.Role.USER.value);
            if (role != null) {
                user.getRoles().add(role);
            }
            userRepository.save(user);
        } else {
            user.setProvider("GOOGLE");
            if (user.getRoles().isEmpty()) {
                var role = roleRepository.findRoleById(huynhtanhao.hutech.haohuynh.constants.Role.USER.value);
                if (role != null) {
                    user.getRoles().add(role);
                }
            }
            userRepository.save(user);
        }

        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        return new DefaultOidcUser(
                authorities,
                oidcUser.getIdToken(),
                oidcUser.getUserInfo(),
                userNameAttributeName);
    }
}

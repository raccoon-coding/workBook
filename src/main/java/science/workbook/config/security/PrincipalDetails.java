package science.workbook.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import science.workbook.domain.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class PrincipalDetails implements UserDetails {
    private final User user;

    public PrincipalDetails(User userData) {
        this.user = userData;
    }

    public User getUserData() {
        return user;
    }

    public String getUserEmail() {
        return user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String userType = user.getUserType().toString();
        return Arrays.stream(userType.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

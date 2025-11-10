package com.mycompany.ecommerce.backend.service;
import com.ecommerce.dto.UserDto;
import com.ecommerce.entity.User;
import com.ecommerce.exception.ApiException;
import com.ecommerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }
    public UserDto getById(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new ApiException("User not found"));
        return toDto(user);
    }
    public UserDto update(Long id, UserDto dto) {
        var user = userRepository.findById(id).orElseThrow(() -> new ApiException("User not found"));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        userRepository.save(user);
        return toDto(user);
    }
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
    public UserDto changePassword(Long id, String newPassword) {
        var user = userRepository.findById(id).orElseThrow(() -> new ApiException("User not found"));
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
        return toDto(user);
    }
    private UserDto toDto(User u) {
        return UserDto.builder().id(u.getId()).name(u.getName()).email(u.getEmail()).role(u.getRole().name()).build();
    }
}

package com.dathihida.service.impl;

import com.dathihida.config.JwtProvider;
import com.dathihida.domain.USER_ROLE;
import com.dathihida.model.Cart;
import com.dathihida.model.Seller;
import com.dathihida.model.User;
import com.dathihida.model.VerificationCode;
import com.dathihida.repository.CartRepository;
import com.dathihida.repository.SellerRepository;
import com.dathihida.repository.UserRepository;
import com.dathihida.repository.VerificationCodeRepository;
import com.dathihida.request.LoginRequest;
import com.dathihida.request.SignupRequest;
import com.dathihida.response.AuthResponse;
import com.dathihida.service.AuthService;
import com.dathihida.service.EmailService;
import com.dathihida.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserServiceImpl;
    private final SellerRepository sellerRepository;

    @Override
    public void sendLoginOpt(String email, USER_ROLE role) throws Exception {
        String SIGNING_PREFIX = "signing_";

        if(email.startsWith(SIGNING_PREFIX)) {
            email = email.substring(SIGNING_PREFIX.length());

            if (role.equals(USER_ROLE.ROLE_CUSTOMER)){
                User user = userRepository.findByEmail(email);
                if(user == null) {
                    throw new Exception("user not exist with provided email");
                }
            }else if (role.equals(USER_ROLE.ROLE_SELLER)){
                Seller seller = sellerRepository.findByEmail(email);
                if(seller == null) {
                    throw new Exception("seller not exist with provided email");
                }
            }
        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);
        if(isExist != null) {
            verificationCodeRepository.delete(isExist);
        }
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setOtp(otp);
        verificationCodeRepository.save(verificationCode);

        String subject = "tiendat login/signup otp";
        String text = "your login/ signup otp is - "+ otp;
        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }

    @Override
    public String createUser(SignupRequest request) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());
        if (verificationCode == null || !verificationCode.getOtp().equals(request.getOpt())) {
            throw new Exception("wrong opt ...");
        }

        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            User createdUser = new User();
            createdUser.setEmail(request.getEmail());
            createdUser.setFullName(request.getFullName());
            createdUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createdUser.setMobile("0987654321");
            createdUser.setPassword(passwordEncoder.encode(request.getOpt()));

            user = userRepository.save(createdUser);

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signIn(LoginRequest request) {
        String email = request.getEmail();
        String otp = request.getOtp();

        Authentication authentication = authenticate(email, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login Success");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));


        return authResponse;
    }

    private Authentication authenticate(String username, String otp) {
        UserDetails userDetails = customUserServiceImpl.loadUserByUsername(username);

        String SELLER_PREFIX = "seller_";
        if(username.startsWith(SELLER_PREFIX)) {
            username = username.substring(SELLER_PREFIX.length());
        }

        String CUSTOMER_PREFIX = "customer_";
        if(username.startsWith(CUSTOMER_PREFIX)) {
            username = username.substring(CUSTOMER_PREFIX.length());
        }

        if(userDetails == null){
            throw new BadCredentialsException("invalid username or password");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

        if(verificationCode.getOtp() == null || !verificationCode.getOtp().equals(otp)) {
            throw new BadCredentialsException("wrong opt ...");
        }
        return new UsernamePasswordAuthenticationToken
                (userDetails, null, userDetails.getAuthorities());
    }
}

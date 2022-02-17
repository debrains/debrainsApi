package com.debrains.debrainsApi.security.oauth;

import com.debrains.debrainsApi.common.AuthProvider;
import com.debrains.debrainsApi.common.UserRole;
import com.debrains.debrainsApi.common.UserState;
import com.debrains.debrainsApi.entity.Profile;
import com.debrains.debrainsApi.entity.User;
import com.debrains.debrainsApi.exception.OAuthProcessingException;
import com.debrains.debrainsApi.repository.ProfileRepository;
import com.debrains.debrainsApi.repository.UserRepository;
import com.debrains.debrainsApi.security.CustomUserDetails;
import com.debrains.debrainsApi.security.oauth.user.OAuth2UserInfo;
import com.debrains.debrainsApi.security.oauth.user.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        return process(oAuth2UserRequest, oAuth2User);
    }

    private OAuth2User process(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        AuthProvider authProvider = AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, oAuth2User.getAttributes());

        if (userInfo.getEmail().isEmpty()) {
            throw new OAuthProcessingException("Email not found from OAuth2 provider");
        }
        Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            if (authProvider != user.getAuthProvider()) {
                throw new OAuthProcessingException("Wrong Match Auth Provider");
            }
            log.info(user.getEmail() + " 로그인!");

        } else {
            user = createUser(userInfo, authProvider);
            log.info(user.getEmail() + " 가입 완료!");
        }
        return CustomUserDetails.create(user, oAuth2User.getAttributes());
    }

    private User createUser(OAuth2UserInfo userInfo, AuthProvider authProvider) {
        User user = User.builder()
                .email(userInfo.getEmail())
                .img(userInfo.getImageUrl())
                .role(UserRole.USER)
                .state(UserState.ACT)
                .authProvider(authProvider)
                .blogUrl(userInfo.getBlogUrl())
                .githubUrl(userInfo.getGithubUrl())
                .tier(1)
                .exp(0L)
                .build();
        User save = userRepository.save(user);

        Profile profile = Profile.builder()
                .user(user)
                .skills("")
                .build();
        profileRepository.save(profile);

        return save;
    }

}

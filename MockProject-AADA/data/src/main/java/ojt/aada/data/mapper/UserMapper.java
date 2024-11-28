package ojt.aada.data.mapper;

import ojt.aada.data.datasource.remote.response.UserProfileResponse;
import ojt.aada.domain.models.UserProfile;

public class UserMapper {
    public static UserProfile mapToDomain(UserProfileResponse userProfileResponse) {
        return new UserProfile(
                userProfileResponse.getId(),
                userProfileResponse.getName(),
                userProfileResponse.getEmail(),
                userProfileResponse.getAvatar(),
                userProfileResponse.getGender(),
                userProfileResponse.getDob()
        );
    }

    public static UserProfileResponse mapToData(UserProfile userProfile) {
        return new UserProfileResponse(
                userProfile.getId(),
                userProfile.getName(),
                userProfile.getEmail(),
                userProfile.getAvatar(),
                userProfile.getGender(),
                userProfile.getDob()
        );
    }
}

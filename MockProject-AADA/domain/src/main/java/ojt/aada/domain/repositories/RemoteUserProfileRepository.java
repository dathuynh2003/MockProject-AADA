package ojt.aada.domain.repositories;

import androidx.lifecycle.LiveData;

import ojt.aada.domain.models.UserProfile;

public interface RemoteUserProfileRepository {
    LiveData<UserProfile> getUserProfile(String id);

    void updateUserProfile(UserProfile userProfile);
}

package ojt.aada.domain.usecase;

import androidx.lifecycle.LiveData;

import javax.inject.Inject;

import ojt.aada.domain.models.UserProfile;
import ojt.aada.domain.repositories.RemoteUserProfileRepository;

public class ManageUserProfileUseCase {
    private RemoteUserProfileRepository mRemoteUserProfileRepository;

    @Inject
    public ManageUserProfileUseCase(RemoteUserProfileRepository remoteUserProfileRepository) {
        mRemoteUserProfileRepository = remoteUserProfileRepository;
    }

    public LiveData<UserProfile> getUserProfile(String id) {
        return mRemoteUserProfileRepository.getUserProfile(id);
    }

    public void updateUserProfile(UserProfile userProfile) {
        mRemoteUserProfileRepository.updateUserProfile(userProfile);
    }
}

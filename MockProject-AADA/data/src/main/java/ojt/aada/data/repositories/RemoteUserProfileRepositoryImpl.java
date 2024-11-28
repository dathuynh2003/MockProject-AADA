package ojt.aada.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import ojt.aada.data.datasource.remote.datasouces.UserProfileRemoteDataSource;
import ojt.aada.data.mapper.UserMapper;
import ojt.aada.domain.models.UserProfile;
import ojt.aada.domain.repositories.RemoteUserProfileRepository;

public class RemoteUserProfileRepositoryImpl implements RemoteUserProfileRepository {

    private UserProfileRemoteDataSource mUserProfileRemoteDataSource;

    @Inject
    public RemoteUserProfileRepositoryImpl(UserProfileRemoteDataSource userProfileRemoteDataSource) {
        mUserProfileRemoteDataSource = userProfileRemoteDataSource;
    }

    @Override
    public LiveData<UserProfile> getUserProfile(String id) {
        MutableLiveData<UserProfile> user = new MutableLiveData<>();
        mUserProfileRemoteDataSource.getUserProfile(id).observeForever(userProfileResponse -> {
            user.setValue(UserMapper.mapToDomain(userProfileResponse));
        });
        return user;
    }

    @Override
    public void updateUserProfile(UserProfile userProfile) {
        mUserProfileRemoteDataSource.updateUserProfile(UserMapper.mapToData(userProfile));
    }
}

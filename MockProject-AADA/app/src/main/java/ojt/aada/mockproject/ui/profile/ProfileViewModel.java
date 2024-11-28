package ojt.aada.mockproject.ui.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ojt.aada.domain.models.UserProfile;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<UserProfile> mUserProfileMutableLiveData;

    public ProfileViewModel() {
        mUserProfileMutableLiveData = new MutableLiveData<>();
    }

    /**
     * Get user profile live data
     * @return MutableLiveData<UserProfile>
     */
    public MutableLiveData<UserProfile> getUserProfileMutableLiveData() {
        return mUserProfileMutableLiveData;
    }

    /**
     * Set user profile to live data
     * If isSamePlace is true -> set user profile to live data with same place in memory
     * If isSamePlace is false -> set user profile to live data with different place in memory
     * @param userProfile UserProfile object
     * @param isSamePlace boolean type
     */
    public void setUserProfileMutableLiveData(UserProfile userProfile, boolean isSamePlace) {
        if (isSamePlace) {
            mUserProfileMutableLiveData.setValue(userProfile);
        } else {
            UserProfile newUserProfile = new UserProfile();
            newUserProfile.setId(userProfile.getId());
            newUserProfile.setName(userProfile.getName());
            newUserProfile.setEmail(userProfile.getEmail());
            newUserProfile.setGender(userProfile.getGender());
            newUserProfile.setDob(userProfile.getDob());
            newUserProfile.setAvatar(userProfile.getAvatar());
            mUserProfileMutableLiveData.setValue(newUserProfile);
        }
    }

}

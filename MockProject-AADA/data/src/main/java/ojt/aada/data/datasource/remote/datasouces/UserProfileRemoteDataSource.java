package ojt.aada.data.datasource.remote.datasouces;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import ojt.aada.data.datasource.remote.response.UserProfileResponse;

public class UserProfileRemoteDataSource {
    FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private MutableLiveData<UserProfileResponse> user;

    @Inject
    public UserProfileRemoteDataSource() {
        mFirebaseDatabase = FirebaseDatabase.getInstance("https://aada-mockproject-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = mFirebaseDatabase.getReference("users");
        user = new MutableLiveData<>();
    }

    public LiveData<UserProfileResponse> getUserProfile(String id) {
        mDatabaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // If the user exists, return the user
                // Otherwise, create a sample user and return it
                if (snapshot.exists()) {
                    UserProfileResponse userProfileResponse = snapshot.getValue(UserProfileResponse.class);
                    user.setValue(userProfileResponse);
                } else {
                    UserProfileResponse sampleUserProfile = new UserProfileResponse(id, "Sample User", "sample@example.com", "", "Male", "1970/01/01");
                    mDatabaseReference.child(id).setValue(sampleUserProfile);
                    user.setValue(sampleUserProfile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return user;
    }

    public void updateUserProfile(UserProfileResponse userProfileResponse) {
        mDatabaseReference.child(userProfileResponse.getId()).setValue(userProfileResponse);
    }
}

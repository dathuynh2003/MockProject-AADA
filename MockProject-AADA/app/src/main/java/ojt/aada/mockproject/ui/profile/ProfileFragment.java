package ojt.aada.mockproject.ui.profile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.Calendar;

import javax.inject.Inject;

import ojt.aada.mockproject.R;
import ojt.aada.mockproject.databinding.FragmentProfileBinding;
import ojt.aada.mockproject.di.MyApplication;
import ojt.aada.mockproject.ui.MainViewModel;
import ojt.aada.mockproject.utils.ImageValidator;
import ojt.aada.mockproject.utils.Validator;


public class ProfileFragment extends Fragment {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int GALLERY_PERMISSION_CODE = 101;

    @Inject
    MainViewModel mViewModel;

    private ProfileViewModel mProfileViewModel;

    private FragmentProfileBinding binding;
    private NavController navController;
    private ActivityResultLauncher<Intent> launcher;
    private ActivityResultLauncher<String> permissionLauncher;

    private String currentImageSource;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MyApplication) requireContext().getApplicationContext()).appComponent.inject(this);

        mProfileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                if ("camera".equals(currentImageSource)) {
                    Bundle extras = result.getData().getExtras();
                    if (extras != null && extras.containsKey("data")) {
//                        Log.d("TAG123", "onCreate: " + extras.get("data"));
                        Bitmap avatarBitmap = (Bitmap) extras.get("data");
//                        avatarImageView.setImageBitmap(avatarBitmap);
                        binding.imgAvatar.setImageBitmap(avatarBitmap);
                        mProfileViewModel.getUserProfileMutableLiveData().getValue().setAvatar(ImageValidator.bitmapToBase64(avatarBitmap));
                    }
                } else if ("gallery".equals(currentImageSource)) {
                    Uri imageUri = result.getData().getData();
                    Bitmap avatarBitmap = ImageValidator.convertUriToBitmap(getContext(), imageUri);
                    binding.imgAvatar.setImageBitmap(avatarBitmap);
                    mProfileViewModel.getUserProfileMutableLiveData().getValue().setAvatar(ImageValidator.bitmapToBase64(avatarBitmap));
                }
            }
        });



        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                if ("camera".equals(currentImageSource)) {
                    openCamera();
                } else if ("gallery".equals(currentImageSource)) {
                    openGallery();
                }
            } else {
                Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        mViewModel.getUserProfileLiveData("").observe(getViewLifecycleOwner(), user -> {
            mProfileViewModel.setUserProfileMutableLiveData(user, false);
        });

        mProfileViewModel.getUserProfileMutableLiveData().observe(getViewLifecycleOwner(), binding::setUser);

        binding.editGender.setOnCheckedChangeListener((group, checkedId) -> {
            String gender = checkedId == R.id.male_rb ? "Male" : "Female";
            mProfileViewModel.getUserProfileMutableLiveData().getValue().setGender(gender);
        });

        // Open gallery to select image
        binding.imgAvatar.setOnClickListener(this::showAvatarPopupMenu);

        binding.editUserDob.setOnClickListener(v -> showDatePicker(binding));

        binding.cancelBtn.setOnClickListener(v -> {
            navController.popBackStack();
            ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
        });

        binding.doneBtn.setOnClickListener(v -> {
            // Save user profile
            mViewModel.updateUserProfile(mProfileViewModel.getUserProfileMutableLiveData().getValue());

            navController.popBackStack();
            ((AppCompatActivity) requireActivity()).getSupportActionBar().show();
        });

        return binding.getRoot();
    }

    private void showDatePicker(FragmentProfileBinding binding) {
        final Calendar calendar = Calendar.getInstance();
        String dob = mProfileViewModel.getUserProfileMutableLiveData().getValue().getDob();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (dob != null && !dob.isEmpty()) {
            String[] dobParts = dob.split("/");
            year = Integer.parseInt(dobParts[0]);
            month = Integer.parseInt(dobParts[1]) - 1; // Month is 0-based in Calendar
            day = Integer.parseInt(dobParts[2]);
//            calendar.set(year, month, day);
        }


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedYear + "/" + (selectedMonth + 1) + "/" + selectedDay;
                    if (!Validator.isDobValid(selectedDate)) {
                        Toast.makeText(getActivity(), "Invalid date of birth", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    binding.editUserDob.setText(selectedDate);
                    mProfileViewModel.getUserProfileMutableLiveData().getValue().setDob(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showAvatarPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.avatar_popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_camera) {
                checkPermission("android.permission.CAMERA", CAMERA_PERMISSION_CODE);
                currentImageSource = "camera";
                return true;
            } else if (item.getItemId() == R.id.menu_gallery) {
                checkPermission("android.permission.READ_MEDIA_IMAGES", GALLERY_PERMISSION_CODE);
                currentImageSource = "gallery";
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        } else {
            Toast.makeText(getActivity(), "Permission already granted", Toast.LENGTH_SHORT).show();
            if (requestCode == CAMERA_PERMISSION_CODE) {
                openCamera();
            } else if (requestCode == GALLERY_PERMISSION_CODE) {
                openGallery();
            }
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        launcher.launch(cameraIntent);
    }


    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        galleryIntent.setType("image/*");
        launcher.launch(galleryIntent);
    }
}
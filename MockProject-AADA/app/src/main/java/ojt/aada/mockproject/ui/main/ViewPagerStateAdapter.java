package ojt.aada.mockproject.ui.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerStateAdapter extends FragmentStateAdapter {
    ArrayList<Fragment> fragmentArrayList;

    public ViewPagerStateAdapter(@NonNull FragmentActivity fragmentActivity, ArrayList<Fragment> fragmentArrayList) {
        super(fragmentActivity);
        this.fragmentArrayList = fragmentArrayList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getIDForFragment(fragmentArrayList.get(position));
    }

    @Override
    public boolean containsItem(long itemId) {
        for (Fragment fragment : fragmentArrayList) {
            if (getIDForFragment(fragment) == itemId)
                return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return fragmentArrayList.size();
    }

    public void replaceFragment(int position, Fragment fragment) {
        fragmentArrayList.set(position, fragment);
        notifyItemChanged(position);
    }

    private long getIDForFragment(Fragment fragment)
    {
        return fragment.hashCode();
    }

}

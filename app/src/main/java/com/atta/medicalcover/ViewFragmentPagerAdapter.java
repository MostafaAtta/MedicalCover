package com.atta.medicalcover;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.atta.medicalcover.ui.fragments.LoginFragment;
import com.atta.medicalcover.ui.fragments.RegisterFragment;

public class ViewFragmentPagerAdapter extends FragmentStateAdapter {

    public ViewFragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
            default:
                return  new RegisterFragment();
            case 1:
                return new LoginFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

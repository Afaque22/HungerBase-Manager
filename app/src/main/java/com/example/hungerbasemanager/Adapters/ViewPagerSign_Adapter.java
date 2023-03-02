package com.example.hungerbasemanager.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hungerbasemanager.Fragments.LogInFragment;
import com.example.hungerbasemanager.Fragments.SignUpFragment;

public class ViewPagerSign_Adapter extends FragmentStateAdapter {


    public ViewPagerSign_Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new LogInFragment();
            case 1:
                return new SignUpFragment();
            default:
                return new LogInFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

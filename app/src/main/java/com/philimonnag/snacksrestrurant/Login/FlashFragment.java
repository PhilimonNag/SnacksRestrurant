package com.philimonnag.snacksrestrurant.Login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philimonnag.snacksrestrurant.R;
import com.philimonnag.snacksrestrurant.databinding.FragmentFlashBinding;


public class FlashFragment extends Fragment {
private FragmentFlashBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentFlashBinding.inflate(inflater,container,false);
        View root= binding.getRoot();
        binding.Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_flashFragment_to_signInFragment);
            }
        });
        binding.Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_flashFragment_to_signUpFragment);
            }
        });
        return root;
    }
}
package com.philimonnag.snacksrestrurant.Login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.philimonnag.snacksrestrurant.R;
import com.philimonnag.snacksrestrurant.databinding.FragmentSignInBinding;

import org.jetbrains.annotations.NotNull;

public class SignInFragment extends Fragment {
private FragmentSignInBinding binding;
FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      binding=FragmentSignInBinding.inflate(inflater,container,false);
      View root=binding.getRoot();
        mAuth= FirebaseAuth.getInstance();
        binding.SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email= binding.email.getText().toString();
                String password= binding.password.getText().toString();
                if(TextUtils.isEmpty(Email)){
                    binding.email.setError("Email is Required");
                }else if(TextUtils.isEmpty(password)){
                    binding.password.setError("Password is required");
                }else{
                    signIn(Email,password,v);

                }
            }
        });
        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_signInFragment_to_forgotPasswordFragment);
            }
        });
        binding.SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.Or.setVisibility(View.VISIBLE);
                binding.mobileTV.setVisibility(View.VISIBLE);
                binding.signIn.setVisibility(View.VISIBLE);
                binding.SignUp.setVisibility(View.GONE);
            }
        });
      return root;
    }
    private void signIn(String email, String password,View view) {
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(getContext(), "Yes God is with me...", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_menuFragment);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
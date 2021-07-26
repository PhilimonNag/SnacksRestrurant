package com.philimonnag.snacksrestrurant.Login;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.PagerAdapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.philimonnag.snacksrestrurant.Models.Restaurant;
import com.philimonnag.snacksrestrurant.R;
import com.philimonnag.snacksrestrurant.databinding.FragmentSignUpBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class SignUpFragment extends Fragment {
private FragmentSignUpBinding binding;
FirebaseAuth mAuth;
String mVerificationId;
String Email,password,hotel,mobile,postalAddress,Description;
PhoneAuthProvider.ForceResendingToken token;
ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding=FragmentSignUpBinding.inflate(inflater,container,false);
       View root= binding.getRoot();
       mAuth=FirebaseAuth.getInstance();
       pd=new ProgressDialog(getContext());
       pd.setMessage("Sending Otp...");
       binding.SignUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               hotel=binding.name.getText().toString();
               Email=binding.email.getText().toString();
               password=binding.password.getText().toString();
               mobile="+91"+binding.mobile.getText().toString();
               Description=binding.HotelDescription.getText().toString();
               postalAddress=binding.PostalAddress.getText().toString();
               if(TextUtils.isEmpty(hotel)){
                   binding.name.setError("Restaurant Name is required");
               }else if(TextUtils.isEmpty(Email)){
                   binding.email.setError("Email is Required");
               }else if(TextUtils.isEmpty(mobile)){
                   binding.mobile.setError("mobile number is Required");
               }else if(TextUtils.isEmpty(Description)){
                   binding.HotelDescription.setError(" Description is Required");
               }else if(TextUtils.isEmpty(postalAddress)){
                   binding.PostalAddress.setError("Address is Required");
               }else if(password.length()<6){
                   binding.password.setError("password length must be greater than 6");
               }else{
                   pd.show();
                   PhoneAuthOptions options= PhoneAuthOptions.newBuilder(mAuth)
                           .setPhoneNumber(mobile)       // Phone number to verify
                           .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                           .setActivity(requireActivity())                 // Activity (for callback binding)
                           .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                               @Override
                               public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                                   mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                       @Override
                                       public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                           pd.dismiss();
                                           if(task.isSuccessful()){
                                               AuthCredential EmailCredential=EmailAuthProvider.getCredential(Email,password);
                                               mAuth.getCurrentUser().linkWithCredential(EmailCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                   @Override
                                                   public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                                       if(task.isSuccessful()){
                                                           Toast.makeText(getContext(), "Login", Toast.LENGTH_SHORT).show();
                                                           uploadUser();
                                                       }
                                                   }
                                               }).addOnFailureListener(new OnFailureListener() {
                                                   @Override
                                                   public void onFailure(@NonNull @NotNull Exception e) {
                                                       Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                   }
                                               });
                                           }else {
                                               Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });
                                   pd.dismiss();
                               }

                               @Override
                               public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                                   pd.dismiss();
                                   Toast.makeText(getContext(), "it because "+e.getMessage(), Toast.LENGTH_SHORT).show();
                               }

                               @Override
                               public void onCodeSent(@NonNull @NotNull String s, @NonNull @NotNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                   super.onCodeSent(s, forceResendingToken);
                                   pd.dismiss();
                                   mVerificationId=s;
                                   token = forceResendingToken;
                                   binding.otpView.setVisibility(View.VISIBLE);
                                   binding.verify.setVisibility(View.VISIBLE);

                               }
                           })          // OnVerificationStateChangedCallbacks
                           .build();
                   PhoneAuthProvider.verifyPhoneNumber(options);

               }
           }
       });
       binding.verify.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String otp=binding.otpView.getText().toString();
               if(TextUtils.isEmpty(otp)){
                   binding.otpView.setError("otp is required");
               }else {
                   PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mVerificationId,otp);
                   mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                           if(task.isSuccessful()){
                               AuthCredential EmailCredential=EmailAuthProvider.getCredential(Email,password);
                               mAuth.getCurrentUser().linkWithCredential(EmailCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                   @Override
                                   public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                       if(task.isSuccessful()){
                                           Toast.makeText(getContext(), "Login", Toast.LENGTH_SHORT).show();
                                           uploadUser();
                                       }
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull @NotNull Exception e) {
                                       Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                   }
                               });


                           }else {
                               Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });
               }
           }
       });
       return  root;
    }
    private void uploadUser() {
        String userId= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        Restaurant restaurant=new Restaurant(userId,hotel,mobile,Email,Description,postalAddress);
        FirebaseDatabase.getInstance().getReference().child("Restaurant").
                child(mAuth.getCurrentUser().getUid()).
                setValue(restaurant).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
             if(task.isSuccessful()){
                 Toast.makeText(getContext(), "God is Good all the time", Toast.LENGTH_SHORT).show();
                 Navigation.findNavController(getView()).navigate(R.id.action_signUpFragment_to_menuFragment);
             }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
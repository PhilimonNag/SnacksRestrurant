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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.philimonnag.snacksrestrurant.R;
import com.philimonnag.snacksrestrurant.databinding.FragmentForgotPasswordBinding;
import org.jetbrains.annotations.NotNull;
public class ForgotPasswordFragment extends Fragment {
    private FragmentForgotPasswordBinding binding;
    FirebaseAuth mAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentForgotPasswordBinding.inflate(inflater,container,false);
        View root= binding.getRoot();
        mAuth=FirebaseAuth.getInstance();
        binding.recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String re=binding.phoneTv.getText().toString().trim();

                if(TextUtils.isEmpty(re)){
                    binding.phoneTv.setError("Email Address is required");
                }else{
                    mAuth.sendPasswordResetEmail(re).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Please Check your Email , A link has sent to Your register email", Toast.LENGTH_SHORT).show();
                                Navigation.findNavController(v).navigate(R.id.action_forgotPasswordFragment_to_signInFragment);
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
        });
        return root;
    }
}
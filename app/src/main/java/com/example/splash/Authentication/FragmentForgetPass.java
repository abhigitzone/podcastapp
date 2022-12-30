package com.example.splash.Authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.splash.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import org.jetbrains.annotations.NotNull;

public class FragmentForgetPass extends BottomSheetDialogFragment {

    EditText forgetEmail;
    Button forgetBtn;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        forgetEmail = view.findViewById(R.id.forgetEmail);
        progressBar = view.findViewById(R.id.forgetProgress);
        forgetBtn = view.findViewById(R.id.forgetBtn);

        firebaseAuth = FirebaseAuth.getInstance();

        forgetBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String forget_email = forgetEmail.getText().toString().trim();
            if (forget_email.isEmpty()) {
                forgetEmail.setError(getString(R.string.mandatory));
            } else
                firebaseAuth.fetchSignInMethodsForEmail(forget_email).addOnCompleteListener(task -> {
                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                    if(isNewUser) {
                        Toast.makeText(getContext(), getString(R.string.email_not_found), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } else {
                        firebaseAuth.sendPasswordResetEmail(forget_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                Toast.makeText(getContext(), getString(R.string.emailLinkVerr), Toast.LENGTH_SHORT).show();
                            }
                        });
                        progressBar.setVisibility(View.GONE);
                    }
                });
        });
    }
}

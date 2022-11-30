package com.example.testrool.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.testrool.HomePageActivity;
import com.example.testrool.MessActivity;
import com.example.testrool.R;
import com.example.testrool.UserInfoDisplayActivity;
import com.example.testrool.bean.LoggedInUser;
import com.example.testrool.databinding.FragmentMyInfoBinding;
import com.example.testrool.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentMyInfoBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
/*        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);*/
        binding = FragmentMyInfoBinding.inflate(inflater, container, false);
        LoggedInUser user = LoggedInUser.getLoggedInUser();

        binding.userName.setText(user.getDisplayName());
        binding.userAccount.setText(user.getEmail());

        //----------------------------------------------------------------------
        binding.guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserInfoDisplayActivity.class);
                String str1=getString(R.string.usestr);
                intent.putExtra("mycontent", str1);
                startActivity(intent);
            }
        });
        binding.about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserInfoDisplayActivity.class);
                String str1=getString(R.string.aboutstr);
                intent.putExtra("mycontent", str1);
                startActivity(intent);
            }
        });
        binding.agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserInfoDisplayActivity.class);
                String str1=getString(R.string.agreestr);
                intent.putExtra("mycontent", str1);
                startActivity(intent);
            }
        });
        binding.power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserInfoDisplayActivity.class);
                String str1=getString(R.string.powerstr);
                intent.putExtra("mycontent", str1);
                startActivity(intent);
            }
        });
        binding.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UserInfoDisplayActivity.class);
                String str1=getString(R.string.helpstr);
                intent.putExtra("mycontent", str1);
                startActivity(intent);
            }
        });
        //over----------------------------------------------------------------------

        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
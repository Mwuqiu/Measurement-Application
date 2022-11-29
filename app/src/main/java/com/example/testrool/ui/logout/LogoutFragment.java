package com.example.testrool.ui.logout;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testrool.ActivityCollectorUtil;
import com.example.testrool.ModeBuildActivity;
import com.example.testrool.R;
import com.example.testrool.bean.LoggedInUser;
import com.example.testrool.databinding.ActivityMineInfoBinding;
import com.example.testrool.databinding.FragmentGalleryBinding;
import com.example.testrool.databinding.FragmentLogoutBinding;
import com.example.testrool.databinding.FragmentMyInfoBinding;
import com.example.testrool.login.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogoutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LogoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogoutFragment newInstance(String param1, String param2) {
        LogoutFragment fragment = new LogoutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private FragmentLogoutBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentLogoutBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        binding.outButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 退出登录
                ActivityCollectorUtil.finishAllActivity();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}
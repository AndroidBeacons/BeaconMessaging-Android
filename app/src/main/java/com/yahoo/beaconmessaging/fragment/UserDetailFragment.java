package com.yahoo.beaconmessaging.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.activity.ProfileActivity;

import java.io.File;
import java.io.IOException;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USER_ID = "user";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String userObjectId;
    private String mParam2;
    public Uri uri;
    private ParseUser parseUser;

    private ImageClickListener imageClickListener;

    public interface ImageClickListener {
        void setUser(ParseUser user);
        void imageClicked(View view);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param imageClickListener Parameter 2.
     * @return A new instance of fragment UserDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserDetailFragment newInstance(String param1, ImageClickListener imageClickListener) {
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, param1);
        fragment.imageClickListener = imageClickListener;
        fragment.setArguments(args);
        return fragment;
    }

    public UserDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userObjectId = getArguments().getString(ARG_USER_ID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        final TextView tvName = (TextView)view.findViewById(R.id.tvName);
        final TextView tvDescription = (TextView)view.findViewById(R.id.tvDescription);
        final ImageView ivProfile = (ImageView)view.findViewById(R.id.ivProfileImage);
        final TextView tvFavoriteCount = (TextView)view.findViewById(R.id.tvFavoriteCount);
        final TextView tvPostCount = (TextView)view.findViewById(R.id.tvPostCount);
        final ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.getInBackground(userObjectId, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e != null)
                {
                    Toast.makeText(getActivity(),"Error retrieving user",Toast.LENGTH_SHORT).show();
                    return;
                }
                UserDetailFragment.this.parseUser = parseUser;
                imageClickListener.setUser(parseUser);
                tvName.setText(parseUser.getUsername());
                tvDescription.setText(parseUser.getString("description"));
                tvFavoriteCount.setText("" + parseUser.getInt("favoriteCount"));
                tvPostCount.setText("" + parseUser.getInt("postCount"));
                ParseFile imageFile = parseUser.getParseFile("imageFile");
                if (imageFile != null)
                {
                    Picasso.with(getActivity()).load(imageFile.getUrl()).into(ivProfile);
                }
            }
        });
        
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageClickListener != null)
                {
                    imageClickListener.imageClicked(v);
                }
            }
        });
        return view;
        
    }


}

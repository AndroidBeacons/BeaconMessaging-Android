package com.yahoo.beaconmessaging.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.codec.binary.StringUtils;
import com.squareup.picasso.Picasso;
import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.api.ExhibitClient;
import com.yahoo.beaconmessaging.model.Post;


public class AddPostFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String exhibitId;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText etPost;
    private Button btnPost;
    private TextView tvCurrentUser;
    private ImageView ivCurrentUserProfileImage;

//    private OnFragmentInteractionListener mListener;

    public interface AddPostDialogListener {
        void onFinishAddPostDialog();
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddTweetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPostFragment newInstance(String exhibitId) {
        AddPostFragment fragment = new AddPostFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        fragment.exhibitId = exhibitId;
        return fragment;
    }

    public AddPostFragment() {
    }

    private void showCurrentUser()
    {
        ParseUser parseUser = ParseUser.getCurrentUser();
        ParseFile file = parseUser.getParseFile("imageFile");
        Picasso.with(getActivity()).load(file.getUrl()).into(ivCurrentUserProfileImage);
        tvCurrentUser.setText(capitalize(parseUser.getUsername()));
    }

    private String capitalize(String username) {
        String[] arr = username.split(" ");
        StringBuilder sb = new StringBuilder();

        for (String anArr : arr) {
            sb.append(Character.toUpperCase(anArr.charAt(0)))
                    .append(anArr.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_post, container, false);
        Button btnCancel = (Button)v.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPostFragment.this.dismiss();
            }
        });
        
        btnPost = (Button)v.findViewById(R.id.btnPost);
        btnPost.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AddPostFragment.this.onAdd(getView());

                                        }
                                    }
        );
        
        etPost = (EditText)v.findViewById(R.id.etPost);
        tvCurrentUser = (TextView)v.findViewById(R.id.tvUser);
        ivCurrentUserProfileImage = (ImageView)v.findViewById(R.id.ivCurrentUserProfile);
        showCurrentUser();
        return v;

    }

    public void onAdd(View view)
    {
        final Post post = ParseObject.create(Post.class);
        post.put("userId", ParseUser.getCurrentUser().getObjectId());
        post.put("description", etPost.getText().toString());
        post.put("exhibitId", exhibitId);
        btnPost.setClickable(false);
        
        SaveCallback postSaveCallBack = new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                {
                    Toast.makeText(getActivity(),"Comment has been posted", Toast.LENGTH_SHORT).show();
                    AddPostDialogListener listener = (AddPostDialogListener) getActivity();
                    listener.onFinishAddPostDialog();
                    AddPostFragment.this.dismiss();
                }
                else
                {   
                    Toast.makeText(getActivity(),"Error saving comment:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    btnPost.setClickable(true);
                }
            }
        };

        ExhibitClient.addPost(post, postSaveCallBack);
        
    }

    public void onCancelClick(View view)
    {
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return d;
    }
}

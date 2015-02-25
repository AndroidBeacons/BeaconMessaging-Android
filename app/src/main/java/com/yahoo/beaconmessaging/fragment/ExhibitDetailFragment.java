package com.yahoo.beaconmessaging.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yahoo.beaconmessaging.R;
import com.yahoo.beaconmessaging.button.FloatingActionButton;
import com.yahoo.beaconmessaging.model.Exhibit;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExhibitDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExhibitDetailFragment extends Fragment {

    @InjectView(R.id.tvName)TextView tvName;
    @InjectView(R.id.ivExhibitImage) ImageView ivExhibitImage;
    @InjectView(R.id.tvDescription)  TextView tvDescription;
    @InjectView(R.id.tvFavoriteCount) TextView tvFavoriteCount;
    @InjectView(R.id.tvPostCount) TextView tvPostCount;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExhibitDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExhibitDetailFragment newInstance(Exhibit exhibit) {
        ExhibitDetailFragment fragment = new ExhibitDetailFragment();
        return fragment;
    }

    public ExhibitDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.item_exhibit, container, false);
        ButterKnife.inject(getActivity());
        FloatingActionButton fabButton = new FloatingActionButton.Builder(getActivity())
                .withDrawable(getResources().getDrawable(R.drawable.ic_plus))
                .withButtonColor(Color.TRANSPARENT)
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 0, 0)
                .create();        
        return  view;
    }


}

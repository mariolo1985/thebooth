package com.example.kinfonglo.photobooth;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

public class FragHeader extends Fragment implements View.OnClickListener {

    View _mainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_header, container, false);
        _mainView = v;
        Button b = (Button) v.findViewById(R.id.btn_menu);
        b.setOnClickListener(this);

        RelativeLayout sidenav = v.findViewById(R.id.sidenav);
        sidenav.setVisibility(View.INVISIBLE);

        return v;
    }

    // CLick events

    @Override
    public void onClick(View v) {
        // show menu
        switch (v.getId()) {
            case R.id.btn_menu:
                Log.d("WBM", "clicked");


                RelativeLayout sidenav = _mainView.findViewById(R.id.sidenav);
                sidenav.setVisibility(View.VISIBLE);
                sidenav.animate().translationX(0).translationXBy(100).setDuration(1000);
                break;

            default:
                break;
        }
    }
}

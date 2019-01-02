package com.ahmetkilic.ophelia.ea_slider;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ahmetkilic.ophelia.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class SlideFragment extends Fragment {

    private static final String ARG_SLIDE = "EASlide";
    private static final String ARG_PROGRESS = "progress_id";
    private static final String ARG_ERROR = "error_id";
    private EASlide EASlide;
    private SliderClickListener mListener;
    private View progressView;
    private int progressViewResId, errorViewResId;

    /**
        ---------------IMPORTANT ---------------
        Starting with Android 9.0 (API level 28), cleartext support is disabled by default.
        https://stackoverflow.com/a/50834600

     */
    public SlideFragment() {
    }

    public static SlideFragment newInstance(EASlide EASlide, int progressViewResId, int errorViewResId, SliderClickListener sliderClickListener) {
        SlideFragment fragment = new SlideFragment();
        fragment.setListener(sliderClickListener);
        Bundle args = new Bundle();
        args.putSerializable(ARG_SLIDE, EASlide);
        args.putInt(ARG_PROGRESS, progressViewResId);
        args.putInt(ARG_ERROR, errorViewResId);
        fragment.setArguments(args);
        return fragment;
    }

    void setListener(SliderClickListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            EASlide = (EASlide) getArguments().getSerializable(ARG_SLIDE);
            progressViewResId = getArguments().getInt(ARG_PROGRESS);
            errorViewResId = getArguments().getInt(ARG_ERROR);
        }
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_slide, container, false);
        AppCompatImageView imageView = view.findViewById(R.id.slider_image);

        if (progressViewResId != 0) {
            progressView = inflater.inflate(progressViewResId, (ViewGroup) view, false);
            ((ViewGroup) view).addView(progressView);
        }

        Callback.EmptyCallback callback = new Callback.EmptyCallback() {
            @Override
            public void onSuccess() {
                if (progressView != null) {
                    progressView.animate().alpha(0.0f).setDuration(500).start();
                }
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                if (progressView != null) {
                    progressView.animate().alpha(0.0f).setDuration(500).start();
                }
                showImageError(inflater, view);
            }
        };

        if (EASlide.getFile() != null)
            Picasso.get().load(EASlide.getFile()).fit().centerCrop().into(imageView, callback);
        else if (EASlide.getPath() != null)
            Picasso.get().load(EASlide.getPath()).fit().centerCrop().into(imageView, callback);
        else if (EASlide.getUri() != null)
            Picasso.get().load(EASlide.getUri()).fit().centerCrop().into(imageView, callback);
        else if (EASlide.getResourceId() != 0)
            Picasso.get().load(EASlide.getResourceId()).fit().centerCrop().into(imageView, callback);
        else
            showImageError(inflater, view);

        /*
        Glide can be used.

        RequestListener<Drawable> glideListener = new RequestListener<Drawable>() {
            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                if (progressView != null) {
                    progressView.animate().alpha(0.0f).setDuration(500).start();
                }
                return false;
            }

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                if (progressView != null) {
                    progressView.animate().alpha(0.0f).setDuration(500).start();
                }
                showImageError(inflater, view);
                return false;
            }
        };

        RequestOptions options = new RequestOptions();
        options.centerCrop();

        if (EASlide.getFile() != null)
            Glide.with(container.getContext()).load(EASlide.getFile()).apply(options).listener(glideListener).into(imageView);
        else if (EASlide.getPath() != null)
            Glide.with(container.getContext()).load(EASlide.getPath()).apply(options).listener(glideListener).into(imageView);
        else if (EASlide.getUri() != null)
            Glide.with(container.getContext()).load(EASlide.getUri()).apply(options).listener(glideListener).into(imageView);
        else if (EASlide.getResourceId() != 0)
            Glide.with(container.getContext()).load(EASlide.getResourceId()).apply(options).listener(glideListener).into(imageView);
        else
            showImageError(inflater, view);
        */

        if (mListener != null)
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null)
                        mListener.onSliderClicked(EASlide);
                }
            });

        return view;
    }

    private void showImageError(LayoutInflater inflater, View view) {
        if (errorViewResId != 0 && view != null && inflater != null) {
            View errorView = inflater.inflate(errorViewResId, (ViewGroup) view, false);
            ((ViewGroup) view).addView(errorView);
        }
    }

}

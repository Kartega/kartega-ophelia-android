package com.ahmetkilic.eaframework;

import android.os.Bundle;

import com.ahmetkilic.ophelia.EABaseActivity;
import com.ahmetkilic.ophelia.ea_slider.EASlide;
import com.ahmetkilic.ophelia.ea_slider.EASlider;
import com.ahmetkilic.ophelia.ea_slider.EASliderAdapter;
import com.ahmetkilic.ophelia.ea_slider.SliderClickListener;
import com.ahmetkilic.ophelia.ea_slider.indicator.EAIndicator;
import com.ahmetkilic.ophelia.ea_slider.transformers.TabletTransformer;


public class SliderActivity extends EABaseActivity implements SliderClickListener {

    private EASlider eaSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        eaSlider = findViewById(R.id.slider);
        EASliderAdapter eaSliderAdapter = new EASliderAdapter(getSupportFragmentManager());
        eaSliderAdapter.setProgressLayoutId(R.layout.progress_layout);
        eaSliderAdapter.setErrorLayoutId(R.layout.error_layout);
        eaSliderAdapter.setSliderClickListener(this);

        /*
        //Default Values
        eaSlider.enableSwipe();
        eaSlider.setBlockTimeMillis(100);
        eaSlider.setAutoScrollDuration(4000);
        eaSlider.setScrollDuration(400);

        eaSliderAdapter.setDisableLoop(false);
        eaSliderAdapter.setDisableLoopForOneElement(true);
        */

        EASlide eASlide1 = new EASlide("http://demo.smartaddons.com/extensions/joomla17/cache/mod_sj_content_slider/2c04826e09a2d5e19101b62a6843ba68.jpeg");
        EASlide eASlide2 = new EASlide("https://ksassets.timeincuk.net/wp/uploads/sites/55/2017/08/GettyImages-496903944-920x584.jpg");
        EASlide eASlide3 = new EASlide("http://wowslider.com/sliders/demo-7/data/images/gate.jpg");
        EASlide eASlide4 = new EASlide("http://wowslider.com/sliders/demo-46/data1/images/blue_ocean.jpg");

        eASlide1.getExtras().putString("title", "EASlide 1");
        eASlide2.getExtras().putString("title", "EASlide 2");
        eASlide3.getExtras().putString("title", "EASlide 3");
        eASlide4.getExtras().putString("title", "EASlide 4");

        eaSliderAdapter.addSlide(eASlide1);
        eaSliderAdapter.addSlide(eASlide2);
        eaSliderAdapter.addSlide(eASlide3);
        eaSliderAdapter.addSlide(eASlide4);

        eaSlider.setAdapter(eaSliderAdapter);
        eaSlider.setPageTransformer(false, new TabletTransformer());

        EAIndicator indicator = findViewById(R.id.indicator);
        indicator.attachToSlider(eaSlider);
    }

    @Override
    public void onSliderClicked(EASlide EASlide) {
        if (EASlide.getExtras() != null) {
            String extra_title = EASlide.getExtras().getString("title", "");
            showToast("EASlide Title:" + extra_title);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        eaSlider.stopAutoScroll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        eaSlider.startAutoScroll();
    }
}

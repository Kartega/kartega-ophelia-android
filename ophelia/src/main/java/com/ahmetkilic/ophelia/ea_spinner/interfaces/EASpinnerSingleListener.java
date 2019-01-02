package com.ahmetkilic.ophelia.ea_spinner.interfaces;

import android.view.View;

/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 * <p>
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public interface EASpinnerSingleListener<T> {
    void onItemSelected(View spinner, T selection);
}

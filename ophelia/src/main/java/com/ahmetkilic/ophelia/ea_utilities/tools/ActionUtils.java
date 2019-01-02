package com.ahmetkilic.ophelia.ea_utilities.tools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class ActionUtils {

    /**
     * Dials phone number from string
     *
     * @param context context
     * @param phoneNumber phone number in digits
     */
    public static void dialPhoneNumber(Context context, String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length()>0){
            if (!phoneNumber.startsWith("0"))
                phoneNumber = "0"+phoneNumber;

            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            context.startActivity(intent);
        }
    }
}

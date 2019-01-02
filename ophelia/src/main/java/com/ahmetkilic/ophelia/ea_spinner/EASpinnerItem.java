package com.ahmetkilic.ophelia.ea_spinner;


import com.ahmetkilic.ophelia.ea_recycler.interfaces.EATypeInterface;

import java.io.Serializable;
/**
 * Created by Ahmet Kılıç on 18.12.2018.
 * Copyright © 2018, Kartega. All rights reserved.
 *
 * For the full copyright and license information,
 * please view the LICENSE file that was distributed with this source code.
 */
public class EASpinnerItem<T> implements EATypeInterface, Serializable {

    private boolean checked;
    private boolean showCheckBox;
    private T object;

    public EASpinnerItem() {
    }

    public EASpinnerItem(T object) {
        this.object = object;
    }

    public EASpinnerItem(T object, boolean checked) {
        this.checked = checked;
        this.object = object;
    }

    public boolean isShowCheckBox() {
        return showCheckBox;
    }

    public void setShowCheckBox(boolean showCheckBox) {
        this.showCheckBox = showCheckBox;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public T getObject() {
        return object;
    }

    @Override
    public String toString() {
        return getObject().toString();
    }

    @Override
    public int getRecyclerType() {
        return EAItemHolder.EA_SPINNER_ITEM;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EASpinnerItem)
            return getObject().equals(((EASpinnerItem) obj).getObject());
        return false;
    }
}

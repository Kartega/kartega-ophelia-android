package com.ahmetkilic.eaframework.object;


import com.ahmetkilic.ophelia.ea_recycler.interfaces.EATypeInterface;

import java.io.Serializable;

public class TempObject implements Serializable,EATypeInterface {

    private String text;

    public TempObject() {
    }

    public TempObject(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return getText();
    }

    @Override
    public int getRecyclerType() {
        return 10;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TempObject)
            return text.equals(((TempObject) obj).getText());
        return false;
    }
}

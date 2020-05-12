package com.example.demoapp.utilities;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

public class CheckedCardView extends CardView implements Checkable {

    private static final int[] CHECKED_STATE_SET = {
        android.R.attr.state_checked,
    };
    private boolean isChecked=false;
    public CheckedCardView(@NonNull Context context) {
        super(context);
    }

    public CheckedCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckedCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState= super.onCreateDrawableState(extraSpace+1);
        if (isChecked){
            mergeDrawableStates(drawableState,CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override
    public void setChecked(boolean b) {
        this.isChecked=b;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        setChecked(!this.isChecked);
    }

}

package com.leafpic.app.Views;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.leafpic.app.utils.Measure;

/**
 * Created by dnld on 06/03/16.
 */
public class FABScrollBehavior extends FloatingActionButton.Behavior {
    public FABScrollBehavior(Context context, AttributeSet attributeSet) {
        super();
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        /*if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            child.animate().translationY(child.getHeight()*2).setInterpolator(new AccelerateInterpolator(2)).start();
        } else {
            child.animate().translationY(-Measure.getNavBarHeight(Resources.getSystem())).setInterpolator(new DecelerateInterpolator(2)).start();
        }*/

        }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}

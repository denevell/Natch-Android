package org.denevell.droidnatch.uitests;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import android.app.Activity;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;

public class CustomMatchers {

    public static Matcher<View> listViewHasElements() {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof ListView)) {
                    return false;
                }
                Adapter adapter = ((ListView) view).getAdapter();
                if(adapter.getCount()>0) {
                    return true;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("list view has elements");
            }
        };
    }

    public static Matcher<View> viewHasActivityTitle(final String suggestedTitle) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof View)) {
                    return false;
                }
                String title = ((Activity) view.getContext()).getTitle().toString();
                if(title.equals(suggestedTitle)) {
                    return true;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("list view has elements");
            }
        };
    }

    public static Matcher<View> listViewHasElements(final int numOfElements) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof ListView)) {
                    return false;
                }
                Adapter adapter = ((ListView) view).getAdapter();
                if(adapter.getCount()==numOfElements) {
                    return true;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("list view has so many elements: "  + numOfElements);
            }
        };
    }

}

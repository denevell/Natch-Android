package org.denevell.droidnatch.uitests;

import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class CustomMatchers {

    public static Matcher<View> listViewHasElements() {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof ListView)) {
                    return false;
                }
                @SuppressWarnings("rawtypes")
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

    public static Matcher<View> listViewElementContains(int pos, String containsText) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof ListView)) {
                    return false;
                }
                @SuppressWarnings("rawtypes")
                Adapter adapter = ((ListView) view).getAdapter();
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("list view has elements");
            }
        };
    }

}

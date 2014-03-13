package org.denevell.droidnatch.uitests;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import android.app.Activity;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.apps.common.testing.ui.espresso.UiController;
import com.google.android.apps.common.testing.ui.espresso.ViewAction;

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

            private int mAdapterCount;

			@Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof ListView)) {
                    return false;
                }
                ListView listView = (ListView) view;
				Adapter adapter = listView.getAdapter();
                mAdapterCount = adapter.getCount();
				int countMinusHeadersAndFooters = mAdapterCount-listView.getFooterViewsCount()-listView.getHeaderViewsCount();
				mAdapterCount = countMinusHeadersAndFooters;
				if(countMinusHeadersAndFooters==numOfElements) {
                    return true;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("list view should have "  + numOfElements + " elements but got: " + mAdapterCount);
            }
        };
    }

    public static ViewAction scrollToBottomOfListView() {
    		return new ViewAction() {
				@Override
				public void perform(UiController arg0, View view) {
                        ListView listView = (ListView) view;
                        int last = listView.getAdapter().getCount();
                        listView.smoothScrollToPosition(last);
				}
				
				@Override
				public String getDescription() {
					return "Problem scrolling to bottom of ListView";
				}
				
				@Override
				public Matcher<View> getConstraints() {
					return new TypeSafeMatcher<View>() {
						@Override public void describeTo(Description description) {
							description.appendText("View should be a ListView with an adapter");
						}
						@Override public boolean matchesSafely(View view) {
							if (!(view instanceof ListView)) {
								return false;
							} else if(((ListView)view).getAdapter()==null) {
								return false;
							} else {
								return true;
							}
						}
					};
				}
			};
    }

	public static Matcher<? super View> showsErrorString() {
        return new TypeSafeMatcher<View>() {

			@Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }
                EditText v = (EditText) view;
                String error = v.getError().toString();
				if(error != null && error.length()>0) {
                    return true;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Edit text should have error string");
            }
        };
	}

	public static Matcher<? super View> showsErrorString(final String contains) {
        return new TypeSafeMatcher<View>() {

			@Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }
                EditText v = (EditText) view;
                String error = v.getError().toString();
                String c = contains;
				if(error != null && error.length()>0 && error.contains(c)) {
                    return true;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Edit text should contain error string: " + contains);
            }
        };
	}

	public static Matcher<? super View> withRegexText(final String regex) {
        return new TypeSafeMatcher<View>() {
			private String text;
			@Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof TextView)) {
                    return false;
                }
                TextView v = (TextView) view;
                text = v.getText().toString();
				if(text == null) {
					return false;
				}
				boolean ret = text.matches(regex);
                return ret;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(text + " should match the regex: " + regex);
            }
        };
	}

}

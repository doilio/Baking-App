package com.doiliomatsinhe.bakingapp.ui.recipe;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.util.EspressoUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


@RunWith(AndroidJUnit4.class)
public class RecipeActivityTest {

    @Rule
    public ActivityTestRule<RecipeActivity> activityTestRule = new ActivityTestRule<>(RecipeActivity.class);

    private IdlingResource idlingResource = EspressoUtils.getIdlingResource();


    @Before
    public void setUp() {
        IdlingRegistry.getInstance().register(idlingResource);
    }

    /**
     * Checks if the list exists
     */
    @Test
    public void checkListExists() {
        onView(withId(R.id.recycler_recipe)).check(matches(isDisplayed()));
    }

    /**
     * Clicks on the Items in the RecyclerView
     */
    @Test
    public void iterateRecyclerViewItems() {
        for (int i = 0; i < 4; i++) {
            onView(withId(R.id.recycler_recipe)).perform(actionOnItemAtPosition(i, click()));
            Espresso.pressBack();
        }
    }

    /**
     * Checks if each Recipe has Steps
     */
    @Test
    public void iterateRecipe_chechHasSteps() {
        for (int i = 0; i < 4; i++) {
            onView(withId(R.id.recycler_recipe)).perform(actionOnItemAtPosition(i, click()));
            onView(withId(R.id.recycler_step)).check(matches(isDisplayed()));
            Espresso.pressBack();
        }
    }

    /**
     * Checks if each recipe has atleast 5 steps
     */
    @Test
    public void iterateRecipe_chechHasAtleastFiveSteps() {
        for (int i = 0; i < 4; i++) {
            onView(withId(R.id.recycler_recipe)).perform(actionOnItemAtPosition(i, click()));

            for (int j = 0; j < 5; j++) {
                onView(withId(R.id.recycler_step)).perform(actionOnItemAtPosition(j, click()));
                Espresso.pressBack();
            }
            Espresso.pressBack();
        }
    }

    @After
    public void tearDown() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}
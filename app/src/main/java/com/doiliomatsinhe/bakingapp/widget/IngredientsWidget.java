package com.doiliomatsinhe.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.model.Recipe;
import com.doiliomatsinhe.bakingapp.util.JsonUtil;

import static com.doiliomatsinhe.bakingapp.ui.recipe.RecipeActivity.MY_RECIPE;
import static com.doiliomatsinhe.bakingapp.ui.recipe.RecipeActivity.RECIPE_PREF;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {

    private static final String TAG = IngredientsWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        Intent serviceIntent = new Intent(context, IngredientWidgetService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setRemoteAdapter(R.id.ingredients_widget_stack, serviceIntent);
        views.setEmptyView(R.id.ingredients_widget_stack, R.id.ingredients_widget_text_empty);

        SharedPreferences sharedPref = context.getSharedPreferences(RECIPE_PREF,Context.MODE_PRIVATE);
        String recipeJSON = sharedPref.getString(MY_RECIPE,"");
        Recipe recipe = (Recipe) JsonUtil.getObjectFromJson(recipeJSON,Recipe.class);
        if (recipe != null){
            Log.d(TAG, "recipe name: "+recipe.getName());
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

            // Ingredients
            Intent serviceIntent = new Intent(context, IngredientWidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));

            // Get the Views
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
            views.setRemoteAdapter(R.id.ingredients_widget_stack, serviceIntent);
            views.setEmptyView(R.id.ingredients_widget_stack, R.id.ingredients_widget_text_empty);

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


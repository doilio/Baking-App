package com.doiliomatsinhe.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.doiliomatsinhe.bakingapp.R;
import com.doiliomatsinhe.bakingapp.model.Recipe;
import com.doiliomatsinhe.bakingapp.util.JsonUtil;

import static com.doiliomatsinhe.bakingapp.ui.recipe.RecipeActivity.MY_RECIPE;
import static com.doiliomatsinhe.bakingapp.ui.recipe.RecipeActivity.RECIPE_PREF;
import static com.doiliomatsinhe.bakingapp.ui.recipeDetail.RecipeDetailActivity.MY_INGREDIENTS;

public class IngredientWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientWidgetItemFactory(getApplicationContext(), intent);
    }

    class IngredientWidgetItemFactory implements RemoteViewsFactory {
        private Context context;
        private Recipe recipe;
        private String listOfIngredients;

        IngredientWidgetItemFactory(Context context, Intent intent) {
            this.context = context;
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            // Refresh this data
            SharedPreferences sharedPref = context.getSharedPreferences(RECIPE_PREF, Context.MODE_PRIVATE);
            String recipeJSON = sharedPref.getString(MY_RECIPE, "");
            recipe = (Recipe) JsonUtil.getObjectFromJson(recipeJSON, Recipe.class);
            listOfIngredients = sharedPref.getString(MY_INGREDIENTS, "");
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            // if the recipe isn't null data is set on the widget's views
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_item);
            if (recipe != null){
                views.setTextViewText(R.id.my_widget_title, recipe.getName());
                views.setTextViewText(R.id.my_widget_text, listOfIngredients);
            }
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}

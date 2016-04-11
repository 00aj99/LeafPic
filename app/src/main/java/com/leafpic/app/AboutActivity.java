package com.leafpic.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leafpic.app.Views.ThemedActivity;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;

/**
 * Created by Jibo on 02/03/2016.
 */
public class AboutActivity extends ThemedActivity {

    Toolbar toolbar;

    TextView txtLP;//LEAFPIC
    TextView txtAT;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtLP = (TextView) findViewById(R.id.about_app_title);
        txtAT = (TextView) findViewById(R.id.about_authors_title);
        setNavBarColor();
    }

    @Override
    public void onPostResume() {
        super.onPostResume();
        setTheme();
    }

    public void setTheme(){

        /**** ToolBar *****/
        toolbar.setBackgroundColor(getPrimaryColor());
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(
                new IconicsDrawable(this)
                        .icon(GoogleMaterial.Icon.gmd_arrow_back)
                        .color(Color.WHITE)
                        .sizeDp(19));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setTitle(getString(R.string.about));

        /**** Status Bar */
        setStatusBarColor();

        /**** Nav Bar ****/
        setNavBarColor();

        /**** Recent App */
        setRecentApp(getString(R.string.settings));

        txtAT.setTextColor(getAccentColor());
        txtLP.setTextColor(getAccentColor());

        setThemeOnChangeListener();
    }

    public void setThemeOnChangeListener(){

        /** BackGround **/
        LinearLayout bg = (LinearLayout) findViewById(R.id.about_background);
        bg.setBackgroundColor(getBackgroundColor());

        /** Cards **/
        CardView cvApp = (CardView) findViewById(R.id.about_app_card);
        CardView cvAuthor = (CardView) findViewById(R.id.about_authors_card);

        int color = getCardBackgroundColor();
        cvApp.setBackgroundColor(color);
        cvAuthor.setBackgroundColor(color);

        /** Icons **/
        //ABOUT APP
        IconicsImageView imgAAV = (IconicsImageView) findViewById(R.id.about_version_icon);
        IconicsImageView imgAALL = (IconicsImageView) findViewById(R.id.about_libs_icon);

        //ABOUT AUTHOR
        IconicsImageView imgDonald = (IconicsImageView) findViewById(R.id.about_author_donald_icon);
        IconicsImageView imgGilbert = (IconicsImageView) findViewById(R.id.about_author_gilbert_icon);

        color = getIconColor();
        imgAAV.setColor(color);
        imgAALL.setColor(color);
        imgDonald.setColor(color);
        imgGilbert.setColor(color);

        /** TextViews **/
        TextView txtAV = (TextView) findViewById(R.id.about_version_item);
        TextView txtAL = (TextView) findViewById(R.id.about_libs_item);
        TextView txtDName = (TextView) findViewById(R.id.about_author_donald_item);
        TextView txtGName = (TextView) findViewById(R.id.about_author_gilbert_item);

        color=getTextColor();
        txtAV.setTextColor(color);
        txtAL.setTextColor(color);
        txtDName.setTextColor(color);
        txtGName.setTextColor(color);

        /** Sub Text Views**/
        TextView txtAV_Sub = (TextView) findViewById(R.id.about_version_item_sub);
        TextView txtAL_Sub = (TextView) findViewById(R.id.about_libs_item_sub);
        TextView txtDName_Sub = (TextView) findViewById(R.id.about_author_donald_item_sub);
        TextView txtGName_Sub = (TextView) findViewById(R.id.about_author_gilbert_item_sub);

        color=getSubTextColor();
        txtAV_Sub.setTextColor(color);
        txtAL_Sub.setTextColor(color);
        txtDName_Sub.setTextColor(color);
        txtGName_Sub.setTextColor(color);
    }
}

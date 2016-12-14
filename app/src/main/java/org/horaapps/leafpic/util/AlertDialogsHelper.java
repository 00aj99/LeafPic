package org.horaapps.leafpic.util;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.drew.lang.GeoLocation;

import org.horaapps.leafpic.BuildConfig;
import org.horaapps.leafpic.R;
import org.horaapps.leafpic.activities.base.ThemedActivity;
import org.horaapps.leafpic.model.Media;
import org.horaapps.leafpic.model.base.MediaDetailsMap;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Created by dnld on 19/05/16.
 */
public class AlertDialogsHelper {

    public static  AlertDialog getInsertTextDialog(final ThemedActivity activity, EditText editText, @StringRes int title) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity, activity.getDialogStyle());
        View dialogLayout = activity.getLayoutInflater().inflate(org.horaapps.leafpic.R.layout.dialog_insert_text, null);
        TextView textViewTitle = (TextView) dialogLayout.findViewById(org.horaapps.leafpic.R.id.rename_title);

        ((CardView) dialogLayout.findViewById(org.horaapps.leafpic.R.id.dialog_chose_provider_title)).setCardBackgroundColor(activity.getCardBackgroundColor());
        textViewTitle.setBackgroundColor(activity.getPrimaryColor());
        textViewTitle.setText(title);
        ThemeHelper.setCursorColor(editText, activity.getTextColor());

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(layoutParams);
        editText.setSingleLine(true);
        editText.getBackground().mutate().setColorFilter(activity.getTextColor(), PorterDuff.Mode.SRC_IN);
        editText.setTextColor(activity.getTextColor());

        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editText, null);
        } catch (Exception ignored) { }

        ((RelativeLayout) dialogLayout.findViewById(org.horaapps.leafpic.R.id.container_edit_text)).addView(editText);

        dialogBuilder.setView(dialogLayout);
        return dialogBuilder.create();
    }

    public static AlertDialog getTextDialog(final ThemedActivity activity, @StringRes int title, @StringRes int Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,activity.getDialogStyle());
        View dialogLayout = activity.getLayoutInflater().inflate(org.horaapps.leafpic.R.layout.dialog_text, null);

        TextView dialogTitle = (TextView) dialogLayout.findViewById(org.horaapps.leafpic.R.id.text_dialog_title);
        TextView dialogMessage = (TextView) dialogLayout.findViewById(org.horaapps.leafpic.R.id.text_dialog_message);

        ((CardView) dialogLayout.findViewById(org.horaapps.leafpic.R.id.message_card)).setCardBackgroundColor(activity.getCardBackgroundColor());
        dialogTitle.setBackgroundColor(activity.getPrimaryColor());
        dialogTitle.setText(title);
        dialogMessage.setText(Message);
        dialogMessage.setTextColor(activity.getTextColor());
        builder.setView(dialogLayout);
        return builder.create();
    }

    public static AlertDialog getProgressDialog(final ThemedActivity activity,  String title, String message){
        AlertDialog.Builder progressDialog = new AlertDialog.Builder(activity, activity.getDialogStyle());
        View dialogLayout = activity.getLayoutInflater().inflate(org.horaapps.leafpic.R.layout.dialog_progress, null);
        TextView dialogTitle = (TextView) dialogLayout.findViewById(org.horaapps.leafpic.R.id.progress_dialog_title);
        TextView dialogMessage = (TextView) dialogLayout.findViewById(org.horaapps.leafpic.R.id.progress_dialog_text);

        dialogTitle.setBackgroundColor(activity.getPrimaryColor());
        ((CardView) dialogLayout.findViewById(org.horaapps.leafpic.R.id.progress_dialog_card)).setCardBackgroundColor(activity.getCardBackgroundColor());
        ((ProgressBar) dialogLayout.findViewById(org.horaapps.leafpic.R.id.progress_dialog_loading)).getIndeterminateDrawable().setColorFilter(activity.getPrimaryColor(), android.graphics
                                                                                                                                                                                   .PorterDuff.Mode.SRC_ATOP);

        dialogTitle.setText(title);
        dialogMessage.setText(message);
        dialogMessage.setTextColor(activity.getTextColor());

        progressDialog.setCancelable(false);
        progressDialog.setView(dialogLayout);
        return progressDialog.create();
    }

    public static AlertDialog getDetailsDialog(final ThemedActivity activity, final Media f) {
        AlertDialog.Builder detailsDialogBuilder = new AlertDialog.Builder(activity, activity.getDialogStyle());
        MediaDetailsMap<String, String> mainDetails = f.getMainDetails(activity.getApplicationContext());
        final View dialogLayout = activity.getLayoutInflater().inflate(org.horaapps.leafpic.R.layout.dialog_media_detail, null);
        ImageView imgMap = (ImageView) dialogLayout.findViewById(org.horaapps.leafpic.R.id.photo_map);
        dialogLayout.findViewById(org.horaapps.leafpic.R.id.details_title).setBackgroundColor(activity.getPrimaryColor());
        ((CardView) dialogLayout.findViewById(org.horaapps.leafpic.R.id.photo_details_card)).setCardBackgroundColor(activity.getCardBackgroundColor());

        final GeoLocation location;
        if ((location = f.getGeoLocation()) != null) {
            PreferenceUtil SP = PreferenceUtil.getInstance(activity.getApplicationContext());

            StaticMapProvider staticMapProvider = StaticMapProvider.fromValue(
                    SP.getInt(activity.getString(R.string.preference_map_provider), StaticMapProvider.GOOGLE_MAPS.getValue()));

            Glide.with(activity.getApplicationContext())
                    .load(staticMapProvider.getUrl(location))
                    .asBitmap()
                    .centerCrop()
                    .animate(org.horaapps.leafpic.R.anim.fade_in)
                    .into(imgMap);

            imgMap.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=%d", location.getLatitude(), location.getLongitude(), 17);
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
                }
            });

            imgMap.setVisibility(View.VISIBLE);
            dialogLayout.findViewById(org.horaapps.leafpic.R.id.details_title).setVisibility(View.GONE);

        }

        final TextView showMoreText = (TextView) dialogLayout.findViewById(R.id.details_showmore);
        showMoreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreDetails(dialogLayout, activity, f);
                showMoreText.setVisibility(View.GONE);
            }
        });

        detailsDialogBuilder.setView(dialogLayout);
        loadDetails(dialogLayout,activity, mainDetails);
        return detailsDialogBuilder.create();
    }

    private static void loadDetails(View dialogLayout, ThemedActivity activity, MediaDetailsMap<String, String> metadata) {
        LinearLayout detailsTable = (LinearLayout) dialogLayout.findViewById(R.id.ll_list_details);

        int tenPxInDp = Measure.pxToDp (10, activity);

        for (int index : metadata.getKeySet()) {
            LinearLayout row = new LinearLayout(activity.getApplicationContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setWeightSum(10);

            TextView label = new TextView(activity.getApplicationContext());
            TextView value = new TextView(activity.getApplicationContext());
            label.setText(metadata.getLabel(index));
            label.setLayoutParams((new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3f)));
            value.setText(metadata.getValue(index));
            value.setLayoutParams((new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 7f)));
            label.setTextColor(activity.getTextColor());
            label.setTypeface(null, Typeface.BOLD);
            label.setGravity(Gravity.END);
            label.setTextSize(16);
            value.setTextColor(activity.getTextColor());
            value.setTextSize(16);
            value.setPaddingRelative(tenPxInDp, 0, 0, 0);
            row.addView(label);
            row.addView(value);
            detailsTable.addView(row, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private static void showMoreDetails(View dialogLayout, ThemedActivity activity, Media media) {

        MediaDetailsMap<String, String> metadata = media.getAllDetails();
        loadDetails(dialogLayout ,activity , metadata);
    }

    public static AlertDialog changelogDialog(final ThemedActivity activity) {
        AlertDialog.Builder changelogDialogBuilder = new AlertDialog.Builder(activity, activity.getDialogStyle());
        View dialogLayout = activity.getLayoutInflater().inflate(R.layout.dialog_changelog, null);

        TextView dialogTitle = (TextView) dialogLayout.findViewById(R.id.dialog_changelog_title);
        TextView dialogMessage = (TextView) dialogLayout.findViewById(R.id.dialog_changelog_text);
        CardView cvBackground = (CardView) dialogLayout.findViewById(R.id.dialog_changelog_card);
        ScrollView scrChangelog = (ScrollView) dialogLayout.findViewById(R.id.changelog_scrollview);

        cvBackground.setCardBackgroundColor(activity.getCardBackgroundColor());
        dialogTitle.setBackgroundColor(activity.getPrimaryColor());
        activity.getThemeHelper().setScrollViewColor(scrChangelog);

        dialogTitle.setText(StringUtils.html(String.format(Locale.ENGLISH,"%s <font color='%d'>%s</font>", activity.getString(R.string.changelog), activity.getAccentColor(), BuildConfig.VERSION_NAME )));

        Spanned changelogText = StringUtils.html("<b>#Fixed</b><br/>\n" +
                "        &#8226; Fixed crash on startup and some random crash<br/>\n" +
                "        &#8226; FIied crash opening video (Nougat)<br/>\n" +
                "        &#8226; Fixed zoom out issue with SubScaling ImageView enabled<br/>\n" +
                "        <b>#Update</b><br/>\n" +
                "        &#8226; Updated translations<br/>\n" +
                "        &#8226; General improvements<br/>");
        dialogMessage.setTextColor(activity.getTextColor());
        dialogMessage.setText(changelogText);
        changelogDialogBuilder.setView(dialogLayout);
        return changelogDialogBuilder.create();
    }
}

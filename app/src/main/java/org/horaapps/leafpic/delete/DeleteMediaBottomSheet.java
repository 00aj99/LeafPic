package org.horaapps.leafpic.delete;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import org.horaapps.leafpic.R;
import org.horaapps.leafpic.data.Media;
import org.horaapps.leafpic.data.MediaHelper;
import org.horaapps.leafpic.util.StringUtils;
import org.horaapps.leafpic.util.file.DeleteException;
import org.horaapps.liz.ThemeHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dnld on 11/03/18.
 */

public class DeleteMediaBottomSheet extends BottomSheetDialogFragment {

    public interface DeleteMediaListener {
        void onCompleted();

        void onDeleted(Media media);
    }


    public static final String TAG = "delete_bootomsheet";
    public static final String EXTRA_MEDIA = "media";

    @BindView(R.id.header)
    RelativeLayout header;

    @BindView(R.id.cancel_delete)
    AppCompatButton cancelButton;

    @BindView(R.id.delete_progress_bar)
    DonutProgress progress;

    @BindView(R.id.txt_errors)
    TextView txtErrors;

    private ArrayList<Media> media;

    DeleteMediaListener listener;
    boolean cancelRequested = false;

    public void setListener(DeleteMediaListener listener) {
        this.listener = listener;
    }

    public static DeleteMediaBottomSheet make(ArrayList<Media> media, DeleteMediaListener listener) {
        DeleteMediaBottomSheet deleteMediaBottomSheet = new DeleteMediaBottomSheet();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(EXTRA_MEDIA, media);
        deleteMediaBottomSheet.setArguments(bundle);
        deleteMediaBottomSheet.setCancelable(false);
        deleteMediaBottomSheet.setListener(listener);
        return deleteMediaBottomSheet;
    }

    private void setProgress(int p) {
        progress.setProgress(p);
        // TODO: 06/04/18 use string resource when merged in dev
        progress.setText(String.format(Locale.ENGLISH, "%d/%d", p, progress.getMax()));
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        ThemeHelper th = ThemeHelper.getInstanceLoaded(getContext());


        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_delete_media, null, false);
        ButterKnife.bind(this, view);
        view.setBackgroundColor(th.getBackgroundColor());

        header.setBackgroundColor(th.getPrimaryColor());

        txtErrors.setTextColor(th.getTextColor());
        progress.setFinishedStrokeColor(th.getAccentColor());
        progress.setTextColor(th.getTextColor());
        progress.setMax(media.size());
        setProgress(0);

        dialog.setContentView(view);
    }

    private void showErrors(HashSet<String> errors) {
        StringBuilder b = new StringBuilder();
        b.append("<b>").append("Errors:").append("</b>").append("<br/>");

        for (String error : errors)
            b.append("<i>").append(" - ").append(error).append("</i>").append("<br/>");

        txtErrors.setText(StringUtils.html(b.toString()));
        txtErrors.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);


    }

    @OnClick(R.id.cancel_delete)
    void cancelDelete() {
        Log.wtf(TAG, "delete stop");
        cancelRequested = true;
        listener.onCompleted();
        dismiss();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        media = getArguments().getParcelableArrayList(EXTRA_MEDIA);

        HashSet<String> errors = new HashSet<>(0);
        Disposable end = MediaHelper.deleteMedia(getContext(), media)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .takeUntil(m -> !cancelRequested)
                .subscribe(
                        m -> {
                            listener.onDeleted(m);
                            setProgress((int) (progress.getProgress() + 1));
                        },
                        throwable -> {
                            setProgress((int) (progress.getProgress() + 1));

                            if (throwable instanceof DeleteException) {
                                errors.add(((DeleteException) throwable).getMedia().getPath());
                            } else {
                                errors.add(throwable.getLocalizedMessage());
                            }

                        },
                        () -> {
                            setCancelable(true);
                            listener.onCompleted();
                            if (errors.size() == 0)
                                //Log.wtf("asd","ads");
                                dismiss();
                            else {
                                showErrors(errors);
                            }

                        }
                );


    }
}

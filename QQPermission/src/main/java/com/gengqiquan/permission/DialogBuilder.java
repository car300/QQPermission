package com.gengqiquan.permission;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by 耿 on 2016/7/11.
 */
class DialogBuilder {
    private Context context;
    private View mLayout;
    private boolean mCancelable = false;
    Dialog dialog;

    public DialogBuilder(Context context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayout = inflater.inflate(R.layout.new_permission_dialog_layout, null);
        initColor(context);

    }

    private void initColor(Context context) {
        String pn = context.getPackageName();
        TextView tvApply = mLayout.findViewById(R.id.tv_apply);
        TextView tvSetting = mLayout.findViewById(R.id.tv_setting);
        TextView tvSure = mLayout.findViewById(R.id.tv_sure);
        tvApply.setBackgroundResource(R.drawable.bg_btn_settings);
        tvSetting.setBackgroundResource(R.drawable.bg_btn_settings);
        tvSure.setBackgroundResource(R.drawable.bg_btn_settings);
    }

    public DialogBuilder setSureOnClickListener(View.OnClickListener listener) {
        ((TextView) mLayout.findViewById(R.id.tv_sure)).setOnClickListener(listener);
        return this;
    }

    public DialogBuilder setCancelOnClickListener(View.OnClickListener listener) {
        ((ImageView) mLayout.findViewById(R.id.iv_close_dialog)).setOnClickListener(listener);
        return this;
    }

    public DialogBuilder setSettingOnClickListener(View.OnClickListener listener) {
        ((TextView) mLayout.findViewById(R.id.tv_setting)).setOnClickListener(listener);
        return this;
    }

    public DialogBuilder setApplyOnClickListener(View.OnClickListener listener) {
        ((TextView) mLayout.findViewById(R.id.tv_apply)).setOnClickListener(listener);
        return this;
    }

    public DialogBuilder message(SpannableStringBuilder message) {
        ((TextView) mLayout.findViewById(R.id.tv_hint_message)).setText(message);
        return this;
    }

    public DialogBuilder showApply() {
        ((TextView) mLayout.findViewById(R.id.tv_apply)).setVisibility(View.VISIBLE);
        ((TextView) mLayout.findViewById(R.id.tv_setting)).setVisibility(View.GONE);
        ((TextView) mLayout.findViewById(R.id.tv_sure)).setVisibility(View.GONE);
        return this;
    }

    public DialogBuilder showSure() {
        ((TextView) mLayout.findViewById(R.id.tv_apply)).setVisibility(View.GONE);
        TextView tvSettings = mLayout.findViewById(R.id.tv_setting);
        tvSettings.setBackgroundResource(R.drawable.bg_btn_settings_stroke);
        tvSettings.setTextColor(tvSettings.getContext().getResources().getColor(R.color.colorPrimary));
        tvSettings.setVisibility(View.VISIBLE);

        TextView tvSure = mLayout.findViewById(R.id.tv_sure);
        tvSure.setVisibility(View.VISIBLE);

        return this;
    }

    public DialogBuilder showSetting() {
        ((TextView) mLayout.findViewById(R.id.tv_apply)).setVisibility(View.GONE);
        TextView tvSettings = mLayout.findViewById(R.id.tv_setting);
        tvSettings.setVisibility(View.VISIBLE);
        tvSettings.setTextColor(Color.WHITE);
        tvSettings.setBackgroundResource(R.drawable.bg_btn_settings);
        ((TextView) mLayout.findViewById(R.id.tv_sure)).setVisibility(View.GONE);
        return this;
    }

    public void shake() {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.qq_permission_shake_anim);
        mLayout.findViewById(R.id.tv_hint_message).startAnimation(anim);
    }


    public Dialog builder() {
        dialog = new Dialog(context, R.style.qq_permission_dialog_style);
        dialog.setCancelable(mCancelable);
        dialog.setContentView(mLayout);
        return dialog;
    }
}

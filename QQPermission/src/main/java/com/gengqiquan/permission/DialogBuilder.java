package com.gengqiquan.permission;

import android.app.Dialog;
import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
        String hint = "请前往手机的<font color='#333333'><strong>“设置-应用信息-权限”</strong></font>中开启权限否则您将无法使用该功能";
        ((TextView)mLayout.findViewById(R.id.tv_hint_message)).setText(Html.fromHtml(hint));
    }

    public DialogBuilder setCancelOnClickListener(View.OnClickListener listener) {
        mLayout.findViewById(R.id.iv_close_dialog).setOnClickListener(listener);
        return this;
    }

    public DialogBuilder setSettingOnClickListener(View.OnClickListener listener) {
        ((TextView) mLayout.findViewById(R.id.tv_setting)).setOnClickListener(listener);
        return this;
    }

    public DialogBuilder message(String message) {
        ((TextView) mLayout.findViewById(R.id.tv_message)).setText(message);
        return this;
    }

    public DialogBuilder message(Spanned message) {
        ((TextView) mLayout.findViewById(R.id.tv_message)).setText(message);
        return this;
    }

    public DialogBuilder message(SpannableString message) {
        ((TextView) mLayout.findViewById(R.id.tv_message)).setText(message);
        return this;
    }

    public DialogBuilder showSetting() {
        ((TextView) mLayout.findViewById(R.id.tv_setting)).setVisibility(View.VISIBLE);
        return this;
    }

    public void shake() {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.qq_permission_shake_anim);
        mLayout.findViewById(R.id.tv_message).startAnimation(anim);
    }

    public Dialog builder() {
        dialog = new Dialog(context, R.style.qq_permission_dialog_style);
        dialog.setCancelable(mCancelable);
        dialog.setContentView(mLayout);
        return dialog;
    }
}

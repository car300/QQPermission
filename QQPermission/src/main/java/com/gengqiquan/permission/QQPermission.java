package com.gengqiquan.permission;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Toast;

import androidx.collection.ArraySet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.gengqiquan.permission.permission_data.PermissionHandler;
import com.gengqiquan.utils.SpanUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by gengqiquan on 2018/10/10.
 */

/**
 * 1、showTips =TRUE ：
 * 拒绝后显示提示弹框
 * 2、showTips =false：
 * 拒绝不显示提示弹框
 * 若勾选了系统权限申请弹框中的不再提示，则以后再次申请直接显示提示弹框
 *
 * @author gengqiquan
 * @date 2018/10/17 下午3:29
 */
public class QQPermission {

    public static Builder with(Activity t, String... p) {
        return new Builder(t, p);
    }

    public static class Builder {
        Activity activity;
        String[] permissions;
        IResult result;
        TipsProxy tipsProxy;
        boolean showTips = true;
        boolean silence = false;
        Request request;

        private Builder(Activity t, String[] p) {
            activity = t;
            permissions = p;
            request = new Request(permissions);
        }

        /**
         * 弹框文案显示代理-"不对外提供了"
         *
         * @author gengqiquan
         * @date 2018/10/18 下午4:32
         */
        @Deprecated()
        private Builder tipsProxy(TipsProxy tipsProxy) {
            this.tipsProxy = tipsProxy;
            return this;
        }

        /**
         * 拒绝后显示自定义弹框
         *
         * @author gengqiquan
         * @date 2018/10/18 下午4:31
         */
        public Builder hideTips() {
            this.showTips = false;
            return this;
        }

        /**
         * 静默申请
         *
         * @author gengqiquan
         * @date 2018/10/18 下午4:31
         */

        public Builder silence() {
            this.silence = true;
            return this;
        }

        public void requestPermissions() {
            requestPermissions(new QQResult(null, null));
        }

        public void requestPermissions(Func1 func1) {
            requestPermissions(new QQResult(func1, null));
        }

        public void requestPermissions(Func1 func1, Func2 func2) {
            requestPermissions(new QQResult(func1, func2));
        }

        public void requestPermissions(final IResult r) {
            requestPermissions(r, true);
        }

        @SuppressLint("NewApi")
        private void requestPermissions(final IResult r, boolean first) {
            result = r;
            if (tipsProxy == null) {
                tipsProxy = new TipsProxy() {

                    @Override
                    public SpanUtil.SpanBuilder makeTrueText(Set<PermissionInfo> infos, SpanUtil.SpanBuilder builder) {
                        Iterator<PermissionInfo> iterator = infos.iterator();
                        ArrayList<String> list = new ArrayList<>();
                        while (iterator.hasNext()) {
                            String desc = PermissionHandler.getPermissionDescription(iterator.next().name);
                            if (!list.contains(desc)) {
                                list.add(desc);
                            }
                        }
                        String message = list.stream().collect(Collectors.joining("、"));
                        builder.addStyleSection(message, Typeface.BOLD);
                        return builder;
                    }
                };
            }
            if (permissions == null) {
                throw new RuntimeException("permission can not be null");
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                result.permit();
                return;
            }

            boolean need = false;
            againList.clear();
            for (int i = 0, l = permissions.length; i < l; i++) {
                int p = PermissionChecker.checkSelfPermission(activity, permissions[i]);
                if (p != PackageManager.PERMISSION_GRANTED) {
                    need = true;
                }
                // 只有在申请了该权限但用户拒绝了返回true 其他均为false
                boolean again = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i]);
                if (!again) {
                    againList.add(permissions[i]);
                }
            }
            //申请的权限全部允许了,走permit
            if (!need) {
                result.permit();
                return;
            }

            for (String permission : permissions) {
                apply(activity, permission);
            }

            request.subscribe(new Observer() {
                @Override
                public void update(Map<String, Boolean> o) {
                    if (checkPermit(o)) {
                        result.permit();
                    }
                }
            });
            final QQFragment appFragment = new QQFragment();
            appFragment.setRequest(request);
            activity.getFragmentManager()
                    .beginTransaction().add(android.R.id.content, appFragment)
                    .commitAllowingStateLoss();
        }

        Dialog dialog;
        @SuppressLint("NewApi")
        List<String> refuseList = new ArrayList<>();
        List<String> againList = new ArrayList<>();

        boolean checkPermit(Map<String, Boolean> map) {
            if (!map.containsValue(false)) {
                return true;
            }

            if (dialog == null) {
                createDialog();
            }
            changeMessage(map);

            boolean showUI = false;
            for (String ps : refuseList) {
                // note 2020-12-29: 只有在申请了该权限但用户拒绝了返回true 其他均为false
                //again：false 1.为首次申请 2.被拒绝且勾选了不再提示（系统申请弹框） 3.已经允许了该权限
                boolean again = ActivityCompat.shouldShowRequestPermissionRationale(activity, ps);

                showUI = applyTimes(activity, ps) > 1 && againList.contains(ps) && !again;

                if (showUI) {  //当前非首次申请且申请前是false，申请后也是false ，说明不是本次申请勾选的不再提示，而是之前勾选的。
                    break;
                }
            }
            if (silence) {//静默申请，如获取通讯录这种
                result.refuse(refuseList);
                return false;
            }

            if (showUI) {
                // 此时需要弹框告知用户为啥需要权限并引导用户去设置打开，
                // 否则用户可能点击后无任何界面反应或者界面直接打开就关闭了
                if (!dialog.isShowing() && !activity.isFinishing()) {
                    dialogBuilder.showSetting();
                    dialog.show();
                }
                return false;
            }
            if (showTips) {//需要弹框告知用户为啥需要权限(强制弹框，例如在相机界面里面的申请，无二次提示界面没任何反应可能就关闭了)
                if (!dialog.isShowing() && !activity.isFinishing()) {
                    boolean again = false;
                    for (String ps : refuseList) {
                        again = ActivityCompat.shouldShowRequestPermissionRationale(activity, ps);
                        again = !again && applyTimes(activity, ps) > 1;
                        if (again) {
                            break;
                        }
                    }
                    if (again) {//勾选了不再提示，直接显示设置界面
                        dialogBuilder.showSetting();
                    } else {
                        dialogBuilder.showApply();
                    }
                    dialog.show();
                }
                return false;

            }

            if (dialog.isShowing() && !activity.isFinishing()) {
                dialog.dismiss();
            }
            result.refuse(refuseList);
            return false;
        }

        private void changeMessage(Map<String, Boolean> map) {
            if (!refuseList.isEmpty()) {
                refuseList.clear();
            }
            PackageManager pm = activity.getPackageManager();
            Set<PermissionInfo> permissionInfoSet = new TreeSet<>(new Comparator<PermissionInfo>() {
                @Override
                public int compare(PermissionInfo o1, PermissionInfo o2) {
                    return o1.name.equals(o2.name) ? 0 : -1;
                }
            });

            for (Map.Entry<String, Boolean> entry : map.entrySet()) {
                String key = entry.getKey();
                boolean value = entry.getValue();
                if (!value) {
                    refuseList.add(key);
                    try {
                        PermissionInfo n = pm.getPermissionInfo(key, 0);
                        permissionInfoSet.add(n);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            SpanUtil.SpanBuilder spanBuilder = SpanUtil.create();
            spanBuilder.addSection("请前往手机的“设置-应用信息-权限”去开启");
            tipsProxy.makeTrueText(permissionInfoSet, spanBuilder);
            spanBuilder.addSection("，否则您将无法使用该功能。");
            dialogBuilder.message(spanBuilder.getSpanStrBuilder());
        }

        DialogBuilder dialogBuilder;

        void createDialog() {
            dialogBuilder = new DialogBuilder(activity)
                    .setApplyOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            requestPermissions(result);
                        }
                    })
                    .setSureOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, Boolean> map = new LinkedHashMap<>();
                            for (int i = 0, l = permissions.length; i < l; i++) {
                                int p = ContextCompat.checkSelfPermission(activity, permissions[i]);
                                map.put(permissions[i], p == PackageManager.PERMISSION_GRANTED);
                            }
                            if (!map.containsValue(false)) {
                                dialog.dismiss();
                                result.permit();
                                return;
                            }
                            changeMessage(map);
                            dialogBuilder.shake();
                        }
                    })
                    .setCancelOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            result.refuse(refuseList);
                        }
                    }).setSettingOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            try {
                                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                intent.setData(uri);
                                activity.startActivity(intent);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialogBuilder.showSure();
                                    }
                                }, 1000);
                            } catch (Exception e) {
                                Toast.makeText(activity, "找不到设置页，请手动进入界面", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            dialog = dialogBuilder.builder();
        }
    }


    private static SharedPreferences getDefaultSharedPreferences(Context context) {
        return context.getApplicationContext().getSharedPreferences(
                "QQPermission", Context.MODE_PRIVATE);
    }

    private static void apply(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        int times = sharedPreferences.getInt(key, 0);
        edit.putInt(key, ++times);
        edit.apply();
    }

    private static int applyTimes(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(key, 0);
    }
}

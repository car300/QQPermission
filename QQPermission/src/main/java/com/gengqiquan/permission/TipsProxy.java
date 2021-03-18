package com.gengqiquan.permission;

import android.content.pm.PermissionInfo;
import android.text.SpannableStringBuilder;

import com.gengqiquan.utils.SpanUtil;

import java.util.Set;

/**
 * Created by gengqiquan on 2018/10/12.
 */

public interface TipsProxy {
    /**
     * 权限组功能高版本没用了，针对每个权限进行更精细化处理
     *
     * @param infos
     * @param builder
     * @return
     */
    SpanUtil.SpanBuilder makeTrueText(Set<PermissionInfo> infos, SpanUtil.SpanBuilder builder);
}

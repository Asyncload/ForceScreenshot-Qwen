package com.lbxq.screen;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

public class XscreenModule implements IXposedHookLoadPackage {

    // 包名 -> 中文名称映射
    private static final Map<String, String> PACKAGE_NAMES = new HashMap<>();

    static {
        PACKAGE_NAMES.put("com.tencent.mm", "微信");
        PACKAGE_NAMES.put("com.tencent.mobileqq", "QQ");
        PACKAGE_NAMES.put("com.eg.android.AlipayGphone", "支付宝");
        PACKAGE_NAMES.put("com.ss.android.ugc.aweme", "抖音");
        PACKAGE_NAMES.put("com.sankuai.meituan", "美团");
        PACKAGE_NAMES.put("com.sankuai.meituan.dispatch.crowdsource.delivery", "美团众包");
        PACKAGE_NAMES.put("me.ele.crowdsource", "蜂鸟众包");
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        String pkg = lpparam.packageName;
        // 获取中文名称，如果不在映射中则返回包名本身
        String appName = PACKAGE_NAMES.getOrDefault(pkg, pkg);

        // 目标应用列表
        if (!PACKAGE_NAMES.containsKey(pkg)) {
            return;
        }

        // 启动检测日志（带中文名）
        XposedBridge.log("[Xscreen] 已检测到: " + appName + " (" + pkg + ")启动，测试完成✅");

        // Hook setFlags 移除 FLAG_SECURE
        XposedHelpers.findAndHookMethod(
            "android.view.Window",
            lpparam.classLoader,
            "setFlags",
            int.class, int.class,
            new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) {
                    int flags = (int) param.args[0];
                    if ((flags & WindowManager.LayoutParams.FLAG_SECURE) != 0) {
                        param.args[0] = flags & ~WindowManager.LayoutParams.FLAG_SECURE;
                        // 解除截图限制日志（带中文名）
                        XposedBridge.log("[Xscreen] : " + appName + " (" + pkg + ")已被提取权限，任务完成！🎉");
                    }
                }
            }
        );
    }
}

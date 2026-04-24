package com.lbxq.screen;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

// LSPosed 模块入口，兼容 API 101
public class LSPosedModule implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals("com.android.systemui")) {
            XposedBridge.log("Hook 到 SystemUI，准备注入截图功能");
            // 在这里可以 hook 系统方法实现强制截图
        }
    }
}

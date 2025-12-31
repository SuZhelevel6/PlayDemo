package com.suzhe.plugin

/**
 * BuildInfo 插件配置扩展
 */
class BuildInfoExtension {
    /** 生成的类名 */
    String className = "BuildInfo"

    /** 生成的包名 */
    String packageName = ""

    /** 是否包含 Git 信息 */
    boolean includeGitInfo = true

    /** 是否包含构建时间 */
    boolean includeBuildTime = true

    /** 是否包含构建用户 */
    boolean includeBuildUser = true
}

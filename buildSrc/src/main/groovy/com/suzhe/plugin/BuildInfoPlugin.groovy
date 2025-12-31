package com.suzhe.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import java.text.SimpleDateFormat

/**
 * BuildInfo 插件
 * 在编译时自动生成包含 Git 信息、构建时间等信息的 Kotlin 类
 */
class BuildInfoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        // 创建扩展配置
        def extension = project.extensions.create("buildInfo", BuildInfoExtension)

        // 确保是 Android 项目
        project.plugins.withId("com.android.application") {
            project.afterEvaluate {
                // 通过反射获取 Android 扩展
                def android = project.extensions.findByName("android")
                def applicationId = android?.defaultConfig?.applicationId ?: "com.example"
                def packageName = extension.packageName ?: applicationId
                def outputDir = new File(project.buildDir, "generated/source/buildinfo")

                // 注册生成任务
                def generateTask = project.tasks.register("generateBuildInfo") { task ->
                    task.group = "build"
                    task.description = "Generate BuildInfo class with git and build information"
                    task.outputs.dir(outputDir)

                    task.doLast {
                        generateBuildInfoClass(project, outputDir, packageName, extension)
                    }
                }

                // 将生成的目录添加到源码集
                android?.sourceSets?.main?.java?.srcDir(outputDir)

                // 确保在编译前执行
                project.tasks.named("preBuild").configure { task ->
                    task.dependsOn(generateTask)
                }
            }
        }
    }

    private void generateBuildInfoClass(Project project, File outputDir, String packageName, BuildInfoExtension extension) {
        def packageDir = new File(outputDir, packageName.replace(".", "/"))
        packageDir.mkdirs()

        def classFile = new File(packageDir, "${extension.className}.kt")

        def content = new StringBuilder()
        content.append("package ${packageName}\n\n")
        content.append("/**\n")
        content.append(" * 构建信息（由 BuildInfoPlugin 自动生成）\n")
        content.append(" * 请勿手动修改此文件\n")
        content.append(" */\n")
        content.append("object ${extension.className} {\n")

        if (extension.includeGitInfo) {
            def gitHash = execGitCommand(project, "rev-parse", "--short", "HEAD")
            def gitBranch = execGitCommand(project, "rev-parse", "--abbrev-ref", "HEAD")
            def gitTag = execGitCommand(project, "describe", "--tags", "--exact-match")
            def isClean = execGitCommand(project, "status", "--porcelain").isEmpty()

            content.append("    /** Git 提交哈希（短） */\n")
            content.append("    const val GIT_HASH: String = \"${gitHash}\"\n\n")
            content.append("    /** Git 分支名 */\n")
            content.append("    const val GIT_BRANCH: String = \"${gitBranch}\"\n\n")
            content.append("    /** Git 标签（如果有） */\n")
            content.append("    const val GIT_TAG: String = \"${gitTag}\"\n\n")
            content.append("    /** 工作区是否干净 */\n")
            content.append("    const val GIT_CLEAN: Boolean = ${isClean}\n\n")
        }

        if (extension.includeBuildTime) {
            def buildTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
            content.append("    /** 构建时间 */\n")
            content.append("    const val BUILD_TIME: String = \"${buildTime}\"\n\n")
        }

        if (extension.includeBuildUser) {
            def buildUser = System.getProperty("user.name") ?: "unknown"
            content.append("    /** 构建用户 */\n")
            content.append("    const val BUILD_USER: String = \"${buildUser}\"\n\n")
        }

        content.append("    /** 构建类型描述 */\n")
        content.append("    val BUILD_DESC: String\n")
        content.append("        get() = buildString {\n")
        if (extension.includeGitInfo) {
            content.append("            append(GIT_BRANCH)\n")
            content.append("            append(\"@\")\n")
            content.append("            append(GIT_HASH)\n")
            content.append("            if (!GIT_CLEAN) append(\"*\")\n")
        }
        if (extension.includeBuildTime) {
            content.append("            append(\" (\")\n")
            content.append("            append(BUILD_TIME)\n")
            content.append("            append(\")\")\n")
        }
        content.append("        }\n")
        content.append("}\n")

        classFile.text = content.toString()
        project.logger.lifecycle("Generated ${extension.className}.kt in ${packageName}")
    }

    private String execGitCommand(Project project, String... args) {
        try {
            def command = ["git"] + args.toList()
            def process = new ProcessBuilder(command)
                .directory(project.rootDir)
                .start()

            def output = process.inputStream.text.trim()
            def exitCode = process.waitFor()

            // 如果命令执行失败，返回空字符串
            if (exitCode != 0) {
                return ""
            }
            return output
        } catch (Exception e) {
            return ""
        }
    }
}

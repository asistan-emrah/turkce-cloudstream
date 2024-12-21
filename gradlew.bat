@IF EXIST "%~dp0\gradle-wrapper.jar" (
    java -cp "%~dp0gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*
) ELSE (
    echo gradle-wrapper.jar not found
)

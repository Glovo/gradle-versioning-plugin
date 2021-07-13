def call(pipelineParams) {
    stage('SonarQube') {
        try {
            withSonarQubeEnv('SonarCloud') {
                sh "./gradlew -s jacocoTestReport sonarqube"
            }

        } finally {
            junit "**/build/test-results/**/*.xml"
        }
    }
    stage('Build Library') {
        sh "./gradlew -s :library:build"
    }
    stage('Build Gradle Plugin') {
        sh "./gradlew -s :plugin:build"
    }
    stage('Build Gradle Plugin for Android') {
        sh "./gradlew -s :plugin-android:build"
    }
}

return this
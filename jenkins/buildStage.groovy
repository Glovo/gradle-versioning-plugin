def call(pipelineParams) {
    stage('Build Library') {
        try {
            sh "./gradlew -s :library:build"
        } finally {
            junit "**/build/test-results/**/*.xml"
        }
    }
    stage('Build Gradle Plugin') {
        try {
            sh "./gradlew -s :plugin:build"
        } finally {
            junit "**/build/test-results/**/*.xml"
        }
    }
    stage('Build Gradle Plugin for Android') {
        try {
            sh "./gradlew -s :plugin-android:build"
        } finally {
            junit "**/build/test-results/**/*.xml"
        }
    }
    stage('Build publications') {
        try {
            sh "./gradlew -s build publishAllPublicationsToLocal"
        } finally {
            junit "**/build/test-results/**/*.xml"
        }
    }
}

return this
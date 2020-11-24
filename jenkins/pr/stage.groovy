def call(pipelineParams, stageConfig, stageParams, input) {

    enforceMostRecentPRBuild()

    withAgent(pipelineParams.mainAgent) {
        stage('Build plugins') {
            try {
                sh "./gradlew --stacktrace clean build"
            }
            finally {
                junit "**/build/test-results/**/*.xml"
            }
        }
        stage('Build Demos') {
            dir('demo/android') {
                sh "./gradlew --stacktrace clean build"
            }
        }
    }
}

private void enforceMostRecentPRBuild() {
    def buildNumber = env.BUILD_NUMBER as int
    milestone(buildNumber - 1)
    milestone(buildNumber)
}

return this

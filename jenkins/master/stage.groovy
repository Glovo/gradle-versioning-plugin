def call(pipelineParams, stageConfig, stageParams, input) {
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
        stage('Publish SDK artifacts') {
            sh "./gradlew --stacktrace artifactoryPublish --debug"
        }
        
    }
}

return this

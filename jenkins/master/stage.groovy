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

        def shouldPublish = (pipelineParams.jobParams.parametersCurrentValues.Publish == 'Yes')
        if(shouldPublish) {
            stage('Publish SDK artifacts') {
                sh "./gradlew --stacktrace artifactoryPublish --debug"
            }
        }
        
    }
}

return this

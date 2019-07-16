def call(pipelineParams, stageConfig, stageParams) {

    withAgent(pipelineParams.mainAgent) {
        stage('Build') {
            checkout scm
            dir('demo/android') {
                echo 'Start build'
                sh './gradlew --no-daemon :app:assemble'
                echo 'Build finished'
            }
        }
    }

}

return this

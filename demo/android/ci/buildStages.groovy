def call(pipelineParams) {

    ecsAgent(pipelineParams.lightAgent) {
        stage('Build') {
            echo 'Build started'
            echo 'Build finished'
        }
    }

}

return this

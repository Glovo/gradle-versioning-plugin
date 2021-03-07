def call(pipelineParams, stageConfig, stageParams, input) {
    withAgent(pipelineParams.mainAgent) {
        def buildStage = load("jenkins/buildStage.groovy")
        buildStage(pipelineParams)
    }
}

return this

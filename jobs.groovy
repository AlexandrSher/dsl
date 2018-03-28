job("TCHILD_1") {
	description()
	keepDependencies(false)
	scm {
		git {
			remote {
				github("AlexandrSher/dsl", "https")
			}
			branch("*/master")
		}
	}
	disabled(false)
	concurrentBuild(false)
	steps {
		shell("""./script.sh > output.txt
tar czvf \$BUILD_TAG.tar.gz output.txt jobs.groovy 
mv \$BUILD_TAG.tar.gz /var/lib/jenkins/workspace/start_dsl/""")
	}
}

job("TMAIN") {
	description()
	keepDependencies(false)
	parameters {
		choiceParam("branch", ["master", "achernak"], "")
	}
	scm {
		git {
			remote {
				github("AlexandrSher/dsl", "https")
			}
			branch("\$branch")
		}
	}
	disabled(false)
	concurrentBuild(false)
	steps {
		downstreamParameterized {
			trigger("") {
				block {
					buildStepFailure("FAILURE")
					unstable("UNSTABLE")
					failure("FAILURE")
				}
			}
		}
	}
}

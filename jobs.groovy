def jobs = (1..4)
def childlist = []
jobs.each {childlist.add("CHILD_$it")}

job("MAIN") {
        description("This is main")
        parameters {choiceParam("branch", ["achernak", "master"], "")
        activeChoiceParam('BUILDS_TRIGGER') {
           description('Choose child builds')
           choiceType('CHECKBOX')
                groovyScript {script("$childlist")}}}
        scm {github("AlexandrSher/dsl", "\$branch")}
        disabled(false)
        concurrentBuild(false)
        childlist.each {
        steps {downstreamParameterized {
                        trigger("$it") {
                                block { buildStepFailure("FAILURE")
                                        unstable("FAILURE")
                                        failure("UNSTABLE")}
                                parameters {predefinedProp('branch', '${branch}')}}}}}}

childlist.each {
job("$it") {
        description()
        keepDependencies(false)
        scm {github("AlexandrSher/dsl", "\$branch")}
        disabled(false)
        concurrentBuild(false)
        steps {shell("""chmod +x script.sh
bash -ex script.sh > output.txt
tar czvf $BUILD_TAG.tar.gz output.txt jobs.groovy
mv $BUILD_TAG.tar.gz /var/lib/jenkins/workspace/MAIN/""")}}}

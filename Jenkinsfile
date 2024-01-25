pipeline  {
    agent any

    tools {
        jdk 'OpenJDK17'
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    stages {
        stage('Main branch release') {
            when { 
                branch 'main' 
            }
            steps {
                echo "I am building on ${env.BRANCH_NAME}"
                sh "./gradlew clean build release -Drelease.dir=$JENKINS_HOME/repo.gecko/release/org.gecko.persistence --info --stacktrace -Dmaven.repo.local=${WORKSPACE}/.m2"
            }
        }
        stage('Snapshot branch release') {
            when { 
                branch 'snapshot'
            }
            steps  {
                echo "I am building on ${env.JOB_NAME}"
                sh "./gradlew clean release --info --stacktrace -Dmaven.repo.local=${WORKSPACE}/.m2"
                sh "mkdir -p $JENKINS_HOME/repo.gecko/snapshot/org.gecko.persistence"
                sh "rm -rf $JENKINS_HOME/repo.gecko/snapshot/org.gecko.persistence/*"
                sh "cp -r cnf/release/* $JENKINS_HOME/repo.gecko/snapshot/org.gecko.persistence"
            }
        }
        stage('Gitlab branch release') {
            when { 
                branch 'gitlab'
            }
            steps  {
                echo "I am building on ${env.JOB_NAME}"
                sh "./gradlew clean build release --info --stacktrace -Dmaven.repo.local=${WORKSPACE}/.m2"
                sh "mkdir -p $JENKINS_HOME/repo.gecko/snapshot/org.gecko.emf.persistence_gitlab"
                sh "rm -rf $JENKINS_HOME/repo.gecko/snapshot/org.gecko.emf.persistence_gitlab/*"
                sh "cp -r cnf/release/* $JENKINS_HOME/repo.gecko/snapshot/org.gecko.emf.persistence_gitlab"
            }
        }
        stage('Main Gitlab branch release') {
            when { 
                branch 'gitlab_main' 
            }
            steps {
                echo "I am building on ${env.BRANCH_NAME}"
                sh "./gradlew clean build release -x testOSGi -Drelease.dir=$JENKINS_HOME/repo.gecko/release/org.gecko.emf.persistence --info --stacktrace -Dmaven.repo.local=${WORKSPACE}/.m2"
            }
        }
    }

}

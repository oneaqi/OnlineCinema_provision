pipeline {
   agent any
    environment {
        gpg_passphrase = credentials("gpg-passphrase")
        dockerImage = '';
        registryCredential = "dockerCredential"
    }

   stages {
    stage('SCM'){
            steps {
                git credentialsId: 'a55fbd81-8017-4f19-97d8-71947b4caf8e', url: 'https://github.com/oneaqi/OnlineCinema_provision.git'
                sh "git secret reveal -p '$gpg_passphrase' "
                
            }
    }
    
    stage('Building image'){
      steps{
        script{
          dockerImage = docker.build "yuqidockerid/cinema"
        }
}
}

    stage('Deploy image'){
      steps{
        script{
        docker.withRegistry('',registryCredential){dockerImage.push()}
        }
}
}

    stage('Launching'){
        steps{
            sh "ansible-playbook --private-key=/admin-key-pair.pem aws_config.yml"        
        }
    }

    stage('Set test IP'){
        steps{
            sh "ansible-playbook --private-key=/admin-key-pair.pem edit_ip_jmeter.yml"        
        }
    }
    
    stage('Test'){
        steps{
            sh "/home/yzhang/apache-jmeter-5.2/bin/jmeter -n -t cinema.jmx -l Yuqi_cinema_report.xml"        
       }
    }
    
   // stage('Terminating'){
       // steps{
        //    sh "ansible-playbook --private-key=/admin-key-pair.pem aws_terminate.yml"        
       // }
   // }    

    stage('Production'){
        steps{
            sh "ansible-playbook --private-key=/admin-key-pair.pem aws_production.yml"    
        }
    }

}
}

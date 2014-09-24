grails.project.dependency.distribution = {
  remoteRepository(id:"nectar-nexus-repo-snapshot", url:"http://130.56.249.242/nexus/content/repositories/snapshots") {
    authentication username: System.getenv("TRAVIS_DEPLOY_USERNAME"), password: System.getenv("TRAVIS_DEPLOY_PASSWORD")
  }

  remoteRepository(id:"nectar-nexus-repo-release",  url:"http://130.56.249.242/nexus/content/repositories/releases") {
    authentication username: System.getenv("TRAVIS_DEPLOY_USERNAME"), password: System.getenv("TRAVIS_DEPLOY_PASSWORD")
  }
}

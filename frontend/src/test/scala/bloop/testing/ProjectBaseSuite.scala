package bloop.testing

import java.nio.file.Files
import bloop.io.{AbsolutePath, Paths}
import bloop.logging.RecordingLogger

class ProjectBaseSuite(buildName: String) extends BaseSuite {
  val workspace = AbsolutePath(Files.createTempDirectory(s"workspace-${buildName}"))
  val build: TestBuild = {
    val logger = new RecordingLogger(ansiCodesSupported = false)
    loadBuildFromResources("cross-test-build-0.6", workspace, logger)
  }

  def testProject(name: String)(fun: (TestBuild, RecordingLogger) => Any): Unit = {
    val newLogger = new RecordingLogger(ansiCodesSupported = false)
    val newBuild = build.withLogger(newLogger)
    test(name)(fun(newBuild, newLogger))
  }

  override def test(name: String)(fun: => Any): Unit = {
    super.test(name)(fun)
  }

  override def afterAll(): Unit = {
    Paths.delete(workspace)
  }
}

name := "ContactsManager-ScalaPlay"

version := "1.0"

lazy val `contactsmanager-scalaplay` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )
libraryDependencies += "com.typesafe.play" %% "anorm" % "2.5.3"
libraryDependencies += evolutions

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"  
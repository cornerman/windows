# wm with scala-native and xcb

Simple stacking wm with open, close, move and resize.

## build and run

Get [scala-native](https://github.com/scala-native/scala-native):
```
git clone https://github.com/scala-native/scala-native
cd scala-native
sbt rtlib/publishLocal nscplugin/publishLocal publishLocal
```

If you have problems building scala-native, you might need to disable generation of docs in the `baseSettings` in `scala-native/build.sbt`:
```
lazy val baseSettings = Seq(
  organization := "org.scala-native",
  version      := nativeVersion,
  scalafmtConfig := Some(file(".scalafmt")),
  sources in doc in Compile := List.empty
)
```

Run windows:
```
git clone https://github.com/cornerman/windows
cd windows
sbt run
```

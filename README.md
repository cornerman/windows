# wm with scala-native and xcb

Simple stacking wm with open, close, move and resize.

## build

```
git clone https://github.com/scala-native/scala-native
cd scala-native
sbt rtlib/publishLocal nscplugin/publishLocal
git clone https://github.com/cornerman/windows
cat windows/build_sbt >> build.sbt
sbt windows/run
```

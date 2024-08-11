# JPass

<!-- TOC -->
* [JPass](#jpass)
  * [Description](#description)
  * [Build and run](#build-and-run)
    * [Usage](#usage)
<!-- TOC -->

## Description

**Jpass** - cli tool for generating temporary passwords

## Build and run

- Clone repo and go to project directory

```shell
git clone https://github.com/ex-neskoro/JPass.git 
```

- You can compile JPass to executable with 
[GraalVM Native Image](https://www.graalvm.org/latest/reference-manual/native-image/) 
with gradle `nativeCompile` task

```shell
./gradlew nativeCompile
```

**pro tip**: you can turn on copying executable file to some directory 
right after nativeCompile gradle task executed; default `-PexecutablePath="~/dev/bin"`
```shell
./gradlew nativeCompile -PtoLocalBin=true -PexecutablePath="full/path/to/executable"
```

---

- Or you can use a plain old java way -> .jar 

```shell
./gradlew build
java -jar build/libs/JPass-1.1.jar
```
### Usage
- to see all flags descriptions
```shell
jpass --help
```

- to generate password with length 32 and with all characters predicate groups 
but without a default excluded characters using secure platform default
random generator

```shell
jpass -ae -l 32 --secure
```
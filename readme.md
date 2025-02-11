# ConfigAPI

Plugin API that allows to register configs and get messages in the correct language for the player.

## Features

- Register configs
- Automaticly apply new keys and values to the config
- Get different Strings dependent on language.

## How to

### Install

1. Clone the repository.
2. Build the plugin.
3. Put the plugin in the /build/libs/ folder.
4. Change the build.gradle and Paper-Plugin.yml

**build.gradle**:

```
dependencies {
    compileOnly files("build/libs/ConfigAPI-0.0.jar")
}
```

**Paper-Plugin.yml**:

```yml
dependencies:
  server:
    ConfigAPI:
      load: BEFORE
      required: true
      join-classpath: true
```

"this" will always refer to the plugin.

### How does an language file look?

An language file can look like this and woud be named "en_US.json". The prefix key can be usefull later on.

```
{
    "prefix": "[GL]",
    "playerNotFound": "<red>This player wasn't found!</red>",
    "key": "value"
}
```

### Register language

#### Single language

```java
registerLanguage(this, "en_US.json", this.getResource("lang/en_US.json"))
```

#### Multilanguage registration

Multilanguage registration is recommended as it do less IO on config loads.

```java
Map<String, InputStream> lang = new HashMap<>();
lang.put("en_US.json", this.getResource("lang/en_US.json"));
LoadConfig.registerLanguage(this, lang);
```

### Get string for the player language

#### Without prefix

```java
Language.getValue(this, Player, "key")
```

#### With prefix

```java
Language.getValue(this, Player, "key", true)
```

### Geting a string from a given language

#### Without prefix

```java
String langCode = "en_US";
Language.getValue(this, langCode, "key")
```

#### With prefix

```java
String langCode = "en_US";
Language.getValue(this, langCode, "key", true)
```

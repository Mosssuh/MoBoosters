# MoBoosters

> A fully configurable booster plugin for Spigot/Paper servers.

MoBoosters allows server owners to create custom boosters with configurable boost values, durations, identifiers, items, requirements, actions and rewards. Boosters can be temporary or permanent and can be applied individually or globally.

The plugin is designed to be extensible and can support additional applicator integrations.

---

# Features

* Unlimited custom boosters
* Personal, Global and SuperiorSkyblock2 booster types
* Temporary and permanent boosters
* Configurable boost values and durations
* Custom booster items
* Requirements, actions and rewards
* PlaceholderAPI support
* Variables and local variables
* Command arguments
* Public API and Events
* Extensible integrations

> **Applicator Compatibility:** Additional plugin integrations can be added when requested.

---

# Installation

## Requirements

| Plugin            | Required |
| ----------------- | -------- |
| MoCore            | ✅        |
| NBTAPI            | ✅        |
| PlaceholderAPI    | Optional |
| SuperiorSkyblock2 | Optional |
| JobsReborn        | Optional |
| PlayerPoints      | Optional |
| EssentialsX       | Optional |
| ExcellentEconomy  | Optional |
| MoPets            | Optional |
| MoArmors          | Optional |

---

# API

MoBoosters includes a public API for integration with other plugins.

**Main Class:** `BoostersAPI`

---

# Events

| Event                           | Description                                   |
| ------------------------------- | --------------------------------------------- |
| `ActivatePermanentBoosterEvent` | Called when a permanent booster is activated. |
| `ActivateTemporaryBoosterEvent` | Called when a temporary booster is activated. |
| `ClaimItemBoosterEvent`         | Called when a booster item is claimed.        |
| `EndsTemporaryBoosterEvent`     | Called when a temporary booster ends.         |
| `IncreaseBoostBoosterEvent`     | Called when a booster boost increases.        |
| `IncreaseTimeBoosterEvent`      | Called when booster time increases.           |
| `RemoveBoostBoosterEvent`       | Called when a booster boost is removed.       |
| `RemoveTimeBoosterEvent`        | Called when booster time is removed.          |
| `ApplyBoostEvent`               | Called when a booster boost is applied.       |

---

# Commands

## General

| Command                                                    | Description                                     |
| ---------------------------------------------------------- | ----------------------------------------------- |
| `/moboosters reload`                                       | Reloads the configuration.                      |
| `/moboosters give <code> <amount> <player> <args...>`      | Gives a configured booster item to a player.    |
| `/moboosters giveall <code> <amount> <args...>`            | Gives a configured booster item to all players. |
| `/moboosters activate <code> <player> <actions> <args...>` | Activates a configured booster for a player.    |

## Booster Management

| Command                                                                                               | Description                             |
| ----------------------------------------------------------------------------------------------------- | --------------------------------------- |
| `/moboosters setbooster <player> <identifier> <type> <applicator> <boosted> <boost> <duration>`       | Creates or sets a temporary booster.    |
| `/moboosters setbooster <player> <identifier> <type> <applicator> <boosted> <boost>`                  | Creates or sets a permanent booster.    |
| `/moboosters addboost <player> <duration type> <identifier> <type> <applicator> <boosted> <boost>`    | Adds a boost to an active booster.      |
| `/moboosters removeboost <player> <duration type> <identifier> <type> <applicator> <boosted> <boost>` | Removes a boost from an active booster. |
| `/moboosters addtime <player> <identifier> <type> <applicator> <boosted> <duration>`                  | Adds time to a temporary booster.       |
| `/moboosters removetime <player> <identifier> <type> <applicator> <boosted> <duration>`               | Removes time from a temporary booster.  |

---

# Booster Types

| Type                | Description                                    |
| ------------------- | ---------------------------------------------- |
| `PERSONAL`          | Applies the booster to a specific player.      |
| `GLOBAL`            | Applies the booster globally.                  |
| `SUPERIORSKYBLOCK2` | Applies the booster through SuperiorSkyblock2. |

---

# Applicator Types

| Applicator | Boosted                                    |
| ---------- | ------------------------------------------ |
| MINECRAFT        | experience                           |
| JOBSREBORN       | money, exp                           |
| PLAYERPOINTS     | points                               |
| ESSENTIALSX      | economy                              |
| EXCELLENTECONOMY | `<currency id>`                      |
| MOPETS           | exp                                  |
| MOARMORS         | exp                                  |

# Booster Structure

Every booster is identified by a unique code and can define its own behavior, item, requirements and rewards.

```yaml
Boosters:

  ExampleBooster:

    identifier: default
    type: Personal
    applicator: Minecraft
    boosted: experience
    boost: 2
    duration: 3600

    item-info:
      ...

    actions:
      ...
```

## Configuration Options

| Option       | Description                                                           |
| ------------ | --------------------------------------------------------------------- |
| `identifier` | Allows multiple boosters to use the same applicator and boosted type. |
| `type`       | Defines how the booster is applied.                                   |
| `applicator` | Defines the system responsible for applying the booster.              |
| `boosted`    | Defines what value is affected.                                       |
| `boost`      | Defines the amount of the boost.                                      |
| `duration`   | Defines the duration in seconds or `PERM` for permanent boosters.     |
| `item-info`  | Defines the booster item.                                             |
| `actions`    | Defines the behavior when the booster is processed.                   |

> **Note:** `applicator` and `boosted` values depend on the integration being used. Additional integrations can be added when requested.

---

# Identifier

The `identifier` allows multiple boosters to use the same `applicator` and `boosted` type.

```yaml
identifier: default
```

For example, different experience boosters can use different identifiers while sharing the same applicator and boosted type.

---

# Applicator & Boosted

The `applicator` defines the system where the booster is applied, while `boosted` defines what value is affected.

```yaml
applicator: Minecraft
boosted: experience
```

Both values depend on the integration being used.

---

# Boost

The `boost` option defines the amount provided by the booster.

```yaml
boost: 2
```

It can also use command arguments:

```yaml
boost: '%args_1%'
```

---

# Duration

Temporary boosters use their duration in seconds:

```yaml
duration: 3600
```

Permanent boosters can use:

```yaml
duration: PERM
```

The duration can also be supplied through command arguments:

```yaml
duration: '%args_2%'
```

---

# Command Arguments

Booster values can be dynamically configured using command arguments.

| Variable          | Description                         |
| ----------------- | ----------------------------------- |
| `%args_1%`        | First command argument.             |
| `%args_2%`        | Second command argument.            |
| `%args_<number>%` | Argument at the specified position. |

Example:

```yaml
boost: '%args_1%'
duration: '%args_2%'
```

Using:

```text
/moboosters give ExampleBooster 1 PlayerName 2.5 3600
```

results in:

```text
%args_1% = 2.5
%args_2% = 3600
```

---

# PlaceholderAPI

MoBoosters provides placeholders for checking and displaying active booster information.

| Placeholder                                                                      | Description                                     |
| -------------------------------------------------------------------------------- | ----------------------------------------------- |
| `%moboosters_has_booster_<duration>:<identifier>:<type>:<applicator>:<boosted>%` | Checks whether the specified booster is active. |
| `%moboosters_boost_<duration>:<identifier>:<type>:<applicator>:<boosted>%`       | Returns the current boost.                      |
| `%moboosters_duration_<identifier>:<type>:<applicator>:<boosted>%`               | Returns the current duration.                   |
| `%moboosters_formatted_duration_<identifier>:<type>:<applicator>:<boosted>%`     | Returns the formatted duration.                 |

Use `ALL` as the identifier when applicable to retrieve the total boost from matching boosters.

---

# Available Variables

## Player

```text
%player%
```

## Booster

| Variable               | Description                     |
| ---------------------- | ------------------------------- |
| `%code%`               | Booster code.                   |
| `%boost%`              | Current boost.                  |
| `%boost_with_base%`    | Boost including its base value. |
| `%duration%`           | Duration in seconds.            |
| `%duration_formatted%` | Formatted duration.             |
| `%identifier%`         | Booster identifier.             |
| `%booster_type%`       | Booster type.                   |
| `%applicator_type%`    | Applicator type.                |
| `%boosted%`            | Boosted value.                  |

---

# Local Variables

| Variable                                                                                       | Description                                         |
| ---------------------------------------------------------------------------------------------- | --------------------------------------------------- |
| `{time_formatter_<time>}`                                                                      | Converts seconds into a formatted duration.         |

---

# Actions

Actions define what happens when a booster is processed.

| Option          | Description                                  |
| --------------- | -------------------------------------------- |
| `requirements`  | Conditions that must be met.                 |
| `rewards`       | Rewards executed when requirements succeed.  |
| `else`          | Rewards executed when requirements fail.     |
| `cancel_action` | Prevents the current action from continuing. |

Example:

```yaml
actions:

  ExampleAction:

    requirements:
      - '[EVAL] %vault_prefix% equals VIP'

    rewards:
      - '[CHANCE -> 100] MESSAGE -> &aBooster activated!'

    else:
      - '[CHANCE -> 100] MESSAGE -> &cYou cannot claim this booster.'
```

---

# Requirements

Requirements determine whether an action can execute.

| Type   | Description                                   |
| ------ | --------------------------------------------- |
| `EVAL` | Evaluates placeholders, variables and values. |

`EVAL` supports PlaceholderAPI, booster variables, local variables and command arguments.

### String Operators

```text
equals
!equals
equalsIgnoreCase
!equalsIgnoreCase
startsWith
!startsWith
contains
!contains
```

### Number Operators

```text
<
<=
>
>=
==
!=
```

Example:

```yaml
requirements:
  - '[EVAL] %vault_prefix% equals VIP'
  - '[EVAL] %boost% >= 2'
```

Multiple requirements can be separated using `||`.

---

# Rewards

Rewards are executed after requirements are successfully completed.

Rewards support:

* `CHANCE`
* `&&` for chaining rewards
* `||` for alternative rewards

Example:

```text
[CHANCE -> 50] EFFECT -> SPEED::10::1
```

## Commands

| Reward                 | Format                              |
| ---------------------- | ----------------------------------- |
| `CONSOLE_COMMAND`      | `CONSOLE_COMMAND -> <command>`      |
| `PLAYER_COMMAND`       | `PLAYER_COMMAND -> <command>`       |
| `PLAYER_COMMAND_AS_OP` | `PLAYER_COMMAND_AS_OP -> <command>` |

## Messages

| Reward              | Format                                   |
| ------------------- | ---------------------------------------- |
| `MESSAGE`           | `MESSAGE -> <message>`                   |
| `TITLE`             | `TITLE -> <title>::<subtitle>`           |
| `SOUND`             | `SOUND -> <sound>::<volume>::<pitch>`    |
| `BROADCAST_MESSAGE` | `BROADCAST_MESSAGE -> <message>`         |
| `BROADCAST_TITLE`   | `BROADCAST_TITLE -> <title>::<subtitle>` |
| `JSON`              | `JSON -> <json>`                         |
| `JSON_BROADCAST`    | `JSON_BROADCAST -> <json>`               |

## Effects

| Reward   | Format                                      |
| -------- | ------------------------------------------- |
| `EFFECT` | `EFFECT -> <effect>::<duration>::<amplifier>` |

Example:

```text
EFFECT -> SPEED::10::1
```

## Booster Control

| Reward           | Description                                   |
| ---------------- | --------------------------------------------- |
| `CANCEL_BOOSTER` | Prevents the booster from being applied.      |
| `CANCEL_CLAIM`   | Prevents the booster item from being claimed. |
| `CANCEL_MESSAGE` | Prevents the default claim message.           |

## Actions

| Reward           | Format                       |
| ---------------- | ---------------------------- |
| `EXECUTE_ACTION` | `EXECUTE_ACTION -> <action>` |

## Boost Management

| Reward               | Format                                                                                |
| -------------------- | ------------------------------------------------------------------------------------- |
| `ADD_BOOST`          | `ADD_BOOST -> <duration type>::<identifier>::<type>::<applicator>::<boosted>::<boost>`     |
| `REMOVE_BOOST`       | `REMOVE_BOOST -> <duration type>::<identifier>::<type>::<applicator>::<boosted>::<boost>`  |
| `ADD_TIME`           | `ADD_TIME -> <identifier>::<type>::<applicator>::<boosted>::<duration>`                   |
| `REMOVE_TIME`        | `REMOVE_TIME -> <identifier>::<type>::<applicator>::<boosted>::<duration>`                |
| `SET_BOOSTER`        | `SET_BOOSTER -> <identifier>::<type>::<applicator>::<boosted>::<boost>::<duration>`        |
| `ACCUMULATE_BOOSTER` | `ACCUMULATE_BOOSTER -> <identifier>::<type>::<applicator>::<boosted>::<boost>::<duration>` |

---

# Booster Item

The `item-info` section defines the item used to represent the booster.

```yaml
item-info:

  material: PAPER
  data: 0

  name: '&fBooster &8| &e&lExperience'

  lore:
    - ''
    - '&bType: &a%booster_type%'
    - '&bBoost: &a+%boost%'
    - '&bDuration: &a%duration_formatted%'
    - ''
    - '&aRight click to claim!'
```

## Item Options

| Option         | Description                             |
| -------------- | --------------------------------------- |
| `material`     | Minecraft material or custom head.      |
| `data`         | Legacy item data value.                 |
| `name`         | Item display name.                      |
| `lore`         | Item description.                       |
| `unbreakable`  | Makes the item unbreakable.             |
| `unique`       | Prevents identical items from stacking. |
| `enchantments` | Item enchantments.                      |
| `flags`        | Item flags.                             |

## Materials

Normal material:

```yaml
material: PAPER
```

Custom head:

```yaml
material: basehead-<base64>
```

## Enchantments

```yaml
enchantments:
  - LUCK:2
```

## Flags

```yaml
flags:
  - HIDE_ENCHANTS
```

---

# Example Booster

The following example creates a temporary Personal experience booster. If the player already has the same boost active, its remaining time is accumulated.

```yaml
Boosters:

  MinecraftV1:

    identifier: default
    type: Personal
    applicator: Minecraft
    boosted: experience
    boost: '%args_1%'
    duration: '%args_2%'

    item-info:

      material: PAPER
      data: 0

      name: '&fBooster &8| &e&lExperience'

      lore:
        - ''
        - '&bType: &aPersonal'
        - '&bBoost: &a+%boost%'
        - '&bDuration: &a%duration_formatted%'
        - ''
        - '&8If you already have the same boost,'
        - '&8the remaining time will be accumulated.'
        - ''
        - '&aRight click to claim!'

    actions:

      ModifiedBoost:

        requirements:
          - '[EVAL] %moboosters_has_booster_temp:default:personal:minecraft:experience% equals false'

        rewards:
          - '[CHANCE -> 100] CANCEL_MESSAGE && MESSAGE -> &b&lPERSONAL BOOST! &aYou claimed a x%boost% experience booster for %duration_formatted%!'

        else:
          - '[CHANCE -> 100] EXECUTE_ACTION -> ModifiedBoostElseIf'

      ModifiedBoostElseIf:

        cancel_action: true

        requirements:
          - '[EVAL] %moboosters_personal_boost_temp:default:minecraft:experience% == %boost%'

        rewards:
          - '[CHANCE -> 100] CANCEL_MESSAGE && CANCEL_BOOSTER'
          - '[CHANCE -> 100] ADD_TIME -> default:personal:minecraft:experience:%args_2%'
          - '[CHANCE -> 100] MESSAGE -> &b&lPERSONAL BOOST! &aHas been added %duration_formatted% to your %applicator_type% for %boosted%!'

        else:
          - '[CHANCE -> 100] CANCEL_BOOSTER && CANCEL_CLAIM'
          - '[CHANCE -> 100] MESSAGE -> &b&lPERSONAL BOOST! &cYou can''t claim this booster because you already have an active booster && MESSAGE -> &cand the time can''t be accumulated because it isn''t the same boost.'
```


Each booster can independently define its appearance, boost, duration, requirements, actions and rewards, allowing the same system to be used for different types of boosters and integrations.

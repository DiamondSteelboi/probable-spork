
---

# SanityXBounty – Minecraft Plugin Specification

## Overview

**SanityXBounty** is a Paper Minecraft plugin designed for a small private SMP (approximately **7 active players**).

Its purpose is to discourage random player killing while still encouraging fair PvP through:

* Duels
* Self-defense
* Bounty hunting
* Psychological sanity effects

Players who repeatedly commit unlawful murders become increasingly wanted, suffer immersive gameplay penalties, and eventually become valuable bounty targets.

---

# Core Sanity System

Every player begins with:

* **100 Sanity**
* **0 Unlawful Kills**
* **No Bounty Rank**

Sanity is **calculated** from unlawful kills instead of being stored independently to prevent inconsistencies.

| Unlawful Kills | Sanity | Rank         |
| -------------- | ------ | ------------ |
| 0              | 100    | None         |
| 1              | 75     | Suspect      |
| 2              | 50     | Outlaw       |
| 3+             | 0      | Public Enemy |

Whenever a player commits an unlawful kill:

* Their unlawful kill count increases.
* Their sanity immediately updates.
* Their bounty rank immediately updates.

---

# Legal vs Illegal Kills

Every player kill is evaluated in this order:

```
Did the players have an accepted duel?

YES
→ Legal

NO

↓

Was it valid self-defense?

YES
→ Legal

NO

↓

Illegal Kill
```

Only illegal kills:

* Reduce sanity
* Increase bounty rank
* Count as unlawful kills

---

# Duel System

Commands

```
/duel <player>

/duel accept

/duel deny
```

## Rules

Players may challenge another player to a duel.

Once accepted:

* All PvP between those players becomes legal.
* No sanity loss.
* No bounty increase.
* No unlawful kills recorded.

The duel ends when:

* One player dies
* Duel timeout expires
* One player disconnects (configurable surrender)

Players cannot:

* Start another duel while already dueling
* Duel while currently in combat
* Accept multiple duel requests

Duel timeout is configurable.

---

# Self-Defense

Whenever:

```
Player A attacks Player B
```

Player B gains a **Self-Defense Window** against Player A.

Default duration:

```
60 seconds
```

During that window:

Player B may legally kill Player A.

Result:

* No sanity loss
* No bounty increase
* No unlawful kill

If Player A kills Player B instead:

Player A still commits an unlawful kill.

Each attacker is tracked independently.

Example:

```
ZombieKing attacks Steve

Alex attacks Steve

Steve may legally kill BOTH players.
```

Self-defense expires when:

* Timer runs out
* Either player dies
* Player disconnects

---

# Combat Detection

The plugin tracks recent PvP.

Combat timer:

```
15 seconds
```

Used to:

* Prevent blindness during combat
* Prevent certain sanity effects from triggering mid-fight
* Improve fairness

---

# Sanity Effects

Effects should create tension without making PvP impossible.

Avoid:

* Weakness II+
* Permanent Blindness
* Slowness
* Severe debuffs

---

## 100 Sanity

No effects.

---

## 75 Sanity — Suspect

Players feel watched.

Randomly every few minutes:

* Darkness (1–2 seconds)
* Ambient cave sounds
* Gray particles
* Slight phantom spawn increase
* Nearby hostile mobs become more interested

---

## 50 Sanity — Outlaw

Permanent:

* Weakness I
* Hunger I

Random:

* Darkness
* Cave whispers
* Smoke particles
* Fake lightning
* Increased hostile mob spawning nearby

---

## 0 Sanity — Public Enemy

Permanent:

* Weakness I
* Hunger I
* Mining Fatigue I

Random:

* Darkness (2–3 seconds)
* Rare Blindness (never during combat)
* Soul particles
* Smoke
* Fake lightning

Visual:

* Hearts disappear briefly every 15 seconds
* Visual only
* Does not affect health

---

# Mob Behavior

Hostile mobs become increasingly attracted to wanted players.

100 Sanity

* Normal targeting

75 Sanity

* Slightly increased priority

50 Sanity

* High priority

0 Sanity

* Highest priority when nearby

This modifies target priority instead of constantly changing AI for better performance.

---

# Fake Lightning

Fake lightning uses visual-only effects.

It:

* Does not start fires
* Does not damage entities
* Exists only for immersion

---

# Bounty Rewards

There is **no economy plugin**.

Instead, killing a wanted player awards physical loot.

Rewards are generated using configurable weighted loot tables.

---

## Suspect

Possible rewards:

* Iron Blocks
* Gold Blocks
* Ender Pearls
* Golden Apples

---

## Outlaw

Possible rewards:

* Diamonds
* Emeralds
* Enchanted Books
* More Golden Apples

---

## Public Enemy

Possible rewards:

* Diamond Blocks
* Netherite Scrap
* Totem of Undying (rare)
* High-level Enchanted Books
* Rare bonus loot

Loot is randomized through weighted pools.

---

# Anti-Farming Protection

To prevent abuse on a small SMP:

* Same victim only rewards bounty once every configurable number of hours
* Optional teammate/faction reward blocking
* Optional kill farming detection
* Configurable reward cooldown

---

# Commands

```
/sanity
```

Displays:

* Current sanity
* Unlawful kills
* Current bounty rank

---

```
/bounty
```

Displays all wanted players.

Example:

```
Wanted Players

Public Enemy
• Steve

Outlaws
• Alex

Suspects
• Bob
```

---

```
/duel <player>

/duel accept

/duel deny
```

---

```
/sanityxbounty reload
```

Reloads:

* Config
* Messages
* Rewards
* Effect settings

---

# Permissions

```
sanityxbounty.use

sanityxbounty.duel

sanityxbounty.reload

sanityxbounty.admin

sanityxbounty.bypass

sanityxbounty.effects.bypass
```

---

# Configuration

Everything should be configurable.

Including:

```
Self-defense timer

Combat timer

Duel timeout

Logout behavior

Sanity effects

Messages

Permissions

Mob attraction

Particle frequency

Lightning frequency

Blindness chance

Phantom modifier

Loot tables

Reward cooldowns

Anti-farming

Sounds

Particles
```

Example:

```yaml
combat:
  self-defense-seconds: 60
  combat-tag-seconds: 15

duel:
  timeout: 300
  logout-loss: true

effects:
  enabled: true

  suspect:
    darkness: true
    sounds: true
    particles: true

  outlaw:
    weakness: true
    hunger: true
    smoke: true

  public_enemy:
    fatigue: true
    blindness: true
    soul_particles: true

rewards:
  suspect: ...
  outlaw: ...
  public_enemy: ...

messages:
  unlawful-kill: "&cYour sanity has decreased."

anti-farming:
  reward-cooldown-hours: 12
```

---

# Player Data

Each player stores:

```yaml
uuid:
  unlawfulKills: 0
  duel:
    active: false
    opponent: null
    expires: 0
  combat:
    attacker: null
    defenderWindowEnds: 0
```

Sanity and bounty rank are calculated automatically from `unlawfulKills`.

---

# Project Structure

```
SanityXBounty
│
├── SanityXBounty.java
│
├── commands
│   ├── SanityCommand.java
│   ├── BountyCommand.java
│   ├── DuelCommand.java
│   └── ReloadCommand.java
│
├── managers
│   ├── PlayerDataManager.java
│   ├── SanityManager.java
│   ├── CombatManager.java
│   ├── DuelManager.java
│   ├── RewardManager.java
│   ├── EffectManager.java
│   └── ConfigManager.java
│
├── listeners
│   ├── DamageListener.java
│   ├── DeathListener.java
│   ├── JoinListener.java
│   ├── QuitListener.java
│   └── MobTargetListener.java
│
├── models
│   ├── BountyRank.java
│   ├── Duel.java
│   ├── CombatWindow.java
│   └── PlayerData.java
│
├── util
│   ├── MessageUtil.java
│   ├── RandomUtil.java
│   └── LootTable.java
│
└── storage
    └── players.yml
```

This combined specification provides a strong foundation for a balanced, immersive PvP system. It discourages random killing through meaningful consequences while preserving fair combat via duels and self-defense. The addition of configurable sanity effects, weighted loot tables, combat-aware debuffs, and anti-farming measures makes it well-suited for a small private SMP where player interactions are frequent and should remain engaging without becoming frustrating.

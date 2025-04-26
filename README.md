# 🗣️ ShadowVCMod

> Ambient sound awareness for Minecraft entities.

**ShadowVCMod** is a backend utility mod providing real-time voice detection capabilities for Minecraft mobs. Built as a modular system intended for a larger mod, it introduces environmental audio awareness based on player speech captured through **Voice Changer API**, commissioned by Shadow.

---

## 🛠️ Core Functionality

- **Voice Capture Integration**: Hooks into OpenAL audio streams to monitor player voice levels in real time.
- **Threshold-Based Detection**: Calculates active decibel levels and triggers events once a configurable threshold is exceeded.
- **Entity Targeting Logic**: Grants mobs the ability to target to nearby players **based solely on detected voice volume**.
- **Decoupled Design**: Can be easily extended into broader AI behavior trees, sound-based stealth mechanics, or ambient event triggers.

---

## 📦 Intended Use

- Designed to serve as a **base module** for larger mods introducing stealth, horror, or ambient survival mechanics.
- Focuses exclusively on **voice volume detection and basic mob reaction hooks** — leaves final behavior implementation to downstream mods.
- Compatible with dynamic difficulty scaling based on environmental sound factors.

---

## ⚙️ Technical Details

- **Language**: Java (Forge)
- **Audio Library**: Voice Changer Mod

---

## 📈 Example Behavior

- A player speaks above 60 dB → Local hostile mobs become aware of the player's location and start pathfinding.
- Optional: dynamic adjustment of detection radius based on biome (e.g., tighter indoors, wider in open spaces).

---

Commissioned project — proprietary rights held by the commissioner Shadow.  
Integration for private or larger project pipelines permitted under contract.

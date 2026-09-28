# ZBK VR Mod

ZBK Vivecraft Offhand Aim is an optional client-side Fabric companion for Zombies Build Kit. It routes Vivecraft aiming and menu pointing through a configurable controller and places the wrist HUD on a separately configurable controller.

This client-only mod is optional for ZBK. Non-VR players and dedicated servers do not need it.

## Compatibility

| Component | Current target |
| --- | --- |
| Minecraft Java | 26.2; mod metadata permits the 26.2.x series |
| Fabric Loader | Built with 0.19.3; mod metadata requires 0.19.3 or newer |
| Vivecraft Fabric | 26.2-1.3.15 |
| Java | 25 |
| Mod version | 0.3.0 |

Build versions are maintained in [gradle.properties](gradle.properties), and client requirements are declared in [fabric.mod.json](src/main/resources/fabric.mod.json). The Gradle wrapper and Fabric Loom versions are pinned in the build files. A matching ZBK datapack and resource pack provide gameplay and presentation; this mod supplies client integration only.

## Build

Install JDK 25 and make it available through `JAVA_HOME` or `PATH`. From this repository's root, run:

```powershell
java -version
.\gradlew.bat build
```

On Linux or macOS, use `./gradlew build`. The wrapper downloads the pinned Gradle distribution and build dependencies when they are not cached; a separate Gradle installation is not required.

The installable artifact is:

```text
build/libs/zbk-vivecraft-offhand-aim-0.3.0.jar
```

The accompanying `-sources.jar` is for development, not installation. Both JARs include the license texts, attribution notice, and media permission. Build output and dependency caches are ignored; required wrapper files remain tracked.

## Install

1. Close Minecraft. Use a Fabric client profile with the Minecraft and Vivecraft versions listed above.
2. Copy the built mod JAR into that profile's `mods` folder, alongside Vivecraft. For the default Windows game directory, this is `%APPDATA%\.minecraft\mods`; custom launchers may use a separate instance directory. Replace an older copy instead of keeping duplicate versions. This client-only mod does not belong in the dedicated server's `mods` folder.
3. Start the client and keep Vivecraft's **Aim Device** set to **Controller**. Install the matching ZBK resource pack and optional VR overlay for the intended weapon presentation.

## Configuration

The first client launch creates `config/zbk-vivecraft-offhand-aim.properties` inside the active game directory:

```properties
enabled=true
controller_index=1
hud_controller_index=0
```

| Setting | Meaning |
| --- | --- |
| `enabled` | Enables the controller overrides; `false` leaves the original Vivecraft behavior active |
| `controller_index` | Controller used for aiming and the menu cursor; default `1` |
| `hud_controller_index` | Controller used for the wrist HUD; default `0` |

Use `0` or `1` for controller indices. If the setup is reversed, swap them. Close the client before editing the file, then restart it; settings are loaded at startup rather than watched for changes.

## Behavior and limits

The mod adjusts Vivecraft aim/crosshair direction, menu pointing, wrist-HUD placement, and player look direction used by Minecraft commands. Existing crosshair-aligned targets remain respected. The datapack's weapon raycast still starts at the player's eyes; this mod does not move a server-side raycast origin to the physical controller.

Check aiming, menus, wrist HUD, and disabled-mode behavior in Vivecraft after changing Minecraft, Fabric, Vivecraft, or the mixins.

## Source

Source, resources, Gradle build configuration, and wrapper files are tracked. Local client profiles, generated JARs, and caches are excluded. No Blockbench projects or map assets are included.

## License and credit

Free noncommercial use, modification, and sharing are allowed with credit to
[MiniStew](https://www.youtube.com/@MiniStew). Monetized videos and streams are
allowed under the [media permission](MEDIA_PERMISSION.md). Selling covered ZBK
content or maps containing it, or charging for server access, is not covered
by that permission. See [licensing and attribution](LICENSE.md) for the code
and asset licenses, their scope, and redistribution requirements.

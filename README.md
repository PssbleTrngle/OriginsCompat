[ISSUES]: https://github.com/PssbleTrngle/OriginsCompat/issues

[DOWNLOAD]: https://www.curseforge.com/minecraft/mc-mods/origins-compat/files

[CURSEFORGE]: https://www.curseforge.com/minecraft/mc-mods/origins-compat

[MODRINTH]: https://modrinth.com/mod/origins-compat

# Origins Compat

[![Release](https://img.shields.io/github/v/release/PssbleTrngle/OriginsCompat?label=Version&sort=semver)][DOWNLOAD]
[![Downloads](https://cf.way2muchnoise.eu/full_711560_downloads.svg)][CURSEFORGE]
[![Version](https://cf.way2muchnoise.eu/versions/711560.svg)][DOWNLOAD]
[![Issues](https://img.shields.io/github/issues/PssbleTrngle/OriginsCompat?label=Issues)][ISSUES]
[![Modrinth](https://img.shields.io/modrinth/dt/xtXAknNA?color=green&logo=modrinth&logoColor=green)][MODRINTH]

## Features

### Water Backtank

Similar to the Copper Backtank which stores pressurized air to breath underwater, the water backtank stores water.
This can be used by waterbreathing origins to walk on land without worrying about suffocating.
It can be filled using a spout placed above.

### HungerBarPowerType `origins_compat:hunger_bar`

Similar to the built-in power type `origins:status_bar_texture`, this power type can be used to solely override the texture of the hunger bar icons.
This has support for the [Apple Skin](https://github.com/squeek502/AppleSkin) overlays

### ScalePowerType `origins_compat:scale`

Support for [Pehkui](https://github.com/Virtuoel/Pehkui) resizing

```json
{
  "type": "origins_compat:scale",
  "attribute": "pehkui:height",
  "size": 1.5
}
```